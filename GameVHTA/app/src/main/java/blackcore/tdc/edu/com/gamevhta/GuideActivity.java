package blackcore.tdc.edu.com.gamevhta;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import blackcore.tdc.edu.com.gamevhta.button.BackButton;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

public class GuideActivity extends AppCompatActivity {

    private BackButton btnBackGuide;
    private WebView webviewGuide;
    private MediaPlayer mpGuide;
    private MusicService mService = new MusicService();
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyBinder mBinder = (MusicService.MyBinder) iBinder;
            mService = mBinder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;

        }
    };

    protected void onPause(){
        mService.pauseMusic(mpGuide);
        super.onPause();

    }
    protected  void onResume(){
        mService.playMusic(mpGuide);
        super.onResume();
    }

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

        mpGuide = MediaPlayer.create(getApplicationContext(),R.raw.guide);
        musicGuide();


    }

    public void musicGuide(){
        mService.playMusic(mpGuide);
        mpGuide.setLooping(true);
    }

    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
