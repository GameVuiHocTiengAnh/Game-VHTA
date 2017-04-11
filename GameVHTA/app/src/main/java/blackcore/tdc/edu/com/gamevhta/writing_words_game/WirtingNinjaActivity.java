package blackcore.tdc.edu.com.gamevhta.writing_words_game;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.RandomGameMemoryChallengeActivity;
import blackcore.tdc.edu.com.gamevhta.RandomGamePracticeActivity;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.CatchingWordsActivity;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.custom_toask.CustomToask;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;

/**
 * Created by Shiro on 4/4/2017.
 */

public class WirtingNinjaActivity extends CatchingWordsActivity{

    private Dialog dialogWriting;
    private ImageView imvDialogWriting;
    private EditText txtDialogWriting;
    private Typeface gothic;
    private String textWord;
    private ArrayList<WordObject> listUsing;

    private ImageView btnCheck;
    private int scores;
    private TextView txtHintWord;
    private Bitmap bmUsing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listUsing = this.getListWordsUsing();
        gothic = this.getTyeface();
        textWord = null;
        bmUsing = null;
        scores = 0;
        this.setWritingMode(true);
    }

    @Override
    protected void initDialogCatchingWords() {
        dialogWriting = new Dialog(this);
        dialogWriting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWriting.setCanceledOnTouchOutside(false);
        dialogWriting.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWriting.setContentView(R.layout.dialog_writing_game);
        dialogWriting.getWindow().getAttributes().windowAnimations = R.style.CatchingWordsDialogAnimation;
        imvDialogWriting = (ImageView)dialogWriting.findViewById(R.id.imvWriting);
        txtDialogWriting = (EditText) dialogWriting.findViewById(R.id.edtEnterWord);
        btnCheck = (ImageView) dialogWriting.findViewById(R.id.btnCheckWriting);
        txtHintWord = (TextView) dialogWriting.findViewById(R.id.txtHintWord);
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Tagtest",textWord);
                if(textWord != null ){
                    String textWriting = txtDialogWriting.getText().toString();
//                    Log.d("Tagtest",textWriting);
                    if(textWriting.trim().compareToIgnoreCase(textWord.trim()) == 0){

//                        Log.d("Tagtest",getCountComplete()+"....."+listUsing.size());
                        if(WirtingNinjaActivity.this.getCountComplete() == listUsing.size()){
                            dialogWriting.dismiss();
                            Intent intent = new Intent(WirtingNinjaActivity.this,RandomGamePracticeActivity.class);
                            Bundle data = new Bundle();
                            data.putInt(ConfigApplication.SCORES_BEFOR_GAME,scores);
                            intent.putExtras(data);
                            WirtingNinjaActivity.this.levelComplete(intent);
                        }else{
                            dialogWriting.dismiss();
                            scores+= 200;
                            txtHintWord.setText("");
                            if(bmUsing != null)
                                setWordObjectActionbar(bmUsing,textWord);
                            setScores(scores);
                            WirtingNinjaActivity.this.setNinjaDefaut();
                            WirtingNinjaActivity.this.onResumeGame();
                        }
                    }else{
                        String hint;
                        char[] chars = textWord.toCharArray();
                        if(chars.length <= 4 && chars.length > 0){
                            hint = textWord.replace(chars[1],'_');
                        }else {
                            if(chars.length > 4 && chars.length <= 6){
                                hint = textWord.replace(chars[1],'_').replace(chars[3],'_');
                            }else{
                                if(chars.length > 6 && chars.length <= 10){
                                    hint = textWord.replace(chars[1],'_').replace(chars[3],'_').replace(chars[6],'_');
                                }else{
                                    hint = textWord.replace(chars[1],'_').replace(chars[3],'_').replace(chars[6],'_').replace(chars[8],'_');
                                }
                            }
                        }
                        Log.d("Tagtest",textWord);
                        Log.d("Tagtest",hint);
                        txtHintWord.setText(hint);
                    }
                }
                txtDialogWriting.setText("");

            }
        });



        imvDialogWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(textWord, TextToSpeech.QUEUE_FLUSH,null);
                StartSmartAnimation.startAnimation(imvDialogWriting, AnimationType.StandUp,2000,0,true,2000);
            }
        });
    }

    @Override
    protected void showDialogCatchingWords(Bitmap image, String text) {
        bmUsing = image;
        imvDialogWriting.setImageBitmap(image);
        textWord = text;
        dialogWriting.show();
    }
}
