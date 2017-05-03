package com.haayhappen.instafarm;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.roughike.bottombar.BottomBar;

import static android.R.attr.button;
import static android.content.ContentValues.TAG;
import static com.haayhappen.instafarm.R.id.bottomBar;
import static com.haayhappen.instafarm.R.id.signinbutton;


///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// *{@link SettingsFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link SettingsFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
public class SettingsFragment extends Fragment {

    private static EditText username;
    private static EditText password;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;
    LoginListener activityCommander;
    public interface LoginListener{
        public void getLoginData(String username,String passwort);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            activityCommander =(LoginListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString());
        }

    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     //* @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1/*, String param2*/) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        try {
//            mListener = (OnFragmentInteractionListener) getActivity();
//        } catch (ClassCastException e) {
//            throw new ClassCastException(getActivity().toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view =inflater.inflate(R.layout.fragment_settings, container, false);

        username =(EditText) view.findViewById(R.id.usernameEditText);
        password =(EditText) view.findViewById(R.id.passwordEditText);
        final Button signInButton=(Button) view.findViewById(R.id.signinbutton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("SettingsFRGMT", "onClick() sign in button");
                signInClicked(v);
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void signInClicked(View view){
        Log.d("SettingsFRGMT", "sign in clicked");

        activityCommander.getLoginData(username.getText().toString(),password.getText().toString());
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }


//
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
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            // Fragment is currently visible
        }
        else {
        }
    }
}
