package com.example.sm_tubo_plast.genesys.util.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sm_tubo_plast.R;
import com.example.sm_tubo_plast.genesys.util.EditTex.ACG_EditText;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class MultiSelectOpcionDialog {

    String txtTitulo;
    RecyclerView recyclerView;
    TextView txt_quitar, txtSeleccTodos;

    AdapterMultiOptions adapter;
    ArrayList<DataOption> listaMultiChoice =new ArrayList<>();
    ArrayList<DataOption> listaMultiChoiceFilter =new ArrayList<>();
    ArrayList<DataOption> listaMultiChoiceSelected =new ArrayList<>();
    Activity activity;
    String opcionSelected;
    MyListener myListener;
    public MultiSelectOpcionDialog(Activity activity, String txtTitulo, ArrayList<DataOption> lista) {
        this.activity = activity;
        this.txtTitulo=txtTitulo;
        this.listaMultiChoice.addAll(lista);
        this.listaMultiChoiceFilter.addAll(lista);
    }
    public void setDataSelected(ArrayList<DataOption> lista){

        for (int i = 0; i < listaMultiChoice.size(); i++) {
            for (DataOption itemS : lista) {
                if((listaMultiChoice.get(i).titulo+listaMultiChoice.get(i).opcion).equalsIgnoreCase(itemS.titulo+itemS.opcion)){
                    listaMultiChoice.get(i).isCheck=true;
                    this.listaMultiChoiceSelected.add(listaMultiChoice.get(i));
                }
            }
        }
    }

    public void show(MyListener myListener){
        this.myListener=myListener;
        Dialog dialogo=new Dialog(activity);
        dialogo.setContentView(R.layout.layout_multi_select_spinner);
        dialogo.setCancelable(false);

        //final View laViewInflada = LayoutInflater.from(activity).inflate(R.layout.seleccion_fase_actividad, null, true);
        TextView tvTitulo=dialogo.findViewById(R.id.tvTitulo);
        EditText etBuscarOpcion=dialogo.findViewById(R.id.etBuscarOpcion);
        recyclerView = dialogo.findViewById(R.id.recyclerView);
        TextView txt_cancelar=dialogo.findViewById(R.id.txt_cancelar);
        txt_quitar=dialogo.findViewById(R.id.txt_quitar);
        txtSeleccTodos=dialogo.findViewById(R.id.txtSeleccTodos);
        TextView txt_aceptar=dialogo.findViewById(R.id.txt_aceptar);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(activity, 1));


        refrescarEstadoBotones();
        tvTitulo.setText(txtTitulo);
        dialogo.show();


        adapter=new AdapterMultiOptions(activity, listaMultiChoiceFilter);
        recyclerView.setAdapter(adapter);
        adapter.setOnClik((position, selected) -> {
            listaMultiChoiceSelected.remove(listaMultiChoiceFilter.get(position));
            if (selected){
                listaMultiChoiceSelected.add(listaMultiChoiceFilter.get(position));
            }
            //----ESTADO Botones
            refrescarEstadoBotones();
            //--fin----
        });

        new ACG_EditText(activity, etBuscarOpcion).OnListen(searchString ->{
            listaMultiChoiceFilter.clear();
            if (searchString.length()==0) {
                listaMultiChoiceFilter.addAll(listaMultiChoice);
            }else{
                String texto=searchString.replace(" ",".*").toLowerCase();
                Pattern pattern = Pattern.compile(".*"+texto+".*");
                for (DataOption multichoiceCustom : listaMultiChoice) {
                    String textProducto= (multichoiceCustom.titulo+""+multichoiceCustom.opcion).replaceAll("[^a-zA-Z0-9 ]", "");
                    if (pattern.matcher((textProducto).toLowerCase()).matches()) {
                        listaMultiChoiceFilter.add(multichoiceCustom);
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
        txt_quitar.setOnClickListener(vie->{
            limpiarSelecciones();
            adapter.notifyDataSetChanged();
            vie.setVisibility(View.GONE);
            txtSeleccTodos.setVisibility(View.VISIBLE);
        });
        txtSeleccTodos.setOnClickListener(view->{
            limpiarSelecciones();
            adapter.notifyDataSetChanged();
            view.setVisibility(View.GONE);
            txt_quitar.setVisibility(View.VISIBLE);
            //-------------------------------Close and notify cambios-------------------------------
            dialogo.dismiss();
            myListener.Result(listaMultiChoiceSelected);
            myListener.dismis();

        });
        txt_aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogo.dismiss();
                myListener.Result(listaMultiChoiceSelected);
                myListener.dismis();
            }
        });

    }
    private void limpiarSelecciones(){
        if(listaMultiChoiceSelected.size()==0) return;

        for (int i = 0; i < listaMultiChoiceFilter.size(); i++) {
            listaMultiChoiceFilter.get(i).isCheck=false;
        }
        listaMultiChoiceSelected.clear();
    }
    private void refrescarEstadoBotones(){
        if(listaMultiChoiceSelected.size()==0){
            txt_quitar.setVisibility(View.GONE);
            txtSeleccTodos.setVisibility(View.VISIBLE);
        }else{
            txt_quitar.setVisibility(View.VISIBLE);
            txtSeleccTodos.setVisibility(View.GONE);
        }
    }
    public void clearData(){
        activity=null;
        txtTitulo=null;
        txt_quitar=null;
        txtSeleccTodos=null;
        adapter=null;
        recyclerView=null;
        listaMultiChoice.clear();
        listaMultiChoiceSelected.clear();
        listaMultiChoiceFilter.clear();

    }
    private String getPatronDivicion(){
        return  "#";//#
    }


    public static class DataOption {
        public String titulo;
        public String opcion;
        public int orden;
        public  Object tag;
        public boolean isCheck=false;

        public DataOption() {

        }
        public DataOption(String opcion, int orden) {
            this.opcion = opcion;
            this.orden=orden;
        }
        public DataOption(String titulo, String opcion, Object tag) {
            this.titulo = titulo;
            this.opcion = opcion;
            this.tag= tag;
        }
    }

    public interface MyListener{
        void  Result(ArrayList<DataOption> listaSelected);
        void dismis();
    }

}


class AdapterMultiOptions extends RecyclerView.Adapter<AdapterMultiOptions.ViewHolder> {
    ArrayList<MultiSelectOpcionDialog.DataOption> lista;
    Activity activity;

    MyCallback myCallback;
    public interface MyCallback{
        void onClik(int position, boolean selected);
    }

    public AdapterMultiOptions(Activity activity, ArrayList<MultiSelectOpcionDialog.DataOption> lista ) {
        this.lista = lista;
        this.activity = activity;
    }

    @Override
    public AdapterMultiOptions.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_multiselect_spinner, null, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AdapterMultiOptions.ViewHolder viewHolder, final int position) {
        final MultiSelectOpcionDialog.DataOption item=lista.get(position);
        viewHolder.tvTitulo.setText(item.titulo);
        viewHolder.tvOrden.setText(String.valueOf(position+1));
        viewHolder.tvOpcion.setText(item.opcion);
        viewHolder.checkBox.setChecked(item.isCheck);
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
        TextView tvTitulo, tvOpcion, tvOrden;
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitulo=itemView.findViewById(R.id.tvTitulo);
            tvOrden=itemView.findViewById(R.id.tvOrden);
            tvOpcion =itemView.findViewById(R.id.tvOpcion);
            checkBox=itemView.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    lista.get(position).isCheck=checkBox.isChecked();
                    if (myCallback!=null){
                        myCallback.onClik(position, lista.get(position).isCheck);
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkBox.performClick();
                }
            });
        }
    }
}

