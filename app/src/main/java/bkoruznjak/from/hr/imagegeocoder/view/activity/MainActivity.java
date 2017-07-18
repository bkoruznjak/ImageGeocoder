package bkoruznjak.from.hr.imagegeocoder.view.activity;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import bkoruznjak.from.hr.imagegeocoder.R;
import bkoruznjak.from.hr.imagegeocoder.databinding.ActivityMainBinding;
import bkoruznjak.from.hr.imagegeocoder.library.ImageMetaReader;
import bkoruznjak.from.hr.imagegeocoder.library.ImageScanner;
import bkoruznjak.from.hr.imagegeocoder.util.PermissionHelper;

public class MainActivity extends AppCompatActivity implements ImageScanner.MediaListener {

    private ActivityMainBinding mainBinding;
    private ImageScanner mImageScanner;
    private ArrayList<File> mFileList;
    private ImageMetaReader mImageMetaReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mFileList = new ArrayList<>();
        mImageScanner = new ImageScanner();
        mImageMetaReader = new ImageMetaReader();
        mainBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionHelper.hasReadStorageRights(getBaseContext())) {
                    mImageScanner.gatherImageInfo();
                } else {
                    PermissionHelper.requestReadStorage(getParent());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mImageScanner.prepare(this, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mImageScanner.clean();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PermissionHelper.READ_EXTERNAL_STORAGE_PERMISSION_ID:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mImageScanner != null) {
                        mImageScanner.gatherImageInfo();
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
        File imgFile = new File(data);
        Log.d("žžž", "created new file:" + imgFile.isFile());
        mFileList.add(imgFile);

        mImageMetaReader.reatMetadataLoc(data);
    }
}
