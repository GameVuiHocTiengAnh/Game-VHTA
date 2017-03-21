package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.CatchingWordsActivity;
import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;

/**
 * Created by Shiro on 3/15/2017.
 */

public class BackgroudGameView extends View  implements View.OnTouchListener{
    private Bitmap imgRoot;
    private Backgroud backgroud;
    private Backgroud backgroudClone;
    private BackgroudThread thread;
    private Bitmap trunk;
    private Bitmap bgTrunk;
    private CharacterGameView characterGameView;
    private CatchingWordsActivity context;
    private boolean pauseGame;
    private int xleftTrunk, yTrunk, xTrunk; //xleftTrunk is current x of trunk on screen

    public BackgroudGameView(CatchingWordsActivity context, int xLeft, int yTop, CharacterGameView characterGameView) {
        super(context);
        this.setFocusable(true);
        this.context = context;
        Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.mh6_backgroud_dark);
        trunk = BitmapFactory.decodeResource(getResources(),R.drawable.mh6_trunk);
        imgRoot = Bitmap.createScaledBitmap(img, SizeOfDevice.getScreenWidth(),SizeOfDevice.getScreenHeight(),false);
        backgroud = new Backgroud(imgRoot,xLeft,yTop);
        trunk = BitmapFactory.decodeResource(getResources(),R.drawable.mh6_trunk);
        bgTrunk = overlay(imgRoot,trunk);
        backgroudClone = new Backgroud(bgTrunk,backgroud.getWIDTH(),0);
        this.characterGameView = characterGameView;
        pauseGame = false;
        this.setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
        backgroud.draw(canvas);
        backgroudClone.draw(canvas);
        super.onDraw(canvas);
    }
    protected void moveBgWithXleft(int x){
        backgroud.tranlate(x,0,null);
        backgroudClone.tranlate(x,0,bgTrunk);
    }

    public void startMoveBG(CatchingWordsActivity context){
        thread = new BackgroudThread(this,characterGameView, context);
        thread.setGameRunning(true);
        thread.start();
        characterGameView.onResumeMySurfaceView();
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
        characterGameView.onPauseMySurfaceView();
    }
    private Bitmap overlay(Bitmap backgroud, Bitmap trunk) {
        Bitmap bmOverlay = Bitmap.createBitmap(backgroud.getWidth(), backgroud.getHeight(), backgroud.getConfig());
        Canvas canvas = new Canvas(bmOverlay);
        canvas.drawBitmap(backgroud,new Matrix(), null);
        canvas.drawBitmap(trunk,SizeOfDevice.getScreenWidth() - 500,SizeOfDevice.getScreenHeight() - 270, null);
        return bmOverlay;
    }
    public int getxLeftBGClone(){
        return backgroudClone.getXleft();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(pauseGame){
            backgroudClone.setBackgroud(imgRoot);
            this.startMoveBG(this.context);
            pauseGame = false;
            return true;
        }else
        return false;
    }

    public void setPauseGame(boolean pauseGame) {
        this.pauseGame = pauseGame;
    }

    public int getXleftTrunk() {
        return xleftTrunk = backgroudClone.getXleft() + (SizeOfDevice.getScreenWidth() - 500);
    }

}
