package bkoruznjak.from.hr.antenazagreb.fragments;

import android.content.Intent;
import android.net.Uri;
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
import bkoruznjak.from.hr.antenazagreb.activity.MainActivity;
import bkoruznjak.from.hr.antenazagreb.adapters.SocialRecycleAdapter;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.listener.OnItemClickListener;
import bkoruznjak.from.hr.antenazagreb.listener.RecyclerItemClickListener;
import bkoruznjak.from.hr.antenazagreb.model.network.SocialModel;
import bkoruznjak.from.hr.antenazagreb.social.SocialStore;
import butterknife.BindView;
import butterknife.ButterKnife;


public class SocialFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.socialRecyclerView)
    RecyclerView socialRecyclerView;

    private RecyclerView.Adapter socialRecycleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RadioBus myBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View socialFragmentView = inflater.inflate(R.layout.fragment_social, container, false);
        socialFragmentView.setBackground(((MainActivity) getActivity()).getBackgroundBitmap());
        init(socialFragmentView);
        return socialFragmentView;

    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        myBus = ((RadioApplication) getActivity().getApplication()).getBus();
        bindOnClickListeners();
        handleSocialView();
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

    private void handleSocialView() {
        socialRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        socialRecyclerView.setLayoutManager(layoutManager);
        socialRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<SocialModel> socialData = ((MainActivity) getActivity()).getSocialData();
        if (socialData == null) {
            new SocialStore().fetchRecentSocials();
        } else {
            handleSocialData(socialData);
        }
    }

    @Subscribe
    public void handleSocialData(final ArrayList<SocialModel> socialData) {
        if (socialData.get(0) instanceof SocialModel) {
            socialRecycleAdapter = new SocialRecycleAdapter(socialData);
            if (socialRecyclerView != null) {
                socialRecyclerView.setAdapter(socialRecycleAdapter);
                socialRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplicationContext(), socialRecyclerView, new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        final Intent openSocialIntent = new Intent(Intent.ACTION_VIEW);
                        String uri = socialData.get(position).onlineLink;
                        openSocialIntent.setData(Uri.parse(uri));
                        startActivity(openSocialIntent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));
            }
        }
    }
}
