package com.example.sm_tubo_plast.genesys.util.recyclerView;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;


public class RecyclerViewCustom extends LinearLayout {

    private TextView txtEmptyData;
    private RecyclerView recyclerView;
    private ProgressBar myProgressBar;
    private boolean isLoading = false;
    private float pocentajePantalla=100;
    public RecyclerViewCustom(Context context) {
        super(context);
        init(context);
    }

    public RecyclerViewCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.layout_custom_recycler_view, this, true);

        txtEmptyData = findViewById(R.id.txtEmptyData);
        recyclerView = findViewById(R.id.recyclerView);
        myProgressBar = findViewById(R.id.myProgressBar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        setEmptyText("No hay datos para mostrar");
    }

    public void setEmptyText(String text) {
        txtEmptyData.setText(text);
        txtEmptyData.setVisibility(VISIBLE);
        recyclerView.setVisibility(GONE);
        myProgressBar.setVisibility(GONE);
        isLoading=false;
    }
    public void setLoadingData(String text) {
        txtEmptyData.setText(text);
        txtEmptyData.setVisibility(GONE);
        recyclerView.setVisibility(GONE);
        myProgressBar.setVisibility(VISIBLE);
        isLoading=true;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
    public void setAdapter(@Nullable RecyclerView.Adapter adapter) {
        txtEmptyData.setVisibility(GONE);
        myProgressBar.setVisibility(GONE);
        recyclerView.setVisibility(VISIBLE);
        recyclerView.setAdapter(adapter);
        isLoading=false;
        gestionarHeight();
    }
    //get  para: isLoading
    public boolean isLoading() {
        return isLoading;
    }

    public void setHeight(float pocentajePantalla) {
        this.pocentajePantalla=pocentajePantalla;
    }
    private void gestionarHeight() {
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        if(recyclerView.getAdapter().getItemCount()>5){
            int screenHeight = getResources().getDisplayMetrics().heightPixels;
            int desiredHeight = (int) (screenHeight * (pocentajePantalla/100));
            layoutParams.height = desiredHeight;
        }
        else layoutParams.height = RecyclerView.LayoutParams.WRAP_CONTENT;
        recyclerView.setLayoutParams(layoutParams);
    }
}
