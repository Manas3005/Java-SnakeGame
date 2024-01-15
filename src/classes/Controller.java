package classes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class Controller{

    private static String inputtedPlayerName;

    @FXML
    private TextField playerNameField;

    @FXML
    private  Label firstPlayerName;

    @FXML
    private  Label firstPlayerHighScore;

    @FXML
    private  Label secondPlayerName;

    @FXML
    private  Label secondPlayerHighScore;

    @FXML
    private  Label thirdPlayerName;

    @FXML
    private  Label thirdPlayerHighScore;

    @FXML
    private  Label fourthPlayerName;

    @FXML
    private  Label fourthPlayerHighScore;

    @FXML
    private  Label fifthPlayerName;

    @FXML
    private  Label fifthPlayerHighScore;

    @FXML
    private Label continueAsNameLabel;
    @FXML
    private Stage primaryStage;
    @FXML
    private Scene scene;
    @FXML
    private Parent root;

    @FXML
    private GridPane gridPane;

    @FXML
    private AnchorPane pausedModalPanel;

    @FXML
    private AnchorPane gameOverScreen;

    private boolean switchedToMenu = false;

    private AudioEffect audioEffect;

    private SnakeBody snakeBody;


    private static boolean isNameSaved = false;
    private static Timeline gameTimeline;

    private static Leaderboard leaderboard = new Leaderboard();


    @FXML
    private static Label currentScoreNumber;

    @FXML
    private static Label highScoreNumber;

    @FXML
    private Button saveNameButton;

    @FXML
    private Button pauseButton;

    @FXML
    private Label enterNameLabel;

    @FXML
    private Label invalidNameLabel;


    static double NORMAL_SPEED = 200;
    static double FAST_SPEED = 100;
    static double SLOW_SPEED = 300;
    static double snakeSpeed = NORMAL_SPEED;



    @FXML
    public void slowSpeedSelected(){
            snakeSpeed = SLOW_SPEED;
    }

    @FXML
    public void normalSpeedSelected(){
            snakeSpeed = NORMAL_SPEED;
    }

    @FXML
    public void fastSpeedSelected(){
            snakeSpeed = FAST_SPEED;
    }

    /*-------------------------------------------------------------------------------------------------*/

    /*---------------------------------------Overloaded methods to get the appropriate stage---------------------------*/
    public void fetchStage(File fileName, ActionEvent event) throws IOException {//ActionEvent
        Parent root = FXMLLoader.load(getClass().getResource("/resources/" + String.valueOf(fileName)));
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void fetchStage(File fileName, MouseEvent event) throws IOException {//MouseEvent
        Parent root = FXMLLoader.load(getClass().getResource("/resources/" + String.valueOf(fileName)));
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /*---------------------------------------------------------------------------------------------------*/

    /*-----------------------------------------SWITCH SCENES METHODS-------------------------------------*/
    public void switchToStartSceneMouse(MouseEvent event) throws IOException {
        fetchStage(new File("StartMenu.fxml"), event);
        audioEffect = new AudioEffect(new File("tooCrazy.wav"));
        audioEffect.playEffect();

}
    public void switchToStartSceneBtn(ActionEvent event) throws IOException {
        fetchStage(new File("StartMenu.fxml"), event);
        Grid.currentScore = 0;

        if (leaderboard.getLeaderboard().isEmpty()){
            System.out.print("Empty list");
        }

        for (int i= 0; leaderboard.getLeaderboard().size() > i; i++) {
            System.out.println(leaderboard.getLeaderboard().get(i).getPlayerName());
            System.out.println(leaderboard.getLeaderboard().get(i).getHighScore());
        }


        if (audioEffect ==  null){
            audioEffect = new AudioEffect(new File("tooCrazy.wav"));
            audioEffect.playEffect();
        }
        audioEffect = new AudioEffect(new File("buttonClick.wav"));
        audioEffect.playEffect();

    }

    @FXML
    public void generateGrid(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("/resources/gameView.fxml"));
        scene = new Scene(root);
        primaryStage.setScene(scene);

        this.gridPane = (GridPane) root.lookup("#gridPane");
        this.pausedModalPanel = (AnchorPane) root.lookup("#pausedModalPanel");
        this.gameOverScreen = (AnchorPane) root.lookup("#gameOverScreen");
        this.pauseButton = (Button) root.lookup("#pauseButton");
        this.currentScoreNumber = (Label) root.lookup("#currentScoreNumber");
        this.highScoreNumber = (Label) root.lookup("#highScoreNumber");

        updateGridHighScoreLabel();
        runGame(this.gridPane);
        pausedModalPanel.setVisible(false);
        gameOverScreen.setVisible(false);
    }


    @FXML
    public void switchToMenu(ActionEvent event) throws IOException{
        switchedToMenu = true;
        fetchStage(new File("StartMenu.fxml"), event);
        Grid.setHighScore(Grid.currentScore);
        leaderboard.getLeaderboard().get(leaderboard.getLeaderboard().indexOf(returnPlayerThroughPlayerName(inputtedPlayerName))).updatePlayerHighScore(Grid.highScore);
        Grid.currentScore = 0;
    }


    @FXML
    public void switchToDifficultySettings(ActionEvent event) throws IOException {
        fetchStage(new File("difficultySettings.fxml"), event);                                                                                                                                                                                                                                                                                                                                       
        audioEffect = new AudioEffect(new File("buttonClick.wav"));
        audioEffect.playEffect();
        normalSpeedSelected();
    }

    public void switchToNameHandlingScene (ActionEvent event) throws IOException {
      if (leaderboard.getLeaderboard().isEmpty()){
          switchToNameScene(event);

      }else {
          switchToContinueAsSetNameScene(event);
      }
    }

    public void switchToContinueAsSetNameScene (ActionEvent event) throws IOException{
        fetchStage(new File("continueAsSetName.fxml"), event);
        root = FXMLLoader.load(getClass().getResource("/continueAsSetName.fxml"));
        scene = new Scene(root);
        this.primaryStage.setScene(scene);
        this.primaryStage.show();
        this.continueAsNameLabel = (Label) root.lookup("#continueAsNameLabel");
        continueAsNameLabel.setText(inputtedPlayerName);
    }
                                                                                                               
    public void switchToNameScene(ActionEvent event) throws IOException {
        fetchStage(new File("playerName.fxml"), event);
        root = FXMLLoader.load(getClass().getResource("/playerName.fxml"));

        this.saveNameButton = (Button) root.lookup("#saveNameButton");
        this.playerNameField = (TextField) root.lookup("#playerNameField");
        this.enterNameLabel = (Label) root.lookup("enterNameLabel");
        this.invalidNameLabel = (Label) root.lookup("#invalidNameLabel");
        isNameSaved = false;

        audioEffect = new AudioEffect(new File("buttonClick.wav"));
        audioEffect.playEffect();
    }

    public void switchToLeaderboardScene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("/Leaderboard.fxml"));
        primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);

        if (leaderboard.getLeaderboard().isEmpty()){
            System.out.print("Empty list");
        }

        for (int i= 0; leaderboard.getLeaderboard().size() > i; i++) {
            System.out.println(leaderboard.getLeaderboard().get(i).getPlayerName());
            System.out.println(leaderboard.getLeaderboard().get(i).getHighScore());
        }

        this.firstPlayerName = (Label) root.lookup("#firstPlayerName");
        this.firstPlayerHighScore = (Label) root.lookup("#firstPlayerHighScore");

        this.secondPlayerName = (Label) root.lookup("#secondPlayerName");
        this.secondPlayerHighScore = (Label) root.lookup("#secondPlayerHighScore");

        this.thirdPlayerName = (Label) root.lookup("#thirdPlayerName");
        this.thirdPlayerHighScore = (Label) root.lookup("#thirdPlayerHighScore");

        this.fourthPlayerName = (Label) root.lookup("#fourthPlayerName");
        this.fourthPlayerHighScore = (Label) root.lookup("#fourthPlayerHighScore");

        this.fifthPlayerName = (Label) root.lookup("#fifthPlayerName");
        this.fifthPlayerHighScore = (Label) root.lookup("#fifthPlayerHighScore");
        leaderboard.sortLeaderboard();
        updateLeaderboardStandingsLabels();

    }

    public void switchToGameplayScene(ActionEvent event) throws IOException {
        if (!isNameSaved){
            invalidNameLabel.setText("Name must be saved before continuing to game - Try Again");
        }else {

            fetchStage(new File("gameView.fxml"), event);
            generateGrid(event);
            spawnObject(event);
            pausedModalPanel.setVisible(false);
            Grid.currentScore = 0;
            this.primaryStage.show();

            audioEffect = new AudioEffect(new File("buttonClick.wav"));
            audioEffect.playEffect();
            snakeBody.resetGradient();
        }
    }
    /*----------------------------------------------------------------------------------------------------*/

    /*---------------------------------Exit Stage Controller Methods --------------------------------------*/
    public void exitButton() throws MalformedURLException {
        App.close();
    }
    /*---------------------------------------------------------------------------------------------------*/

    /*-----------------------------------Update Labels Method---------------------------------------------------*/

    public void updateLeaderboardStandingsLabels (){ //this method can be used to update the labels in the leaderboard, instead of updateLabel method above
        if(!leaderboard.getLeaderboard().isEmpty()) {
            firstPlayerName.setText(leaderboard.getLeaderboard().get(0).getPlayerName());
            firstPlayerHighScore.setText(String.valueOf(leaderboard.getLeaderboard().get(0).getHighScore()));
        }

        if(leaderboard.getLeaderboard().size() >= 2) {
            secondPlayerName.setText(leaderboard.getLeaderboard().get(1).getPlayerName());
            secondPlayerHighScore.setText(String.valueOf(leaderboard.getLeaderboard().get(1).getHighScore()));
        }

        if(leaderboard.getLeaderboard().size() >= 3) {
            thirdPlayerName.setText(leaderboard.getLeaderboard().get(2).getPlayerName());
            thirdPlayerHighScore.setText(String.valueOf(leaderboard.getLeaderboard().get(2).getHighScore()));
        }

        if(leaderboard.getLeaderboard().size() >= 4) {
            fourthPlayerName.setText(leaderboard.getLeaderboard().get(3).getPlayerName());
            fourthPlayerHighScore.setText(String.valueOf(leaderboard.getLeaderboard().get(3).getHighScore()));
        }

        if(leaderboard.getLeaderboard().size() >= 5) {
            fifthPlayerName.setText(leaderboard.getLeaderboard().get(4).getPlayerName());
            fifthPlayerHighScore.setText(String.valueOf(leaderboard.getLeaderboard().get(4).getHighScore()));
        }
    }

    public void readPlayerName(ActionEvent event){

        if(playerNameField.getLength() == 0){
            invalidNameLabel.setText("Cannot save blank name - Try again");
        }else {
            leaderboard.createPlayer(playerNameField.getText());
            inputtedPlayerName = playerNameField.getText();
            Grid.highScore = 0;
            isNameSaved = true;
            enterNameLabel.setText("Player Name saved as: " + playerNameField.getText());

            saveNameButton.setVisible(false);
            playerNameField.setVisible(false);
            invalidNameLabel.setVisible(false);

            for (int i = 0; leaderboard.getLeaderboard().size() > i; i++) {
                System.out.println(leaderboard.getLeaderboard().get(i).getPlayerName());
                System.out.println(leaderboard.getLeaderboard().get(i).getHighScore());
            }
        }
    }

    public Player returnPlayerThroughPlayerName (String playerName) {
        for (Player player : leaderboard.getLeaderboard()) {
            if (player.getPlayerName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

     @FXML
     public void spawnObject(ActionEvent event) {
        EdibleObject.spawnFruit(this.gridPane);
     }


     /*--------------------------------------------------------pause screen---------------------------------------------- */

    public void pauseGame(ActionEvent event) {//this method pauses the game and stops the snake movement
        pausedModalPanel.setVisible(true);
        gameTimeline.getCuePoints().put("pauseCue",gameTimeline.getCurrentTime());
        gameTimeline.pause();
        System.out.println(gameTimeline.getCuePoints());
        this.pauseButton.setVisible(false);
    }



    public void resumeGame (ActionEvent event){
        pausedModalPanel.setVisible(false);
        this.pauseButton.setVisible(true);
        gridPane.requestFocus();
        gridPane.setOnKeyPressed(keyPressEvent -> moveSnakeWhenKeyPressed(keyPressEvent.getCode()));
        gameTimeline.playFrom("pauseCue");
    }

    public void runGame(GridPane gridPane){
        snakeBody = new SnakeBody(this);
        snakeBody.spawnSnakeBody(3, gridPane);
        gridPane.setFocusTraversable(true);
        snakeBody.setCurrentDirection(Direction.DOWN);
        gridPane.requestFocus();

        gameTimeline = new Timeline(new KeyFrame(Duration.millis(snakeSpeed), event -> {

            // game and collision logic goes here
            snakeBody.moveSnake(gridPane);
            gridPane.setOnKeyPressed(keyPressEvent -> moveSnakeWhenKeyPressed(keyPressEvent.getCode()));
            snakeBody.checkDeathCollision(gridPane);
        }));
        gameTimeline.setCycleCount(Timeline.INDEFINITE);
        gameTimeline.play();
    }


    public void stopTimeline(){
        gameTimeline.stop();
    }

    public void moveSnakeWhenKeyPressed (KeyCode code){
        System.out.println("Key pressed: "+ code);
        if(snakeBody.getCurrentDirection() == Direction.UP){
            switch (code) {
                case A -> snakeBody.setCurrentDirection(Direction.LEFT);
                case D -> snakeBody.setCurrentDirection(Direction.RIGHT);
                default -> snakeBody.setCurrentDirection(Direction.UP);
            }
        } else if(snakeBody.getCurrentDirection() == Direction.DOWN){
            switch (code) {
                case A -> snakeBody.setCurrentDirection(Direction.LEFT);
                case D -> snakeBody.setCurrentDirection(Direction.RIGHT);
                default -> snakeBody.setCurrentDirection(Direction.DOWN);
            }
        }else if (snakeBody.getCurrentDirection() == Direction.RIGHT){
            switch (code) {
                case W -> snakeBody.setCurrentDirection(Direction.UP);
                case S -> snakeBody.setCurrentDirection(Direction.DOWN);
                default -> snakeBody.setCurrentDirection(Direction.RIGHT);
            }
        }else{
            switch (code) {
                case W -> snakeBody.setCurrentDirection(Direction.UP);
                case S -> snakeBody.setCurrentDirection(Direction.DOWN);
                default -> snakeBody.setCurrentDirection(Direction.LEFT);
            }
        }
    }


    public static void updateGridScoreLabel (){
        currentScoreNumber.setText(String.valueOf(Grid.currentScore));
        updateGridHighScoreLabel();
    }

    public static void updateGridHighScoreLabel (){
        highScoreNumber.setText(String.valueOf(Grid.highScore));
    }

    public void makeGameOverScreenVisible (){
        this.gameOverScreen.setVisible(true);
        this.pauseButton.setVisible(false);
    }

    public boolean getIsSwitchedToMenu() {
        return switchedToMenu;
    }
}
