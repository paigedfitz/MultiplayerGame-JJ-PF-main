package Client;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;


public class Game {

    static String connectionStatus = "not connected";
    final  boolean DEBUG = true;
    Pong pongGame;
    Client c;
    boolean paused = true;
    JLabel playerCnt;

    public static void main(String[] args) {
        
        Game g = new Game();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                g.createAndShowGUI();
            }
        });
        g.run();
    }

    public Game(){
        pongGame = new Pong();
    }
        
    public void createAndShowGUI(){
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(800,50));
        window.add(topPanel, BorderLayout.NORTH);

        //user:
        JLabel userLabel = new JLabel("User:");
        JTextField userText = new JTextField("test"+ (int) (Math.random()*10000) );
        userText.setColumns(10);
        topPanel.add(userLabel);
        topPanel.add(userText);

        //port
        JLabel portLabel = new JLabel("Port:");
        JTextField portText = new JTextField("4567");
        portText.setColumns(6);
        topPanel.add(portLabel);
        topPanel.add(portText);
        
        //port
        JLabel ipLabel = new JLabel("IP:");
        JTextField ipText = new JTextField("127.0.0.1");
        ipText.setColumns(8);
        topPanel.add(ipLabel);
        topPanel.add(ipText);

        //connection:
        JLabel connStatus = new JLabel(connectionStatus);

        // players:
        playerCnt = new JLabel("");

        JButton connectBtn = new JButton("Connect");
        connectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                System.out.println("Let's try to connect: ");
                
                try {
                    c = new Client(Integer.parseInt(portText.getText()), userText.getText(), pongGame, ipText.getText());
                    connStatus.setText( connectionStatus = "connected");
                    connectBtn.setText("Connected");
                    connectBtn.setEnabled(false);
                    userText.setFocusable(false);
                    portText.setFocusable(false);
                    window.getContentPane().repaint();
                    
                    Thread t = new Thread(c);
                    t.start();
                
                } catch (Exception excpt) {
                    excpt.printStackTrace();
                    connStatus.setText( connectionStatus = "refused");
                    connectBtn.setEnabled(true);
                    userText.setFocusable(true);
                    portText.setFocusable(true);
                    window.getContentPane().repaint();
                }
			}
		});
        topPanel.add(connectBtn);
        topPanel.add(connStatus);
        topPanel.add(playerCnt);

// ************ CONSOLE THINGY *********//
        // special console to try and see the sysout output... remove once working
        if (DEBUG) {
            JTextArea consoleOutputArea = new JTextArea(15, 30);
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
            consoPanel.setPreferredSize(new Dimension(300,500));
            //Add Textarea in to middle panel
            consoPanel.add ( scroll );
            // consoPanel.add(consoleOutputArea);
            window.add(consoPanel, BorderLayout.CENTER);
        }
// ************ END CONSOLE THINGY *********//

        window.add(pongGame, BorderLayout.SOUTH);


        window.pack();
        window.setVisible(true);
        
        // SwingUtilities.invokeLater(Pong::new);
        paused = false;
        
    }

    public void run() {

        // game loop
        while (true) {
            if (!paused){
                // pongGame.moveball();
                // pongGame.movePaddles();
                // pongGame.checkCollisions();
                pongGame.repaint();

                // playerCnt.setText(""+pongGame.getPlayerCount());
                
                //publish the changes in the game to the server
                if (c != null  ) //how to only send changes??
                    c.sendUpdates();
            }    
            try {
                 Thread.sleep(1000/60);
            } catch (InterruptedException e) {}
         }
    }
}
