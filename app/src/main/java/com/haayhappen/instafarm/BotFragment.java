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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    InteractionListener activityCommander;

    public interface InteractionListener {
        void runbot();
        void stopbot();
        void getBot(bot bot);
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
        Instafarm ins = (Instafarm)new Instafarm();
        boolean loggedin = ins.isLoggedIn();

//        this.stopBotButton.setEnabled(false);
//        this.startButton.setEnabled(true);
        activityCommander.stopbot();
    }


    private void runBotButtonClicked(View v) {

//        this.stopBotButton.setEnabled(true);
//        this.startButton.setEnabled(false);

        activityCommander.runbot();
    }

    private void getBotDetails(View v) {

//        this.stopBotButton.setEnabled(true);
//        this.startButton.setEnabled(false);
//get all data TODO add variableslike contruktor
        bot bot = new bot();
        activityCommander.getBot(bot);
    }
    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

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
