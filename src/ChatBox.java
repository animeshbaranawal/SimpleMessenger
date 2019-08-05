import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatBox {
    /// ui elements
    private JFrame mainFrame;
    private JPanel controlPanel;
    private JTextArea commentTextArea;
    private JTextField chatTextField;
    private JButton sendButton;

    private JTextField fileTextField;
    private JButton sendFileButton;
    private JButton receiveFileButton;
    private JLabel receiveStatus;

    /// handling parent
    private JavaClient parent;

    /// properties of chatbox
    private String recipientID;
    private boolean multicast;

    public ChatBox(String id, boolean chatRoom, JavaClient parentClient){
        multicast = chatRoom;
        recipientID = id;
        parent = parentClient;
        prepareGUI();
    }

    public void disable(){ mainFrame.setEnabled(false); }
    public void enable(){mainFrame.setEnabled(true); }

    void addMessage(String senderID, String message) {
        commentTextArea.append("\n"+senderID+": "+message);
        commentTextArea.repaint();
        commentTextArea.revalidate();
    }

    void setReceiveStatus(String msg) {
        receiveStatus.setText(msg);
        receiveStatus.repaint();
        receiveStatus.revalidate();
    }

    private void prepareGUI(){
        mainFrame = new JFrame(parent.getID()+"->"+recipientID);
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(1, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                if(multicast) {
                    parent.leaveGroup(recipientID);
                }

                parent.closeChat(recipientID);
            }
        });

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        commentTextArea = new JTextArea("",20,20);
        commentTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(commentTextArea);
        chatTextField = new JTextField("",20);
        sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                boolean sendSuccess = parent.sendMessage(recipientID, chatTextField.getText(), multicast);
                if (sendSuccess) {
                 addMessage(parent.getID(), chatTextField.getText());
                }
            }
        });
        controlPanel.add(scrollPane);
        controlPanel.add(chatTextField);
        controlPanel.add(sendButton);


        fileTextField = new JTextField("",20);
        sendFileButton = new JButton("Send File");
        sendFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                parent.sendFile(fileTextField.getText(), recipientID, multicast);
            }
        });
        receiveFileButton = new JButton("Receive File");
        receiveFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                parent.receiveFile(recipientID, fileTextField.getText());
            }
        });
        receiveStatus = new JLabel("",JLabel.RIGHT);
        controlPanel.add(fileTextField);
        controlPanel.add(sendFileButton);
        controlPanel.add(receiveFileButton);
        controlPanel.add(receiveStatus);

        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
    }
}