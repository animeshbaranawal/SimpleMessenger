import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class HomeBox {
    /// ui elements
    private JFrame mainFrame;
    private JPanel controlPanel;
    private JLabel userIDLabel;
    private JTextField userIDField;
    private JButton connectButton;
    private JLabel connectStatusLabel;

    private JLabel groupLabel;

    /// controlling parent
    private JavaClient parent;

    public HomeBox(JavaClient parentClient){
        parent = parentClient;
        prepareGUI();
    }

    private void setConnectStatus(boolean success){
        String status = "";
        if(success) {
            status = "Connected";
        }
        else {
            status = "Could not connect";
        }
        connectStatusLabel.setText(status);
        connectStatusLabel.repaint();
        connectStatusLabel.revalidate();
    }

    void addGroupButton(String groupID) {
        JButton groupButton = new JButton(groupID);
        groupButton.addActionListener(e -> parent.joinGroup(groupButton.getText()));
        controlPanel.add(groupButton);
        controlPanel.repaint();
        controlPanel.revalidate();
    }

    private void prepareGUI(){
        mainFrame = new JFrame("HOME " + parent.getID());
        mainFrame.setSize(400,400);
        mainFrame.setLayout(new GridLayout(1, 1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                parent.closeConnection();
            }
        });

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(10,1));

        userIDLabel = new JLabel("User ID: ", JLabel.LEFT);
        userIDField = new JTextField("");
        connectButton = new JButton("Connect");
        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                boolean success = parent.connectTo(userIDField.getText());
                setConnectStatus(success);
            }
        });
        connectStatusLabel = new JLabel("",JLabel.CENTER);
        connectStatusLabel.setSize(350,100);

        controlPanel.add(userIDLabel);
        controlPanel.add(userIDField);
        controlPanel.add(connectButton);
        controlPanel.add(connectStatusLabel);

        groupLabel = new JLabel("Groups", JLabel.CENTER);
        controlPanel.add(groupLabel);

        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);
    }
}