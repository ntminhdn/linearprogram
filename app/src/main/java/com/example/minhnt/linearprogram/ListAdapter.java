package com.example.minhnt.linearprogram;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by minh.nt on 8/22/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<List<ItemObject>> list;
    private SparseIntArray listPositionFunc = new SparseIntArray();
    private SparseIntArray listPositionList = new SparseIntArray();
    public static final int FUNC = 10;
    public static final int CONSTRAINT = 11;

    public ListAdapter(List<List<ItemObject>> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FUNC) {
            return new FuncVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        } else {
            return new ListVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return FUNC;
        } else {
            return CONSTRAINT;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FuncVH) {
            FuncVH cellViewHolder = (FuncVH) holder;

            FuncAdapter adapter = new FuncAdapter(list.get(position));
            cellViewHolder.rvItem.setAdapter(adapter);

            int lastSeenFirstPosition = listPositionFunc.get(position, 0);
            if (lastSeenFirstPosition >= 0) {
                cellViewHolder.rvItem.scrollToPosition(lastSeenFirstPosition);
            }
        } else if (holder instanceof ListVH) {
            ListVH cellViewHolder = (ListVH) holder;

            ItemListAdapter adapter = new ItemListAdapter(list.get(position));
            cellViewHolder.rvItem.setAdapter(adapter);

            int lastSeenFirstPosition = listPositionList.get(position, 0);
            if (lastSeenFirstPosition >= 0) {
                cellViewHolder.rvItem.scrollToPosition(lastSeenFirstPosition);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ListVH extends RecyclerView.ViewHolder {
        public RecyclerView rvItem;

        public ListVH(View itemView) {
            super(itemView);
            rvItem = itemView.findViewById(R.id.rv_item);
            rvItem.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    class FuncVH extends RecyclerView.ViewHolder {
        public RecyclerView rvItem;

        public FuncVH(View itemView) {
            super(itemView);
            rvItem = itemView.findViewById(R.id.rv_item);
            rvItem.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof ListVH) {
            final int position = viewHolder.getAdapterPosition();
            ListVH cellViewHolder = (ListVH) viewHolder;
            LinearLayoutManager layoutManager = ((LinearLayoutManager) cellViewHolder.rvItem.getLayoutManager());
            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
            listPositionList.put(position, firstVisiblePosition);
        } else if (viewHolder instanceof FuncVH) {
            final int position = viewHolder.getAdapterPosition();
            FuncVH cellViewHolder = (FuncVH) viewHolder;
            LinearLayoutManager layoutManager = ((LinearLayoutManager) cellViewHolder.rvItem.getLayoutManager());
            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
            listPositionFunc.put(position, firstVisiblePosition);
        }

        super.onViewRecycled(viewHolder);
    }
}
