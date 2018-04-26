package glebshanshin.trashclicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity extends Activity {
    private String choice,tag;
    ImageView trash;
    TextView TSH,TSHs;
    int Adder=1;
    String tsh="         0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.play_main);
        trash= findViewById(R.id.trash);
        TSH=findViewById(R.id.TSH);
        TSHs=findViewById(R.id.TSHs);
        TSH.setText(tsh);
        newTrash();
    }
    public void toMenu(View view) {
        Intent intent1 = new Intent(PlayActivity.this, MainActivity.class);
        startActivity(intent1);
        finish();
    }
    public void increaseTSH(){
        String newa=""+(Integer.parseInt(tsh.replaceAll(" ",""))+Adder);
        String prefix=new String(new char[10-newa.length()]).replace("\0", " ");
        tsh=prefix+newa;
        TSH.setText(tsh);

    }
    public void newTrash(){
        String id = ""+((int)(Math.random()*16)+1);
        tag=getTag(id);
        trash.setImageDrawable(getDrawable(getResources().getIdentifier("trash"+id , "trash", getPackageName())));
    }

    private String getTag(String id) {
        int id1=Integer.parseInt(id);
        if ((id1>=1)&(id1<=3)){
            return "glass";
        }
        else if ((id1>=4)&(id1<=5)){
            return "metal";
        }
        else if ((id1>=6)&(id1<=8)){
            return "notrecycle";
        }
        else if ((id1>=9)&(id1<=9)){
            return "organic";
        }
        else if ((id1>=10)&(id1<=13)){
            return "paper";
        }
        else if ((id1>=14)&(id1<=16)){
            return "plastic";
        }
        else{
            return "none";
        }
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
        if (tag==choice) {
            Toast.makeText(this, "Правильно", Toast.LENGTH_SHORT).show();
            increaseTSH();
        }
        else
            Toast.makeText(this,"неПравильно",Toast.LENGTH_SHORT).show();
        choice="null";
        newTrash();
    }



}
