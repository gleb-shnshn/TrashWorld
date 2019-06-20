package glebshanshin.trashworld;

import android.app.Application;
import android.content.SharedPreferences;
import android.media.MediaPlayer;

public class App extends Application {
    MediaPlayer menuPlayer, clickPlayer;

    public static App getInstance() {
        return instance;
    }

    static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        clickPlayer = MediaPlayer.create(this, R.raw.click);//настройка плеера для кликов
        menuPlayer = MediaPlayer.create(this, R.raw.menu);
        menuPlayer.setLooping(true);
        instance = this;
    }

    public SharedPreferences getStorage() {
        SharedPreferences sPref = getInstance().getSharedPreferences("user_info", MODE_PRIVATE);
        return sPref;
    }
}
