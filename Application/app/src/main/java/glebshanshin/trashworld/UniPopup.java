package glebshanshin.trashworld;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

public class UniPopup extends BlurPopupWindow {//класс как AlertDialog только с размытием задней части экрана
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
        l.setText(mText);//установка текста
        if (isFact) {//если это факт
            ImageView i = mView.findViewById(R.id.image);
            i.setImageDrawable(getResources().getDrawable(R.drawable.loopa));//установка изображения лупы
            TextView t = mView.findViewById(R.id.title);
            t.setText(getResources().getString(R.string.know));//установка нового заголовка
        }
        return mView;
    }

    @Override
    protected void onShow() {
        super.onShow();
    }


    public static class Builder extends BlurPopupWindow.Builder<UniPopup> {
        public Builder(Context context, String text, boolean flag) {//передача данных
            super(context);
            mText = text;//текст для установки в TextView
            isFact = flag;//значение определяющее является ли это уведомление фактом
            this.setScaleRatio(0.25f).setBlurRadius(8).setTintColor(0x30000000);
        }

        @Override
        protected UniPopup createPopupWindow() {
            return new UniPopup(mContext);
        }
    }
}