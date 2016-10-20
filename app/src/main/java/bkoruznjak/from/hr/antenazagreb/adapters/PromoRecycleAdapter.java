package bkoruznjak.from.hr.antenazagreb.adapters;

import android.content.Context;
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
import bkoruznjak.from.hr.antenazagreb.model.network.PromoModel;

/**
 * Created by bkoruznjak on 20/10/2016.
 */

public class PromoRecycleAdapter extends RecyclerView.Adapter<PromoRecycleAdapter.PromoViewHolder> {

    private ArrayList<PromoModel> dataSet;
    private int imageWidth;
    private int imageHeight;
    private Context mContext;

    public PromoRecycleAdapter(ArrayList<PromoModel> data) {
        this.dataSet = data;
        this.imageHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, UtilConstants.ARTICLE_IMAGE_HEIGHT, RadioApplication.getContext().getResources().getDisplayMetrics());
        this.imageWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, UtilConstants.ARTICLE_IMAGE_WIDTH, RadioApplication.getContext().getResources().getDisplayMetrics());
    }

    @Override
    public PromoRecycleAdapter.PromoViewHolder onCreateViewHolder(ViewGroup parent,
                                                                  int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.promo_card_layout, parent, false);
        mContext = parent.getContext();
        PromoRecycleAdapter.PromoViewHolder myViewHolder = new PromoRecycleAdapter.PromoViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final PromoRecycleAdapter.PromoViewHolder holder, final int listPosition) {
        TextView textTitle = holder.promoTitle;
        TextView textDescription = holder.promoDescription;
        TextView textExpiresAt = holder.promoExpiresAt;
        ImageView promoTypeImageView = holder.promoImage;

        Picasso.with(RadioApplication.getContext()).load(dataSet.get(listPosition).imageUrl).into(promoTypeImageView);

        textTitle.setText(dataSet.get(listPosition).name);
        textExpiresAt.setText(dataSet.get(listPosition).humanReadableExpiresTime);
        textDescription.setText(dataSet.get(listPosition).description);
        setScaleAnimation(holder.itemView);
    }

    @Override
    public void onViewDetachedFromWindow(PromoRecycleAdapter.PromoViewHolder holder) {
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

    public static class PromoViewHolder extends RecyclerView.ViewHolder {

        TextView promoTitle;
        TextView promoDescription;
        TextView promoExpiresAt;
        ImageView promoImage;
        View mItemView;

        public PromoViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
            this.promoTitle = (TextView) itemView.findViewById(R.id.promo_title);
            this.promoDescription = (TextView) itemView.findViewById(R.id.promo_description);
            this.promoExpiresAt = (TextView) itemView.findViewById(R.id.promo_expires_at);
            this.promoImage = (ImageView) itemView.findViewById(R.id.promoImageView);
        }

        public void clearAnimation() {
            if (mItemView != null) {
                mItemView.clearAnimation();
            }
        }
    }
}