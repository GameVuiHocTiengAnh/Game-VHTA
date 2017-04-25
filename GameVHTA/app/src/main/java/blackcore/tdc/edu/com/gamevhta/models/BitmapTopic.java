package blackcore.tdc.edu.com.gamevhta.models;

import android.graphics.Bitmap;

/**
 * Created by phong on 3/3/2017.
 */

public class BitmapTopic {
    String topic;
    Bitmap imageTopic;
    String keyWordTable;

    public BitmapTopic(String topic, Bitmap imageTopic,String keyWordTable ) {
        this.topic = topic;
        this.imageTopic = imageTopic;
        this.keyWordTable = keyWordTable;
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

    public String getKeyWordTable() {
        return keyWordTable;
    }

    public void setKeyWordTable(String keyWordTable) {
        this.keyWordTable = keyWordTable;
    }
}
