package com.example.fuerzaventaschema.genesys.fuerza_ventas.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.adapters.SessionesAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentContenedorAgendado.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentContenedorAgendado#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentContenedorAgendado extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private final String TAG = this.getClass().getSimpleName();
    private AppBarLayout appBar;
    private TabLayout pestañas;
    private ViewPager viewPager;
    SessionesAdapter adapterSession;

    private OnFragmentInteractionListener mListener;

    Fragment_actividad_campo fragment_actividad_campo;
    public FragmentContenedorAgendado() {
        // Required empty public constructor
    }

    public FragmentContenedorAgendado(Fragment_actividad_campo campo) {
        // Required empty public constructor
        this.fragment_actividad_campo=campo;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentContenedorAgendado.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentContenedorAgendado newInstance(String param1, String param2) {
        FragmentContenedorAgendado fragment = new FragmentContenedorAgendado();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista=inflater.inflate(R.layout.fragment_contenedor_agendado, container, false);

        View parent=(View)container.getParent();

        if (appBar==null){
            appBar=(AppBarLayout)parent.findViewById(R.id.AppBar);
            pestañas=new TabLayout(getActivity());//2979FF
            pestañas.setTabTextColors(getActivity().getResources().getColor(R.color.teal_200), getActivity().getResources().getColor(R.color.teal_700));
            pestañas.setSelectedTabIndicatorColor(getActivity().getResources().getColor(R.color.teal_600));
            appBar.addView(pestañas);
            appBar.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
            viewPager=(ViewPager)vista.findViewById(R.id.fragmentContenedores);

            llenarViewPager(viewPager);
            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    Log.i(TAG, " onPageScrolled "+position+", positionOffset "+positionOffset+", positionOffsetPixels "+positionOffsetPixels);
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    /*if (Old_position!=position && positionOffset==0.0){
                        Old_position=position;
                        mListener.EvalularFilter(position, true);
                    }*/
                }
            });

            pestañas.setupWithViewPager(viewPager);

        }
        pestañas.setTabGravity(TabLayout.GRAVITY_FILL);
        //viewPager.getChildAt(1).performClick();

        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void llenarViewPager(ViewPager viewPagers) {

        adapterSession=new SessionesAdapter(getFragmentManager());

//        listaFragment.clear();
//        listaFragment.add(graficarFragment);
//        listaFragment.add(lineaNegocioFragment);
//        listaFragment.add(nivelProductoFragmen);

        adapterSession.agregarFragments(fragment_actividad_campo, "Actividad");

        viewPagers.setAdapter(adapterSession);
        Log.i(TAG, "adapterSession.getCount(); 1 "+adapterSession.getCount());


    }
    public void CambiarTituloByTAB(int pos, String titulo){
        adapterSession.CambiarTitulo(0+pos, ""+titulo);
        adapterSession.notifyDataSetChanged();
    }

    public boolean viewPagerItem(int pos){
        try {
            Log.i(TAG, "viewPagerItem:: position "+pos);

            viewPager.setCurrentItem(pos);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }
}
