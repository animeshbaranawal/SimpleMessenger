import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import simpleMessenger.*;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;


public class ServerServiceHandler implements ServerService.Iface {

    public class Pair {
        private String clientRecipientID;
        private Message msg;

        public Pair(String c, Message m) {
            clientRecipientID = c;
            msg = m;
        }

        public final String first(){return clientRecipientID;}
        public final Message second(){return msg;}
    }

    public class Dispatcher {
        private Queue<Pair> messageQueue = new LinkedList<>();

        private final Lock queueLock = new ReentrantLock();
        private final Condition emptyQueue = queueLock.newCondition();

        Dispatcher() {
            /// run receiver
            Runnable dispatcher = new Runnable() {
                @Override
                public void run() {
                    try {
                        dispatch();
                    } catch (InterruptedException e) {
                        parentServer.logger.log(Level.SEVERE, e.toString());
                    }
                }
            };

            Thread dispatcherThread = new Thread(dispatcher);
            dispatcherThread.start();
        }

        private void dispatch() throws InterruptedException {
            while (true) {
                queueLock.lock();
                try {
                    while(messageQueue.isEmpty()) emptyQueue.await();

                    Pair msg = messageQueue.peek();
                    if(msg != null) {
                        messageQueue.remove();
                        String toID = msg.first();
                        Message message = msg.second();

                        if(clientSockets.containsKey(toID)) {
                            ClientService.Client clientSocket = clientSockets.get(toID);
                            try {
                                clientSocket.receive(message);
                            } catch (TException e) {
                                parentServer.logger.log(Level.SEVERE, "Thrift exception caught");
                                parentServer.logger.log(Level.SEVERE, e.toString());
                            }
                        }
                        else {
                            parentServer.logger.log(Level.SEVERE, "Client "+toID+" not found");
                        }
                    } else {
                        parentServer.logger.log(Level.SEVERE, "Queue empty");
                    }
                } finally {
                    queueLock.unlock();
                }
            }
        }
    }

    private final ArrayList<Dispatcher> dispatchers;
    private HashMap<String, ClientService.Client> clientSockets;
    private HashMap<String, GroupDefinition> chatRooms;
    private int numberOfThreads;

    private JavaServer parentServer;

    ServerServiceHandler(JavaServer server, int numThreads) {
        parentServer = server;
        clientSockets = new HashMap<>();
        chatRooms = new HashMap<>();

        /// create 3 chatRooms
        for(int i=0; i<3; i++) {
            GroupDefinition chatRoom = new GroupDefinition();
            chatRoom.groupID = "room"+(i+1);
            chatRoom.members = new ArrayList<>();
            chatRooms.put(chatRoom.groupID, chatRoom);
        }

        numberOfThreads = numThreads;
        dispatchers = new ArrayList<>();
        for(int i=0; i<numberOfThreads; i++) {
            dispatchers.add(new Dispatcher());
        }
    }

    private int hashString(String s) {
        return s.hashCode()%numberOfThreads;
    }

    /////////////////////////////// THRIFT APIS ////////////////////////////////////
    public boolean join(UserDefinition cl) {
        String ID = cl.uniqueID;
        boolean success = false;

        if(!clientSockets.containsKey(ID)) {
            // connect to client service
            TTransport transport;
            transport = new TSocket(cl.ip_addr, cl.port);
            TProtocol protocol = new TBinaryProtocol(transport);
            ClientService.Client client = new ClientService.Client(protocol);
            try {
                transport.open();
                clientSockets.put(ID, client);
                success = true;
            } catch(TException t) {
                parentServer.logger.log(Level.SEVERE, "Thrift exception caught");
                parentServer.logger.log(Level.SEVERE, t.toString());
            }
        }

        parentServer.logger.log(Level.INFO, success ? "Added client "+ID : "Cannot add client");
        return success;
    }

    public void closeConnection(UserDefinition cl) {
        if(clientSockets.containsKey(cl.uniqueID)) {
            clientSockets.remove(cl.uniqueID);
            parentServer.logger.log(Level.INFO, "Removed client "+cl.uniqueID);
        } else {
            parentServer.logger.log(Level.WARNING, "Client "+cl.uniqueID+" not found");
        }
    }

    public boolean connectTo(String cl2) {
        return clientSockets.containsKey(cl2);
    }

    public void sendMessage(Message msg) {
        /// add message to queue
        ArrayList<String> recipients = new ArrayList<>();
        if(!msg.toGroup) {
            recipients.add(msg.toID);
        }
        else {
            for(String cl: chatRooms.get(msg.toID).members) {
                if(!cl.equals(msg.fromID)) {
                    recipients.add(cl);
                }
            }
        }

        for(String r: recipients) {
            int dispatcherNumber = hashString(r);
            Dispatcher d = dispatchers.get(dispatcherNumber);
            d.queueLock.lock();
            try {
                d.messageQueue.add(new Pair(r, msg));
                d.emptyQueue.signal();
            } finally {
                d.queueLock.unlock();
            }
        }
    }

    public List<String> enquireGroups(String clientID) {
        return new ArrayList<>(chatRooms.keySet());
    }

    public boolean joinGroup(String groupID, String clientID) {
        if(chatRooms.containsKey(groupID)) {
            List<String> memberList = chatRooms.get(groupID).members;
            if(!memberList.contains(clientID)) { /// if not member, make it a member
                memberList.add(clientID);
                parentServer.logger.log(Level.INFO, "Added client "+clientID+" to group "+groupID);
                return true;
            } else {
                parentServer.logger.log(Level.WARNING, "Client "+clientID+" already member of "+groupID);
            }
        } else {
            parentServer.logger.log(Level.WARNING, "Group "+groupID+" not found");
        }

        return false;
    }

    public boolean leaveGroup(String groupID, String clientID) {
        if(chatRooms.containsKey(groupID)){
            List<String> memberList = chatRooms.get(groupID).members; /// if member, remove from list
            if(memberList.contains(clientID)) {
                memberList.remove(clientID);
                parentServer.logger.log(Level.INFO, "removed client "+clientID+" to group "+groupID);
                return true;
            } else {
                parentServer.logger.log(Level.WARNING, "Client "+clientID+" not member of "+groupID);
            }
        } else {
            parentServer.logger.log(Level.WARNING, "Group "+groupID+" not found");
        }

        return false;
    }
}
