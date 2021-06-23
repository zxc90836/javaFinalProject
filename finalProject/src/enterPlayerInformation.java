import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImageFilter;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class enterPlayerInformation extends JFrame {
    private static String[] rank = {"青銅","白銀","黃金","白金","鑽石","大師","頂獵"};
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JTextField userID;
    private JTextField averageDamage;
    private JComboBox userRank;
    private JButton enterButton;
    public enterPlayerInformation(){
        super("Welcome");
        setLayout(new GridLayout(4,1));
        panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());
        panel1.add(new JLabel("APEX_ID"));
        userID = new JTextField("請輸入你的遊戲ID",13);
        panel1.add(userID);

        panel2 = new JPanel();
        panel2.setLayout(new FlowLayout());
        panel2.add(new JLabel("牌位"));
        userRank = new JComboBox(rank);
        panel2.add(userRank);

        panel3 = new JPanel();
        panel3.setLayout(new FlowLayout());
        panel3.add(new JLabel("均傷"));
        averageDamage = new JTextField("0",7);
        panel3.add(averageDamage);


        panel4 = new JPanel();
        panel4.setLayout(new FlowLayout());
        enterButton = new JButton("確認");
        panel4.add(enterButton);
        MyEventHandler handler = new MyEventHandler();
        enterButton.addActionListener(handler);
        add(panel1);
        add(panel2);
        add(panel3);
        add(panel4);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(380, 300);
        setVisible(true); // display frame
    }
    private class MyEventHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == enterButton) {
                File file = new File("playerID.txt");
                FileWriter writer = null;
                BufferedWriter bufferedWriter = null;
                String data;
                int result=JOptionPane.showConfirmDialog(enterPlayerInformation.this,
                        "確定保存?(之後可更改)",
                        "確認訊息",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (result==JOptionPane.YES_OPTION)
                {
                    try {
                        writer = new FileWriter(file.getName());
                        bufferedWriter = new BufferedWriter(writer);
                        data = String.format("%d %s %s %.2f%n",1,userID.getText(),userRank.getSelectedItem(),Double.parseDouble(averageDamage.getText()));
                        bufferedWriter.write(data);
                        bufferedWriter.close();

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    //進入主程式位置
                    dispose();
                }

            }
        }
    }
}
