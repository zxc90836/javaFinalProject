import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.List;

public class ChatRoomForm extends JFrame{
    public JPanel panel1;
    private JTextArea textRecord;
    private JPanel sendPanel;
    private JButton 送出Button;
    private JTextArea sendArea;
    private JPanel userPanel;
    private JSplitPane userJSplitPane;
    private JPanel mainPanel;
    private JPanel onlineListPane;
    private JPanel currentUserPane;
    private JComboBox comboBox1;
    private JLabel currentUserLbl;
    private JLabel chatRoomName;
    private JLabel playerLabel1;
    private JLabel playerLabel2;
    private JLabel playerLabel3;
    recvMSG r = new recvMSG();
    private PlayerViewer playerViewer = new PlayerViewer("playerID");
    public ChatRoomForm() {
        this.setTitle("聊天室");
        this.setSize(550, 500);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        textRecord.setEditable(false);
        setContentPane(panel1);
        int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);
        this.setResizable(false);
        setVisible(true);
        sendArea.addKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == Event.ENTER){
                    try {
                        sendTxtMsg(sendArea.getText());
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    sendArea.setText("");
                }
            }
        });
        送出Button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    sendTxtMsg(sendArea.getText());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendArea.setText("");
            }
        });
        this.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });
    }
    public void setChatValue(chatRoom thisChat){
        chatRoomName.setText(thisChat.getName());
        currentUserLbl.setText(playerViewer.getUserInfo().getName());
        onlineListPane = new JPanel();
        onlineListPane.setVisible(true);
        int regi=0;
        playerLabel1.setText("施工中，請敬請期待");
        r.start();
    }
    public void logout(){
        System.out.println("todo");
        r.stop();
        MainUI.socket.leave();
    }
    public void sendTxtMsg(String msg) throws IOException {
        //todo
        MainUI.socket.sendMsg(msg);
    }
    public class recvMSG extends Thread {
        @Override
        public void run(){
            while (true){
                try {
                    String msg = MainUI.socket.receiveMsg();
                    textRecord.setText(textRecord.getText() + msg+"\n");
                    System.out.println("do:"+msg);
                    if(msg.equals("do:error"))
                        break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }catch (Exception e){
                    break;
                }
            }
        }
    }
    public static void main(String[] args) {
        ChatRoomForm chatRoomForm = new ChatRoomForm();
        chatRoom tr = new chatRoom();
        tr.addMember(new Player("幹你娘",Rank.SILVER,2000));
        chatRoomForm.setChatValue(tr);
    }
}
