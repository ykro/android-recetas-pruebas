package edu.galileo.android.facebookrecipes.support;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

/**
 *
 */
@Implements(RecyclerView.Adapter.class)
public class ShadowRecyclerViewAdapter {
    @RealObject
    RecyclerView.Adapter realAdapter;
    private RecyclerView recyclerView;
    private final SparseArray<RecyclerView.ViewHolder> holders = new SparseArray<>();

    @Implementation
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Implementation
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        realAdapter.onBindViewHolder(holder, position);
        holders.put(position, holder);
    }

    public int getItemCount() {
        return realAdapter.getItemCount();
    }

    public boolean performItemClick(int position) {
        View holderView = holders.get(position).itemView;
        return holderView.performClick();
    }

    public boolean performClickOverViewInViewHolder(int position, int id) {
        boolean valueToReturn = false;
        View holderView = holders.get(position).itemView;
        View viewToClick = holderView.findViewById(id);
        if (viewToClick != null) {
            valueToReturn = viewToClick.performClick();
        }
        return valueToReturn;
    }

    public void itemVisible(int position) {
        RecyclerView.ViewHolder holder =
                    realAdapter.createViewHolder(recyclerView,
                                                 realAdapter.getItemViewType(position));
        onBindViewHolder(holder, position);
    }

    public RecyclerView.ViewHolder getViewHolderForPosition(int position){
        return holders.get(position);
    }
}
