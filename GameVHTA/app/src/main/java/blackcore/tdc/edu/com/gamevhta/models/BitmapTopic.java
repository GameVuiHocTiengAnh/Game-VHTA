package blackcore.tdc.edu.com.gamevhta.models;

import android.graphics.Bitmap;

/**
 * Created by phong on 3/3/2017.
 */

public class BitmapTopic {
    String topic;
    Bitmap imageTopic;

    public BitmapTopic(String topic, Bitmap imageTopic) {
        this.topic = topic;
        this.imageTopic = imageTopic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Bitmap getImageTopic() {
        return imageTopic;
    }

    public void setImageTopic(Bitmap imageTopic) {
        this.imageTopic = imageTopic;
    }
}
