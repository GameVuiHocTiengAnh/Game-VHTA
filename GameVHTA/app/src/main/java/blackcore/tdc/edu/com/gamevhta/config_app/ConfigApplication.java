package blackcore.tdc.edu.com.gamevhta.config_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by HOANG on 3/18/2017.
 */

public class ConfigApplication {

    /*------------- START HOANG -----------------*/
    public static final String PACKAGE = "blackcore.tdc.edu.com.gamevhta";
    //Time left default
    public static final int TIME_LEFT_GAME = 50;


    //Object
    public static final String OBJECT_SELECTED = "OBJECT_SELECTED";
    //School
    public static final String OBJECT_SCHOOL = "SCH";
    //Animals
    public static final String OBJECT_ANIMALS = "ANI";
    //Plants
    public static final String OBJECT_PLANTS = "PLA";
    //CountrySide
    public static final String OBJECT_COUNTRY_SIDE = "COU";
    //Household Appliances
    public static final String OBJECT_HOUSEHOLD_APPLIANCES = "HOU";

    //Database
    public static final String DATABASE_NAME = "GAMEVHTA";
    public static final int DATABASE_VERSION = 1;
    //TABLE
    public static final String TABLE_WORD = "TABLE_WORD";
    public static final String TABLE_SCORE = "TABLE_SCORE";

    //COLUMN
    //TABLE WORD
    public static final String W_COLUMN_ID_WORD = "ID_WORD";
    public static final String W_COLUMN_WORD_ENG = "WORD_ENG";
    public static final String W_COLUMN_WORD_VIE = "WORD_VIE";
    public static final String W_COLUMN_PATH_IMAGE = "PATH_IMAGE";
    public static final String W_COLUMN_PATH_SOUND = "PATH_SOUND";
    public static final String W_COLUMN_OBJECT = "OBJECT";
    public static final String W_COLUMN_LEVEL = "LEVEL";

    //TABLE SCORE
    public static final String S_COLUMN_ID_PLAYER = "ID_PLAYER";
    public static final String S_COLUMN_PLAYER_NAME = "PLAYER_NAME";
    public static final String S_COLUMN_PLAYER_SCORE = "PLAYER_SCORE";

    /*------------- END HOANG -----------------*/


    /*------------- START TRIEU -----------------*/

    //Time left default
    public static final int TIME_LEFT_GAME_MARKET = 60;

    /*------------- END TRIEU -----------------*/

     /*------------- GAME CATCHING WORDS-----------------*/
     public  static final String SCREEN_USING_PAUSE_DIALOG_MH6 = "MH6";
     public  static final String BG_FOREST_LIGHT = "mh6_backgroud_light";
    public  static final String BG_FOREST_DARK = "mh6_backgroud_dark";
    public  static final String BG_CITY_LIGHT = "mh6_backgroud_city_map_light";
    public  static final String BG_CITY_DARK = "mh6_backgroud_city_map_dark";
    public  static final String BG_FARM_LIGHT = "mh6_backgroud_farm_map_light";
    public  static final String BG_FRAM_DARK = "mh6_backgroud_farm_map_dark";
     /*------------- END GAME CATCHING WORDS-----------------*/

     public  static final String NAME_DATA_LIST = "dataListUsing";
    public  static final String SCORES_BEFOR_GAME = "scores";

    //Add image from sdcard to ImageButton
    public static Bitmap getImageBitmapFromSDCard(String imageName) {
        String path = Environment.getExternalStorageDirectory().toString() + "/" + imageName + ".jpg";
        File fileImage = new File(path);
        Bitmap bmp = null;
        if (fileImage.exists()) {
            bmp = BitmapFactory.decodeFile(fileImage.getAbsolutePath());
        }
        return bmp;
    }
    //Add image from sdcard to ImageButton
    public static ArrayList<String> getListImageFromSDCard(String folder) {
        String root = Environment.getExternalStorageDirectory().toString();
        File file = new File(root+ folder);
        File listFile[] = file.listFiles();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i<listFile.length;i++){
            arrayList.add(listFile[i].getName());
        }
        //String path = Environment.getExternalStorageDirectory().toString() + "" + folder + "";
        return arrayList;
    }

    //Rotate bimap
    public static Bitmap doRotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source,0,0, source.getWidth(),source.getHeight(),matrix,true);
    }
}
