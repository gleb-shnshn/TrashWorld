package glebshanshin.trashworld;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

public class test extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        final TextView textView =findViewById(R.id.textView2);
        DiscreteSeekBar discreteSeekBar1 = findViewById(R.id.discrete1);
        discreteSeekBar1.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                String k = "Номинал: ";
                if (value<1000) {
                    textView.setText(k+value+"K TSH");
                } else if (value<2000) {
                    textView.setText(k+value % 1000+"M TSH");
                }
                else{
                    textView.setText(k+value % 1000+"B TSH");
                }
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
            }
        });

    }

}
