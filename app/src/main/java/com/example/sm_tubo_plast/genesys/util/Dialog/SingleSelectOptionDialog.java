package com.example.sm_tubo_plast.genesys.util.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.util.EditTex.ACG_EditText;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class SingleSelectOptionDialog {

    String txtTitulo;
    RecyclerView recyclerView;
    FaseSingleActividadAdapter adapter;
    ArrayList<SinglechoiceCustom> listaSingleChoice =new ArrayList<>();
    ArrayList<SinglechoiceCustom> listaSingleChoiceFilter =new ArrayList<>();
    SinglechoiceCustom singleChoiceSelected;
    Activity activity;
    MyListener myListener;
    public SingleSelectOptionDialog(Activity activity, String txtTitulo, ArrayList<SinglechoiceCustom> lista) {
        this.activity = activity;
        this.txtTitulo=txtTitulo;
        this.listaSingleChoice.addAll(lista);
        this.listaSingleChoiceFilter.addAll(lista);

    }
    public void clearData(){
        txtTitulo=null;
        adapter=null;
        recyclerView=null;
        listaSingleChoice.clear();
        singleChoiceSelected=null;
        listaSingleChoiceFilter.clear();

    }

    public void setDataSelected(ArrayList<SinglechoiceCustom> lista){

        for (int i = 0; i < listaSingleChoice.size(); i++) {
            for (SinglechoiceCustom itemS : lista) {
                if((listaSingleChoice.get(i).titulo+ listaSingleChoice.get(i).opcion).equalsIgnoreCase(itemS.titulo+itemS.opcion)){
                    singleChoiceSelected= listaSingleChoice.get(i);
                }
            }
        }
    }

    public void show(MyListener myListener){
        this.myListener=myListener;
        Dialog dialogo=new Dialog(activity);
        dialogo.setContentView(R.layout.layout_single_select_spinner);
        dialogo.setCancelable(false);

        //final View laViewInflada = LayoutInflater.from(activity).inflate(R.layout.seleccion_fase_actividad, null, true);
        TextView tvTitulo=dialogo.findViewById(R.id.tvTitulo);
        EditText etBuscarOpcion=dialogo.findViewById(R.id.etBuscarOpcion);
        recyclerView = dialogo.findViewById(R.id.recyclerView);
        Button txt_cancelar=dialogo.findViewById(R.id.txt_cancelar);
        Button txt_aceptar=dialogo.findViewById(R.id.txt_aceptar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));

        tvTitulo.setText(txtTitulo);
        dialogo.show();


        adapter=new FaseSingleActividadAdapter(activity, listaSingleChoiceFilter);
        recyclerView.setAdapter(adapter);
        adapter.setOnClik(new FaseSingleActividadAdapter.MyCallback() {
            @Override
            public void onClik(int position, boolean isSelected) {
                if(isSelected){
                    singleChoiceSelected= listaSingleChoiceFilter.get(position);
                }
                else singleChoiceSelected=null;
            }

            @Override
            public SinglechoiceCustom getItemSelected() {
                return singleChoiceSelected;
            }
        });


        new ACG_EditText(activity, etBuscarOpcion).OnListen(searchString ->{
            listaSingleChoiceFilter.clear();
            if (searchString.length()==0) {
                listaSingleChoiceFilter.addAll(listaSingleChoice);
            }else{
                String texto=searchString.replace(" ",".*").toLowerCase();
                listaSingleChoiceFilter.clear();
                for (SinglechoiceCustom multichoiceCustom : listaSingleChoice) {
                    Pattern pattern = Pattern.compile(".*"+texto+".*");
                    if (pattern.matcher((multichoiceCustom.titulo+""+multichoiceCustom.opcion).toLowerCase()).matches()) {
                        listaSingleChoiceFilter.add(multichoiceCustom);
                    }
                }
            }
            adapter.notifyDataSetChanged();

        });

        txt_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogo.dismiss();
                myListener.dismis();
            }
        });
        txt_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(singleChoiceSelected!=null){
                    myListener.Result(singleChoiceSelected);
                    myListener.dismis();
                    dialogo.dismiss();
                }
                else {
                    myListener.Result(null);
                }


            }
        });

    }


    public static class SinglechoiceCustom{
        public String titulo;
        public String opcion;
        public int orden;
        public  Object tag;

        public SinglechoiceCustom() {

        }
        public SinglechoiceCustom(String opcion, int orden) {
            this.opcion = opcion;
            this.orden=orden;
        }
        public SinglechoiceCustom(String titulo, String opcion) {
            this.titulo = titulo;
            this.opcion = opcion;
            this.orden=orden;
        }
        public SinglechoiceCustom(String titulo, String opcion, Object tag) {
            this.titulo = titulo;
            this.opcion = opcion;
            this.tag= tag;
        }
    }

    public interface MyListener{
        void  Result(SinglechoiceCustom listaSelected);
        void dismis();
    }

}


class FaseSingleActividadAdapter extends RecyclerView.Adapter<FaseSingleActividadAdapter.ViewHolder> {
    ArrayList<SingleSelectOptionDialog.SinglechoiceCustom> lista;
    Activity activity;

    MyCallback myCallback;
    public interface MyCallback{
        void onClik(int position, boolean isSelected);
        SingleSelectOptionDialog.SinglechoiceCustom getItemSelected();
    }

    public FaseSingleActividadAdapter(Activity activity, ArrayList<SingleSelectOptionDialog.SinglechoiceCustom> lista ) {
        this.lista = lista;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_single_select_spinner, null, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final SingleSelectOptionDialog.SinglechoiceCustom item=lista.get(position);

        viewHolder.tvTitulo.setVisibility(View.GONE);
        if(item.titulo!=null && item.titulo.length()>0){
            viewHolder.tvTitulo.setText(item.titulo);
            viewHolder.tvTitulo.setVisibility(View.VISIBLE);
        }
        viewHolder.tvOrden.setText(String.valueOf(position+1));
        viewHolder.tvOpcion.setText(item.opcion);
        boolean isSelected=false;
        if(myCallback!=null){
            if(myCallback.getItemSelected()!=null){
                isSelected=item==myCallback.getItemSelected();
            }
        }
        viewHolder.radioButton.setChecked(isSelected);
        viewHolder.itemView.setBackgroundColor(activity.getResources().getColor(position%2==0?R.color.grey_50:R.color.white));
        //-----------------------------------------------------------------------------------------------
        if (position+1==lista.size()) {
            viewHolder.itemView.setPadding(0,0,0,200);
        }else viewHolder.itemView.setPadding(0,0,0,0);
    }

    @Override
    public int getItemCount() {
        return this.lista.size();
    }

    public void  setOnClik(MyCallback myCallback){
        this.myCallback=myCallback;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout llContainer;
        TextView tvTitulo, tvOpcion, tvOrden;
        RadioButton radioButton;
        public ViewHolder(View itemView) {
            super(itemView);
            llContainer=itemView.findViewById(R.id.llContainer);
            tvTitulo=itemView.findViewById(R.id.tvTitulo);
            tvOrden=itemView.findViewById(R.id.tvOrden);
            tvOpcion =itemView.findViewById(R.id.tvOpcion);
            radioButton =itemView.findViewById(R.id.radioButton);
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if (myCallback!=null){
                        myCallback.onClik(position, radioButton.isChecked());
                        notifyDataSetChanged();
                    }
                }
            });
            llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    radioButton.performClick();
                }
            });
        }
    }
}

