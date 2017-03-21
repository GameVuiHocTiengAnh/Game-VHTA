package blackcore.tdc.edu.com.gamevhta.models;

/**
 * Created by Administrator on 3/20/2017.
 */

public class WordObject {
    public String getwID() {
        return wID;
    }

    public void setwID(String wID) {
        this.wID = wID;
    }

    public String getwEng() {
        return wEng;
    }

    public void setwEng(String wEng) {
        this.wEng = wEng;
    }

    public String getwVie() {
        return wVie;
    }

    public void setwVie(String wVie) {
        this.wVie = wVie;
    }

    public String getwPathImage() {
        return wPathImage;
    }

    public void setwPathImage(String wPathImage) {
        this.wPathImage = wPathImage;
    }

    public String getwPathSound() {
        return wPathSound;
    }

    public void setwPathSound(String wPathSound) {
        this.wPathSound = wPathSound;
    }

    public String getwObject() {
        return wObject;
    }

    public void setwObject(String wObject) {
        this.wObject = wObject;
    }

    private String wID;
    private String wEng;
    private String wVie;
    private String wPathImage;
    private String wPathSound;
    private String wObject;

    public String getưLevel() {
        return ưLevel;
    }

    public void setưLevel(String ưLevel) {
        this.ưLevel = ưLevel;
    }

    private String ưLevel;

    public WordObject(String wID, String wEng, String wVie, String wPathImage, String wPathSound, String wObject) {
        this.wID = wID;
        this.wEng = wEng;
        this.wVie = wVie;
        this.wPathImage = wPathImage;
        this.wPathSound = wPathSound;
        this.wObject = wObject;
    }

    public WordObject() {
    }
}
