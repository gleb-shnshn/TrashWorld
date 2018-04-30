package glebshanshin.trashclicker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class StoreActivity extends Activity{
    int factory,robot,car,man;
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
        factory=0;
        robot=0;
        car=0;
        man=0;
    }
    public void buyFactory(View view) {
        factory++;
    }

    public void buyRobot(View view) {
        robot++;
    }

    public void buyCar(View view) {
        car++;
    }

    public void buyMan(View view) {
        man++;
    }
}
