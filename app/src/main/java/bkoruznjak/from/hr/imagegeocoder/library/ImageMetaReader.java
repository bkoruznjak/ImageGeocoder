package bkoruznjak.from.hr.imagegeocoder.library;

import android.media.ExifInterface;
import android.util.Log;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import java.io.File;
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


    public void readMetadata(File imageFile) {
        try {
            Metadata mMetadata = ImageMetadataReader.readMetadata(imageFile);

            for (Directory directory : mMetadata.getDirectories()) {
                for (Tag tag : directory.getTags()) {
                    System.out.format("[%s] - %s = %s",
                            directory.getName(), tag.getTagName(), tag.getDescription());
                }
                if (directory.hasErrors()) {
                    for (String error : directory.getErrors()) {
                        System.err.format("ERROR: %s", error);
                    }
                }
            }

        } catch (ImageProcessingException imgEx) {
            Log.e("žžž", "image processing exception:" + imgEx);

        } catch (IOException ioEx) {
            Log.e("žžž", "IO exception:" + ioEx);
        }
    }
}
