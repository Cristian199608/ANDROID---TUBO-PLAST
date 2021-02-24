package com.example.fuerzaventaschema.genesys.fuerza_ventas.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuerzaventaschema.R;
import com.example.fuerzaventaschema.genesys.BEAN.San_Visitas;
import com.example.fuerzaventaschema.genesys.DAO.DAO_San_Visitas;
import com.example.fuerzaventaschema.genesys.adapters.AdapterAgendaActividades;
import com.example.fuerzaventaschema.genesys.datatypes.DBclasses;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_actividad_campo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_actividad_campo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_actividad_campo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private  boolean REFRESH_onResume=true;
    private  int ACTUAL_SELECTED_ANIO = 2019;
    private  int ACTUAL_POSITION_MES = -1;

    AdapterAgendaActividades ADAPTER_agenda;
    ArrayList<San_Visitas> lista_visitasORIGINAL =new ArrayList<>();
    ArrayList<San_Visitas> lista_visitasMOV =new ArrayList<>();

    RecyclerView recyclerView2;
    DBclasses dBclasses;
    String FECHA;
    String CODVEN;
    public Fragment_actividad_campo() {
        // Required empty public constructor
    }


    public Fragment_actividad_campo(DBclasses dBclasses, String CODVEN, ArrayList<San_Visitas> lista_visitasMOV, String FECHA ) {
        // Required empty public constructor
        this.dBclasses=dBclasses;
        this.lista_visitasMOV=lista_visitasMOV;
        this.FECHA=FECHA;
        this.CODVEN=CODVEN;
    }


    public static Fragment_actividad_campo newInstance(String param1, String param2) {
        Fragment_actividad_campo fragment = new Fragment_actividad_campo();
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
        REFRESH_onResume=false;
        View view= inflater.inflate(R.layout.fragment_actividad_campo, container, false);

        recyclerView2=view.findViewById(R.id.recyclerView2);
        LlenarRecyclerView(FECHA, ACTUAL_SELECTED_ANIO, ACTUAL_POSITION_MES);
        return  view;
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


    @Override
    public void onResume() {
        super.onResume();
        if (REFRESH_onResume){
            LlenarRecyclerView(this.FECHA, this.ACTUAL_SELECTED_ANIO, this.ACTUAL_POSITION_MES );
        }else{
            REFRESH_onResume=true;
        }

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void LlenarRecyclerView( int anio, int nroMes){
        ACTUAL_SELECTED_ANIO=anio;
        ACTUAL_POSITION_MES=nroMes;
    }
    public void  LlenarRecyclerView(String fecha, int anio, int mes ){

        ACTUAL_SELECTED_ANIO=anio;
        ACTUAL_POSITION_MES=mes;
        this.FECHA=fecha;

        String f="0"+(ACTUAL_POSITION_MES+1);
        lista_visitasMOV= DAO_San_Visitas.getSan_VisitasByFecha(dBclasses,CODVEN, "%", ""+fecha);
        //lista_visitasMOV.clear();
        //lista_visitasMOV.addAll(lista_visitasORIGINAL);

        if (ACTUAL_POSITION_MES>=0){
            recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
            ADAPTER_agenda=new AdapterAgendaActividades(getActivity() ,dBclasses, this.lista_visitasMOV,fecha);
            recyclerView2.setAdapter(ADAPTER_agenda);
        }
    }

    public void refresRecyclerView(ArrayList<San_Visitas> lista, String fecha){
        this.lista_visitasMOV=lista;
//        ACTUAL_SELECTED_ANIO=anio;
//        ACTUAL_POSITION_MES=nroMes;
        this.FECHA=fecha;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        ADAPTER_agenda=new AdapterAgendaActividades(getActivity() ,dBclasses, this.lista_visitasMOV,fecha);
        recyclerView2.setAdapter(ADAPTER_agenda);
    }
    public int sizeLISTA(){
        return  lista_visitasMOV.size();
    }
}
