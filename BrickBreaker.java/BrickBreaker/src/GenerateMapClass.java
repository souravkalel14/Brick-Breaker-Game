import java.awt.*;
import java.util.Arrays;

public class GenerateMapClass {
    int[][] map; //map for all bricks
    int brickWidth; //brick width
    int brickHeight; //brick height
    GenerateMapClass(int rows, int cols){
        /*
        Generator function to fill the map with 1s and set the brickWidth and brickHeight
        Initialises a map of bricks with dimension rows*cols
        The map has rows*cols number of bricks
         */
        map = new int[rows][cols];
        for(int[] m:map)
            Arrays.fill(m,1); //fill the map with 1, indicating that each brick has not been hit

        //setting the dimension of the brick based on the row and column size
        brickHeight = 150/rows;
        brickWidth = 540/cols;
    }
    public void makeBricks(Graphics2D brickGrid){
        /*
        Function to generate a grid for the bricks
         */
        for(int i=0;i<map.length;i++){
            for(int j=0;j<map[0].length;j++){
                if(map[i][j]>0) { //if brick has not been broken
                    brickGrid.setColor(Color.red);
                    brickGrid.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight); //draw a solid red color rectangular brick
                    brickGrid.setStroke(new BasicStroke(3.0f)); //border width set to 10.0
                    brickGrid.setColor(Color.black);
                    brickGrid.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight); //draw black outline of the rectangular brick
                }
            }
        }
    }
    public void setVal(int row, int col, int val){
        map[row][col] = val;
    }
}