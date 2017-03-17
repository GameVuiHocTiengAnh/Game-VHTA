package blackcore.tdc.edu.com.gamevhta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.WindowManager;
import android.widget.TextView;

import blackcore.tdc.edu.com.gamevhta.button.BackButton;

public class GuideActivity extends AppCompatActivity {

    private BackButton btnBackGuide;
    private TextView txtGuide;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnBackGuide = (BackButton) findViewById(R.id.btnBackGuide);
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        btnBackGuide.setMoveActivity(intent,GuideActivity.this);
        txtGuide = (TextView) findViewById(R.id.txtGuide);


        txtGuide.setText("hướng dẫn chơi : " +
                "khi người chơi nhấp vào play sẽ vào màn chọn chủ đề chơi ," +
                "khi chọn chủ đề sẽ hiện ra dialog báo người chơi chọn cấp độ chơi" +
                "khi người chơi muốn trở về màn hình menu chính sẽ nhấp nút back để trở về" +
                "nếu người chơi muốn xem điểm thì nhấp vào hình bảng điểm trên màn hình menu chính và xem điểm" +
                "khi không muốn chơi nữa hãy nhấp vào hình tấm thảm trên màn hình menu chính để exit"+
                "Phần chơi game:" +
                "khi chọn chủ để chơi sẽ xuất hiện 3 phần tương ứng là chọn học từ , nhớ từ và luyện từ, khi vào màn chơi sẽ random các game nhằm tạo hứng thú cho người chơi" +
                ", bạn sẽ không chọn dc màn chơi mà sẽ phụ thuộc vào việc random" +
                "khi kết thúc 1 màn chơi : nếu thắng sẽ được điểm thưởng và qua màn mới , nếu thua hiện ra một bảng nhập tên người chơi vừa rồi để kết thúc game và chơi lại");

        txtGuide.setMovementMethod(new ScrollingMovementMethod());

    }

    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
