package blackcore.tdc.edu.com.gamevhta.models;

/**
 * Created by phong on 2/27/2017.
 */

public class Score {
    private String Name;
    private String Score;

    public String getGuide() {
        return Guide;
    }

    public void setGuide(String guide) {
        Guide = guide;
    }

    private String Guide;

    @Override
    public String toString() {
        return super.toString();
    }

    public Score(String guide) {
        this.Guide = guide;
    }

    public Score(String name, String score) {
        this.Name = name;
        this.Score = score;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public Score(){

    }
}

