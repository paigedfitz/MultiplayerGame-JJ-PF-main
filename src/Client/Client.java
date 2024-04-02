package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class Client implements Runnable {
    
    Socket socket;
    String user;
    DataInputStream dis;
    DataOutputStream dos;
    ObjectOutputStream objectOut;
    ObjectInputStream objectIn;
    
    Pong pongGame;



    public Client(int port, String u, Pong game, String ipString ) throws Exception{
        // getting localhost ip 
        InetAddress ip = InetAddress.getByName(ipString); 
      
        // establish the connection with server port 5056       
        socket = new Socket(ip, port);

        this.user = u;

        // make sure we have a reference to the game that will be played
        this.pongGame = game;

    }

    @Override
    public void run() {
        System.out.println("client running");
        // obtaining input and out streams 

        try {
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream()); 
            objectIn = new ObjectInputStream(socket.getInputStream());
            objectOut = new ObjectOutputStream(socket.getOutputStream());

            // the following performs the exchange of 
            // information between client and client handler 
            String received = dis.readUTF(); 
            System.out.println(received); 

            received = dis.readUTF(); 
            System.out.println(received); 

            if(received.equals("please identify yourself")) {
                dos.writeUTF(user);
            }
            
            // read the object from the server
                //objectIn.readObject();
            // now make this the object be the player...
            // and start the game if you can
            // this loop will now listen for updates on other players
            while (true) {
                
                // reads from server:
                //updateObj ob = (updateObj) objectIn.readObject();
                // need a user:paddle object to check if we have it

            }
    
        } catch (IOException e) {
            System.out.println("error reading, this usually means the connection closed.");
            // how do I get the connection button back to working from here??
            e.printStackTrace();
        } 
        // catch (ClassNotFoundException e) {
        //     // TODO Auto-generated catch block
        //     e.printStackTrace();
        // } 

    }

    public void sendUpdates(){
        
    }
}