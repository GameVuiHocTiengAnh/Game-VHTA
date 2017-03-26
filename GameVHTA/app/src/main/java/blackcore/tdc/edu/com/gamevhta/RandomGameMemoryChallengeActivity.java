package blackcore.tdc.edu.com.gamevhta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.image_guessing_game.ImageGuessingActivity;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;

/**
 * Created by phong on 2/27/2017.
 */


public class RandomGameMemoryChallengeActivity extends AppCompatActivity {

    private Random ran;
    private final int IMAGE_GUESING_GAME = 0;
    private ArrayList<Integer> screenGame;
    private Bundle listAndScores = null;
    
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_go_in);

        //GET BUNDLE HERE
        Intent intent = getIntent();
        if(intent != null){
            listAndScores = intent.getExtras();
        }

        screenGame = new ArrayList<Integer>();
        screenGame.add(IMAGE_GUESING_GAME);

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
                    case IMAGE_GUESING_GAME:
                        intent = new Intent(RandomGameMemoryChallengeActivity.this,ImageGuessingActivity.class);
                        break;
                }
                //ADD BUNDLE HERE
                if(listAndScores != null)
                    intent.putExtras(listAndScores);

                startActivity(intent);
                finish();
            }
        }, 2000);
    }

}
