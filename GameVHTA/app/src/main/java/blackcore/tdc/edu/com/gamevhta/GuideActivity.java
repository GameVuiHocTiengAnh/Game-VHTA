package blackcore.tdc.edu.com.gamevhta;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import blackcore.tdc.edu.com.gamevhta.button.BackButton;

public class GuideActivity extends AppCompatActivity {

    private BackButton btnBackGuide;
    private WebView webviewGuide;
    private MediaPlayer mpGuide;

    @Override
    protected void onPause(){
        super.onPause();
        if (mpGuide != null) {
            mpGuide.pause();
        }

    }
    @Override
    protected  void onResume(){
        super.onResume();
        mpGuide.start();
        mpGuide.setLooping(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mpGuide.start();
        mpGuide.setLooping(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mpGuide != null) {
            if (mpGuide.isPlaying()) {
                mpGuide.stop();
                mpGuide.release();
            } else
                mpGuide.release();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnBackGuide = (BackButton) findViewById(R.id.btnBackGuide);
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        btnBackGuide.setMoveActivity(intent,GuideActivity.this);

        webviewGuide = (WebView) findViewById(R.id.webviewGuide);
        WebSettings ws = webviewGuide.getSettings();
        ws.setJavaScriptEnabled(true);
        webviewGuide.setBackgroundColor(Color.TRANSPARENT);
        webviewGuide.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        webviewGuide.loadUrl("file:///android_asset/guide/guide.html");

        mpGuide = MediaPlayer.create(getApplicationContext(),R.raw.picturepuzzle);
        musicGuide();


    }

    public void musicGuide(){
        mpGuide.start();
        mpGuide.setLooping(true);
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        startActivity(intent);
        finish();
    }
}
