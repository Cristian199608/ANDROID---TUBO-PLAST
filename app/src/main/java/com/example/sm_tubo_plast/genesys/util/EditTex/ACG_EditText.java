package com.example.sm_tubo_plast.genesys.util.EditTex;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class ACG_EditText {
    Activity activity;
    EditText editText;
    MyListener myListener;

    public ACG_EditText(Activity activity, EditText editText) {
        this.activity = activity;
        this.editText = editText;
    }
    public void OnListen(MyListener _myListener){
        this.myListener=_myListener;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                myListener.OnChanged(editable.toString());
            }
        });
    }

    public interface MyListener{
        void OnChanged(String texto);
    }
}
