package bkoruznjak.from.hr.antenazagreb.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bkoruznjak.from.hr.antenazagreb.R;

/**
 * Created by bkoruznjak on 18/09/16.
 */
public class TutorialFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_tutorial_content, container, false);

        return rootView;
    }
}
