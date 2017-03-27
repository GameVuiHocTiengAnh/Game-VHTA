package blackcore.tdc.edu.com.gamevhta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.catching_words_game.CatchingWordsActivity;
import blackcore.tdc.edu.com.gamevhta.choosing_objects_game.ChoosingObjectActivity;
import blackcore.tdc.edu.com.gamevhta.picture_puzzle_game.PicturePuzzleActivity;

/**
 * Created by phong on 2/27/2017.
 */


public class LoadingGoInGameActivity extends AppCompatActivity {

    private Random ran;
    private final int  PICTURE_PUZZLE_GAME = 0;
    private final int MH6_CATCHING_WORDS_GAME = 1;
    private final int CHOOSE_IMAGE_OBJECT_GAME= 2;
    private ArrayList<Integer> screenGame;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_go_in);
        screenGame = new ArrayList<Integer>();
        screenGame.add(PICTURE_PUZZLE_GAME);
        screenGame.add(MH6_CATCHING_WORDS_GAME);
        screenGame.add(CHOOSE_IMAGE_OBJECT_GAME);

        Random ran = new Random();
        int dom = ran.nextInt(screenGame.size());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AVLoadingIndicatorView loadingGoIn = (AVLoadingIndicatorView) findViewById(R.id.aviSplashLoadingNext);
        loadingGoIn.show();
        goNextActivity(dom);
    }

    public void goNextActivity(final int screen){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = null;
                switch (screen){
                    case PICTURE_PUZZLE_GAME:
                        intent = new Intent(LoadingGoInGameActivity.this,PicturePuzzleActivity.class);
                        break;
                    case MH6_CATCHING_WORDS_GAME:
                        intent = new Intent(LoadingGoInGameActivity.this,CatchingWordsActivity.class);
                        break;
                    case CHOOSE_IMAGE_OBJECT_GAME:
                        intent = new Intent(LoadingGoInGameActivity.this,ChoosingObjectActivity.class);
                        break;
                                }
                startActivity(intent);
                finish();
            }
        }, 500);
    }

}
