package blackcore.tdc.edu.com.gamevhta.catching_words_game;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.BackgroudGameView;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.BackgroudThread;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.CharacterGameView;
import blackcore.tdc.edu.com.gamevhta.models.ConfigCWGame;
import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;

public class CatchingWordsActivity extends AppCompatActivity {

    private BackgroudGameView backgroudGameView;
    private CharacterGameView ninja;
    private BackgroudThread autoMoveBg;
    private FrameLayout frameGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowManager.LayoutParams attribute = getWindow().getAttributes();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        frameGame = new FrameLayout(this);
        FrameLayout.LayoutParams lpframe = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        frameGame.setLayoutParams(lpframe);
        setContentView(frameGame);

        //add backgroud for frame game
        backgroudGameView = new BackgroudGameView(this,0,0);
        frameGame.addView(backgroudGameView);
        backgroudGameView.startMoveBG(this);

        // add charracter for game
        ninja = new CharacterGameView(this);
        ninja.setZOrderOnTop(true);
        ninja.getHolder().setFormat(PixelFormat.TRANSPARENT);

        frameGame.addView(ninja);


    }

    @Override
    protected void onRestart() {
        backgroudGameView.startMoveBG(this);
        ninja.onResumeMySurfaceView();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        backgroudGameView.stopMoveBG();
        ninja.onPauseMySurfaceView();
        super.onStop();
    }


}
