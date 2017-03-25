package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.models.ConfigCWGame;
import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;

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


    public CharacterGameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
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
        layoutParams.setMargins(250,0,0,ConfigCWGame.maginBotOfCharacter());
        ConfigCWGame.WIDTH_SUB_CHARACTER = ninja.getWidthSubImg();
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

    public void ninjaSpeak(){
        Random ran = new Random();
        MediaPlayer ninjaSpeak = null;
        int dom = ran.nextInt(4);
        switch (dom){
            case 0:
                ninjaSpeak = MediaPlayer.create(getContext(),R.raw.ninja_speak1);
                break;
            case 1:
                ninjaSpeak = MediaPlayer.create(getContext(),R.raw.ninja_speak2);
                break;
            case 2:
                ninjaSpeak = MediaPlayer.create(getContext(),R.raw.ninja_speak3);
                break;
            case 3:
                ninjaSpeak = MediaPlayer.create(getContext(),R.raw.ninja_speak4);
                break;
        }

        if(ninjaSpeak != null){
            ninjaSpeak.setVolume(0.4f,0.4f);
            ninjaSpeak.seekTo(0);
            ninjaSpeak.start();
        }
    }

    public void ninjaLaugh(){
//        MediaPlayer ninjaLaugh = MediaPlayer.create(getContext(),R.raw.ninja_laugh);
//        ninjaLaugh.start();
    }

    public int getCharacHeight(){
        return  ninja.getHeightSubImg();
    }
}
