package bkoruznjak.from.hr.imagegeocoder.view.activity;

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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bkoruznjak.from.hr.imagegeocoder.R;
import bkoruznjak.from.hr.imagegeocoder.databinding.ActivityMainBinding;
import bkoruznjak.from.hr.imagegeocoder.geocode.GeocodeAsyncTask;
import bkoruznjak.from.hr.imagegeocoder.library.ImageMetaReader;
import bkoruznjak.from.hr.imagegeocoder.library.ImageScanner;
import bkoruznjak.from.hr.imagegeocoder.util.PermissionHelper;
import bkoruznjak.from.hr.imagegeocoder.view.adapter.ImageRecyclerAdapter;

public class MainActivity extends AppCompatActivity implements ImageScanner.MediaListener, OnMapReadyCallback {

    //map
    private static final float DEFAULT_ZOOM_LEVEL = 6.0f;
    private final String mDatePattern = "dd-MM-yyyy";
    private ActivityMainBinding mainBinding;
    private ImageScanner mImageScanner;
    private ImageRecyclerAdapter mImageAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<File> mFileList;
    private ImageMetaReader mImageMetaReader;
    private float mCoordinateOffset = 0.0001f;
    private boolean isReducingOffset = false;
    private GoogleMap mMap;
    private double mLatitude = 45.77;
    private double mLongitude = 15.98;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mFileList = new ArrayList<File>();
        mImageMetaReader = new ImageMetaReader();
        mainBinding.mapView.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mImageAdapter = new ImageRecyclerAdapter(this, mFileList);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mainBinding.imageRecyclerView.setLayoutManager(mLayoutManager);
        mainBinding.imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mainBinding.imageRecyclerView.setAdapter(mImageAdapter);


        mainBinding.mapView.getMapAsync(this);
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
            PermissionHelper.requestReadStorage(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mainBinding.mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mainBinding.mapView.onPause();
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
    }

    public void manageProgressBar(boolean show) {
        if (show) {
            mainBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            mainBinding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void checkForLocation(int imageIndex) {
        Float[] locationDataArray = mImageMetaReader.readMetadataLoc(mFileList.get(imageIndex).getPath());
        if (locationDataArray != null && locationDataArray[0] != 0 && locationDataArray[1] != 0) {
            new GeocodeAsyncTask(this).execute(locationDataArray[0], locationDataArray[1]);
        } else {
            Toast.makeText(this, "Image does not have coordinates", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //user marker
        mMap = googleMap;
        LatLng userLocation = new LatLng(mLatitude, mLongitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, DEFAULT_ZOOM_LEVEL));
    }

    public void addImageMarker(float latitude, float longitude, String address) {
        LatLng imageLocation = new LatLng(latitude, longitude);
        mMap.clear();
        Marker imageMarker = mMap.addMarker(new MarkerOptions()
                .position(imageLocation)
                .title(address)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.img_geolocal)));
    }
}
