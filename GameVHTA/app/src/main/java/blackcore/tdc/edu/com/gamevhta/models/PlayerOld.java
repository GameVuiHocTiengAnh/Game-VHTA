package blackcore.tdc.edu.com.gamevhta.models;

/**
 * Created by Shiro on 4/26/2017.
 */

public class PlayerOld {
    private String name;
    private int id;
    private int lvPass;

    public PlayerOld(String name, int id, int lvPass) {
        this.name = name;
        this.id = id;
        this.lvPass = lvPass;
    }

    public PlayerOld(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLvPass() {
        return lvPass;
    }

    public void setLvPass(int lvPass) {
        this.lvPass = lvPass;
    }
}
