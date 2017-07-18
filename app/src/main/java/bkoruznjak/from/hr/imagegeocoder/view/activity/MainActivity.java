package bkoruznjak.from.hr.imagegeocoder.view.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bkoruznjak.from.hr.imagegeocoder.R;
import bkoruznjak.from.hr.imagegeocoder.databinding.ActivityMainBinding;
import bkoruznjak.from.hr.imagegeocoder.library.ImageMetaReader;
import bkoruznjak.from.hr.imagegeocoder.library.ImageScanner;
import bkoruznjak.from.hr.imagegeocoder.util.PermissionHelper;
import bkoruznjak.from.hr.imagegeocoder.view.adapter.ImageRecyclerAdapter;

public class MainActivity extends AppCompatActivity implements ImageScanner.MediaListener {

    private ActivityMainBinding mainBinding;
    private ImageScanner mImageScanner;
    private ImageRecyclerAdapter mImageAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<File> mFileList;
    private ImageMetaReader mImageMetaReader;
    private Float[] mLocData = new Float[2];
    private final Activity INSTANCE = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mFileList = new ArrayList<File>();
        mImageMetaReader = new ImageMetaReader();
        init();
    }

    private void init() {
        mImageAdapter = new ImageRecyclerAdapter(this, mFileList);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mainBinding.imageRecyclerView.setLayoutManager(mLayoutManager);
        mainBinding.imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mainBinding.imageRecyclerView.setAdapter(mImageAdapter);
    }

    private void startImageScanner() {
        mImageScanner = new ImageScanner();
        mImageScanner.prepare(this, this);
        mImageScanner.gatherImageInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PermissionHelper.hasReadStorageRights(getBaseContext())) {
            startImageScanner();
        } else {
            PermissionHelper.requestReadStorage(INSTANCE);
        }
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
                    startImageScanner();
                } else {
                    Toast.makeText(getApplicationContext(), "App does not have sufficient permissions to check your media", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onMediaFound(String data) {
        File imgFile = new File(data);
        mFileList.add(imgFile);
        mImageAdapter.notifyDataSetChanged();
//        mLocData = mImageMetaReader.readMetadataLoc(data);
//        Log.d("žžž", "hvatam metadata za lat:" + mLocData[0] + ", long:" + mLocData[1]);
//        new GeocodeAsyncTask(INSTANCE).execute(mLocData[0], mLocData[1]);
    }

    public void manageProgressBar(boolean show) {
        if (show) {
            mainBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            mainBinding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}
