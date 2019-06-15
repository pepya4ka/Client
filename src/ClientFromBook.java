import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientFromBook {

    JTextArea incoming;
    JTextField outgoing;
    JTextField jTextFieldForName;
    BufferedReader reader;
    PrintWriter writer;
    Socket sock;
    static int amountClient;
    int numberClient;

    static JFrame frame;
    private String nameClient;
    static ClientFromBook clientFromBook;

    public static void main(String[] args) {
        clientFromBook = new ClientFromBook();
//        clientFromBook.go();
        clientFromBook.beginGo();
    }

    public ClientFromBook() {
        amountClient++;
        numberClient = amountClient;
    }

    public void beginGo() {
        frame = new JFrame("Authorization");

        JPanel mainPanel = new JPanel();
        jTextFieldForName = new JTextField(20);
        JButton jButton = new JButton("Login");
        jButton.addActionListener(new AuthorizationButtonListener());
        mainPanel.add(jTextFieldForName);

        mainPanel.add(jButton);

        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);

        while (true) {
            if (jTextFieldForName.getText().length() > 0) {
                jButton.setEnabled(true);
            } else {
                jButton.setEnabled(false);
            }
        }
    }

    public void go() {
        JFrame frame = new JFrame("Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel();
        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);
        JScrollPane qScroller = new JScrollPane(incoming);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        mainPanel.add(qScroller);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        setUpNetworking();

        JLabel jLabelForName = new JLabel(jTextFieldForName.getText());
        Thread readerThread = new Thread(new IncomingReader());
        readerThread.start();

        frame.getContentPane().add(BorderLayout.NORTH, jLabelForName);
        frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    private void setUpNetworking() {
        try {
            sock = new Socket("192.168.1.3", 5000);
            InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
            reader = new BufferedReader(streamReader);
            writer = new PrintWriter(sock.getOutputStream());
            System.out.println("networking established");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class SendButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            try {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.replace(0, stringBuffer.length(), jTextFieldForName.getText() + ": " + outgoing.getText());
                writer.println(stringBuffer.toString());
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    public class IncomingReader implements Runnable {

        @Override
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    incoming.append(message + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class AuthorizationButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            clientFromBook.go();
        }
    }
}
