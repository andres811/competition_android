package tecno.competitionplatform.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import tecno.competitionplatform.activities.BaseActivity;
import tecno.competitionplatform.activities.R;

/**
 * Created by Andres on 26/10/2015.
 */
public class AlertDialogManager {

    public static void getErrorDialog(final Context context, String title, String message, String btnText, final boolean backToPrevActivity ) {

        ContextThemeWrapper ctw = new ContextThemeWrapper( context, R.style.CustomThemeDialog );

        AlertDialog dialog  = new AlertDialog.Builder(ctw)
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
                .setCancelable(false)
                .create();

        dialog.show();

        Button dialogButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        dialogButton.setBackgroundResource(R.drawable.background_button_color);
        dialogButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dialogButton.setTextColor(context.getResources().getColor(R.color.normal_text));
    }
}