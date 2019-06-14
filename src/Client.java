import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class Client extends JFrame implements Runnable {

    static private Socket socket;
    static private ObjectOutputStream outputStream;
    static private ObjectInputStream inputStream;

    public static void main(String[] args) {
        new Thread(new Client()).start();
    }

    Client() {
        super();
        setLayout(new FlowLayout());
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        final JTextArea jTextAreaJPanel2 = new JTextArea(10, 10);
        JScrollPane scroller2 = new JScrollPane(jTextAreaJPanel2);
        jTextAreaJPanel2.setLineWrap(true);
        scroller2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        final JButton button = new JButton("Send");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == button) {
                    sendData(jTextAreaJPanel2.getText());
//                    jTextAreaJPanel2.setText("");
                }
            }
        });

        add(scroller2);
        add(button);
    }

    @Override
    public void run() {
        try {
            while (true) {
                socket = new Socket(InetAddress.getByName("127.0.0.1"), 5678);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendData(Object obj) {
        try {
            outputStream.flush();
            outputStream.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}