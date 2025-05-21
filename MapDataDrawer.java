import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.util.stream.IntStream;

public class MapDataDrawer
{

   private int[][] grid;
   public DrawingPanel panel;
   private Graphics g;
   private TreeMap<Integer, Integer> topPaths;

   public MapDataDrawer(String filename, int rows, int cols){
      grid = new int[rows][cols];
      if (filename.toLowerCase().endsWith(".png")) {
         try {
            File imgFile = new File(filename);
            BufferedImage img = ImageIO.read(imgFile);
             // recheck the dimensions
            if (img.getWidth() != cols || img.getHeight() != rows) {
               System.out.println("Image dimensions do not match specified rows and cols.");
               System.out.println("Image: " + img.getWidth() + " x " + img.getHeight() + " y");
               rows = img.getHeight();
               cols = img.getWidth();
               grid = new int[rows][cols];
            }
            for (int i = 0; i < rows; i++) {
               for (int j = 0; j < cols; j++) {
                  int rgb = img.getRGB(j, i);
                  int red = (rgb >> 16) & 0xFF;
                  grid[i][j] = red;
               }
            }
         } catch (IOException e) {
            System.out.println("Error reading PNG file: " + filename);
         }
      } else {
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
     
     panel = new DrawingPanel(rows, cols);
     g = panel.getGraphics();
     topPaths = new TreeMap<>();
     fillTopPaths();
   }

  /**
   * @return flat Stream of grid
   */
   private IntStream toStream() {
     return Arrays.stream(grid).flatMapToInt((i) -> Arrays.stream(i));
   }

   /**
    * @return the min value in the entire grid
    */
   public int findMinValue(){
     /*
      int min = Integer.MAX_VALUE;
      for (int i = 0; i < grid.length; i++) {
         for (int j = 0; j < grid[i].length; j++) {
            if (grid[i][j] < min) {
               min = grid[i][j];
            }
         }
      }
      return min;
      */
     return toStream().min().orElse(0);
   }
   /**
    * @return the max value in the entire grid
    */
   public int findMaxValue(){
     /*
      int max = Integer.MIN_VALUE;
      for (int i = 0; i < grid.length; i++) {
         for (int j = 0; j < grid[i].length; j++) {
            if (grid[i][j] > max) {
               max = grid[i][j];
            }
         }
      }
      return max;
      */
     return toStream().max().orElse(0);
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
   public void drawMap(){
     drawMap(g);
   }
   public void drawMap(Graphics g){
      int min = findMinValue();
      int max = findMaxValue();
      int range = max - min;

      for (int i = 0; i < grid.length; i++) {
         for (int j = 0; j < grid[i].length; j++) {
            int value = grid[i][j];
            int colorValue = (int) ((value - min) * 255.0 / range);
            g.setColor(new Color(colorValue, colorValue, colorValue));
            g.fillRect(j, i, 1, 1);
         }
      }
   }

   /**
    * Find a path from West-to-East starting at given row.
    * Choose a foward step out of 3 possible forward locations, using greedy method described in assignment.
    * @return the total change in elevation traveled from West-to-East
    */
   public int drawLowestElevPath(int row){
      return drawLowestElevPath(g, row, Color.RED);
   }
   public int drawLowestElevPath(int row, Color c){
      return drawLowestElevPath(g, row, c);
   }
   public int drawLowestElevPath(Graphics g, int row){
      return drawLowestElevPath(g, row, Color.RED);
   }
   public int drawLowestElevPath(Graphics g, int row, Color c){
      int totalChange = 0;
      int currentRow = row;
      int currentCol = 0;

      while (currentCol < grid[0].length - 1) {
         g.setColor(c);
         g.fillRect(currentCol, currentRow, 1, 1);

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
    * Find a path from West-to-East starting at given row.
    * Choose a foward step out of 3 possible forward locations, using greedy method described in assignment.
    * @return the total change in elevation traveled from West-to-East
    */
  public int lowestElevPath(int row){
      int totalChange = 0;
      int currentRow = row;
      int currentCol = 0;

      while (currentCol < grid[0].length - 1) {
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
  private void fillTopPaths() {
    for (int i = 0; i < grid.length; i++){
      topPaths.put(lowestElevPath(i), i);
    }
  }
    

   /**
    * @return the index of the starting row for the lowest-elevation-change path in the entire grid.
    */
   public int indexOfLowestElevPath(){
     return indexOfLowestElevPath(g);
   }
   public int indexOfLowestElevPath(Graphics g){
      int minChange = Integer.MAX_VALUE;
      int bestRow = 0;

      for (int i = 0; i < grid.length; i++) {
         int change = drawLowestElevPath(i, Color.RED);
         if (change < minChange) {
            minChange = change;
            bestRow = i;
         }
      }
      drawLowestElevPath(bestRow, Color.GREEN);
      return bestRow;
   }
   public void drawTopPaths(int a){
     ArrayList<Map.Entry<Integer, Integer>> list = new ArrayList<>(topPaths.entrySet());
     for (int i = 0; i < a; i++){
       System.out.println(list.get(i));
       drawLowestElevPath(list.get(i).getValue());
     }
   }


}