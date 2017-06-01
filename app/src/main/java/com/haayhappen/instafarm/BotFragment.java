package com.haayhappen.instafarm;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

import co.lujun.androidtagview.TagContainerLayout;
import co.lujun.androidtagview.TagView;
import mabbas007.tagsedittext.TagsEditText;

import static android.R.attr.max;
import static android.R.attr.mode;
import static android.R.attr.tag;
import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BotFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button stopBotButton;
    Button startButton;
    Switch likeswitch;
    Switch followswitch;
    Switch unfollowswitch;
    Switch commentswich;

    EditText likecount;
    EditText followcount;
    EditText unfollowcount;
    EditText commentcount;

    TagsEditText tagsEditText;
    TagContainerLayout mTagContainerLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    InteractionListener activityCommander;

    public interface InteractionListener {
        void runbot(bot bot);

        void stopbot();
        //void getBot(bot bot);
    }

    public BotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *               // * @param param2 Parameter 2.
     * @return A new instance of fragment BotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BotFragment newInstance(String param1/*, String param2*/) {
        BotFragment fragment = new BotFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bot, container, false);

        //Get views
        likeswitch = (Switch) view.findViewById(R.id.likeswitch);
        followswitch = (Switch) view.findViewById(R.id.followswitch);
        unfollowswitch = (Switch) view.findViewById(R.id.unfollowswitch);
        commentswich = (Switch) view.findViewById(R.id.commentswitch);

        likecount = (EditText) view.findViewById(R.id.likecount);
        followcount = (EditText) view.findViewById(R.id.followcount);
        unfollowcount = (EditText) view.findViewById(R.id.unfollowcount);
        commentcount = (EditText) view.findViewById(R.id.commentcount);


        //tagsEditText = (TagsEditText) view.findViewById(R.id.tagsEditText);
        final ArrayList<String> tags = new ArrayList<>();
        //TagView
        String[] values=new String[]{"photooftheday","l4l","f4f"};
        for (int i=0;i <values.length;i++){
            tags.add(values[i]);
        }

        mTagContainerLayout = (TagContainerLayout) view.findViewById(R.id.tagcontainerLayout);
        mTagContainerLayout.setTags(tags);
        //mTagContainerLayout.setTags();
        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {

            @Override
            public void onTagClick(int position, String text) {
                // ...
            }

            @Override
            public void onTagLongClick(final int position, String text) {
                // ...
            }

            @Override
            public void onTagCrossClick(int position) {
                // ...
            }
        });

        stopBotButton = (Button) view.findViewById(R.id.stopBotButton);
        stopBotButton.setEnabled(false);
        startButton = (Button) view.findViewById(R.id.runBotButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SettingsFRGMT", "onClick() run bot button");
                runBotButtonClicked(v);
            }
        });

        stopBotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SettingsFRGMT", "onClick() stop button");
                stopBotButtonClicked(v);
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void stopBotButtonClicked(View v) {
        Instafarm ins = (Instafarm) new Instafarm();
        boolean loggedin = ins.isLoggedIn();

//        this.stopBotButton.setEnabled(false);
//        this.startButton.setEnabled(true);
        activityCommander.stopbot();
    }


    private void runBotButtonClicked(View v) {
//TODO validate switches -->if !checked set values to null
        int likes;
        int follows;
        int unfollows;
        int comments;

        likes = Integer.parseInt(likecount.getText().toString());
        follows = Integer.parseInt(followcount.getText().toString());
        unfollows = Integer.parseInt(unfollowcount.getText().toString());
        comments = Integer.parseInt(commentcount.getText().toString());


        int mode =0;
        int maxLikeForOneTag=50;
        int minlikes = 5;
        int maxlikes = 50;
        String[] blacklist = {"porn", "sex"};
        List<String> list = mTagContainerLayout.getTags();
        String[] taglist = list.toArray(new String[list.size()]);

        bot botWithConfig = new bot(likes, follows, unfollows, comments, minlikes, maxlikes, taglist, blacklist,maxLikeForOneTag,mode);
        activityCommander.runbot(botWithConfig);
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }

        try {
            activityCommander = (InteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
