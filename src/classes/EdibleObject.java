package classes;
import java.util.Random;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


public class EdibleObject {
    
    private int[] coordinates;

    public EdibleObject(GridPane gridPane) {
        this.coordinates = getObjectCoordinates(gridPane);
    }

     public static void spawnFruit(GridPane gridPane){ //this method is responsible for spawning a fruit in an available cell on the grid
        Circle fruit = createCircle();
        Random random = new Random();
        
          
        int randomRow = random.nextInt(14);
        int randomColumn = random.nextInt(14);
        
        Boolean circleFound = false;
        for (Node node : gridPane.getChildren()){
               if (node instanceof StackPane){
                    int snakeCoordRow = GridPane.getRowIndex(node);
                    int snakeCoordColumn = GridPane.getColumnIndex(node);
                    if (snakeCoordColumn == randomColumn && snakeCoordRow == randomRow){
                         randomRow = random.nextInt(14);
                         randomColumn = random.nextInt(14);
                         circleFound = false;
                    }
               }
               if (node instanceof Circle){
                    circleFound = true;
                    return;
               }    
        }

        if(!circleFound){
          GridPane.setColumnIndex(fruit, randomColumn);
          GridPane.setRowIndex(fruit, randomRow);
          gridPane.getChildren().add(fruit);
        }
    }

    private static Circle createCircle(){
          Circle circle = new Circle(17, Color.ORANGERED);
          circle.setTranslateX(7.5);
          circle.toFront();
          return circle;
     }

     public static void deSpawnObject(GridPane gridPane){
          int[] coordsObject = getObjectCoordinates(gridPane);          
          if (coordsObject != null){
               Node node = getChild(gridPane, coordsObject[0], coordsObject[1]);
               if (node instanceof Circle){
                    gridPane.getChildren().remove(node);
               }
          }
     }

     public static Node getChild(GridPane gridPane,int row, int column){
          for (Node node : gridPane.getChildren()){
               if (node instanceof Circle){
                    int rowIndex = GridPane.getRowIndex(node);
                    int columnIndex = GridPane.getColumnIndex(node);
                    if (rowIndex == row && columnIndex == column){
                         return node;
                    }
               }
          }
          return null;
     }

        
     public static int[] getObjectCoordinates(GridPane gridPane){
        for(Node node : gridPane.getChildren()){
             if (node instanceof Circle){
               int row = GridPane.getRowIndex(node);
               int column = GridPane.getColumnIndex(node);
               return new int[]{row, column};
          }

        }
        return null;
   }

   public String toString(){
     return String.format("Row: %d Column: %d", this.coordinates[0], this.coordinates[1]);
   }

}
