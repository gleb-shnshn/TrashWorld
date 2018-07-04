package glebshanshin.trashworld;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

public class UniPopup extends BlurPopupWindow {
    static View mView;
    static String mText;
    static boolean isFact;

    public UniPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected View createContentView(ViewGroup parent) {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.popup, parent, false);
        TextView l = mView.findViewById(R.id.text);
        l.setText(mText);
        if (isFact) {
            ImageView i = mView.findViewById(R.id.image);
            i.setImageDrawable(getResources().getDrawable(R.drawable.loopa));
            TextView t = mView.findViewById(R.id.title);
            t.setText(getResources().getString(R.string.know));
        }
        return mView;
    }

    @Override
    protected void onShow() {
        super.onShow();
    }


    public static class Builder extends BlurPopupWindow.Builder<UniPopup> {
        public Builder(Context context, String text, boolean flag) {
            super(context);
            mText = text;
            isFact = flag;
            this.setScaleRatio(0.25f).setBlurRadius(8).setTintColor(0x30000000);
        }

        @Override
        protected UniPopup createPopupWindow() {
            return new UniPopup(mContext);
        }
    }
}