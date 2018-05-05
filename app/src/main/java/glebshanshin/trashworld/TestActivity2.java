package glebshanshin.trashworld;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TestActivity2 extends Activity implements View.OnTouchListener {
    private TextView tv;
    private ImageView mImageView;
    private ViewGroup mMoveLayout;
    private int mX;
    private int mY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        mMoveLayout =findViewById(R.id.move);
        tv=findViewById(R.id.textView);
        mImageView = mMoveLayout.findViewById(R.id.ImageView);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(500, 500);
        mImageView.setLayoutParams(layoutParams);
        mImageView.setOnTouchListener(this);
    }
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                mX = X - lParams.leftMargin;
                mY = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - mX;
                layoutParams.topMargin = Y - mY;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
            case MotionEvent.ACTION_UP:
                tv.setText(X+" "+Y);
        }
        return true;
    }
}