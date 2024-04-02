package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;


public class Pong extends JPanel implements KeyListener{

    String gameState = "waiting"; // waiting, playing, game over
    boolean playerInitiated = false; // tracks if we already set up the player for this game


    public Pong(){
        //
        this.setPreferredSize(new Dimension(500,500));

    }


    public void drawGame(Graphics g){
    }
    
    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0,0,this.getWidth(), this.getHeight());

        
        switch (gameState) {
            case "waiting":
                // g.setColor(Color.BLACK);
                // g.fillRect(0,0,this.getWidth(), this.getHeight());
                g.setColor(Color.WHITE);
                g.drawString("waiting for server", 300, 300);
                break;
            case "playing":
                drawGame(g);
                break;
            case "Game Over":
                break;

            default:
                g.drawString("something went wrong... :/", 300, 300);
                break;
        }
    }

    public void keyPressed(KeyEvent e) {
        // to access and modify the instance variables of PONG, we are going to call this method from game class:
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_DOWN:
                
                break;
            case KeyEvent.VK_UP:
                
                break;
            case KeyEvent.VK_LEFT:
                
                break;
            case KeyEvent.VK_RIGHT:
                
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        // we must implement all three, Pressed, Released, Typed, but for this use case we don't need all of them
        // we can leave this one blank
        int keyCode = e.getKeyCode();
        System.out.println(keyCode);

        switch (keyCode) {
            case KeyEvent.VK_DOWN:
                
                break;
            case KeyEvent.VK_UP:
                
                break;
            case KeyEvent.VK_LEFT:
                
                break;
            case KeyEvent.VK_RIGHT:
                
                break;
        }
    }

    public void keyTyped(KeyEvent e) {
        // we must implement all three, Pressed, Released, Typed, but for this use case we don't need all of them
        // we can leave this one blank
    }

}
