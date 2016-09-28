package bkoruznjak.from.hr.antenazagreb.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import bkoruznjak.from.hr.antenazagreb.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SocialFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SocialFragment extends Fragment {

    //    private AccessTokenTracker accessTokenTracker;
//    private CallbackManager callbackManager;
    private static final String SEARCH_QUERY = "#AntenaZgb";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View socialFragmentView;
    @BindView(R.id.btnHandleTweets)
    AppCompatButton btnHandleTweets;
    @BindView(R.id.tweetListView)
    ListView tweetListView;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SocialFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PodcastFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SocialFragment newInstance(String param1, String param2) {
        SocialFragment fragment = new SocialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
//
//        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App code
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        // savedInstanceState
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        Log.e("bbb", "desilo se sranje:" + exception);
//                    }
//                });
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile, user_groups"));
//
//        accessTokenTracker = new AccessTokenTracker() {
//            @Override
//            protected void onCurrentAccessTokenChanged(
//                    AccessToken oldAccessToken,
//                    AccessToken currentAccessToken) {
//
//            }
//        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        socialFragmentView = inflater.inflate(R.layout.fragment_social, container, false);
        init(socialFragmentView);
        return socialFragmentView;
    }

    private void init(View view) {
        ButterKnife.bind(this, view);
        btnHandleTweets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TwitterApiClient client = TwitterCore.getInstance().getGuestApiClient();
//                SearchService searchService = client.getSearchService();
//                final Call<Search> callRequest = searchService.tweets("@AntenaZGB", null, null, null, "mixed", 15, null, null,
//                        null, true);
//                callRequest.enqueue(new Callback<Search>() {
//                    @Override
//                    public void success(Result<Search> result) {
//                        List<Tweet> tweets = result.data.tweets;
//                        ArrayList<String> tweetsList = new ArrayList<String>();
//                        for (Tweet tweet : tweets) {
//                            tweetsList.add(tweet.text);
//                        }
//
//                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, tweetsList);
//
//                        tweetListView.setAdapter(adapter1);
//
//                    }
//
//                    @Override
//                    public void failure(TwitterException exception) {
//
//                    }
//                });
//                try {
//                    getUserData(AccessToken.getCurrentAccessToken());
//                    Log.d("bbb", "Access Token: " + AccessToken.getCurrentAccessToken().getToken().toString());
//                } catch (Exception e) {
//                    Log.e("bbb", "error is: " + e.toString());
//                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        try {
//            accessTokenTracker.stopTracking();
//        } catch (Exception e) {
//            Log.e("bbb", "error is: " + e.toString());
//        }
    }

//    public void getUserData(AccessToken accessToken) {
//
//        GraphRequest.newGraphPathRequest(
//                accessToken, "/100003667531313/posts",
//                new GraphRequest.Callback() {
//                    @Override
//                    public void onCompleted(GraphResponse graphResponse) {
//                        try {
//                            Log.d("bbb", "Object Length is : " + graphResponse.getJSONObject().length());
//                            Log.d("bbb", "request was: " + graphResponse.getRequest());
//                            String resp = graphResponse.getRawResponse();
//                            Log.d("bbb", "response is: " + resp);
//                        } catch (Exception e) {
//                            Log.e("bbb", "error is: " + e.toString());
//                        }
//                    }
//                }).executeAsync();
//    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("bbb", "tu sam");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
