package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientHandler extends Thread  {

    DataInputStream in;
    DataOutputStream out;
    ObjectOutputStream objectOut;
    ObjectInputStream objectIn;
    Socket s;
    String user;

    public ClientHandler(Socket s) throws IOException{ //, DataInputStream i, DataOutputStream o) {
        // set up the socket
        this.s =s;
        
        // obtaining input and out streams 
        InputStream is = s.getInputStream(); 
        OutputStream os = s.getOutputStream(); 
        
        this.objectOut = new ObjectOutputStream(os);
        this.objectIn = new ObjectInputStream(is);

        this.in = new DataInputStream(is) ;
        this.out = new DataOutputStream(os);
        
        // All set.
    }

    public void run(){
        System.out.println("clientHandler started.");
        String received; 

        try {
            out.writeUTF("Thanks for connecting");
            out.writeUTF("please identify yourself");
            
            // establish who this client is - need a unique ID
            // receive the answer from client 
            received = in.readUTF();
            user = received;
            System.out.println("Client identified as: " + user);
            
            if (Server.connections.get(received) == null) { //is this a new player in the game?
                Server.connections.put(user,this);
                // now go listen for the back and forth, when that dies the catch here should get it
            
            }
            else {
                // means this client already connected... refuse
                out.writeUTF(user + " already connected, closing this connection");
                in.close(); 
                out.close(); 
                s.close();
            }
            
            while (true) {
                
                // try {
                    // this would wait to read in an object from the other side
                    // Paddle myPaddle = (Paddle) objectIn.readObject();
            
                // } catch (ClassNotFoundException e) {
                //     System.out.println("Couldn't read the class sent...");
                //     // e.printStackTrace();
                // }

                //now check if there are changes I need to send my client

            }


            // server should tell the client were to spawn
            // what if I made a paddle/player object here and passed over?

        } catch (IOException e) {
            System.out.println("connection lost");
            Server.connectionCount--;
            Server.connCnt.setText("conns:" + Server.connectionCount);
            Server.connections.remove(user);
            // remove this client from the list, once we have one

            e.printStackTrace();
        }

        // socket loop
        //    check if there are changes from other clients
        //      send those changes to this socket
        //    receive changes from this socket
        //      share those changes with the server for others to read
        // repeat
    }

}
