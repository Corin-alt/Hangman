package fr.corentin.pendu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

import fr.corentin.pendu.R;

/**
 * Class {@link BatteryReceiver}
 * @author Corentin Dupont
 * @version For project Info0306
 */

public class BatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if (plugged == 0) {
            sendToast(context, context.getResources().getString(R.string.tel_not_charged));
        } else {
            sendToast(context, context.getResources().getString(R.string.tel_charged));
        }

    }

    private void sendToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}