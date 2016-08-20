package com.ifirenet.clientfirenetwebhouse.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Viewholders.ViewHolderAttachedInfo;

/**
 * Created by Pouriya.kh on 8/20/2016.
 */
public class AttachedInfoRecycleAdapter extends RecyclerView.Adapter<ViewHolderAttachedInfo> {
    @Override
    public ViewHolderAttachedInfo onCreateViewHolder(ViewGroup parent, int viewType) {
        View vc = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_holder_attached_info, parent, false);
        ViewHolderAttachedInfo viewHolder = new ViewHolderAttachedInfo(vc);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderAttachedInfo holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
