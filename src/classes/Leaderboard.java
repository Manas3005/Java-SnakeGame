package classes;

import java.util.ArrayList;


public class Leaderboard {

    private ArrayList <Player> leaderboard;

    public Leaderboard() {
        this.leaderboard = new ArrayList<>();
    }

    public ArrayList<Player> getLeaderboard() {
        return leaderboard;
    }

    public void createPlayer(String playerName){
        Player player = new Player(playerName);
        leaderboard.add(player);
    }


    public void sortLeaderboard() { //Sorts the leaderboard
        if (leaderboard.size() > 1) {
            for (int j = 0; j < (leaderboard.size() - 1); j++) {
                for (int i = 0; i < (leaderboard.size() - 1); i++) {
                    if (leaderboard.get(i).getHighScore() < leaderboard.get(i + 1).getHighScore()) {
                        Player player1 = leaderboard.get(i);
                        Player player2 = leaderboard.get(i + 1);
                        leaderboard.set(i + 1, player1);
                        leaderboard.set(i, player2);
                    }
                }
            }
        }
    }


}