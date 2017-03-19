package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Shiro on 3/15/2017.
 */

public class Backgroud {
    private Bitmap backgroud;
    private int xleft;
    private int ytop;
    private final int WIDTH;
    private final int HEIGHT;

    public Backgroud(Bitmap img,int xleft, int ytop){
        this.backgroud = img;
        this.xleft = xleft;
        this.ytop = ytop;
        this.WIDTH = img.getWidth();
        this.HEIGHT = img.getHeight();
    }

    public void tranlate(int x, int y,Bitmap bitmap){
        this.xleft += x;
        if(Math.abs(this.xleft)>= WIDTH){
            // Log.d("testx",String.valueOf(SizeOfDevice.getScreenWidth()));
            this.xleft = this.WIDTH;
            if(bitmap != null){
                setBackgroud(bitmap);
            }
        }
    }

    public void draw(Canvas canvas){
        canvas.scale(1,1);
        canvas.drawBitmap(this.backgroud,this.xleft,ytop,null);
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public void setBackgroud(Bitmap backgroud) {
        this.backgroud = backgroud;
    }

    public int getXleft() {
        return xleft;
    }
}
