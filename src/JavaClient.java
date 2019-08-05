import fileTransfer.FileChunk;
import fileTransfer.FileTransferService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.*;
import simpleMessenger.ClientService;
import simpleMessenger.Message;
import simpleMessenger.ServerService;
import simpleMessenger.UserDefinition;

import java.io.*;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class JavaClient {
    /// server side
    private ServerService.Client serverServiceClient;
    private TTransport transport;

    /// mini server on client side
    private ClientService.Processor processor;
    private ClientServiceHandler handler;
    private UserDefinition clientInfo;
    private Thread clientServerThread;

    /// ui boxes
    private HashMap<String, ChatBox> chatBoxes;
    private HomeBox homeBox;
    private boolean UIMode = true;

    /// directories for file storage
    private String inputDirectoryPath = "/home/animeshbaranawal/Downloads/Input";
    private String outputDirectoryRootPath = "/home/animeshbaranawal/Downloads/";
    private String outputDirectoryPath = "";

    final Logger logger = Logger.getLogger(JavaClient.class.getName());
    private final int fileBlockSize = 2048;
    private final String serverIP;

    public class FileTransferThread {
        private boolean mode;
        private String path;
        private String clientID;
        private boolean multicast;

        FileTransferThread(boolean fMode, String filePath, String ID, boolean group) {
            mode = fMode;
            path = filePath;
            clientID = ID;
            multicast = group;

            Runnable fTransfer = new Runnable() {
                @Override
                public void run() {
                    if(mode) receive();
                    else send();
                }
            };

            Thread serviceThread = new Thread(fTransfer);
            serviceThread.start();
        }

        private void send(){
            ChatBox ch = null;
            if(chatBoxes.containsKey(clientID)) {
                ch = chatBoxes.get(clientID);
                ch.disable();
            }

            try {
                TTransport ftransport = new TSocket(serverIP, 9089);
                ftransport.open();

                TProtocol protocol = new TBinaryProtocol(ftransport);
                FileTransferService.Client fServiceClient = new FileTransferService.Client(protocol);

                /// read file
                File f = new File(inputDirectoryPath + "/" + path);
                FileInputStream fStream = new FileInputStream(f);
                FileChannel fChannel = fStream.getChannel();
                int fSize = (int) fChannel.size();
                logger.log(Level.INFO,getTimestamp()+"::File size top be uploaded "+fSize);

                String hashedString = path+"_"+clientID;
                hashedString = String.valueOf(hashedString.hashCode());
                fServiceClient.startUpload(hashedString, fSize);

                int remaining = fSize;
                FileChunk fChunk = new FileChunk();
                ByteBuffer d = ByteBuffer.allocate(fileBlockSize);
                fChunk.size = 2048;
                while (fChannel.read(d) > 0) {
                    d.flip();
                    fChunk.data = d;
                    if (remaining < 2048) {
                        fChunk.size = remaining;
                        remaining -= remaining;
                    } else {
                        fChunk.size = 2048;
                        remaining -= 2048;
                    }
                    logger.log(Level.INFO, getTimestamp()+"::Uploading size="+fChunk.size+",offset="+fChunk.offset);
                    fServiceClient.uploadFileChunk(hashedString, fChunk);
                }

                fStream.close();
                fServiceClient.endUpload(hashedString);
                ftransport.close();
                logger.log(Level.INFO, getTimestamp()+"::File send successful");

                /// send successful
                Message msg = new Message();
                msg.fromID = clientInfo.uniqueID;
                msg.toID = clientID;
                msg.timestamp = getTimestamp();
                msg.toGroup = multicast;
                msg.msgString = "Sent file: " + path;
                serverServiceClient.sendMessage(msg);
                if (UIMode && chatBoxes.containsKey(clientID)) {
                    ChatBox box = chatBoxes.get(clientID);
                    box.addMessage(msg.fromID, msg.msgString);
                    box.setReceiveStatus("Send successful");
                }

            } catch (TException x) {
                logger.log(Level.SEVERE, "Thrift exception caught");
                logger.log(Level.SEVERE, x.toString());
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IO exception caught");
                logger.log(Level.SEVERE, e.toString());
            } finally {
                if (ch != null) ch.enable();
            }
        }

        private void receive(){
            ChatBox ch = null;
            if(chatBoxes.containsKey(clientID)) {
                ch = chatBoxes.get(clientID);
                ch.disable();
            }

            try {
                TTransport ftransport = new TSocket("localhost", 9089);
                ftransport.open();

                TProtocol protocol = new  TBinaryProtocol(ftransport);
                FileTransferService.Client fServiceClient = new FileTransferService.Client(protocol);

                String hashedString = path+"_"+clientInfo.uniqueID;
                hashedString = String.valueOf(hashedString.hashCode());

                int fSize = fServiceClient.startDownload(hashedString);
                logger.log(Level.INFO,getTimestamp()+"::File size top be downloaded "+fSize);

                int remaining = fSize;
                if(fSize > 0) {
                    ByteBuffer fileData = ByteBuffer.allocate(fSize);
                    int offset = 0;
                    while(remaining > 0) {
                        if(remaining < 2048) {
                            FileChunk data = fServiceClient.downloadFileChunk(hashedString, offset, remaining);
                            fileData.put(data.data.array(), 0, remaining);
                            offset += remaining;
                            remaining -= remaining;
                            logger.log(Level.INFO, getTimestamp()+"::Downloading size="+data.size+",offset="+data.offset);
                        }
                        else {
                            FileChunk data = fServiceClient.downloadFileChunk(hashedString, offset, 2048);
                            data.data.flip();
                            fileData.put(data.data.array(), 0, 2048);
                            offset += 2048;
                            remaining -= 2048;
                            logger.log(Level.INFO, getTimestamp()+"::Downloading size="+data.size+",offset="+data.offset);
                        }
                    }

                    fileData.flip();
                    FileChannel fc = new FileOutputStream(outputDirectoryPath +"/" + path).getChannel();
                    fc.write(fileData);
                    fc.close();
                }

                ftransport.close();
                logger.log(Level.INFO, getTimestamp()+"::File receive successful");

                /// receive successful
                if(UIMode && chatBoxes.containsKey(clientID)) {
                    ChatBox box = chatBoxes.get(clientID);
                    box.setReceiveStatus("Receive successful");
                }

            } catch (TException t) {
                logger.log(Level.SEVERE, "Thrift exception caught");
                logger.log(Level.SEVERE, t.toString());
            } catch (IOException e) {
                logger.log(Level.SEVERE, "IO exception caught");
                logger.log(Level.SEVERE, e.toString());
            } finally {
                if(ch != null) ch.enable();
            }
        }
    }

    public JavaClient(String sIP) {
        handler = new ClientServiceHandler(this);
        processor = new ClientService.Processor(handler);
        clientInfo = new UserDefinition();
        chatBoxes = new HashMap<String, ChatBox>();
        serverIP = sIP;
    }

    /// get client details
    final String getID(){
        return clientInfo.uniqueID;
    }
    final int getPort() { return clientInfo.port; }
    final String getIPAddress() { return clientInfo.ip_addr; }
    final long getTimestamp() { return new Date().getTime(); }

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length < 4) {
            System.out.println("Client atleast requires 4 arguments: serverIP, clientIP, uniqueID, port");
            System.exit(3); /// invalid arguments
        }

        JavaClient clientSide = new JavaClient(args[0]);
        if(args.length == 6) {
            File commandFile = new File(args[4]);
            if(commandFile.exists()) { /// if external file mentioned.. UI disabled
                clientSide.UIMode = false;
            }
        }

        /// initiate client details
        InetAddress localhost = InetAddress.getLocalHost();
        System.out.println("System IP Address : " + (localhost.getHostAddress()).trim());

        clientSide.clientInfo.ip_addr = args[1];
        clientSide.clientInfo.uniqueID = args[2];
        clientSide.clientInfo.port = Integer.parseInt(args[3]);

        /// set logger properties
        FileHandler f = new FileHandler(args[2]+"_client.txt");
        f.setFormatter(new SimpleFormatter());
        clientSide.logger.addHandler(f);

        /// run clientServer
        Runnable clientServer = new Runnable() {
            @Override
            public void run() {
                clientSide.startClientServer(clientSide.processor);
            }
        };

        clientSide.clientServerThread = new Thread(clientServer);
        clientSide.clientServerThread.start();
        clientSide.connectToServer();

        /// read commands file if any
        if(!clientSide.UIMode) {
            Random randomNumberGenerator = new Random();
            TimeUnit.SECONDS.sleep((Integer.parseInt(args[5])+2)/3);

            BufferedReader commandReader = new BufferedReader(new FileReader(args[4]));
            String line = commandReader.readLine();
            while(line != null) {
                String[] lineParts = line.split(" ");
                switch (lineParts[0]) {
                    case "CONNECT":
                        clientSide.connectTo(lineParts[1]);
                        break;
                    case "SENDMESSAGE":
                        clientSide.sendMessage(lineParts[1], lineParts[2], false);
                        break;
                    case "CLOSE":
                        clientSide.closeChat(lineParts[1]);
                        break;
                    case "SENDFILE":
                        clientSide.sendFile(lineParts[1], lineParts[2], false);
                        break;
                    case "RECEIVEFILE":
                        clientSide.receiveFile(lineParts[1], lineParts[2]);
                        break;
                    case "JOINGROUP":
                        clientSide.joinGroup(lineParts[1]);
                        break;
                    case "SENDGROUPMESSAGE":
                        clientSide.sendMessage(lineParts[1], lineParts[2], true);
                        break;
                    case "LEAVEGROUP":
                        clientSide.leaveGroup(lineParts[1]);
                        break;
                    case "EXIT":
                        clientSide.closeConnection();
                        break;
                }
                line = commandReader.readLine();
                TimeUnit.MILLISECONDS.sleep(randomNumberGenerator.nextInt(200));
            }
            clientSide.logger.log(Level.INFO, "commands over");
        }
    }

    private void startClientServer(ClientService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(clientInfo.port);
            TServer clientServer = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));

            logger.log(Level.INFO, "Starting client server");
            clientServer.serve();
        } catch(TException e) {
            logger.log(Level.SEVERE, "Thrift exception caught");
            logger.log(Level.SEVERE, e.toString());
            System.exit(2); /// could not start client server
        }
    }

    private void connectToServer() {
        try {
            transport = new TSocket(serverIP, 9090);
            transport.open();

            TProtocol protocol = new  TBinaryProtocol(transport);
            serverServiceClient = new ServerService.Client(protocol);

            boolean success = serverServiceClient.join(clientInfo);
            if(success) {
                if(UIMode) {
                  homeBox = new HomeBox(this);
                  addGroups();
                }
                File outputDirectory = new File(outputDirectoryRootPath+clientInfo.uniqueID);
                if(!outputDirectory.exists()) outputDirectory.mkdir();
                outputDirectoryPath = outputDirectory.exists() ? outputDirectory.getPath() : "";
            }

            logger.log(Level.INFO, success ? "Connected to server" : "Could not connect to server");
        } catch (TException x) {
            logger.log(Level.SEVERE, "Thrift exception caught");
            logger.log(Level.SEVERE, x.toString());
            System.exit(1); /// could not connect to server
        }
    }

    private ChatBox createChatBox(String recipientID, boolean isRoom) {
        ChatBox chatbox = new ChatBox(recipientID, isRoom, this);
        chatBoxes.put(recipientID, chatbox);
        return chatbox;
    }

    /////////////////////////////// HOMEBOX APIS ////////////////////////////////////
    void closeConnection() {
        try {
            serverServiceClient.closeConnection(clientInfo);
            transport.close();
            logger.log(Level.INFO, "Disconnected server");
            System.exit(0);
        } catch (TException e) {
            logger.log(Level.SEVERE, "Thrift exception caught");
            logger.log(Level.SEVERE, e.toString());
        }

        System.exit(3); /// error while closing connection
    }

    boolean connectTo(String P2P) {
        boolean success = false;
        if(!P2P.equals(clientInfo.uniqueID)) {
            try {
                success = serverServiceClient.connectTo(P2P);
            } catch (TException e) {
                logger.log(Level.SEVERE, "Thrift Exception caught");
                logger.log(Level.SEVERE, e.toString());
            }

            if(success) {
                if(UIMode) createChatBox(P2P, false);
                logger.log(Level.INFO,"Connected to client "+P2P);
            }
        }

        return success;
    }

    void joinGroup(String groupID){
        boolean success = false;
        try {
            success = serverServiceClient.joinGroup(groupID, clientInfo.uniqueID);
        } catch (TException e) {
            logger.log(Level.SEVERE, "Thrift Exception caught");
            logger.log(Level.SEVERE, e.toString());
        }

        if(success) {
            if(UIMode) createChatBox(groupID, true);
            logger.log(Level.INFO, "Connected to group "+groupID);
        }
    }

    private void addGroups() {
        try {
            List<String> groupIDs = serverServiceClient.enquireGroups(clientInfo.uniqueID);
            for(String id: groupIDs) {
                homeBox.addGroupButton(id);
            }
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    /////////////////////////////// CHATBOX APIS ////////////////////////////////////
    boolean sendMessage(String P2P, String message, boolean isRoom){
        Message msg = new Message();
        msg.timestamp = getTimestamp();
        msg.msgString = message;
        msg.fromID = clientInfo.uniqueID;
        msg.toID = P2P;
        msg.toGroup = isRoom;

        boolean sendSuccess = false;
        try {
            serverServiceClient.sendMessage(msg);
            sendSuccess = true;
            logger.log(Level.INFO, getTimestamp() + "::" + msg.fromID+"->"+msg.toID+": "+msg.msgString + "(" + msg.timestamp + ")");
        } catch (TException e) {
            logger.log(Level.SEVERE, "Thrift Exception caught");
            logger.log(Level.SEVERE, e.toString());
        }

        return sendSuccess;
    }

    void updateChatBox(Message msg) {
        boolean isGroupMessage = msg.toGroup;

        String chatboxKey = isGroupMessage ? msg.toID : msg.fromID;
        if(UIMode) {
            if(!chatBoxes.containsKey(chatboxKey)) {
                createChatBox(chatboxKey, isGroupMessage);
            }

            ChatBox chBox = chatBoxes.get(chatboxKey);
            chBox.addMessage(msg.fromID, msg.msgString);
        }

        logger.log(Level.INFO, getTimestamp() + "::" + chatboxKey+"->"+clientInfo.uniqueID+": "+msg.msgString + "(" + msg.timestamp + ")");
    }

    void leaveGroup(String groupID){
        try {
            serverServiceClient.leaveGroup(groupID, clientInfo.uniqueID);
            logger.log(Level.INFO, "Disconnected from group "+groupID);
        } catch (TException e) {
            logger.log(Level.SEVERE, "Thrift exception caught");
            logger.log(Level.SEVERE, e.toString());
        }
    }

    void closeChat(String id) {
        if(UIMode) chatBoxes.remove(id);
        logger.log(Level.INFO, "Closed chatbox "+id);
    }

    /////////////////////////////// FILE TRANSFER APIS ////////////////////////////////////
    void sendFile(String filePath, String ID, boolean group)
    {
        logger.log(Level.INFO, getTimestamp()+"::Send File "+filePath+" request to "+ID);
        new FileTransferThread(false, filePath, ID, group);
    }

    void receiveFile(String recipientID, String filePath) {
        logger.log(Level.INFO, getTimestamp()+"::Receive File "+filePath+" request");
        new FileTransferThread(true, filePath, recipientID, false);
    }
}
