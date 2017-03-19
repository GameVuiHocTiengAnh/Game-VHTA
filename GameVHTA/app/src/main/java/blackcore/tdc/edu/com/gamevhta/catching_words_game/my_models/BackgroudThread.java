package blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models;

import android.app.Activity;

import blackcore.tdc.edu.com.gamevhta.models.ConfigCWGame;

/**
 * Created by Shiro on 3/15/2017.
 */

public class BackgroudThread extends Thread {
    private BackgroudGameView backgroudGameView;
    private Boolean gameRunning = false;
    private Activity activity = null;
    private CharacterGameView charac= null;
    private volatile boolean canPauseGame;
    public BackgroudThread(BackgroudGameView backgroudGameView, CharacterGameView charac, Activity activity) {
        this.backgroudGameView = backgroudGameView;
        this.activity = activity;
        this.charac = charac;
    }

    @Override
    public void run() {
        super.run();

        while (gameRunning) {
            try {
                sleep(ConfigCWGame.SPEED_MOVE_BACKGROUD);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (this){
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        backgroudGameView.moveBgWithXleft(-3);
                        backgroudGameView.invalidate();
                        if(backgroudGameView.getxLeftBGClone() >= 0){
                            canPauseGame = true;
                        }
                        if(backgroudGameView.getxLeftBGClone() <= -500 && canPauseGame){
                            gameRunning = false;
                            charac.onPauseMySurfaceView();
                            canPauseGame = false;
                        }
                    }
                });
            }
        }
    }

    public void setGameRunning(Boolean gameRunning) {
        this.gameRunning = gameRunning;
    }
}
