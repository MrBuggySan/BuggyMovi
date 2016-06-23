package com.example.andreibuiza.buggymovi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by AndreiBuiza on 6/19/2016.
 */
public class FrontMenuFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.frontmenufragment, container, false);

        Button myButton= (Button) rootView.findViewById(R.id.popButton);
//        myButton.setOnClickListener(onClickButton(myButton));
        //TODO:Improve aesthetics of the front page fragment

        return rootView;

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    //TODO:Event when click occurs
    public void onClickButton(View view){
        CharSequence text = "Loading...";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getContext(), text, duration);
        toast.show();


    }
}
