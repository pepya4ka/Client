import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;

public class Client extends JFrame implements Runnable {

    static int numberClient;
    private int sendNumberClient;
    static private Socket socket;
    static private ObjectOutputStream outputStream;
    static private ObjectInputStream inputStream;
    final JTextArea jTextAreaJPanel2;
    static JTextArea jTextAreaChat;
    BufferedReader reader;

    public static void main(String[] args) {
        Client client = new Client();
    }

    Client() {
        super();
        numberClient++;
        sendNumberClient = numberClient;
        setLayout(new FlowLayout());
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setLocationRelativeTo(null);

        jTextAreaJPanel2 = new JTextArea(10, 20);
        JScrollPane scroller2 = new JScrollPane(jTextAreaJPanel2);
        jTextAreaJPanel2.setLineWrap(true);
        scroller2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroller2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jTextAreaChat = new JTextArea(10, 20);
        JScrollPane scrollPane = new JScrollPane(jTextAreaChat);
        jTextAreaChat.setLineWrap(true);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


        final JButton button = new JButton("Send");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (actionEvent.getSource() == button) {
                    StringBuffer stringBuffer = new StringBuffer(sendNumberClient + ": " + jTextAreaJPanel2.getText());
                    sendData(stringBuffer.toString());
                    jTextAreaJPanel2.setText("");
                }
            }
        });

        add(scroller2);
        add(button);
        add(scrollPane);

        run();
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();
    }

    @Override
    public void run() {
        try {
//            while (true) {
                socket = new Socket(InetAddress.getByName("127.0.0.1"), 5678);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());
                InputStreamReader streamReader = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(streamReader);
//                jTextAreaJPanel2.append("\n" + (String) inputStream.readObject() + "\n");
//                jTextAreaChat.append((String) inputStream.readObject() + "\n");

//            }
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

    private class IncomingReader implements Runnable {
        public void run() {
            String message;
            try {
                while ( (message = reader.readLine()) != null ) {
                    jTextAreaChat.append(message + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}