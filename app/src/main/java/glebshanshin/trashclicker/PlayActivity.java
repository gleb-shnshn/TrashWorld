package glebshanshin.trashclicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class PlayActivity extends Activity {
    private String choice,tag;
    ImageView trash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.play_main);
        trash= findViewById(R.id.trash);
        newTrash();
        tag="paper";
    }
    public void toMenu(View view) {
        Intent intent1 = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent1);
    }
    public void newTrash(){
        String id = ""+((int)(Math.random()*16)+1);
        tag=getTag(id);
        trash.setImageDrawable(getDrawable(getResources().getIdentifier("trash"+id , "drawable", getPackageName())));
    }

    private String getTag(String id) {
        return "none";
    }

    public void notrecycle(View view) {
        choice="notrecycle";
        game();
    }

    public void organic(View view) {
        choice="organic";
        game();
    }

    public void plastic(View view) {
        choice="plastic";
        game();
    }

    public void paper(View view) {
        choice="paper";
        game();
    }

    public void metal(View view) {
        choice="metal";
        game();
    }

    public void glass(View view) {
        choice="glass";
        game();
    }
    public void game() {
        if (tag==choice)
            Toast.makeText(this,"Правильно",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this,"неПравильно",Toast.LENGTH_SHORT).show();
        choice="null";
    }



}
