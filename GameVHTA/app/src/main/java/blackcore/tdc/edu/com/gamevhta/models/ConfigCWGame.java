package blackcore.tdc.edu.com.gamevhta.models;

/**
 * Created by Shiro on 3/15/2017.
 */

public class ConfigCWGame {
    public static final int SPEED_MOVE_BACKGROUD = 15;
    public static final int ROW_USING_OF_ROOT_IMG_CHAR = 1; //begin by zero
    public static final int ROW_USING_OF_ROOT_IMG_CHAR_FIGTHING = 0; //begin by zero
    public static final int SPEED_MOVE_CHARACTER = 200;

    public static  int getMarginScoresView(){
        int margin = 0;
        if(SizeOfDevice.getScreenWidth() >= 1800){
            margin = 18;
        }else {
            margin = 35;
        }
        return margin;
    }

    public static  int getSizeTextScores(){
        int margin = 0;
        if(SizeOfDevice.getScreenWidth() >= 1800){
            margin = 25;
        }else {
            margin = 35;
        }
        return margin;
    }

    public static int getMarginVocalubary(){
        int margin = 0;
        if(SizeOfDevice.getScreenWidth() >= 1800){
            margin = 300;
        }else {
            margin = 80;
        }
        return margin;
    }
}
