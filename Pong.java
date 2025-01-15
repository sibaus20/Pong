package Projects;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Pong {
    //For input tracking
    private static volatile boolean p1Up = false, p1Down = false, p2Up = false, p2Down = false, startGame = false;
    public static void main(String args[]){
        //JFrame setup to get inputs
        JFrame frame = new JFrame("Pong Game");
        frame.setSize(200,100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
        //Keylistener setup
        frame.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_W){
                    p1Up = true;
                }else if(e.getKeyCode() == KeyEvent.VK_S){
                    p1Down = true;
                }
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    p2Up = true;
                }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    p2Down = true;
                }
            }
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    p1Up = false;
                }else if (e.getKeyCode() == KeyEvent.VK_S) {
                    p1Down = false;
                }else if (e.getKeyCode() == KeyEvent.VK_SPACE){
                    startGame = !startGame;
                }
                if(e.getKeyCode() == KeyEvent.VK_UP){
                    p2Up = false;
                }else if(e.getKeyCode() == KeyEvent.VK_DOWN){
                    p2Down = false;
                }
            }
            public void keyTyped(KeyEvent e) {
                // Not used
            }
        });
        //Variable collection
        final int gameHeight = 10;
        final int gameWidth = 20;
        int p1Score = 0, p2Score = 0;
        
        boolean ballMovedThisInterval = false; //Used to increment score by 1, not by ballSpeedNerf
        int ballSpeedNerf = 5;
        int ballSpeedCounter = 0;
        int ballY = gameHeight / 2, ballX = 4;
        int ballDirX = 1, ballDirY = 1;
        int lPaddleY = gameHeight / 2;
        int rPaddleY = gameHeight / 2;
        final int lPaddleX = 1, rPaddleX = gameWidth - 1;

        boolean isStartScreen = true;
        //build the board
        while(true){
            if(isStartScreen){ //If the game hasn't begun/Paused
                System.out.println("\033[H\033[2J");
                System.out.println("\033[32m"+"Press Space to begin");
                if(startGame){
                    isStartScreen = false;
                }
                try{
                    Thread.sleep(50);
                }catch(InterruptedException e){   
                }
            }else{          //If the game has started
                //clear board
                System.out.println("\033[H\033[2J");
                System.out.flush();
                //Draw board
                System.out.println("P1 Press W or S to move\nP2 Press /\\ or \\/ to move\nPress Space to Pause");
                for(int y=0; y<gameHeight; y++){
                    for(int x=0; x<gameWidth; x++){
                        if(ballY == y && ballX == x){               //Ball
                            System.out.print("O");          
                        }else if(x == lPaddleX && y == lPaddleY){     //Left Paddle
                            System.out.print("|");
                        }else if(x == 0){                           //Left wall
                            System.out.print("]");
                        }else if(x == rPaddleX && y == rPaddleY){       //Right Paddle FINISH
                            System.out.print("|");
                        }else{                                      //Empty Space
                            System.out.print(" ");
                        }    
                    }
                    System.out.print("[\n");                      //Right Wall
                }
                System.out.println("P1's Score [ "+p1Score+ " ]\nP2's Score [ "+p2Score+" ] ");
                //Move Ball (Slower than refresh)
                ballSpeedCounter++;
                if(ballSpeedCounter == ballSpeedNerf){
                    ballX += ballDirX;
                    ballY += ballDirY;
                    ballSpeedCounter = 0;
                    ballMovedThisInterval = true;
                }
                //Move L Paddle
                if(ballMovedThisInterval){
                    if(p1Up && lPaddleY > 0){
                        lPaddleY--;
                    }else if(p1Down && lPaddleY < gameHeight-1){
                        lPaddleY++;
                    }
                }
                //Move R Paddle
                if(ballMovedThisInterval){
                    if(p2Up && rPaddleY > 0){
                        rPaddleY--;
                    }else if(p2Down && rPaddleY < gameHeight-1){
                        rPaddleY++;
                    }
                }

                //Ball X Direction Check
                if(ballX == gameWidth-1 && ballMovedThisInterval){ //Ball hits R wall
                    ballDirX = -1;
                    p2Score--;
                    ballMovedThisInterval = false;
                }else if(ballX == 2 && lPaddleY == ballY && ballMovedThisInterval){//Ball hits L paddle
                    ballDirX = 1;
                    p1Score++;
                    ballMovedThisInterval = false;
                }else if(ballX == gameWidth - 2 && rPaddleY == ballY && ballMovedThisInterval){
                    ballDirX = -1;
                    p2Score++;
                    ballMovedThisInterval = false;
                }else if(ballX == 1 && ballMovedThisInterval){     //Ball hits L wall
                    ballDirX = 1;
                    p1Score--;
                    ballMovedThisInterval = false;
                }
                //Ball Y Direction Check
                if(ballY == gameHeight-1){ //Ball hits floor
                    ballDirY = -1;
                }else if(ballY == 0){      //Ball hits roof
                    ballDirY = 1;
                }
                //Pause game 
                if(startGame == false){
                    isStartScreen = true;
                }
                
                try{
                    Thread.sleep(50);
                }catch(InterruptedException e){
                    
                }
            }
        }
    }
}
