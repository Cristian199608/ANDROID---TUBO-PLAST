package com.example.sm_tubo_plast.genesys.adapters;


import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SessionesAdapter extends FragmentStatePagerAdapter {

    private static final String  TAG = "SessionesAdapter";
    private final ArrayList<Fragment> lisFragment= new ArrayList<>();
    private final ArrayList<String> listaTitulos= new ArrayList<>();

    public SessionesAdapter(FragmentManager fm) {
        super(fm);
    }
    public void agregarFragments(Fragment fragment, String titulos){
        lisFragment.add(fragment);
        listaTitulos.add(titulos);
        Log.i(TAG, "agregarFragments:: start");
        //FragmentStatePagerAdapter.removeFragment(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return  lisFragment.get(position);
    }

    @Override
    public int getCount() {
        return lisFragment.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {

        return listaTitulos.get(position);
    }
    public  void CambiarTitulo(int pos, String titulo){
        try {
            listaTitulos.set(pos, titulo);
            getPageTitle(pos);
        }catch (Exception e){

        }
    }
}
