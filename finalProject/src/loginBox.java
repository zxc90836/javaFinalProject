import javax.swing.*;
import java.awt.*;

public class loginBox {
    private JButton thereSButton;
    private JButton nothingButton;
    private JButton hereButton;
    private JPanel Panel1;
    private JLabel label1;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton loginButton;
    private JButton forgotPasswordButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("loginBox");
        frame.setContentPane(new loginBox().Panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        label1 = new JLabel(new ImageIcon("icon.png"));
    }
}
