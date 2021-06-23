import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class MainUI extends JFrame{
    private JPanel panel1;
    private JList<chatRoom> roomList;
    public static clientSocket socket = new clientSocket();
    private JButton 創建新的房間Button;
    private JButton 進入房間Button;
    private JButton 設定個人資料Button;
    private JPanel roomPanel;
    private JLabel userName;
    private JLabel userAverageDamage;
    private JLabel userRank;
    private JButton 刷新房間Button;
    private chatRoom targetRoom;//要進去的目標房間

    private DefaultListModel<chatRoom> listModel;
    private CreateRoomForm createRoomForm;
    private chatRoom room;
    private PlayerViewer playerViewer;
    public void setUserInfo(){
        PlayerViewer playerViewer= new PlayerViewer("playerID");
        this.userName.setText(playerViewer.getUserInfo().getName());
        this.userAverageDamage.setText(String.valueOf(playerViewer.getUserInfo().getAverageDamage()));
        this.userRank.setText(playerViewer.getUserInfo().getRank().getName());
    }
    public Player getUserInfo(){
        PlayerViewer playerViewer= new PlayerViewer("playerID");
        return playerViewer.getUserInfo();
    }
    public MainUI() {
        socket.start();
        listModel = new DefaultListModel();
        roomList = new JList(listModel);
        roomPanel.add(roomList);
        setRoomList();
        setUserInfo();
        this.setTitle("MainUI");
        this.setSize(600, 350);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(panel1);
        int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);
        this.setResizable(false);
        setVisible(true);


        創建新的房間Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CreateRoomForm createRoomForm = new CreateRoomForm();
                createRoomForm.setVisible(true);
            }
        });
        進入房間Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(targetRoom);
                if(targetRoom==null){
                    JOptionPane.showMessageDialog(null,"請選一個房間之後在按進入哦!!");
                }
                else{
                    //targetRoom
                    ChatRoomForm r = new ChatRoomForm();
                    chatRoom chatRoomJoin = socket.joinRoom(targetRoom.getKey(),getUserInfo());
                    System.out.println(chatRoomJoin);
                    r.setChatValue(chatRoomJoin);
                }
            }
        });
        roomList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(e.getValueIsAdjusting() == false){
                    targetRoom = listModel.elementAt(roomList.getLeadSelectionIndex());
                    //System.out.println(listModel.elementAt(roomList.getLeadSelectionIndex()));
                }
            }

        });
        刷新房間Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setRoomList();
                setUserInfo();
                setVisible(true);
                repaint();
                revalidate();
                roomList.clearSelection();
            }
        });
        設定個人資料Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterPlayerInformation enterInfo = new enterPlayerInformation();
                enterInfo.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            }
        });
    }
    public void setRoomList(){
        listModel.clear();
        List<chatRoom> all = getRoomFromServer();
        System.out.println("add");
        for(chatRoom regChatRoom:all){
                listModel.addElement(regChatRoom);
        }
        System.out.println("dddd");
        roomList.setModel(listModel);

    }
    public List<chatRoom> getRoomFromServer() {
        List<chatRoom> skr= new ArrayList<chatRoom>();
        skr = socket.recvAllRoom();
        System.out.println(skr);
        return skr;
    }

    public static void main(String[] args) {
        MainUI frame = new MainUI();
    }
}
