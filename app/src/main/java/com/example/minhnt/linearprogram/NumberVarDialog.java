package com.example.minhnt.linearprogram;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by minh.nt on 8/22/2017.
 */

public class NumberVarDialog extends DialogFragment {
    private EditText etNumVar, etNumFunc;
    private OnItemSelectedListener listener;
    private Button btnNum;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(STYLE_NO_TITLE, 0);
        setCancelable(false);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_input, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etNumVar = view.findViewById(R.id.etNumberVar);
        etNumFunc = view.findViewById(R.id.etNumberFunc);
        btnNum = view.findViewById(R.id.btnEnterNum);
        btnNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    listener.onItemSelectedListener(Integer.valueOf(etNumVar.getText().toString()), TextUtils.isEmpty(etNumFunc.getText().toString()) ? 1 : Integer.valueOf(etNumFunc.getText().toString()));
                    dismiss();
                }
            }
        });
    }

    private boolean validate() {
        if (TextUtils.isEmpty(etNumVar.getText().toString())) {
            etNumVar.setError("Vui lòng nhập số biến");
            return false;
        }

        if (!TextUtils.isEmpty(etNumFunc.getText().toString())) {
            try {
                int b = Integer.valueOf(etNumFunc.getText().toString());
                if (b <= 0) {
                    etNumFunc.setError("Lỗi: ít nhất một phương trình");
                    return false;
                }
            } catch (Exception e) {
                etNumFunc.setError("Lỗi: số không xác định");
                return false;
            }
        }

        try {
            int a = Integer.valueOf(etNumVar.getText().toString());
            if (a > 1) {
                return true;
            } else {
                etNumVar.setError("Lỗi: Ít nhất 2 biến");
                return false;
            }
        } catch (Exception e) {
            etNumVar.setError("Lỗi: số không xác định");
            return false;
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelectedListener(int numVar, int numFunc);
    }

    public void setListener(OnItemSelectedListener listener) {
        this.listener = listener;
    }
}
