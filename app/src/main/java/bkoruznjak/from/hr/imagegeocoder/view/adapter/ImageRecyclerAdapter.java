package bkoruznjak.from.hr.imagegeocoder.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import bkoruznjak.from.hr.imagegeocoder.R;
import bkoruznjak.from.hr.imagegeocoder.view.activity.MainActivity;

/**
 * Created by bkoruznjak on 18/07/2017.
 */

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.ViewHolder> {
    private List<File> mImageFileList;
    private Context mContext;

    public ImageRecyclerAdapter(Context context, List<File> imageFiles) {
        this.mContext = context;
        this.mImageFileList = imageFiles;
    }

    @Override
    public ImageRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card_image, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso.with(mContext)
                .load(mImageFileList.get(position))
                .resize(600,800)
                .into(holder.imageView);
        holder.index = position;
    }

    @Override
    public int getItemCount() {
        return mImageFileList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected ImageView imageView;
        protected int index;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.imageView = (ImageView) view.findViewById(R.id.imageView);
        }

        @Override
        public void onClick(View v) {
            v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            ((MainActivity)mContext).checkForLocation(index);
        }
    }
}
