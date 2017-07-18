package bkoruznjak.from.hr.imagegeocoder.library;

import android.media.ExifInterface;
import android.util.Log;

import java.io.IOException;

/**
 * Created by bkoruznjak on 18/07/2017.
 */

public class ImageMetaReader {

    Float Latitude, Longitude;

    public Float[] readMetadataLoc(String filepath) {
        try {
            ExifInterface exif = new ExifInterface(filepath);
            String latitude = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String latitude_ref = exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String longitude = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String longitude_ref = exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            if (latitude != null && longitude != null) {
                Log.d("žžž", "lat:" + convertToDegree(latitude) + ", ref:" + latitude_ref + ", long:" + convertToDegree(longitude) + ", ref:" + longitude_ref);
                return new Float[]{convertToDegree(latitude), convertToDegree(longitude)};
            } else {
                return null;
            }

        } catch (IOException ioEx) {
            Log.e("žžž", "error reading exif");
            return null;
        }

    }

    private Float convertToDegree(String stringDMS) {
        Float result = null;
        String[] DMS = stringDMS.split(",", 3);

        String[] stringD = DMS[0].split("/", 2);
        Double D0 = new Double(stringD[0]);
        Double D1 = new Double(stringD[1]);
        Double FloatD = D0 / D1;

        String[] stringM = DMS[1].split("/", 2);
        Double M0 = new Double(stringM[0]);
        Double M1 = new Double(stringM[1]);
        Double FloatM = M0 / M1;

        String[] stringS = DMS[2].split("/", 2);
        Double S0 = new Double(stringS[0]);
        Double S1 = new Double(stringS[1]);
        Double FloatS = S0 / S1;

        result = new Float(FloatD + (FloatM / 60) + (FloatS / 3600));

        return result;


    }

    public int getLatitudeE6(Float latitude) {
        return (int) (latitude * 1000000);
    }

    public int getLongitudeE6(Float longitude) {
        return (int) (longitude * 1000000);
    }
}
