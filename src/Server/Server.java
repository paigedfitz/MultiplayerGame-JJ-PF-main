package Server;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Server {

    // boolean for runnin the server
    final static boolean DEBUG = true;
    static boolean isRunning = false;
    static int connectionCount = 0;
    static JLabel statusLbl, connCnt, ipLbl;
    static HashMap<String,ClientHandler> connections = new HashMap<String,ClientHandler>();
    // static ArrayBlockingQueue<updateObj> changeQ = new ArrayBlockingQueue<updateObj>(1000);

    //Thread for call from action performed
    private final static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static void main(String[] args) throws Exception {
        
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel p = new JPanel();
        p.setPreferredSize(new Dimension(300,100));
        window.add(p, BorderLayout.NORTH);

        JLabel portLabel = new JLabel("Port:");

        JTextField portText = new JTextField("4567");
        portText.setColumns(10);
        p.add(portLabel);
        p.add(portText);
        
        JButton listenBtn = new JButton("Listen");
        listenBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isRunning = true;
                executorService.execute(new Runnable() {
                    public void run() {
                        try {
                            listen(Integer.parseInt(portText.getText() ));
                            System.out.println("server should now be listening");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
		    }
        });

        JButton quitBtn = new JButton("Stop");
        quitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                isRunning = false;
                System.out.println("this should stop the server from listening");
                statusLbl.setText("Not Listening");
			}
		});

        p.add(listenBtn);
        p.add(quitBtn);
        statusLbl = new JLabel("not listening");
        connCnt = new JLabel("conns:" + connectionCount);

        //IP address:
        // NetworkInterface.getNetworkInterfaces();
        // InetAddress IP=InetAddress.getLocalHost();
        // System.out.println("IP of my system is := "+IP.getHostAddress());
        // ipLbl = new JLabel(IP.getHostAddress());//InetAddress.getLocalHost().toString());
        ipLbl = new JLabel("IP: " + getLocalAddress().toString());
        p.add(statusLbl);
        p.add(connCnt);
        p.add(ipLbl);
        
// ************ CONSOLE THINGY *********//
        // special console to try and see the sysout output... remove once working
        if (DEBUG){
            JTextArea consoleOutputArea = new JTextArea(15, 25);
            consoleOutputArea.setEditable(false);
            JScrollPane scroll = new JScrollPane ( consoleOutputArea );
            scroll.setVerticalScrollBarPolicy ( ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );

            PrintStream printStream = new PrintStream(new OutputStream() { //streams terminal to the text area (found online)
                @Override
                public void write(int b) throws IOException {
                    // redirects data to the text area
                    consoleOutputArea.append(String.valueOf((char)b));
                    // scrolls the text area to the end of data
                    consoleOutputArea.setCaretPosition(consoleOutputArea.getDocument().getLength());
                }
            });
            System.setOut(printStream);
            System.setErr(printStream);
            JPanel consoPanel = new JPanel();
            consoPanel.setPreferredSize(new Dimension(400,300));
            //Add Textarea in to middle panel
            consoPanel.add ( scroll );
            // consoPanel.add(consoleOutputArea);
            window.add(consoPanel, BorderLayout.SOUTH);
        }
// ************ END CONSOLE THINGY *********//        


        window.pack();
        window.setVisible(true);
    }

    public static void listen(int port) throws IOException {
        // start listening on port
        ServerSocket listener = new ServerSocket(port);
        System.out.println("I am running: " + listener);
        
        Socket socket = null;
        
        while (isRunning) {

            System.out.println("\nListening ...  ");
            statusLbl.setText("Listening...  ");
            connCnt.setText("conns:" + connectionCount);
            try {
                socket = listener.accept();
                System.out.println("connection made.: " + socket);
                statusLbl.setText("Client Connected");
                socket.getPort();
                connectionCount++;
                System.out.println("connections:" + connectionCount );

                // Capture the user and client 

                
                System.out.println("Assigning new thread for this client"); 

                // create a new thread object 
                Thread t = new ClientHandler(socket); 

                // Invoking the start() method 
                t.start(); 
            }
            catch (Exception e){ 
                socket.close(); 
                e.printStackTrace(); 
            } 
        }
        socket.close();
        listener.close();
    }

    private static InetAddress getLocalAddress(){
        // I found this code on StackOverflow: https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java

        try {
            Enumeration<NetworkInterface> b = NetworkInterface.getNetworkInterfaces();
            while( b.hasMoreElements()){
                for ( InterfaceAddress f : b.nextElement().getInterfaceAddresses())
                    if ( f.getAddress().isSiteLocalAddress())
                        return f.getAddress();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}