package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.view.View;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;

/**
 * Created by Shiro on 3/15/2017.
 */

public class BackgroudGameView extends View {
    private Bitmap imgRoot;
    private Backgroud backgroud;
    private Backgroud backgroudClone;
    private BackgroudThread thread;

    public BackgroudGameView(Context context, int xLeft, int yTop) {
        super(context);
        this.setFocusable(false);
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.mh6_backgroud_dark);
        imgRoot = Bitmap.createScaledBitmap(img, SizeOfDevice.getScreenWidth(),SizeOfDevice.getScreenHeight(),false);
        backgroud = new Backgroud(imgRoot,xLeft,yTop);
        backgroudClone = new Backgroud(imgRoot,backgroud.getWIDTH(),0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        backgroud.draw(canvas);
        backgroudClone.draw(canvas);
        super.onDraw(canvas);
    }
    protected void moveBgWithXleft(int x){
        backgroud.tranlate(x,0);
        backgroudClone.tranlate(x,0);
    }

    public void startMoveBG(Activity context){
        thread = new BackgroudThread(this,context);
        thread.setGameRunning(true);
        thread.start();
    }

    public void stopMoveBG(){
        if(thread.isAlive()){
            thread.setGameRunning(false);
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
