import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        JFrame jframe = new JFrame(); //new JFrame object
        GamePlay gp = new GamePlay(); //new GamePlay object

        jframe.setBounds(10,10,700,600); //setting the x,y,height and width of the frame
        jframe.setResizable(false); //user should not be able to resize
        jframe.setTitle("Best Brick Breaker Game"); //setting the title
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //setting the default close operation
        jframe.setVisible(true); //make the frame visible
        jframe.add(gp); //add a gameplay object(component)
    }
}