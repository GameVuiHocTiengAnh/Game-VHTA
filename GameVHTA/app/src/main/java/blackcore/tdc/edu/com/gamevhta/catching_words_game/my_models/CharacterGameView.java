package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.Log;
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
    private CharacterGame ninjaFighting;
    private CharacterGame ninjaDefaut;
    volatile boolean running = false;
    private boolean isNinjaFighting;
    private int countDrawNinjaFighting;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Random random;

    public CharacterGameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        random = new Random();
        running = true;
        isNinjaFighting = false;
        Bitmap rootCharacter = BitmapFactory.decodeResource(getResources(), R.drawable.mh6_character_ninja);
        ninjaDefaut = new CharacterGame(rootCharacter, 2, 6,ConfigCWGame.ROW_USING_OF_ROOT_IMG_CHAR);
        ninjaFighting = new CharacterGame(rootCharacter,2,6,ConfigCWGame.ROW_USING_OF_ROOT_IMG_CHAR_FIGTHING);
        ninja = ninjaDefaut;
        countDrawNinjaFighting = 0;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ninja.getWidthSubImg(),ninja.getHeightSubImg(),
                Gravity.BOTTOM
                );
        layoutParams.setMargins(300,0,0,50);
        this.setLayoutParams(layoutParams);
    }

    public void onResumeMySurfaceView(){
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
        do{

                try {
                    Thread.sleep(ConfigCWGame.SPEED_MOVE_CHARACTER);
                    Canvas canvas = surfaceHolder.lockCanvas();
                    if(isNinjaFighting){
                        countDrawNinjaFighting++;
                        if(countDrawNinjaFighting == 4){
                            setRunning(false);
                            ninjaFighting.setColUsing(0);
                        }
                    }
                    if(canvas != null){
//                    Log.d("Tagtest","runOK");
                        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        ninja.draw(canvas);
                        ninja.updateColUsing();
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }while(running);
    }
    public void setRunning(boolean running){
        this.running = running;
    }

    public void setNinjaFigting(){
        ninja = ninjaFighting;
        countDrawNinjaFighting = 0;
        isNinjaFighting = true;
    }

    public  void setNinjaDefaut(){
        ninja = this.ninjaDefaut;
        isNinjaFighting = false;
    }

    public boolean isRunning(){
        return running;
    }

}
