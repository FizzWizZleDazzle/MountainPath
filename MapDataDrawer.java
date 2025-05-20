import java.awt.*;
import java.io.*;
import java.util.*;

public class MapDataDrawer
{

   private int[][] grid;

   public MapDataDrawer(String filename, int rows, int cols){
      // initialize grid
      //read the data from the file into the grid
      grid = new int[rows][cols];
      try {
         Scanner sc = new Scanner(new File(filename));
         for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
               grid[i][j] = sc.nextInt();
            }
         }
         sc.close();
      } catch (FileNotFoundException e) {
         System.out.println("File not found: " + filename);
      }

   }

   /**
    * @return the min value in the entire grid
    */
   public int findMinValue(){
      int min = Integer.MAX_VALUE;
      for (int i = 0; i < grid.length; i++) {
         for (int j = 0; j < grid[i].length; j++) {
            if (grid[i][j] < min) {
               min = grid[i][j];
            }
         }
      }
      return min;
   }
   /**
    * @return the max value in the entire grid
    */
   public int findMaxValue(){
      int max = Integer.MIN_VALUE;
      for (int i = 0; i < grid.length; i++) {
         for (int j = 0; j < grid[i].length; j++) {
            if (grid[i][j] > max) {
               max = grid[i][j];
            }
         }
      }
      return max;
   }

   /**
    * @param col the column of the grid to check
    * @return the index of the row with the lowest value in the given col for the grid
    */
   public int indexOfMinInCol(int col){
      int minRow = 0;
      int minValue = Integer.MAX_VALUE;
      for (int i = 0; i < grid.length; i++) {
         if (grid[i][col] < minValue) {
            minValue = grid[i][col];
            minRow = i;
         }
      }
      return minRow;
      
   }

   /**
    * Draws the grid using the given Graphics object.
    * Colors should be grayscale values 0-255, scaled based on min/max values in grid
    */
   public void drawMap(Graphics g){
      int min = findMinValue();
      int max = findMaxValue();
      int range = max - min;

      for (int i = 0; i < grid.length; i++) {
         for (int j = 0; j < grid[i].length; j++) {
            int value = grid[i][j];
            int colorValue = (int) ((value - min) * 255.0 / range);
            g.setColor(new Color(colorValue, colorValue, colorValue));
            g.fillRect(j * 10, i * 10, 10, 10);
         }
      }
   }

   /**
    * Find a path from West-to-East starting at given row.
    * Choose a foward step out of 3 possible forward locations, using greedy method described in assignment.
    * @return the total change in elevation traveled from West-to-East
    */
   public int drawLowestElevPath(Graphics g, int row){
      int totalChange = 0;
      int currentRow = row;
      int currentCol = 0;

      while (currentCol < grid[0].length - 1) {
         g.setColor(Color.RED);
         g.fillRect(currentCol * 10, currentRow * 10, 10, 10);

         int right = grid[currentRow][currentCol + 1];
         int downRight = (currentRow < grid.length - 1) ? grid[currentRow + 1][currentCol + 1] : Integer.MAX_VALUE;
         int upRight = (currentRow > 0) ? grid[currentRow - 1][currentCol + 1] : Integer.MAX_VALUE;

         int minChange = Math.min(Math.abs(grid[currentRow][currentCol] - right), Math.min(Math.abs(grid[currentRow][currentCol] - downRight), Math.abs(grid[currentRow][currentCol] - upRight)));

         if (minChange == Math.abs(grid[currentRow][currentCol] - right)) {
            currentCol++;
         } else if (minChange == Math.abs(grid[currentRow][currentCol] - downRight)) {
            currentRow++;
            currentCol++;
         } else {
            currentRow--;
            currentCol++;
         }

         totalChange += minChange;
      }
      return totalChange;
   }

   /**
    * @return the index of the starting row for the lowest-elevation-change path in the entire grid.
    */
   public int indexOfLowestElevPath(Graphics g){
      int minChange = Integer.MAX_VALUE;
      int bestRow = 0;

      for (int i = 0; i < grid.length; i++) {
         int change = drawLowestElevPath(g, i);
         if (change < minChange) {
            minChange = change;
            bestRow = i;
         }
      }
      return bestRow;
   }


}