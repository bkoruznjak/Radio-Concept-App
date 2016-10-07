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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.constants.AnimationConstants;
import bkoruznjak.from.hr.antenazagreb.constants.UtilConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.SocialModel;

/**
 * Created by bkoruznjak on 02/10/2016.
 */

public class SocialRecycleAdapter extends RecyclerView.Adapter<SocialRecycleAdapter.SocialViewHolder> {

    private ArrayList<SocialModel> dataSet;
    private int imageWidth;
    private int imageHeight;

    public SocialRecycleAdapter(ArrayList<SocialModel> data) {
        this.dataSet = data;
        this.imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, UtilConstants.ARTICLE_IMAGE_HEIGHT, RadioApplication.getContext().getResources().getDisplayMetrics());
        this.imageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, UtilConstants.ARTICLE_IMAGE_WIDTH, RadioApplication.getContext().getResources().getDisplayMetrics());
    }

    @Override
    public SocialRecycleAdapter.SocialViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.social_card_layout, parent, false);

        SocialRecycleAdapter.SocialViewHolder myViewHolder = new SocialRecycleAdapter.SocialViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final SocialRecycleAdapter.SocialViewHolder holder, final int listPosition) {
        TextView textViewDate = holder.textViewDate;
        TextView textViewTitle = holder.textViewTitle;
        ImageView imageView = holder.imageViewIcon;
        ImageView socialNetworkLogoView = holder.imageSocialLogoIcon;

        switch (dataSet.get(listPosition).type) {
            case "FACEBOOK":
                socialNetworkLogoView.setImageDrawable(RadioApplication.getContext().getResources().getDrawable(R.drawable.icon_png_facebook));
                break;
            case "TWITTER":
                socialNetworkLogoView.setImageDrawable(RadioApplication.getContext().getResources().getDrawable(R.drawable.icon_png_twitter));
                break;
            case "INSTAGRAM":
                socialNetworkLogoView.setImageDrawable(RadioApplication.getContext().getResources().getDrawable(R.drawable.icon_png_instagram));
                break;
        }

        textViewDate.setText(dataSet.get(listPosition).postedAt);
        textViewTitle.setText(dataSet.get(listPosition).text);
        Picasso.with(RadioApplication.getContext()).load(dataSet.get(listPosition).pictureFullLink).resize(imageHeight, imageWidth).centerCrop().into(imageView);
        setScaleAnimation(holder.itemView);
    }

    @Override
    public void onViewDetachedFromWindow(SocialRecycleAdapter.SocialViewHolder holder) {
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

    public static class SocialViewHolder extends RecyclerView.ViewHolder {

        TextView textViewDate;
        TextView textViewTitle;
        ImageView imageViewIcon;
        ImageView imageSocialLogoIcon;
        View mItemView;

        public SocialViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
            this.textViewDate = (TextView) itemView.findViewById(R.id.socialCardTopTextView);
            this.textViewTitle = (TextView) itemView.findViewById(R.id.socialCardBottomTextView);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.socialCardImageView);
            this.imageSocialLogoIcon = (ImageView) itemView.findViewById(R.id.socialNetworkIcon);
        }

        public void clearAnimation() {
            if (mItemView != null) {
                mItemView.clearAnimation();
            }
        }
    }
}