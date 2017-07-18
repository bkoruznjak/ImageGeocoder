package bkoruznjak.from.hr.imagegeocoder.library;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

/**
 * Created by bkoruznjak on 18/07/2017.
 */

public class PhotoScanner {
    private boolean goodToGo;
    private Context mContext;
    private MediaListener mMediaListener;

    /**
     * Prepares the PhotoScanner for photo retrieval. We need the context for the Content Resolver.
     * <p>
     * Sets the media listener for new songs.
     * <p>
     * prepareSong() should be called in onStart()
     *
     * @param context
     * @param mediaListener
     */
    public void prepare(Context context, MediaListener mediaListener) {
        goodToGo = true;
        this.mContext = context;
        this.mMediaListener = mediaListener;
    }

    /**
     * Call clean() when you no longer use the PhotoScanner, preferably onStop()
     */
    public void clean() {
        goodToGo = false;
        this.mContext = null;
        this.mMediaListener = null;
    }

    /**
     * Collects photo info from the Content Resolver and returns and notifies the media listener
     * when finished.
     * <p>
     * Throws Illegal Argument Exception when called prior to a prepareSong().
     */
    public void gatherPhotoInfo() {
        if (goodToGo) {
            ContentResolver cr = mContext.getContentResolver();

            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] mProjection = {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA};
            String sortOrder =  MediaStore.Images.Media.DEFAULT_SORT_ORDER;

            Cursor cur = cr.query(uri, mProjection, null, null, sortOrder);
            int count = 0;

            if (cur != null) {
                count = cur.getCount();

                if (count > 0) {
                    while (cur.moveToNext()) {
                        String data = cur.getString(cur.getColumnIndex(MediaStore.Images.Media.DATA));
                        // Add code to get more column here
                        mMediaListener.onMediaFound(data);
                    }
                }
            }

            cur.close();
        } else {
            throw new IllegalArgumentException("read called before prepareSong");
        }
    }

    /**
     * Implement this to get data from the photo scanner back to you.
     */
    public interface MediaListener {

        void onMediaFound(String data);
    }
}