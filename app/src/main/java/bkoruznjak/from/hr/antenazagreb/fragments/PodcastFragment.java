package bkoruznjak.from.hr.antenazagreb.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluelinelabs.logansquare.LoganSquare;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.activity.MainActivity;
import bkoruznjak.from.hr.antenazagreb.activity.SinglePodcastActivity;
import bkoruznjak.from.hr.antenazagreb.adapters.PodcastRecycleAdapter;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.enums.RadioCommandEnum;
import bkoruznjak.from.hr.antenazagreb.listener.OnItemClickListener;
import bkoruznjak.from.hr.antenazagreb.listener.RecyclerItemClickListener;
import bkoruznjak.from.hr.antenazagreb.model.network.PodcastModel;
import bkoruznjak.from.hr.antenazagreb.podcasts.PodcastStore;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PodcastFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.podcastRecyclerView)
    RecyclerView podcastRecyclerView;

    private RecyclerView.Adapter podcastRecycleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RadioBus myBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View podcastFragmentView = inflater.inflate(R.layout.fragment_podcast, container, false);
        podcastFragmentView.setBackground(((MainActivity) getActivity()).getBackgroundBitmap());
        init(podcastFragmentView);
        return podcastFragmentView;

    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        myBus = ((RadioApplication) getActivity().getApplication()).getBus();
        bindOnClickListeners();
        handlePodcastView();
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

    private void handlePodcastView() {
        podcastRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        podcastRecyclerView.setLayoutManager(layoutManager);
        podcastRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<PodcastModel> podcastData = ((MainActivity) getActivity()).getPodcastData();
        if (podcastData == null) {
            new PodcastStore().fetchAllPodcasts();
        } else {
            handlePodcastData(podcastData);
        }
    }

    @Subscribe
    public void handlePodcastData(final ArrayList<PodcastModel> podcastData) {
        if (podcastData.get(0) instanceof PodcastModel) {
            podcastRecycleAdapter = new PodcastRecycleAdapter(podcastData);
            if (podcastRecyclerView != null) {
                podcastRecyclerView.setAdapter(podcastRecycleAdapter);
                podcastRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplicationContext(), podcastRecyclerView, new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("bbb", "pressed podcast id:" + position);
                        myBus.post(RadioCommandEnum.PODCAST);

                        final Intent openPodcastIntent = new Intent(getActivity(), SinglePodcastActivity.class);
                        String jsonPodcast = "";
                        try {
                            jsonPodcast = LoganSquare.serialize(podcastData.get(position));
                        } catch (IOException IOex) {

                        }
                        openPodcastIntent.putExtra("PODCAST", jsonPodcast);
                        startActivity(openPodcastIntent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));
            }
        }
    }
}

