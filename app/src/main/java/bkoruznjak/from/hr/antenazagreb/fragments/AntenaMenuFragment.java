package bkoruznjak.from.hr.antenazagreb.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.mxn.soul.flowingdrawer_core.MenuFragment;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import bkoruznjak.from.hr.antenazagreb.R;
import bkoruznjak.from.hr.antenazagreb.RadioApplication;
import bkoruznjak.from.hr.antenazagreb.activity.AboutActivity;
import bkoruznjak.from.hr.antenazagreb.activity.TutorialActivity;
import bkoruznjak.from.hr.antenazagreb.bus.RadioBus;
import bkoruznjak.from.hr.antenazagreb.constants.AntenaConstants;
import bkoruznjak.from.hr.antenazagreb.constants.PreferenceKeyConstants;
import bkoruznjak.from.hr.antenazagreb.constants.StreamUriConstants;
import bkoruznjak.from.hr.antenazagreb.enums.LanguagesEnum;
import bkoruznjak.from.hr.antenazagreb.model.bus.RadioStateModel;
import bkoruznjak.from.hr.antenazagreb.model.db.SongModel;
import bkoruznjak.from.hr.antenazagreb.model.network.StreamModel;
import bkoruznjak.from.hr.antenazagreb.views.CircleTransformation;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bkoruznjak.from.hr.antenazagreb.R.id.ivMenuUserProfilePhoto;

/**
 * Created by bkoruznjak on 08/08/16.
 */
public class AntenaMenuFragment extends MenuFragment {


    @BindView(R.id.setting_autoplay)
    public SwitchCompat settingAutoplay;
    @BindView(ivMenuUserProfilePhoto)
    public ImageView drawerImage;
    @BindView(R.id.radioStationNameId)
    public TextView radioStationText;
    @BindView(R.id.songNameId)
    public TextView currentlyPlayingText;
    @BindView(R.id.setting_about)
    public Button buttonAbout;
    @BindView(R.id.setting_tutorial)
    public Button buttonTutorial;
    @BindView(R.id.setting_feedback)
    public Button buttonFeedback;
    @BindView(R.id.material_language_spinner)
    public MaterialSpinner materialLanguageSpinner;

    private RadioBus myBus;
    private RadioStateModel mRadioStateModel;
    private SharedPreferences mPreferences;

    private boolean isAutoplayOn;
    private String defaultStation;
    private int volume;
    private int avatarSize;
    private ImageView streamImageView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBus = ((RadioApplication) getActivity().getApplication()).getBus();
        mRadioStateModel = ((RadioApplication) getActivity().getApplication()).getRadioStateModel();
        mPreferences = getActivity().getSharedPreferences(PreferenceKeyConstants.PREFERENCE_NAME, Context.MODE_PRIVATE);

