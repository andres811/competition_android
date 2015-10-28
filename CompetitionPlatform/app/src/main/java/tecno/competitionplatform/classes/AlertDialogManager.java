package tecno.competitionplatform.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Andres on 26/10/2015.
 */
public class AlertDialogManager {

    public static void getErrorDialog(final Context context, String title, String message, String btnText, final boolean backToPrevActivity ) {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(btnText, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (backToPrevActivity == true) {
                            ((Activity) context).finish();
                        }
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}