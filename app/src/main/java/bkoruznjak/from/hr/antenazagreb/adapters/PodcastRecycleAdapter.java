package bkoruznjak.from.hr.antenazagreb.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.constants.AnimationConstants;
import bkoruznjak.from.hr.antenazagreb.constants.UtilConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.PodcastModel;

/**
 * Created by bkoruznjak on 16/10/2016.
 */

public class PodcastRecycleAdapter extends RecyclerView.Adapter<PodcastRecycleAdapter.PodcastViewHolder> {

    private ArrayList<PodcastModel> dataSet;
    private int imageWidth;
    private int imageHeight;

    public PodcastRecycleAdapter(ArrayList<PodcastModel> data) {
        this.dataSet = data;
        this.imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, UtilConstants.ARTICLE_IMAGE_HEIGHT, RadioApplication.getContext().getResources().getDisplayMetrics());
        this.imageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, UtilConstants.ARTICLE_IMAGE_WIDTH, RadioApplication.getContext().getResources().getDisplayMetrics());
    }

    @Override
    public PodcastRecycleAdapter.PodcastViewHolder onCreateViewHolder(ViewGroup parent,
                                                                      int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.podcast_card_layout, parent, false);

        PodcastRecycleAdapter.PodcastViewHolder myViewHolder = new PodcastRecycleAdapter.PodcastViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final PodcastRecycleAdapter.PodcastViewHolder holder, final int listPosition) {
        TextView textViewDate = holder.textViewDate;
        TextView textViewTitle = holder.textViewTitle;
        ImageView podcastTypeImageView = holder.podcastTypeIconImage;

        String url = dataSet.get(listPosition).url;
        String podcastType = url.substring(url.length() - 3, url.length());

        switch (podcastType.toLowerCase()) {
            case "mp3":
                //todo set audio only icon
                podcastTypeImageView.setImageDrawable(RadioApplication.getContext().getResources().getDrawable(R.drawable.ic_podcast_audio_icon));
                break;
            case "mp4":
                //todo set audio/video icon
                podcastTypeImageView.setImageDrawable(RadioApplication.getContext().getResources().getDrawable(R.drawable.ic_podcast_video_icon));
                break;
            default:
                break;
        }

        textViewDate.setText(dataSet.get(listPosition).createdAt);
        textViewTitle.setText(dataSet.get(listPosition).name);
        setScaleAnimation(holder.itemView);
    }

    @Override
    public void onViewDetachedFromWindow(PodcastRecycleAdapter.PodcastViewHolder holder) {
        holder.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(AnimationConstants.FADE_DURATION);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(AnimationConstants.FADE_DURATION);
        view.startAnimation(anim);
    }

    public static class PodcastViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDate;
        TextView textViewTitle;
        ImageView podcastTypeIconImage;
        View mItemView;

        public PodcastViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
            this.textViewDate = (TextView) itemView.findViewById(R.id.podcastCardTopTextView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.podcastCardBottomTextView);
            this.podcastTypeIconImage = (ImageView) itemView.findViewById(R.id.podcastTypeIcon);
        }

        public void clearAnimation() {
            if (mItemView != null) {
                mItemView.clearAnimation();
            }
        }
    }
}