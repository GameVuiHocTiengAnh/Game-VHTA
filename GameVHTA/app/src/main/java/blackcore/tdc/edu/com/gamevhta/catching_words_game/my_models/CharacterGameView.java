package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.models.ConfigCWGame;

/**
 * Created by Shiro on 3/16/2017.
 */

public class CharacterGameView extends SurfaceView implements Runnable{

    private Thread thread = null;
    private SurfaceHolder surfaceHolder;
    private CharacterGame ninja;
    volatile boolean running = false;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Random random;

    public CharacterGameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        random = new Random();
        running = true;
        Bitmap rootCharacter = BitmapFactory.decodeResource(getResources(), R.drawable.mh6_character_ninja);
        ninja = new CharacterGame(rootCharacter, 2, 6);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ninja.getWidthSubImg(),ninja.getHeightSubImg(),
                Gravity.BOTTOM
                );
        layoutParams.setMargins(300,0,0,50);
        this.setLayoutParams(layoutParams);
    }

    public void onResumeMySurfaceView(){
        running = true;
        thread = new Thread(this);
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
        while(running){
            //Log.d("test","okRun");
            if(surfaceHolder.getSurface().isValid()){
                try {
                    Thread.sleep(ConfigCWGame.SPEED_MOVE_CHARACTER);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Canvas canvas = surfaceHolder.lockCanvas();
                if(canvas != null){
                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    ninja.draw(canvas);
                    ninja.updateColUsing();
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
