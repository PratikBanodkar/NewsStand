package pratik.com.newsstand;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class CustomAlertDialog {

    public Dialog createDialog(String title, String msg, String pbtn, String nbtn, Context t, int layoutId)
    {

        Dialog dialog = new Dialog(t);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutId);

        TextView titleTV = (TextView) dialog.findViewById(R.id.alert_title);
        titleTV.setText(title);

        TextView msgTV = (TextView) dialog.findViewById(R.id.alert_msg);
        msgTV.setText(msg);

        Button PButton = (Button) dialog.findViewById(R.id.alert_yes);
        PButton.setText(pbtn);

        Button NButton = (Button) dialog.findViewById(R.id.alert_no);
        NButton.setText(nbtn);

        dialog.setCanceledOnTouchOutside(false);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

}
