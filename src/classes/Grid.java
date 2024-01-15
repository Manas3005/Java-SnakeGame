package classes;

public class Grid {
    static int highScore = 0;

    static int currentScore = 0;




    public static void updateCurrentScore (){
            currentScore++;
            System.out.print(currentScore);
            setHighScore(currentScore);
            Controller.updateGridScoreLabel();
    }

    public static void setHighScore (int currentScore) { //if snake dies check currentscore and compare to highscore
        if (currentScore > highScore){
            highScore = currentScore;
        }
    }

}
