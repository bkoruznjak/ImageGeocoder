package bkoruznjak.from.hr.imagegeocoder.view.activity;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import bkoruznjak.from.hr.imagegeocoder.R;
import bkoruznjak.from.hr.imagegeocoder.databinding.ActivityMainBinding;
import bkoruznjak.from.hr.imagegeocoder.library.PhotoScanner;
import bkoruznjak.from.hr.imagegeocoder.util.PermissionHelper;

public class MainActivity extends AppCompatActivity implements PhotoScanner.MediaListener {

    private ActivityMainBinding mainBinding;
    private PhotoScanner mPhotoScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mPhotoScanner = new PhotoScanner();
        mainBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionHelper.hasReadStorageRights(getBaseContext())) {
                    mPhotoScanner.gatherPhotoInfo();
                } else {
                    PermissionHelper.requestReadStorage(getParent());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mPhotoScanner.prepare(this, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPhotoScanner.clean();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PermissionHelper.READ_EXTERNAL_STORAGE_PERMISSION_ID:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mPhotoScanner != null) {
                        mPhotoScanner.gatherPhotoInfo();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "App does not have sufficient permissions to check your media", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onMediaFound(String data) {
        Log.d("žžž", "photo found:" + data);
    }
}
