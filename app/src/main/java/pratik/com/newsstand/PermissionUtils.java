package pratik.com.newsstand;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

public class PermissionUtils {
    public static int verifyPermissions(int[] grantResults) {
        if (grantResults.length < 1) {
            return -1;
        }

        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return i;
            }
        }
        return -1;
    }

    public static String getGoSettingsMessage(Context context, String permission) {
        String message;

        switch (permission) {

            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                message = context.getString(R.string.permission_android_permission_WRITE_EXTERNAL_STORAGE);
                break;

            default:
                message = context.getString(R.string.permission_default_message);
                break;
        }

        return message;
    }
}
