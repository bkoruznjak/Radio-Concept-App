package bkoruznjak.from.hr.antenazagreb.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.adapters.ArticleRecycleAdapter;
import bkoruznjak.from.hr.antenazagreb.articles.ArticleStore;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
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
    public void handleArticleData(ArrayList<ArticleModel> articleData) {
        newsRecycleAdapter = new ArticleRecycleAdapter(articleData);
        if (newsRecyclerView != null) {
            newsRecyclerView.setAdapter(newsRecycleAdapter);
        }
    }
}
