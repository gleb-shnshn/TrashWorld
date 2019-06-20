package glebshanshin.trashworld;

import android.view.View;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class QRScanActivity extends CaptureActivity {
    @Override
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.qrscan_main);
        return findViewById(R.id.place);
    }

    public void toPromo(View view) {
        finish1();
    }

    private void finish1() {
        App.getInstance().clickPlayer.start();
        finish();
    }

    @Override
    public void onBackPressed() {
        finish1();
        super.onBackPressed();
    }
}
