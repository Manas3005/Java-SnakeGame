package classes;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.LinkedList;

public class SnakeBody {

    int xCordOne = 5;
    int xCordTwo = 6;
    int headxCord = 7;
    final int yCord = 8;
    private LinkedList<StackPane> snakeBody;

    private Direction currentDirection;

    private Controller controller;

    StackPane head = new StackPane(); //initalises the head as a StackPane on the grid
    StackPane bodyPartTwo = new StackPane();
    StackPane bodyPartThree = new StackPane();
    boolean isFruitEaten = false;
    boolean isSnakeDead = false;

    static int baseRed = 140;
    static int baseGreen = 0;
    static int baseBlue = 50;
    public SnakeBody(Controller controller) {
        this.snakeBody = new LinkedList<>();
        this.currentDirection = Direction.RIGHT;
        this.controller = controller;
    }

    public Direction getCurrentDirection(){
        return this.currentDirection;
    }
    public void setCurrentDirection(Direction direction){
        this.currentDirection = direction;
    }

    public void moveSnake(GridPane gridPane) {
        checkDeathCollision(gridPane);
        if(checkEatenFruit(gridPane)){
            EdibleObject.deSpawnObject(gridPane);
            growSnake(gridPane);
            EdibleObject.spawnFruit(gridPane);
            Grid.updateCurrentScore();
        }

        int incrementX = 0;
        int incrementY = 0;

        switch(currentDirection){
            case UP -> incrementY = -1;
            case DOWN -> incrementY = 1;
            case LEFT -> incrementX = -1;
            case RIGHT -> incrementX = 1;
        }

        for(int i = snakeBody.size() -1; i > 0; i--){

            StackPane snakeBackBodyPart = snakeBody.get(i);
            StackPane snakeFrontBodyPart = snakeBody.get(i-1);

            GridPane.setColumnIndex(snakeBackBodyPart, GridPane.getColumnIndex(snakeFrontBodyPart));
            GridPane.setRowIndex(snakeBackBodyPart, GridPane.getRowIndex(snakeFrontBodyPart));
        }

        head = snakeBody.getFirst();

        int currentRowIndex = GridPane.getRowIndex(head);
        int currentColumnIndex = GridPane.getColumnIndex(head);

        int newRowIndex = currentRowIndex + incrementY;
        int newColumnIndex = currentColumnIndex + incrementX;
        //If moving up current row is set to -1 giving errors
        GridPane.setRowIndex(head, newRowIndex);
        GridPane.setColumnIndex(head, newColumnIndex);
        bodyDirection(head, currentDirection);
    }

    private void bodyDirection(StackPane snakeHead, Direction currentDirection){
        double angle = 0;
        switch(currentDirection){
            case UP -> angle = -90;
            case DOWN ->  angle = 90;
            case RIGHT -> angle = 0;
            case LEFT -> angle = 180;
        }
        snakeHead.setRotate(angle);
    }

    public void growSnake(GridPane gridPane) { //this method is responsible for growing the snake

        StackPane oldSnakeTailPane = snakeBody.getLast();

        int oldTailRowIndex = GridPane.getRowIndex(oldSnakeTailPane); //find and assign old tail coordinates
        int oldTailColumnIndex = GridPane.getColumnIndex(oldSnakeTailPane);

        Rectangle newSnakeTailElement = new Rectangle();
        changeBodyGradient();
        newSnakeTailElement = drawBodyPartNode(); //create and assign dimensions and color to new snake rectanlge bodypart

        StackPane newSnakeTailPane = new StackPane();
        newSnakeTailPane.getChildren().addAll(newSnakeTailElement);
        newSnakeTailPane.setRotate(oldSnakeTailPane.getRotate()); //this re-orientates the added snake bodypart to the rest of the snake body


        gridPane.add(newSnakeTailPane, oldTailColumnIndex, oldTailRowIndex); //add these new aspects to the gridpane

        snakeBody.add(newSnakeTailPane); //adding the new element to the snakeBody LinkedList
    }


    public Rectangle drawBodyPartNode() { //creates the Rectangle object and gives it a color
        Rectangle snakeRectangle = new Rectangle(50, 50);
        Color gradientBodyColor = Color.rgb(baseRed, baseGreen, baseBlue);
        snakeRectangle.setFill(gradientBodyColor);
        return snakeRectangle;
    }


