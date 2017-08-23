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

public class ItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ItemObject> list;
    private String[] relations = new String[]{"<=", "=", ">="};

    public ItemListAdapter(List<ItemObject> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ItemObject.INPUT:
                return new InputNumberVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_input_number, parent, false));
            case ItemObject.PLUS:
                return new PlusVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_plus, parent, false));
            case ItemObject.RELATIONSHIP:
                return new RelationshipVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_relationship, parent, false));
            case ItemObject.CONSTANT:
                return new ConstVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_const, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof InputNumberVH) {
            ((InputNumberVH) holder).setData(list.get(position));
        } else if (holder instanceof RelationshipVH) {
            ((RelationshipVH) holder).setData((int) list.get(position).object);
        } else if (holder instanceof ConstVH) {
            ((ConstVH) holder).setData(list.get(position).object);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == list.size() - 1) {
            return ItemObject.CONSTANT;
        } else if (position == list.size() - 2) {
            return ItemObject.RELATIONSHIP;
        } else if (position % 2 == 0) {
            return ItemObject.INPUT;
        } else {
            return ItemObject.PLUS;
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class InputNumberVH extends RecyclerView.ViewHolder {
        private EditText etInput;
        private TextView tvInput;

        public InputNumberVH(final View itemView) {
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
                        list.set(getLayoutPosition(), new ItemObject(ItemObject.INPUT, a, itemObject.position));
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

    class PlusVH extends RecyclerView.ViewHolder {

        public PlusVH(View itemView) {
            super(itemView);
        }
    }

    class RelationshipVH extends RecyclerView.ViewHolder {
        private Spinner spnRela;
        private ArrayAdapter adapter;

        public RelationshipVH(final View itemView) {
            super(itemView);
            spnRela = itemView.findViewById(R.id.spnRelationship);
            adapter = new ArrayAdapter(itemView.getContext(), android.R.layout.simple_spinner_item, relations);
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            spnRela.setAdapter(adapter);
            spnRela.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    list.set(getLayoutPosition(), new ItemObject(ItemObject.RELATIONSHIP, i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    list.set(getLayoutPosition(), new ItemObject(ItemObject.RELATIONSHIP, 0));
                }
            });

            spnRela.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    InputMethodManager imm = (InputMethodManager) itemView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(itemView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    return false;
                }
            });
        }

        public void setData(int pos) {
            spnRela.setSelection(pos);
        }
    }

    class ConstVH extends RecyclerView.ViewHolder {
        private EditText etConst;

        public ConstVH(View itemView) {
            super(itemView);
            etConst = itemView.findViewById(R.id.et_const);
            etConst.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    try {
                        double a = Double.valueOf(etConst.getText().toString());
                        list.set(getLayoutPosition(), new ItemObject(ItemObject.CONSTANT, a));
                    } catch (Exception e) {

                    }
                }
            });
        }

        public void setData(Object data) {
            String a = String.valueOf(data);
            if (a.endsWith(".0")) {
                a = a.substring(0, a.indexOf("."));
            }
            etConst.setText(a);
        }
    }
}
