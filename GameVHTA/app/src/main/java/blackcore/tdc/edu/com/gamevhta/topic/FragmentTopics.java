package blackcore.tdc.edu.com.gamevhta.topic;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;

import blackcore.tdc.edu.com.gamevhta.LoadingGoInGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.RandomGameMemoryChallengeActivity;
import blackcore.tdc.edu.com.gamevhta.RandomGamePracticeActivity;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.models.BitmapTopic;

/**
 * Created by phong on 2/27/2017
 */


public class FragmentTopics extends android.support.v4.app.Fragment{

    private MediaPlayer mClick;
    private Animation alpha1,alpha2,alpha3,alpha4,toPicDown,toPicUp;
    private Bitmap imageTopic;
    private String toPic;
    private Dialog dialog;
    private ImageView btnLearn,btnCancel,btnPractice,btnMemory;
    private String newName = null;
    private Bundle dataTranfer = null;


    public FragmentTopics(){

    }
    public FragmentTopics(BitmapTopic bitmapTopic, @Nullable String newName){
        this.imageTopic = bitmapTopic.getImageTopic();
        this.toPic = bitmapTopic.getTopic();
        ConfigApplication.CURRENT_CHOOSE_TOPIC = bitmapTopic.getKeyWordTable();
        if(newName != null){
            this.newName = newName;
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_fragment_topic,container,false);

        dataTranfer = new Bundle();
        if(newName != null)
            dataTranfer.putString(ConfigApplication.BUNDLE_NEW_NAME,this.newName);
        final ImageView btnChoosemap = (ImageView) view.findViewById(R.id.btnSchool);
        alpha1 = AnimationUtils.loadAnimation(view.getContext(), R.anim.alpha);
        alpha2 = AnimationUtils.loadAnimation(view.getContext(), R.anim.alpha);
        alpha3 = AnimationUtils.loadAnimation(view.getContext(), R.anim.alpha);
        alpha4 = AnimationUtils.loadAnimation(view.getContext(), R.anim.alpha);
        toPicDown = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_down);
        toPicUp = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_up);

        dialog = new Dialog(view.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_choose_topic);
        dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        btnCancel = (ImageView) dialog.findViewById(R.id.btnCancel);
        btnLearn = (ImageView) dialog.findViewById(R.id.btnLearn);
        btnMemory = (ImageView) dialog.findViewById(R.id.btnMemory);
        btnPractice = (ImageView) dialog.findViewById(R.id.btnPractice);

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), getResources().getString(R.string.fontPath));

        TextView txtNameTopic = (TextView) view.findViewById(R.id.txtNameTopic);
        txtNameTopic.setText(toPic);
        txtNameTopic.setTypeface(custom_font);

        mClick = MediaPlayer.create(view.getContext(),R.raw.click);

        btnChoosemap.setImageBitmap(imageTopic);

        btnChoosemap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnChoosemap.startAnimation(toPicDown);
                mClick.start();
                EventDialogChooseTopic();
                dialog.show();

            }
        });

        return view;
    }
    public void EventDialogChooseTopic(){
        btnCancel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnCancel.setSelected(!btnCancel.isSelected());
                        btnCancel.isSelected();
                        mClick.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        mClick.start();
                        dialog.dismiss();
                        return true;
                }
                return false;
            }
        });

        btnLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.start();
                btnLearn.startAnimation(alpha1);
                Intent intent = new Intent(view.getContext(),LoadingGoInGameActivity.class);
                if(newName != null){
                    intent.putExtra("data",dataTranfer);
                }
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.start();
                btnMemory.startAnimation(alpha2);
                Intent intent = new Intent(view.getContext(),RandomGameMemoryChallengeActivity.class);
                if(newName != null){
                    intent.putExtra("data",dataTranfer);
                }
                startActivity(intent);
                getActivity().finish();
            }
        });

        btnPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.start();
                btnPractice.startAnimation(alpha3);
                Intent intent = new Intent(view.getContext(),RandomGamePracticeActivity.class);
                if(newName != null){
                    intent.putExtra("data",dataTranfer);
                }
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

}
