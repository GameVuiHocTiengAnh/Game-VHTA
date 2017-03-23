package blackcore.tdc.edu.com.gamevhta.topic;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import blackcore.tdc.edu.com.gamevhta.LoadingGoInGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.models.BitmapTopic;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

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

    public FragmentTopics(BitmapTopic bitmapTopic){
        this.imageTopic = bitmapTopic.getImageTopic();
        this.toPic = bitmapTopic.getTopic();
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_fragment_topic,container,false);

        final ImageView btnChoosemap = (ImageView) view.findViewById(R.id.btnSchool);
        alpha1 = AnimationUtils.loadAnimation(view.getContext(), R.anim.alpha);
        alpha2 = AnimationUtils.loadAnimation(view.getContext(), R.anim.alpha);
        alpha3 = AnimationUtils.loadAnimation(view.getContext(), R.anim.alpha);
        alpha4 = AnimationUtils.loadAnimation(view.getContext(), R.anim.alpha);
        toPicDown = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_down);
        toPicUp = AnimationUtils.loadAnimation(view.getContext(), R.anim.slide_up);

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
                mService.playMusic(mClick);
                dialog = new Dialog(view.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_dialog_choose_topic);
                dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.show();
                btnCancel = (ImageView) dialog.findViewById(R.id.btnCancel);
                btnLearn = (ImageView) dialog.findViewById(R.id.btnLearn);
                btnMemory = (ImageView) dialog.findViewById(R.id.btnMemory);
                btnPractice = (ImageView) dialog.findViewById(R.id.btnPractice);

                btnCancel.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                btnCancel.setSelected(!btnCancel.isSelected());
                                btnCancel.isSelected();
                                mService.playMusic(mClick);
                                return true;
                            case MotionEvent.ACTION_UP:
                                btnChoosemap.startAnimation(toPicUp);
                                mService.playMusic(mClick);
                                dialog.dismiss();
                                return true;
                        }
                        return false;
                    }
                });

                btnLearn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mService.playMusic(mClick);
                        btnLearn.startAnimation(alpha1);
                        Intent intent = new Intent(view.getContext(),LoadingGoInGameActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                btnMemory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mService.playMusic(mClick);
                        btnMemory.startAnimation(alpha2);
                        Intent intent = new Intent(view.getContext(),LoadingGoInGameActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

                btnPractice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mService.playMusic(mClick);
                        btnPractice.startAnimation(alpha3);
                        Intent intent = new Intent(view.getContext(),LoadingGoInGameActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
            }
        });

        return view;
    }

}
