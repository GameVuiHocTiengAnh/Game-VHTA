package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;

/**
 * Created by Shiro on 3/15/2017.
 */

public class Backgroud {
    private Bitmap backgroud;
    private int xleft;
    private int ytop;
    private final int WIDTH;
    private final int HEIGHT;
    private boolean isPause;

    public Backgroud(Bitmap img, int xleft, int ytop) {
        this.backgroud = img;
        this.xleft = xleft;
        this.ytop = ytop;
        this.WIDTH = img.getWidth();
        this.HEIGHT = img.getHeight();
        this.isPause = false;
    }

    public void tranlate(int x, int y) {
        this.xleft += x;
        if (Math.abs(this.xleft) >= WIDTH) {
            // Log.d("testx",String.valueOf(SizeOfDevice.getScreenWidth()));
            this.xleft = this.WIDTH - 8;
        }
    }

    public void tranlateTrunk(int x, int y) {
        this.xleft += x;
        if (xleft <= SizeOfDevice.getScreenWidth() / 2) {
            isPause = true;
        }
    }

    public void draw(Canvas canvas) {
        canvas.scale(1, 1);
        canvas.drawBitmap(this.backgroud, this.xleft, ytop, null);
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

    public boolean isPause() {
        return isPause;
    }

    public void setPause(boolean pause) {
        this.isPause = pause;
    }
}
