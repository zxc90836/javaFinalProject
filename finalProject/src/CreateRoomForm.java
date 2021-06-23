import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CreateRoomForm extends JFrame{
    private JButton createRoomButton;
    private JButton cancelButton;
    private JTextField textField1;
    private JTextField textField2;
    public JPanel Panel1;
    private JRadioButton 青銅RadioButton;
    private JRadioButton 白銀RadioButton;
    private JRadioButton 黃金RadioButton;
    private JRadioButton 白金RadioButton;
    private JRadioButton 鑽石RadioButton;
    private JRadioButton 大師RadioButton;
    private JRadioButton 頂獵RadioButton;
    private PlayerViewer playerViewer = new PlayerViewer("playerID");
    /////////////
    private String rankLimits;

    public CreateRoomForm() {
        rankLimits="青銅";
        this.setTitle("正在建立房間");
        this.setSize(550, 200);
        //设置默认窗体在屏幕中央
        setContentPane(Panel1);
        int x = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int y = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
        this.setLocation((x - this.getWidth()) / 2, (y-this.getHeight())/ 2);
        this.setResizable(false);
        setVisible(true);

        createRoomButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                    System.out.printf("Create room button!\n");
                chatRoom room = new chatRoom(textField1.getText(),Double.parseDouble(textField2.getText()),rankLimits);

                /*System.out.printf("%s\n",textField1.getText());
                System.out.printf("%s\n",Double.parseDouble(textField2.getText()));
                System.out.printf("%s\n",rankLimits);*/

                System.out.println(room);
                CreateRoomForm.this.setVisible(false);
                room.addMember(playerViewer.getUserInfo());
                chatRoom newRoom = MainUI.socket.createRoom(room);
                ChatRoomForm chatRoomForm = new ChatRoomForm();
                chatRoomForm.setChatValue(newRoom);
            }
        });
        青銅RadioButton.addItemListener(new RadioButtonHandler1(0));
        白銀RadioButton.addItemListener(new RadioButtonHandler1(1));
        黃金RadioButton.addItemListener(new RadioButtonHandler1(2));
        大師RadioButton.addItemListener(new RadioButtonHandler1(3));
        白金RadioButton.addItemListener(new RadioButtonHandler1(4));
        頂獵RadioButton.addItemListener(new RadioButtonHandler1(5));
        鑽石RadioButton.addItemListener(new RadioButtonHandler1(6));
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {


                CreateRoomForm.this.setVisible(false);
            }
        });

    }

    private class RadioButtonHandler1 implements ItemListener
    {
        private String changerank;

        public RadioButtonHandler1(int x)
        {
            switch (x){
                case 0:changerank="青銅";break;
                case 1:changerank="白銀";break;
                case 2:changerank="黃金";break;
                case 3:changerank="白金";break;
                case 4:changerank="鑽石";break;
                case 5:changerank="大師";break;
                case 6:changerank="頂獵";break;
                default:break;
            }
        }

        // handle radio button events
        @Override
        public void itemStateChanged(ItemEvent event)
        {
            rankLimits = changerank;
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("正在創建新的房間");
        frame.setContentPane(new CreateRoomForm().Panel1);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
