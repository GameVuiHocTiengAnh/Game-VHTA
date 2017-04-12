package blackcore.tdc.edu.com.gamevhta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.market_game.Market_game;
import blackcore.tdc.edu.com.gamevhta.writing_practice_game.writing_practice_game;

/**
 * Created by phong on 2/27/2017.
 */


public class RandomGamePracticeActivity extends AppCompatActivity {

    private Random ran;
    private final int MAKET_GAME = 0;
    private final int WRITING_PRACTICE_GAME = 1;
    private ArrayList<Integer> screenGame;
    private Bundle listWordsUsing = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_go_in);

        //GET BUNDLE HERE
        Intent intent = getIntent();
        if(intent != null){
            listWordsUsing = intent.getExtras();
        }

        screenGame = new ArrayList<Integer>();
        screenGame.add(MAKET_GAME);
        screenGame.add(WRITING_PRACTICE_GAME);


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
                    case MAKET_GAME:
                        intent = new Intent(RandomGamePracticeActivity.this,Market_game.class);
                        break;
                    case WRITING_PRACTICE_GAME:
                        intent = new Intent(RandomGamePracticeActivity.this,writing_practice_game.class);
                        break;
                }

                //ADD BUNDLE HERE
                if(listWordsUsing != null)
                    intent.putExtras(listWordsUsing);
                startActivity(intent);
                finish();
            }
        }, 500);
    }

}
