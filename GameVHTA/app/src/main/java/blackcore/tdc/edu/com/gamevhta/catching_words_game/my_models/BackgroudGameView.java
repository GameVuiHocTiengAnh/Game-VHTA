package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.CatchingWordsActivity;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.models.ConfigCWGame;
import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;

/**
 * Created by Shiro on 3/15/2017.
 */

public class BackgroudGameView extends View implements View.OnTouchListener{
    private Bitmap imgRoot;
    private Backgroud backgroud;
    private Backgroud backgroudClone;
    private BackgroudThread thread;

    private Bitmap bitmapTrunk;
    private Trunk trunk;

    private CharacterGameView characterGameView;
    private CatchingWordsActivity context;

    private int countTouch;
    private int progressHelth;
    private int uinitHelth;
    private Canvas canvas;

    public BackgroudGameView(CatchingWordsActivity context, int xLeft, int yTop, CharacterGameView characterGameView) {
        super(context);
        this.setFocusable(true);
        this.context = context;

        bitmapTrunk = BitmapFactory.decodeResource(getResources(), R.drawable.mh6_trunk);
        int idImgRoot = randomBG();
        Bitmap bg = BitmapFactory.decodeResource(getResources(),idImgRoot);
        imgRoot = Bitmap.createScaledBitmap(bg,SizeOfDevice.getScreenWidth(),SizeOfDevice.getScreenHeight(),false);

        backgroud = new Backgroud(imgRoot, xLeft, yTop);
        bitmapTrunk = BitmapFactory.decodeResource(getResources(), R.drawable.mh6_trunk);
        trunk = new Trunk(bitmapTrunk, SizeOfDevice.getScreenWidth() + 500, ConfigCWGame.yPositonOfTrunk());
        backgroudClone = new Backgroud(imgRoot, (backgroud.getWIDTH() - 5), 0);
        this.characterGameView = characterGameView;
        this.setOnTouchListener(this);
        countTouch = 0;
        progressHelth = 100;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.canvas = canvas;
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        backgroud.draw(canvas);
        backgroudClone.draw(canvas);
        trunk.draw(canvas);
        super.onDraw(canvas);
    }

    protected void moveBgWithXleft(int x) {
        backgroud.tranlate(x, 0);
        backgroudClone.tranlate(x, 0);
        trunk.tranlate(x, SizeOfDevice.getScreenHeight() + 200);
    }

    public void startMoveBG(CatchingWordsActivity context) {
        thread = new BackgroudThread(this, characterGameView, context);
        thread.setGameRunning(true);
        thread.start();
        characterGameView.setRunning(true);
        characterGameView.onResumeMySurfaceView();
    }

    public void startCharacterFighting(CatchingWordsActivity context) {
        characterGameView.setRunning(true);
        characterGameView.setNinjaFigting();
        characterGameView.onResumeMySurfaceView();
    }

    public void stopMoveBG() {
        if (thread.isAlive()) {
            thread.setGameRunning(false);
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        characterGameView.onPauseMySurfaceView();
    }

    public int getxLeftBGClone() {
        return backgroudClone.getXleft();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (trunk.isPause() && !characterGameView.isRunning()) {
            countTouch++;
            Log.d("Tagtest","countTouch: "+countTouch);
            if(countTouch == 1){
                characterGameView.ninjaSpeak();
            }
            //Log.d("Tagtest",leghtVoca+" leght");
            int leghtVoca = context.leghtWordOfOjectUsing();

            uinitHelth = (int) (100 / leghtVoca);
            progressHelth -= uinitHelth;
            context.updateProgressHeth(progressHelth);
            this.startCharacterFighting(this.context);
            boolean daylayThrowShuriken = new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    context.throwShuriken();
                }
            },300);

            //Log.d("Tagtest",countTouch+"countTouch");
            if (countTouch == leghtVoca) {
                boolean handlerdelay = new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        countTouch = 0;
                        context.hideHelthbar();
//                        characterGameView.ninjaLaugh();
                        characterGameView.setNinjaDefaut();
                        backgroudClone.setBackgroud(imgRoot);
                        context.updateProgressHeth(100);
                        progressHelth = 100;
                        trunk.setPause(false);
                        trunk.resetXleft();
                        context.setImvVocalubary();
                        startMoveBG(context);
                    }
                }, 1000);

            }
            return true;
        } else
            return false;
    }

    public void setPauseGame(boolean pauseGame) {
        trunk.setPause(pauseGame);
    }


    public boolean isPauseGame() {
        return trunk.isPause();
    }

    private int randomBG(){
       int idBg = -1;
        Random ran = new Random();
        int dom = ran.nextInt(6);
        switch (dom){
            case 0: idBg = getResources().getIdentifier(ConfigApplication.BG_CITY_DARK,"drawable",getContext().getPackageName());
                break;
            case 1: idBg = getResources().getIdentifier(ConfigApplication.BG_CITY_LIGHT,"drawable",getContext().getPackageName());
                break;
            case 2: idBg = getResources().getIdentifier(ConfigApplication.BG_FARM_LIGHT,"drawable",getContext().getPackageName());
                break;
            case 3: idBg = getResources().getIdentifier(ConfigApplication.BG_FRAM_DARK,"drawable",getContext().getPackageName());
                break;
            case 4: idBg = getResources().getIdentifier(ConfigApplication.BG_FOREST_LIGHT,"drawable",getContext().getPackageName());
                break;
            case 5: idBg = getResources().getIdentifier(ConfigApplication.BG_FOREST_DARK,"drawable",getContext().getPackageName());
                break;
        }
        return idBg;
    }

}
