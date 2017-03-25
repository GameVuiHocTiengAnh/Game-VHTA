package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.CatchingWordsActivity;
import blackcore.tdc.edu.com.gamevhta.models.ConfigCWGame;

/**
 * Created by Shiro on 3/16/2017.
 */

public class ShurikenView extends SurfaceView implements Runnable{

    private Thread thread = null;
    private SurfaceHolder surfaceHolder;
    private boolean running = false;
    private Bitmap bmShuriken;
    private Shuriken shuriken;
    public ShurikenView(CatchingWordsActivity context) {
        super(context);
        surfaceHolder = getHolder();
        running = true;
        int heightFrame = (int)((context.getCharacHeight()*2)/3);
             FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,heightFrame,
                Gravity.BOTTOM
                );
        layoutParams.setMargins(250,0,0,ConfigCWGame.maginBotOfCharacter());
        Bitmap shuri = BitmapFactory.decodeResource(getResources(),R.drawable.mh6_shuriken);
        int sizeShuriken = ConfigCWGame.sizeOfShuriken();
        bmShuriken = Bitmap.createScaledBitmap(shuri,sizeShuriken,sizeShuriken,false);
        this.shuriken = new Shuriken(bmShuriken,ConfigCWGame.WIDTH_SUB_CHARACTER,0);
        this.setLayoutParams(layoutParams);
    }

    public void onResumeMySurfaceView(){
        thread = new Thread(this);
        running = true;
        thread.start();
    }

    public void onPauseMySurfaceView(){
        boolean retry = true;
        running = false;
        while(retry){
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (running){
                Canvas canvas = surfaceHolder.lockCanvas();
                if(canvas != null && !shuriken.isComplete()){
//                    Log.d("Tagtest","runOK");
                    shuriken.tranlate(10,0);
                    shuriken.draw(canvas);
                    surfaceHolder.unlockCanvasAndPost(canvas);
                } else{
                    if(canvas != null) {
                        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        surfaceHolder.unlockCanvasAndPost(canvas);
                        running = false;
                    }
                }

        }
    }
    public void setRunning(boolean running){
        this.running = running;
    }

    public boolean isRunning(){
        return running;
    }

    public void setComplete(boolean complete){
        shuriken.setComplete(complete);
    }
}