    public void spawnSnakeBody(int snakeLength, GridPane gridPane) { //this spawns the snake body
        for (int i = 0; i < snakeLength; i++) {
            Rectangle snakeRectangle = drawBodyPartNode(); //CREATE the rectangle for each bodyPart (3 times)
            //StackPane bodyPart = new StackPane(); //create the bodypart (3 times)
            //bodyPart.getChildren().addAll(snakeRectangle);
            //    snakeBody.add(bodyPartOne); //add the body part to the LinkedList (3 times)
            if (i == 2) {
                bodyPartThree.getChildren().addAll(snakeRectangle);
                gridPane.add(bodyPartThree, xCordOne, yCord); //adds first bodyPart to grid
                snakeBody.add(bodyPartThree);

            }
            if (i == 1) {
                bodyPartTwo.getChildren().addAll(snakeRectangle);
                gridPane.add(bodyPartTwo, xCordTwo, yCord); //adds second bodyPart to grid
                snakeBody.add(bodyPartTwo);
            }
            if (i == 0) {
                Circle eyeOne = new Circle();
                Circle eyeTwo = new Circle();
                Circle eyeBallOne = new Circle();
                Circle eyeBallTwo = new Circle();
                Arc tongue = new Arc(0, 0, 13, 10, -90, 180); //Parameters are centerX, centerY, radiusX, radiusY, startAngle, length
                tongue.setType(ArcType.ROUND);
                tongue.setFill(Color.RED);
                eyeOne.setFill(Color.WHITE);
                eyeBallOne.setFill(Color.BLACK);
                eyeTwo.setFill(Color.WHITE);
                eyeBallTwo.setFill(Color.BLACK);
                eyeOne.setRadius(5);
                eyeBallOne.setRadius(2);
                eyeTwo.setRadius(5);
                eyeBallTwo.setRadius(2);
                head.getChildren().addAll(snakeRectangle, eyeOne, eyeTwo, eyeBallOne, eyeBallTwo, tongue);
                StackPane.setMargin(eyeOne, new Insets(-12, -4, 0, 0)); //Parameters are double top, double right, double bottom, double left
                StackPane.setMargin(eyeTwo, new Insets(0, -4, -12, 0));
                StackPane.setMargin(eyeBallOne, new Insets(-12, -7, 0, 0));
                StackPane.setMargin(eyeBallTwo, new Insets(0, -7, -12, 0));
                StackPane.setMargin(tongue, new Insets(-6, -39, -6, 0));
                snakeBody.add(head);
                head.toFront();

                gridPane.add(head, headxCord, yCord);
            }
        }
        resetGradient();
    }



    public int[] getHeadCoordinates () { //retrieves the coordinates of the position of the head in the grid
        StackPane head = snakeBody.getFirst();
        int rowIndex = GridPane.getRowIndex(head);
        int columnIndex = GridPane.getColumnIndex(head);
        return new int [] {rowIndex,columnIndex};
    }

    public boolean checkDeathCollision(GridPane gridPane) {
        StackPane head = snakeBody.getFirst();
        for (Node node : gridPane.getChildren()) { //this checks self collision
            if (node instanceof StackPane && node!= head) { //makes sure it does not iterate through head
                int row = GridPane.getRowIndex(node);
                int column = GridPane.getColumnIndex(node);
                int[] torsoCoordinates = new int[]{row,column};
                if(Arrays.equals(torsoCoordinates, getHeadCoordinates())){
                    isSnakeDead = true;
                    resetGradient();
                    controller.stopTimeline();
                    controller.makeGameOverScreenVisible();
                }
                else {
                    isSnakeDead = false;
                }
            }
        }
        if(getHeadCoordinates()[0]<0 || getHeadCoordinates()[1]<0 || getHeadCoordinates()[0]>15 || getHeadCoordinates()[1]>14 ) { //this checks boundary collision
            isSnakeDead = true;
            controller.stopTimeline();
            controller.makeGameOverScreenVisible();
        }
        return isSnakeDead;
    }

    
    public boolean checkEatenFruit(GridPane gridPane) { //checks if the snake has eaten the fruit
            if (Arrays.equals(EdibleObject.getObjectCoordinates(gridPane), getHeadCoordinates())) {
                isFruitEaten = true;
            } else {
                isFruitEaten = false;
            }
            return isFruitEaten;

        }


    public void changeBodyGradient() { //this method is responsible for controlling the color gradient of the snake body
        if (baseRed < 40) {
            baseGreen = baseGreen + 10;
        } else if (baseBlue > 130) {
            baseRed = baseRed + 10;
        } else {
            baseRed = baseRed - 10;
            baseBlue = baseBlue + 10;
        }
        if(baseRed > 190 || baseBlue > 190 || baseGreen > 190 || baseGreen < 0 || baseRed < 0 || baseBlue < 0){
            baseRed = 140; //this makes sure rgb values stay in between 0 and 255
            baseGreen = 0;
            baseBlue = 50;
        }

    }

    public void resetGradient (){ //this method is responsible for resetting the gradient
        if(isSnakeDead || controller.getIsSwitchedToMenu()){
            baseRed = 140;
            baseGreen = 0;
            baseBlue = 50;
        }
        
    }

}
