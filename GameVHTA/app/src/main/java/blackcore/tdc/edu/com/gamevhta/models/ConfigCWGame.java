package blackcore.tdc.edu.com.gamevhta.models;

/**
 * Created by Shiro on 3/15/2017.
 */

public class ConfigCWGame {
    public static final int SPEED_MOVE_BACKGROUD = 15;
    public static final int ROW_USING_OF_ROOT_IMG_CHAR = 1; //begin by zero
    public static final int ROW_USING_OF_ROOT_IMG_CHAR_FIGTHING = 0; //begin by zero
    public static final int SPEED_MOVE_CHARACTER = 200;
    public static int WIDTH_SUB_CHARACTER = 200;
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

    public static int withProgressHelth(){
        int width = 0;
        int device = SizeOfDevice.getScreenWidth();
        if(device<= 1204){
            width = 100;
        }else{
            if(device > 1204 &&  device <= 1820){
                width = 250;
            }else {
                width = 350;
            }
        }
        return width;
    }

    public static int marginBotOfHelth(){
        int height = 0;
        int device = SizeOfDevice.getScreenHeight();
        if(device<= 768){
            height = device - 150;
        }else{
            if(device > 768 &&  device <= 1400){
                height = device - 350;
            }else {
                height = device - 400;
            }
        }
        return height;
    }
    public static int yPositonOfTrunk(){
        int y = 0;
        int device = SizeOfDevice.getScreenHeight();
        if(device<= 768){
            y = device - 100;
        }else{
            if(device > 768 &&  device <= 1400){
                y = device - 290;
            }else {
                y = device - 350;
            }
        }
        return y;
    }

    public static int maginBotOfCharacter(){
        int margin = 0;
        int device = SizeOfDevice.getScreenHeight();
        if(device<= 768){
            margin = 30;
        }else{
            if(device > 768 &&  device <= 1400){
                margin = 40;
            }else {
                margin = 50;
            }
        }
        return margin;
    }

    public static int sizeOfShuriken(){
        int size = 0;
        int device = SizeOfDevice.getScreenWidth();
        if(device<= 1204){
            size = 20;
        }else{
            if(device > 1204 &&  device <= 1820){
                size = 35;
            }else {
                size = 60;
            }
        }
        return size;
    }


//    public static int positionOverlayTrunk_Y(){
//        int position = 0;
//        if(SizeOfDevice.getScreenWidth())
//    }
}
