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
    private FrameLayout frameGame;
    private Typeface gothic;    //fontface
    private ImageView imvVocalubary;
    private TextView txtVocalubary;
    private TextView tvScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowManager.LayoutParams attribute = getWindow().getAttributes();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        frameGame = new FrameLayout(this);
        FrameLayout.LayoutParams lpframe = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        frameGame.setLayoutParams(lpframe);
        setContentView(frameGame);



        // add charracter for game
        ninja = new CharacterGameView(this);
        ninja.setZOrderOnTop(true);
        ninja.getHolder().setFormat(PixelFormat.TRANSPARENT);

        //add backgroud for frame game
        backgroudGameView = new BackgroudGameView(this,0,0,ninja);
        backgroudGameView.startMoveBG(this);

        frameGame.addView(backgroudGameView);
        frameGame.addView(ninja);
        this.addTopBarView(frameGame);

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

    private void addTopBarView(FrameLayout rootLayout){

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lpLinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lpLinear.setMargins(0,10,0,0);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(lpLinear);

        gothic = Typeface.createFromAsset(getAssets(),"fonts/gothic.ttf");

        LinearLayout.LayoutParams lpHorizaltal = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        lpHorizaltal.weight = 1;
        lpHorizaltal.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams lpWrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int marginLeftRight = ConfigCWGame.getMarginVocalubary();
        lpHorizaltal.setMargins(marginLeftRight,10,marginLeftRight,10);
        lpWrap.setMargins(10,10,10,10);
        LinearLayout lnBack = new LinearLayout(this);
        LinearLayout lnVocalubary = new LinearLayout(this);
        LinearLayout lnScore = new LinearLayout(this);

        lnBack.setLayoutParams(lpWrap);
        lnScore.setLayoutParams(lpHorizaltal);

        LinearLayout.LayoutParams lpSubElement = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lpSubElement.gravity = Gravity.CENTER;
        // add btn back
        ImageView btnBack = new ImageView(this);
        btnBack.setLayoutParams(lpSubElement);
        btnBack.setImageResource(R.drawable.back1);
        lnBack.addView(btnBack);

        // show vocalubary
        lnVocalubary.setLayoutParams(lpHorizaltal);
        lnVocalubary.setGravity(Gravity.CENTER_HORIZONTAL);
        imvVocalubary = new ImageView(this);
        imvVocalubary.setLayoutParams(lpWrap);
        imvVocalubary.setImageResource(R.drawable.cow_answer);
        txtVocalubary = new TextView(this);
        txtVocalubary.setTypeface(gothic);
        txtVocalubary.setTextColor(Color.RED);
        txtVocalubary.setLayoutParams(lpWrap);
        txtVocalubary.setText("Cow");
        txtVocalubary.setTextSize(ConfigCWGame.getSizeTextScores());
        lnVocalubary.addView(imvVocalubary);
        lnVocalubary.addView(txtVocalubary);

        // show score

        tvScore = new TextView(this);
        lnScore.setLayoutParams(lpWrap);
        lnScore.setGravity(Gravity.CENTER_HORIZONTAL);
        lnScore.setBackgroundResource(R.drawable.backgroud_word);
        LinearLayout.LayoutParams lpWrapScores = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lpWrapScores.setMargins(0, ConfigCWGame.getMarginScoresView(),0,0);
        tvScore.setLayoutParams(lpWrapScores);
        tvScore.setText("1000");
        tvScore.setTextColor(Color.RED);
        tvScore.setTextSize(ConfigCWGame.getSizeTextScores());
        tvScore.setTypeface(gothic);
        lnScore.addView(tvScore);

        linearLayout.addView(lnBack);
        linearLayout.addView(lnVocalubary);
        linearLayout.addView(lnScore);

        rootLayout.addView(linearLayout);
    }


}
