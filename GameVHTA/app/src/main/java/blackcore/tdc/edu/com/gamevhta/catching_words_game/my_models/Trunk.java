package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;

/**
 * Created by Shiro on 3/15/2017.
 */

public class Trunk {
    private Bitmap trunk;
    private int xleft;
    private int ytop;
    private boolean isPause;
    private int originalXleft;

    public Trunk(Bitmap img, int xleft, int ytop) {
        this.trunk = img;
        this.xleft = xleft;
        this.originalXleft = xleft;
        this.ytop = ytop;
        this.isPause = false;
    }


    public void tranlate(int x, int y) {
        this.xleft += x;
        if (xleft <= SizeOfDevice.getScreenWidth() / 2) {
            isPause = true;
        }
    }

    public void draw(Canvas canvas) {
        canvas.scale(1, 1);
        canvas.drawBitmap(this.trunk, this.xleft, ytop, null);
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
    public void resetXleft(){
        this.xleft = originalXleft;
    }
}
