package bkoruznjak.from.hr.imagegeocoder.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by bkoruznjak on 18/07/2017.
 */

public class PermissionHelper {
    public static final int READ_EXTERNAL_STORAGE_PERMISSION_ID = 1337;

    public static boolean hasReadStorageRights(@NonNull final Context context) {
        int permissionCheckExternalStorage = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionCheckExternalStorage != PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
        }
    }

    public static void requestReadStorage(Activity activity) {
        ActivityCompat.requestPermissions(
                activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_ID);
    }

}
