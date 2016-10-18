package bkoruznjak.from.hr.antenazagreb.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bluelinelabs.logansquare.LoganSquare;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.constants.UtilConstants;
import bkoruznjak.from.hr.antenazagreb.model.network.ArticleModel;
import bkoruznjak.from.hr.antenazagreb.util.ResourceUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by bkoruznjak on 07/07/16.
 */
public class SingleArticleActivity extends AppCompatActivity {

    @BindView(R.id.articleCardImageViewBig)
    ImageView articleImageView;
    @BindView(R.id.articleCardTitleTextViewBig)
    TextView articleTitle;
    @BindView(R.id.articleCardTopTextViewBig)
    TextView articleTopText;
    @BindView(R.id.articleCardBottomTextViewBig)
    TextView articleBottomText;

    private ArticleModel articleModel;
    private BitmapDrawable mBackgroundBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_article);
        overridePendingTransition(R.anim.article_enter_in, R.anim.article_enter_out);
        ButterKnife.bind(this);
        mBackgroundBitmap = new BitmapDrawable(ResourceUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.antena_bg, UtilConstants.BACKGROUND_BITMAP_WIDTH, UtilConstants.BACKGROUND_BITMAP_HEIGHT));
        RelativeLayout mainContainer = (RelativeLayout) findViewById(R.id.singleArticleContainer);
        mainContainer.setBackground(mBackgroundBitmap);
        String jsonArticle = getIntent().getStringExtra("ARTICLE");
        try {
            articleModel = LoganSquare.parse(jsonArticle, ArticleModel.class);
        } catch (IOException IOex) {

        }

        updateViewContainer(articleModel);

    }

    private void updateViewContainer(ArticleModel articleModel) {
        if (articleModel != null) {
            articleTitle.setText(articleModel.title);
            articleTopText.setText("" + articleModel.human_diff_time);
            articleBottomText.setText("" + articleModel.body);
            ArrayList<ArticleModel.Image> imageList = (ArrayList<ArticleModel.Image>) articleModel.images;
            for (ArticleModel.Image articleImage : imageList) {
                Picasso.with(this).load(articleImage.url).into(articleImageView);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.article_exit_in, R.anim.article_exit_out);
    }
}
