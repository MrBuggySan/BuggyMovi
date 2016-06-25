package com.example.andreibuiza.buggymovi;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by AndreiBuiza on 6/19/2016.
 */
public class FrontMenuFragment extends Fragment implements View.OnClickListener{
    OnButtonSelectedListener mListener;
    private String LOG_TAG= FrontMenuFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try {
            mListener = (OnButtonSelectedListener) context;
            Log.d(LOG_TAG, "Activity has succesfully implemented callback function");
        } catch (ClassCastException e) {
            Log.e(LOG_TAG, "Activity did not implement the required OnButtonSelectedListener");
            throw new ClassCastException(context.toString() + " must implement OnButtonSelectedListener");

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.frontmenufragment, container, false);

        //Set the onClick event for each button
        Button myButton=null;
        int[] buttonIds= new int[]{R.id.popButton, R.id.topButton, R.id.nowPlayingButton, R.id.upcomingButton};
        for (int i = 0 ; i < buttonIds.length ; i++){
            myButton=(Button) rootView.findViewById(buttonIds[i]);
            myButton.setOnClickListener(this);
        }

        //Change the title bar to the title of the app
        Toolbar myToolbar = (Toolbar) getActivity().findViewById(R.id.my_toolbar);
        myToolbar.setTitle(getString(R.string.app_name));

        //TODO:Improve aesthetics of the front page fragment, prettify

        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onClick(View view){
        int pressedButtonID= view.getId();
        mListener.OnButtonSelected(pressedButtonID);
    }

    // Container Activity must implement this interface
    public interface OnButtonSelectedListener {
        public void OnButtonSelected(int buttonID);
    }



}
