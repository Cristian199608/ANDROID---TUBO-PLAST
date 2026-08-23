package com.example.sm_tubo_plast.genesys.util.spinner;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.sm_tubo_plast.R;

import java.util.ArrayList;

public class ArrayAdapterSpinnerCustom {
    Context context;
    int pintarIndexPosition;
    public ArrayAdapterSpinnerCustom(@NonNull Context context,
                                     int pintarIndexPosition) {
        this.context =context;
        this.pintarIndexPosition=pintarIndexPosition;
    }

    public ArrayAdapter<CharSequence> get(ArrayList<CharSequence> objects){
        return new ArrayAdapter<CharSequence>(
                context,
                R.layout.spinner_item,
                objects
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                if (position == pintarIndexPosition) {
                    view.setBackgroundColor(getContext().getResources().getColor(R.color.blue_100));
                } else {
                    view.setBackground(getContext().getResources().getDrawable(R.drawable.box_border));
                }
                return view;
            }
        };
    }

}
