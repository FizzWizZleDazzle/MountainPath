
public class Driver
{
   public static void main(String[] args){

      //Test Step 1 - construct mountain map data
      MapDataDrawer map = new MapDataDrawer("h.png", 1920, 911);

      //Test Step 2 - min, max, minRow in col
      int min = map.findMinValue();
      System.out.println("Min value in map: "+min);

      int max = map.findMaxValue();
      System.out.println("Max value in map: "+max);

      int minRow = map.indexOfMinInCol(0);
      System.out.println("Row with lowest val in col 0: "+minRow);

      //Test Step 3 - draw the map
      map.drawMap();

      //Test Step 4 - draw a greedy path

      int totalChange = map.drawLowestElevPath(minRow); //use minRow from Step 2 as starting point
      System.out.println("Lowest-Elevation-Change Path starting at row "+minRow+" gives total change of: "+totalChange);

      //Test Step 5 - draw the best path
      int bestRow = map.indexOfLowestElevPath();

      //map.drawMap(g); //use this to get rid of all red lines

      totalChange = map.drawLowestElevPath(bestRow);
      System.out.println("The Lowest-Elevation-Change Path starts at row: "+bestRow+" and gives a total change of: "+totalChange);

      //map.drawMap();
      //map.drawTopPaths(10);
      //map.panel.rescaleToScreen();
   }
}