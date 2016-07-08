package bkoruznjak.from.hr.antenazagreb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.kogitune.activity_transition.ActivityTransitionLauncher;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.activity.SingleArticleActivity;
import bkoruznjak.from.hr.antenazagreb.adapters.ArticleRecycleAdapter;
import bkoruznjak.from.hr.antenazagreb.articles.ArticleStore;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.listener.OnItemClickListener;
import bkoruznjak.from.hr.antenazagreb.listener.RecyclerItemClickListener;
import bkoruznjak.from.hr.antenazagreb.model.network.ArticleModel;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NewsFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.newsRecyclerView)
    RecyclerView newsRecyclerView;

    private RecyclerView.Adapter newsRecycleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RadioBus myBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View newsFragmentView = inflater.inflate(R.layout.fragment_news, container, false);
        init(newsFragmentView);
        return newsFragmentView;

    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        myBus = ((RadioApplication) getActivity().getApplication()).getBus();
        bindOnClickListeners();
        handleNewsView();
    }

    @Override
    public void onResume() {
        myBus.register(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        myBus.unregister(this);
        super.onPause();
    }

    private void bindOnClickListeners() {
    }

    @Override
    public void onClick(View v) {
    }

    private void handleNewsView() {
        newsRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        newsRecyclerView.setLayoutManager(layoutManager);
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        new ArticleStore().fetchAllArticles();
    }

    @Subscribe
    public void handleArticleData(final ArrayList<ArticleModel> articleData) {
        newsRecycleAdapter = new ArticleRecycleAdapter(articleData);
        if (newsRecyclerView != null) {
            newsRecyclerView.setAdapter(newsRecycleAdapter);
            newsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplicationContext(), newsRecyclerView, new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Toast.makeText(getActivity().getApplicationContext(), "clicked item:" + position, Toast.LENGTH_SHORT).show();
//                    Intent openArticleIntent = new Intent(getActivity().getApplicationContext(), SingleArticleActivity.class);
//                    startActivity(openArticleIntent);

//                    Intent openArticleIntent = new Intent(getActivity().getApplicationContext(), SingleArticleActivity.class);
//                    String transitionName = getString(R.string.material_transition_name);
//                    ActivityOptionsCompat options =
//                            ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
//                                    view,   // The view which starts the transition
//                                    transitionName    // The transitionName of the view we’re transitioning to
//                            );
//                    ActivityCompat.startActivity(getActivity(), openArticleIntent, options.toBundle());

                    final Intent openArticleIntent = new Intent(getActivity(), SingleArticleActivity.class);
                    String jsonArticle = "";
                    try {
                        jsonArticle = LoganSquare.serialize(articleData.get(position));
                    } catch (IOException IOex) {

                    }
                    openArticleIntent.putExtra("ARTICLE", jsonArticle);
                    ActivityTransitionLauncher.with(getActivity()).from(view).launch(openArticleIntent);
                }

                @Override
                public void onItemLongClick(View view, int position) {
                    Toast.makeText(getActivity().getApplicationContext(), "long clicked item:" + position, Toast.LENGTH_SHORT).show();
                }
            }));
        }
    }
}
