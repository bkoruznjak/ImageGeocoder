package bkoruznjak.from.hr.imagegeocoder.library;

import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Created by bkoruznjak on 18/07/2017.
 */

public class ImageMetaReader {

    public void reatMetadataLoc(String filepath) {
        try {
            ExifInterface exif = new ExifInterface(filepath);
            String latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String latitude_ref = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String longitude_ref = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            Log.d("žžž", "lat:" + latitude + ", ref:" + latitude_ref + ", long:" + longitude + ", ref:" + longitude_ref);
        } catch (IOException ioEx) {
            Log.e("žžž", "error reading exif");
        }

    }
}
