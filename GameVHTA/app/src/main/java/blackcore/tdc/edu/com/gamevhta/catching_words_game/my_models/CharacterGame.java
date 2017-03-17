package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import blackcore.tdc.edu.com.gamevhta.models.ConfigCWGame;

/**
 * Created by Shiro on 3/15/2017.
 */

public class CharacterGame {
    private Bitmap[]  movingCharacter = null;
    private int colUsing;
    private int colCount;
    private Bitmap rootImg;
    private int widthSubImg;
    private int heightSubImg;
    private final int WIDTH_ROOT;
    private final int HEIGHT_ROOT;

    public CharacterGame(Bitmap rootImg, int rowCount, int colCountReal) {
        this.rootImg = rootImg;
        this.movingCharacter = new Bitmap[colCountReal];
        this.colCount = colCountReal -1;
        this.colUsing = 0;
        this.WIDTH_ROOT = rootImg.getWidth();
        this.HEIGHT_ROOT = rootImg.getHeight();
        this.widthSubImg = WIDTH_ROOT/colCountReal;
        this.heightSubImg = HEIGHT_ROOT/rowCount;
        for(int i = 0; i < colCountReal; i++){
            movingCharacter[i] = createSubImageAt(ConfigCWGame.ROW_USING_OF_ROOT_IMG_CHAR,i);
        }
    }

    private Bitmap createSubImageAt(int rowUsingOfRootImgChar, int col) {
//        Log.d("test",WIDTH_ROOT+"");
        Bitmap subBitmap = Bitmap.createBitmap(rootImg,  col*widthSubImg, rowUsingOfRootImgChar*heightSubImg, widthSubImg, heightSubImg);
        return subBitmap;
    }

    public void updateColUsing(){
        this.colUsing++;
        if(colUsing == this.colCount){
            this.colUsing = 0;
        }
    }

    private Bitmap getUsingBitmap(){
        return movingCharacter[this.colUsing];
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(getUsingBitmap(),0,0,null);
    }

    public int getWidthSubImg() {
        return widthSubImg;
    }

    public int getHeightSubImg() {
        return heightSubImg;
    }
}
