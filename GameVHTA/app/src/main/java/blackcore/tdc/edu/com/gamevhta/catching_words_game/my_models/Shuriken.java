package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.util.Log;

import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;

/**
 * Created by Shiro on 3/15/2017.
 */

public class Shuriken {
    private Bitmap shuriken;
    private int xleft;
    private int ytop;
    private boolean isComplete;
    private int originalXleft;
    private Thread thread;
    private int xComplete;
    private Matrix matrix;
    Random ran;

    public Shuriken(Bitmap img, int xleft, int ytop) {
        matrix = new Matrix();
        matrix.postRotate(10,100,200);
        this.shuriken = img;
        this.xleft = xleft;
        this.originalXleft = xleft;
        this.ytop = ytop;
        this.isComplete = false;
        ran = new Random();
        xComplete = (SizeOfDevice.getScreenWidth()/2 - 230);
    }


    public void tranlate(int x, int y) {
        this.xleft += x;
        if (xleft >= xComplete) {
            isComplete = true;
            xleft = originalXleft;
        }
    }

    public void draw(Canvas canvas) {
        this.tranlate(3,0);
//            Log.d("Tagtest","drawShuriken");
        int dre = ran.nextInt(8);
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        canvas.save();
        canvas.rotate(dre);
        canvas.drawBitmap(this.shuriken, this.xleft, ytop, null);
        canvas.restore();
    }

    public int getXleft() {
        return xleft;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean pause) {
        this.isComplete = pause;
    }
    public void resetXleft(){
        this.xleft = originalXleft;
    }
}
