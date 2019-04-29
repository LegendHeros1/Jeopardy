package cs125.jeopardy;

import java.io.Serializable;

public class Player implements Serializable {
    private int score;
    private String name;

    public Player(String setName, int setScore) {
        name = setName;
        score = setScore;
    }

    public String getName() {
        return name;
    }
    public int getScore() {
        return score;
    }
    public void increaseScore(int value) {
        score += value;
    }

}
