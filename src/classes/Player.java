package classes;

public class Player {

    private String playerName;
    private int highScore;

    public Player (String playerName){
        this.playerName = playerName;
        this.highScore = 0;
    }

    public String getPlayerName(){
        return this.playerName;
    }

    public int getHighScore() {
        return this.highScore;
    }

    public void updatePlayerHighScore (int highScore){
        this.highScore = highScore;
    }

}
