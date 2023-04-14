import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.Scanner;

public class GamePlay extends JPanel implements ActionListener, KeyListener {
    private int score = 0;
    private boolean isPlaying = false;
    private int totalBricks = 0;
    private final int delay = 8; //delay before game starts after losing
    private int playerX = 310; //position of player at start
    private int ballposX = 120; //initial x-position of ball
    private int ballposY = 350; //initial y-position of ball
    private int ballXdir = -1; //X-direction of the ball
    private int ballYdir = -2; //Y-direction of the ball
    private final Timer timer;
    private GenerateMapClass map;
    private int Highest_score = 0;
    GamePlay() throws IOException{
        map = new GenerateMapClass(3,7);
        totalBricks = 3*7;
        this.addKeyListener(this);
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false); //focus traversal keys like TAB, SHIFT+TAB etc. can't be used to move to the next component
        timer = new Timer(delay,this);
        timer.start();
        try {
            Scanner sc = new Scanner(new File("highscore.txt"));
            Highest_score = Integer.parseInt(sc.next());
        }
        catch (FileNotFoundException e)
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"));
            writer.write(0+"");
            writer.close();
        }
    }

    public void paint(Graphics graphics){
        //background
        graphics.setColor(Color.black);
        graphics.fillRect(1,1,692,592);
        map.makeBricks((Graphics2D) graphics);

        //border
        graphics.setColor(Color.yellow);
        graphics.fillRect(0,0,3,592); //leftmost column
        graphics.fillRect(0,0,692,3); //bottom most column

        //score
        graphics.setColor(Color.white);
        graphics.setFont(new Font("serif",Font.BOLD,24));
        graphics.drawString(score+"",590,30);

        //Wedge
        graphics.setColor(Color.yellow);
        graphics.fillRect(playerX,550,100,8);

        //Ball
        graphics.setColor(Color.green);
        graphics.fillOval(ballposX,ballposY,20,20);

        try {
            Scanner sc = new Scanner(new File("highscore.txt"));
            Highest_score = Integer.parseInt(sc.next());
        }
        catch (FileNotFoundException e)
        {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"));
                writer.write(0+"");
                writer.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (ballposY > 570) { //Ball touched the floor
            isPlaying = false;
            ballXdir = 0;
            ballYdir = 0;
            graphics.setColor(Color.red);
            if(score > Highest_score)
            {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"));
                    writer.write(score+"");
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(score>=Highest_score){
                graphics.setColor(Color.red);
                Highest_score = score;
                graphics.setFont(new Font("serif", Font.BOLD, 30));
                graphics.drawString("  High Score: " + score, 190, 260);
            }
            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("Game Over Score: " + score, 190, 300);
            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("   Press Enter to Restart", 190, 340);

        }
        if(totalBricks == 0){ //all bricks broken by player
            isPlaying = false;
            ballYdir = -2;
            ballXdir = -1;
            graphics.setColor(Color.red);
            if(score > Highest_score)
            {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"));
                    writer.write(score+"");
                    writer.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(score>=Highest_score){
                graphics.setColor(Color.red);
                Highest_score = score;
                graphics.setFont(new Font("serif", Font.BOLD, 30));
                graphics.drawString("  High Score: " + score, 190, 300);
            }
            graphics.setFont(new Font("serif",Font.BOLD,30));
            graphics.drawString("  You Won: "+score,190,300);
            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("   Press Enter to Restart", 190, 340);

        }
        graphics.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (isPlaying) {
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) //we have made a rectangle around the ball to check for collision
                ballYdir = -ballYdir; //on collision, ball's y-direction gets reversed

            loops://we use keyword:(any letter can be used like x: it is the syntax for breaking out of loops) to break out of multiple nested loops.
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) { //if brick has not been broken already
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int bricksWidth = map.brickWidth;
                        int bricksHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, bricksWidth, bricksHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);

                        if (ballRect.intersects(brickRect)) { //Collision with a brick
                            map.setVal( i, j,0); //set the brick value to 0
                            totalBricks--; //decrement the bricks remaining
                            score += 5; //hitting each brick gives 5 points
                            if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + bricksWidth) //if ball hits brick horizontally from left or right side
                                ballXdir = -ballXdir; //reverse X direction of ball
                            else
                                ballYdir = -ballYdir; //reverse Y direction of ball
                            break loops;
                        }
                    }
                }
            }
            ballposX += ballXdir;
            ballposY += ballYdir;
            if (ballposX < 0) //ball goes out of bounds on left
                ballXdir = -ballXdir;
            if (ballposY < 0)  //ball goes above the roof
                ballYdir = -ballYdir;
            if (ballposX > 670) //ball goes out of bounds on right
                ballXdir = -ballXdir;
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) //if player tries to move wedge out of bounds on the right side
                playerX = 600;
            else
                moveRight();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) //if player tries to move wedge out of bounds on the left side
                playerX = 10;
            else
                moveLeft();
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!isPlaying) { //Restart game with default values
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                score = 0;
                playerX = 310;
                totalBricks = 21;
                map = new GenerateMapClass(3, 7);
                repaint(); //inbuilt function that invokes the paint method
            }
        }
    }
    public void moveLeft(){
        isPlaying = true;
        playerX -= 20;
    }
    public void moveRight(){
        isPlaying = true;
        playerX += 20;
    }
    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}