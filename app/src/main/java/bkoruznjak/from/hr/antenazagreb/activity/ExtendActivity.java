package bkoruznjak.from.hr.antenazagreb.activity;

import android.os.Bundle;

import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsActivity;

import bkoruznjak.from.hr.antenazagreb.R;

/**
 * Created by bkoruznjak on 11/10/2016.
 */

public class ExtendActivity extends LibsActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setIntent(new LibsBuilder().withLibraries("exoplayer", "logansquare", "ripplebackground", "flowingdrawer", "skyfishjy", "cleveroad", "materialspinner").withActivityTheme(R.style.AppTheme_NoActionBar).intent(this));
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.drawer_enter_in, R.anim.drawer_enter_out);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.drawer_exit_in, R.anim.drawer_exit_out);
    }
}