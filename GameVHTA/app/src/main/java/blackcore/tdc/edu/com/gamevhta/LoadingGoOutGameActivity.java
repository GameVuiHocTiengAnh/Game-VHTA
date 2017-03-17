package blackcore.tdc.edu.com.gamevhta;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by phong on 2/27/2017.
 */


public class LoadingGoOutGameActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_go_out);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        AVLoadingIndicatorView loadingGoOut = (AVLoadingIndicatorView) findViewById(R.id.aviSplashLoadingPrev);
        loadingGoOut.show();
        goNextActivity();
    }

    public void goNextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingGoOutGameActivity.this, TopisChoosingActivity.class);
                startActivity(intent);
                finish();
            }
        }, 4000);
    }

}
