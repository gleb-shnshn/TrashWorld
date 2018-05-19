package glebshanshin.trashworld;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {
    MediaPlayer menuPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        menuPlayer=MediaPlayer.create(this,R.raw.menu);
        menuPlayer.start();
    }

    public void toStart(View view) {
        Intent intent = new Intent(MainActivity.this, PlayActivity.class);
        startActivity(intent);
        finish1();
    }

    private void finish1() {
        menuPlayer=MediaPlayer.create(this,R.raw.click);
        menuPlayer.setVolume(0.4f,0.4f);
        menuPlayer.start();
        finish();
    }

    public void toExit(View view) {
        finish1();
    }

    public void toAchievements(View view) {
        Intent intent = new Intent(MainActivity.this, AchievementsActivity.class);
        startActivity(intent);
        finish1();
    }

    public void toSettings(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
        finish1();
    }
}