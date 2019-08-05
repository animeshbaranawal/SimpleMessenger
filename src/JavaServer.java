import fileTransfer.FileTransferService;
import org.apache.thrift.TException;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;
import simpleMessenger.ServerService;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class JavaServer {
    private ServerService.Processor processor;
    private FileTransferService.Processor fileProcessor;

    private Thread serverServiceThread;
    private Thread fileServiceThread;

    final Logger logger = Logger.getLogger(JavaServer.class.getName());

    public JavaServer(int numThreads, String storagePath) {
        ServerServiceHandler handler = new ServerServiceHandler(this, numThreads);
        processor = new ServerService.Processor(handler);

        FileTransferServiceHandler fileHandler = new FileTransferServiceHandler(this, storagePath);
        fileProcessor = new FileTransferService.Processor(fileHandler);
    }

    public static void main(String [] args) throws IOException {
        if(args.length < 2)
        {
            System.out.println("Server needs 2 argument");
            System.exit(3); /// invalid arguments
        }

        File s = new File(args[1]);
        if(!s.exists() || !s.isDirectory()) {
            System.out.println("Invalid path given");
            System.exit(4);
        }

        JavaServer serverSide = new JavaServer(Integer.parseInt(args[0]), args[1]);

        /// logger properties
        FileHandler f = new FileHandler("server.txt");
        f.setFormatter(new SimpleFormatter());
        serverSide.logger.addHandler(f);

        /// start server service
        Runnable server = new Runnable() {
            @Override
            public void run() {
                serverSide.startServerService(serverSide.processor);
            }
        };

        serverSide.serverServiceThread = new Thread(server);
        serverSide.serverServiceThread.start();

        /// start file transfer service
        Runnable fileService = new Runnable() {
            @Override
            public void run() {
                serverSide.startFileService(serverSide.fileProcessor);
            }
        };

        serverSide.fileServiceThread = new Thread(fileService);
        serverSide.fileServiceThread.start();
    }

    private void startServerService(ServerService.Processor processor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9090);
            TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

            logger.log(Level.INFO, "Starting server");
            server.serve();
        } catch(TException e) {
            logger.log(Level.SEVERE, "Thrift exception caught");
            logger.log(Level.SEVERE, e.toString());
            System.exit(1); /// could not start server
        }
    }

    private void startFileService(FileTransferService.Processor fileProcessor) {
        try {
            TServerTransport serverTransport = new TServerSocket(9089);
            TServer fServer = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(fileProcessor));

            logger.log(Level.INFO, "Starting filer server");
            fServer.serve();
        } catch (TTransportException e) {
            logger.log(Level.SEVERE, "Thrift exception caught");
            logger.log(Level.SEVERE, e.toString());
            System.exit(2);
        }
    }
}