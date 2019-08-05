import simpleMessenger.ClientService;
import simpleMessenger.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

public class ClientServiceHandler implements ClientService.Iface {
    private Queue<Message> messageQueue;

    private final Lock queueLock = new ReentrantLock();
    private final Condition emptyQueue = queueLock.newCondition();

    private Thread receiverThread;
    private JavaClient parentClient;

    ClientServiceHandler(JavaClient parent) {
        messageQueue = new LinkedList<>();
        parentClient = parent;

        /// run receiver thread
        Runnable receiver = new Runnable() {
            @Override
            public void run() {
                try {
                    send();
                } catch (InterruptedException e) {
                    parentClient.logger.log(Level.SEVERE, e.toString());
                }
            }
        };

        receiverThread = new Thread(receiver);
        receiverThread.start();
    }

    private void send() throws InterruptedException {
        while (true) {
            queueLock.lock();
            try {
                while(messageQueue.isEmpty()) {
                    parentClient.logger.log(Level.INFO, "ClientServiceHandler waiting for messages");
                    emptyQueue.await(); ///  wait if no message in queue
                }

                Message msg = messageQueue.peek();
                messageQueue.remove();
                parentClient.updateChatBox(msg);
            } finally {
                queueLock.unlock();
            }
        }
    }

    /////////////////////////////// THRIFT APIS ////////////////////////////////////
    public void receive(Message msg) {
        queueLock.lock();
        try {
            parentClient.logger.log(Level.INFO, "ClientServiceHandler queueing messages");
            messageQueue.add(msg); /// message added to queue
            emptyQueue.signal();
        } finally {
            queueLock.unlock();
        }
    }
}
