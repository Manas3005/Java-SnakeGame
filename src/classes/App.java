package classes;


import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;


public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception, IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/startImage.fxml"));

        // Create a scene with the layout and set it on the stage
        Scene primaryScene = new Scene(root);
        primaryStage.setScene(primaryScene);
        root.requestFocus();
        // Show the stage
        primaryStage.show();

        Parent root1 = FXMLLoader.load(getClass().getResource("/resources/pausedScreen.fxml"));
        final Scene pauseScene = new Scene(root1);

        
        final EventHandler<KeyEvent> primaryScenePauseFilter = new EventHandler<>() {
            final KeyCombination pauseKeyCombo = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
        
            public void handle(KeyEvent e) {
                if (pauseKeyCombo.match(e)) {

                    if (primaryStage.getScene() == primaryScene) {
                        primaryStage.setScene(pauseScene);
                        root1.requestFocus();

                    } else if (primaryStage.getScene() == pauseScene) {
                        primaryStage.setScene(primaryScene);
                        root.requestFocus();
                    }
                    e.consume();
                }
            }
        };
        
        final EventHandler<KeyEvent> pauseSceneResumeFilter = new EventHandler<>() {
            final KeyCombination resumeKeyCombo = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN);
        
            public void handle(KeyEvent e) {
                
                if (resumeKeyCombo.match(e)) {
                    primaryStage.setScene(primaryScene);
                    e.consume();
                }
            }
        };
        
        primaryScene.addEventFilter(KeyEvent.KEY_PRESSED, primaryScenePauseFilter);
        pauseScene.addEventFilter(KeyEvent.KEY_PRESSED, pauseSceneResumeFilter);
        /*----------------------------------------------------------------------------------------------------- */

        //open the exitConfirmation stage
        primaryStage.setOnCloseRequest(event -> {
            try {
                event.consume();
                exitConfirmation(primaryStage);// this method closes the primaryStage and open the secondaryStage where the exitConfirmation happens
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void exitConfirmation(Stage primaryStage) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        //Set-up the second stage/window where the exit confirmation happens
        Parent secondaryRoot = FXMLLoader.load(getClass().getResource("/resources/exitConfirmationScene.fxml"));
        Stage secondaryStage = new Stage();
        Scene secondaryScene = new Scene(secondaryRoot);
        secondaryStage.setScene(secondaryScene);
        secondaryStage.show();
        //close the previous stage so that we do not have two windows
        primaryStage.close();

        //open the exitConfirmation stage
        secondaryStage.setOnCloseRequest(event -> {//Call the method inside itself in order to view the confirmation window again on the secondaryStage
            try {
                event.consume();
                exitConfirmation(secondaryStage);// this method closes the primaryStage and open the secondaryStage where the exitConfirmation happens
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedAudioFileException e) {//this catch was suggested by Intellij
                throw new RuntimeException(e);
            } catch (LineUnavailableException e) {//this catch was suggested by Intellij
                throw new RuntimeException(e);
            }
        });
    }

    public static void close(){
        Platform.exit();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

