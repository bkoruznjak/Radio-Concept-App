package bkoruznjak.from.hr.antenazagreb.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.activity.MainActivity;
import bkoruznjak.from.hr.antenazagreb.adapters.PromoRecycleAdapter;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.listener.OnItemClickListener;
import bkoruznjak.from.hr.antenazagreb.listener.RecyclerItemClickListener;
import bkoruznjak.from.hr.antenazagreb.model.network.PromoModel;
import bkoruznjak.from.hr.antenazagreb.promo.PromoStore;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PromoFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.promoRecyclerView)
    RecyclerView promoRecyclerView;

    private RecyclerView.Adapter promoRecycleAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private RadioBus myBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View promoFragmentView = inflater.inflate(R.layout.fragment_promo, container, false);
        promoFragmentView.setBackground(((MainActivity) getActivity()).getBackgroundBitmap());
        init(promoFragmentView);
        return promoFragmentView;

    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        myBus = ((RadioApplication) getActivity().getApplication()).getBus();
        bindOnClickListeners();
        handlePromoView();
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

    private void handlePromoView() {
        promoRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        promoRecyclerView.setLayoutManager(layoutManager);
        promoRecyclerView.setItemAnimator(new DefaultItemAnimator());

        ArrayList<PromoModel> promoData = ((MainActivity) getActivity()).getPromoData();
        if (promoData == null) {
            promoRecycleAdapter = new PromoRecycleAdapter(new ArrayList<PromoModel>());
            promoRecyclerView.setAdapter(promoRecycleAdapter);
            new PromoStore().fetchAllPromotions();
        } else {
            handlePromoData(promoData);
        }
    }

    @Subscribe
    public void handlePromoData(final ArrayList<PromoModel> promoData) {
        if (promoData.get(0) instanceof PromoModel) {
            promoRecycleAdapter = new PromoRecycleAdapter(promoData);
            if (promoRecyclerView != null) {
                promoRecyclerView.setAdapter(promoRecycleAdapter);
                promoRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplicationContext(), promoRecyclerView, new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("bbb", "pressed promo id:" + position);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                    }
                }));
            }
            promoRecycleAdapter.notifyDataSetChanged();
        }
    }
}

