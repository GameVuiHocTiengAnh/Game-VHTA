package blackcore.tdc.edu.com.gamevhta.models;

/**
 * Created by Hoang on 3/20/2017.
 */

public class ScoreObject {
    private String sID, sPlayer;
    private int sScore;

    public ScoreObject(String sID, String sPlayer, int sScore) {
        this.sID = sID;
        this.sPlayer = sPlayer;
        this.sScore = sScore;
    }

    public ScoreObject() {
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsPlayer() {
        return sPlayer;
    }

    public void setsPlayer(String sPlayer) {
        this.sPlayer = sPlayer;
    }

    public int getsScore() {
        return sScore;
    }

    public void setsScore(int sScore) {
        this.sScore = sScore;
    }
}
