package bkoruznjak.from.hr.antenazagreb.adapters;

import android.support.v7.widget.RecyclerView;
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
import bkoruznjak.from.hr.antenazagreb.model.network.ArticleModel;

/**
 * Created by bkoruznjak on 07/07/16.
 */
public class ArticleRecycleAdapter extends RecyclerView.Adapter<ArticleRecycleAdapter.ArticleViewHolder> {

    private ArrayList<ArticleModel> dataSet;

    public ArticleRecycleAdapter(ArrayList<ArticleModel> data) {
        this.dataSet = data;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent,
                                                int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_card_layout, parent, false);

        //view.setOnClickListener(MainActivity.myOnClickListener);

        ArticleViewHolder myViewHolder = new ArticleViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int listPosition) {
        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).title);
        textViewVersion.setText(dataSet.get(listPosition).published_at.toString());
        ArrayList<ArticleModel.Image> articleImagesList = (ArrayList<ArticleModel.Image>) dataSet.get(listPosition).images;
        for (ArticleModel.Image articleImage : articleImagesList) {
            Picasso.with(RadioApplication.getContext()).load(articleImage.url).into(imageView);
        }
        setScaleAnimation(holder.itemView);
    }

    @Override
    public void onViewDetachedFromWindow(ArticleViewHolder holder) {
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

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;
        View mItemView;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
            this.textViewName = (TextView) itemView.findViewById(R.id.articleCardTopTextView);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.articleCardBottomTextView);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.articleCardImageView);
        }

        public void clearAnimation() {
            if (mItemView != null) {
                mItemView.clearAnimation();
            }
        }
    }
}