        isAutoplayOn = mPreferences.getBoolean(PreferenceKeyConstants.KEY_AUTOPLAY, true);
        defaultStation = mPreferences.getString(PreferenceKeyConstants.KEY_DEFAULT_STATION, StreamUriConstants.ANTENA_MAIN);
        volume = mPreferences.getInt(PreferenceKeyConstants.KEY_VOLUME, 5);
    }

    @Override
    public void onResume() {
        super.onResume();
        myBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        myBus.unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View drawerMenu = inflater.inflate(R.layout.fragment_drawer_menu, container, false);
        ButterKnife.bind(this, drawerMenu);
        avatarSize = getResources().getDimensionPixelSize(R.dimen.item_height);
        final NavigationView naviView = (NavigationView) drawerMenu.findViewById(R.id.vNavigation);
        streamImageView = (ImageView) drawerMenu.findViewById(ivMenuUserProfilePhoto);
        setupHeader();

        //radio and song information
        radioStationText.setText(mRadioStateModel.getRadioStationName());
        currentlyPlayingText.setText(mRadioStateModel.getSongAuthor().concat(" - ").concat(mRadioStateModel.getSongTitle()));
        currentlyPlayingText.setSelected(true);
        //switch settings department
        if (isAutoplayOn) {
            settingAutoplay.setChecked(true);
        } else {
            settingAutoplay.setChecked(false);
        }
        settingAutoplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mPreferences.edit().putBoolean(PreferenceKeyConstants.KEY_AUTOPLAY, isChecked).commit();
            }
        });
        //other info department
        buttonAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });
        buttonTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tutorialIntent = new Intent(getActivity(), TutorialActivity.class);
                startActivity(tutorialIntent);
            }
        });
        buttonFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendFeedbackIntent = new Intent(Intent.ACTION_SEND);
                sendFeedbackIntent.setData(Uri.parse("mailto:"));
                sendFeedbackIntent.setType("text/plain");
                String subject = getActivity().getResources().getString(R.string.setting_subject);
                String message = getActivity().getResources().getString(R.string.setting_message);
                String description = getActivity().getResources().getString(R.string.setting_feedback_description);
                String noClient = getActivity().getResources().getString(R.string.setting_no_mail_client);
                sendFeedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{AntenaConstants.SUPPORT_EMAIL});
                sendFeedbackIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                sendFeedbackIntent.putExtra(Intent.EXTRA_TEXT, message);

                try {
                    startActivity(Intent.createChooser(sendFeedbackIntent, description));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getActivity(), noClient, Toast.LENGTH_SHORT).show();
                }
            }
        });

        materialLanguageSpinner.setItems(AntenaConstants.LANGUAGE_ARRAY[1], AntenaConstants.LANGUAGE_ARRAY[0]);
        final String language = mPreferences.getString(PreferenceKeyConstants.KEY_LANGUAGE, AntenaConstants.DEFAULT_LOCALE);
        if (LanguagesEnum.hr.toString().equals(language)) {
            materialLanguageSpinner.setSelectedIndex(0);
        } else if (LanguagesEnum.en.toString().equals(language)) {
            materialLanguageSpinner.setSelectedIndex(1);
        }
        materialLanguageSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {

                if (position == 1 && !LanguagesEnum.en.toString().equals(language)) {
                    mPreferences.edit().putString(PreferenceKeyConstants.KEY_LANGUAGE, LanguagesEnum.en.toString()).commit();
                    myBus.post(LanguagesEnum.en);
                } else if (position == 0 && !LanguagesEnum.hr.toString().equals(language)) {
                    mPreferences.edit().putString(PreferenceKeyConstants.KEY_LANGUAGE, LanguagesEnum.hr.toString()).commit();
                    myBus.post(LanguagesEnum.hr);
                }

            }
        });

        return setupReveal(drawerMenu);
    }

    @Subscribe
    public void handleSongMetadata(SongModel song) {
//        radioStationText.setText(mRadioStateModel.getRadioStationName());
        currentlyPlayingText.setText(mRadioStateModel.getSongAuthor().concat(" - ").concat(mRadioStateModel.getSongTitle()));
        currentlyPlayingText.setSelected(true);
    }

    @Subscribe
    public void handleStreamModelChange(StreamModel streamEvent) {
        if (!radioStationText.getText().equals(streamEvent.name)) {
            radioStationText.setText(streamEvent.name);
            if (streamEvent.imageUrl != null) {
                Picasso.with(getActivity())
                        .load(streamEvent.imageUrl)
                        .placeholder(R.drawable.img_article_placeholder)
                        .resize(avatarSize, avatarSize)
                        .centerCrop()
                        .transform(new CircleTransformation())
                        .into(streamImageView);
            }

        }
    }

    private void setupHeader() {
        String streamImageUrl = mRadioStateModel.getStreamModel().imageUrl;
        if (streamImageView != null) {
            if (streamImageUrl != null) {
                Picasso.with(getActivity())
                        .load(streamImageUrl)
                        .placeholder(R.drawable.img_article_placeholder)
                        .resize(avatarSize, avatarSize)
                        .centerCrop()
                        .transform(new CircleTransformation())
                        .into(streamImageView);
            } else {
                Picasso.with(getActivity())
                        .load(R.drawable.random_background_image)
                        .placeholder(R.drawable.img_article_placeholder)
                        .resize(avatarSize, avatarSize)
                        .centerCrop()
                        .transform(new CircleTransformation())
                        .into(streamImageView);
            }
        }
    }

    public void onOpenMenu() {
        //Toast.makeText(getActivity(),"onOpenMenu",Toast.LENGTH_SHORT).show();
    }

    public void onCloseMenu() {
        //Toast.makeText(getActivity(),"onCloseMenu",Toast.LENGTH_SHORT).show();
    }
}