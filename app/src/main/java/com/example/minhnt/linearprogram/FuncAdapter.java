package com.example.minhnt.linearprogram;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by minh.nt on 8/22/2017.
 */

public class FuncAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ItemObject> list;
    private String[] func = new String[]{"max", "min"};
    public static final int FUNC_OBJECTS = 100;
    public static final int FUNC_VAR = 200;
    public static final int FUNC_PLUS = 300;

    public FuncAdapter(List<ItemObject> list) {
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FUNC_OBJECTS;
        } else if ((position + 1) % 2 == 0) {
            return FUNC_VAR;
        } else {
            return FUNC_PLUS;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FUNC_OBJECTS) {
            return new FuncObjectsVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relationship, parent, false));
        } else if (viewType == FUNC_VAR) {
            return new VarVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_input_number, parent, false));
        } else if (viewType == FUNC_PLUS) {
            return new PlusVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plus, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FuncObjectsVH) {
            ((FuncObjectsVH) holder).setData((int) list.get(position).object);
        } else if (holder instanceof VarVH) {
            ((VarVH) holder).setData(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class FuncObjectsVH extends RecyclerView.ViewHolder {
        private Spinner spnObjects;
        private ArrayAdapter adapter;

        public FuncObjectsVH(final View itemView) {
            super(itemView);
            spnObjects = itemView.findViewById(R.id.spnRelationship);
            adapter = new ArrayAdapter(itemView.getContext(), android.R.layout.simple_spinner_item, func);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            spnObjects.setAdapter(adapter);
            spnObjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    list.set(getLayoutPosition(), new ItemObject(FUNC_OBJECTS, i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    list.set(getLayoutPosition(), new ItemObject(FUNC_OBJECTS, 0));
                }
            });

            spnObjects.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(itemView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return false;
                }
            });
        }

        public void setData(int pos) {
            spnObjects.setSelection(pos);
        }
    }

    class PlusVH extends RecyclerView.ViewHolder {

        public PlusVH(View itemView) {
            super(itemView);
        }
    }

    class VarVH extends RecyclerView.ViewHolder {
        private EditText etInput;
        private TextView tvInput;

        public VarVH(View itemView) {
            super(itemView);
            etInput = itemView.findViewById(R.id.et_input_number);
            tvInput = itemView.findViewById(R.id.tv_variable);
            etInput.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        ItemObject itemObject = list.get(getLayoutPosition());
                        double a = Double.valueOf(etInput.getText().toString());
                        list.set(getLayoutPosition(), new ItemObject(FUNC_VAR, a, itemObject.position));
                    } catch (Exception e) {

                    }
                }
            });
        }

        public void setData(ItemObject data) {
            String a = String.valueOf(data.object);
            if (a.endsWith(".0")) {
                a = a.substring(0, a.indexOf("."));
            }
            etInput.setText(a);
            tvInput.setText("x" + data.position);
        }
    }
}
