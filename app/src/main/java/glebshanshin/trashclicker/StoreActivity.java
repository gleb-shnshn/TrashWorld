package glebshanshin.trashclicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class StoreActivity extends Activity{
    int factory,robot,car,man,TSH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        setContentView(R.layout.store_main);
    }
    public void init(){
        TSH=Integer.parseInt(getIntent().getStringExtra("TSH"));
        factory=0;
        robot=0;
        car=0;
        man=0;
    }
    public void buyMan(View view) {
        if (man+1<=TSH){
            man++;
            TSH-=(man+1);
        }
        else{
            toast();
        }
    }

    private void toast() {
        Toast.makeText(getApplicationContext(),"Не достаточно TSH",Toast.LENGTH_SHORT).show();
    }

    public void buyCar(View view) {
        if ((car+1)*10<=TSH){
            car++;
            TSH-=((car+1)*10);
        }
        else{
            toast();
        }
    }
    public void buyRobot(View view) {
        if ((robot+1)*50<=TSH){
            robot++;
            TSH-=((robot+1)*50);
        }
        else{
            toast();
        }
    }
    public void buyFactory(View view) {
        if ((factory+1)*100<=TSH){
            factory++;
            TSH-=((factory+1)*100);
        }
        else{
            toast();
        }
    }



}
