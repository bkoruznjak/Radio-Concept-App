package bkoruznjak.from.hr.antenazagreb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
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
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.articleCardTopTextView);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.articleCardBottomTextView);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.articleCardImageView);
        }
    }
}