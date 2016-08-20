package com.ifirenet.clientfirenetwebhouse.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Utils.DetailTicket;
import com.ifirenet.clientfirenetwebhouse.Viewholders.ViewHolderDetailTicket;

import java.util.ArrayList;

/**
 * Created by Pouriya.kh on 8/20/2016.
 */
public class DetailTicketRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int DETAIL_TICKET= 1;

    Context context;
    ArrayList<DetailTicket> detailTicketList;
    public DetailTicketRecycleAdapter(Context context, ArrayList<DetailTicket> detailTicketList){
        this.context = context;
        this.detailTicketList = detailTicketList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType) {
            case DETAIL_TICKET:
                View vdt = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_holder_detail_ticket, parent, false);
                viewHolder = new ViewHolderDetailTicket(vdt);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case DETAIL_TICKET:
                ViewHolderDetailTicket vhdt= (ViewHolderDetailTicket) viewHolder;
                configureViewHolderDetailTicket(vhdt, position);
                break;
        }
    }
    private void configureViewHolderDetailTicket(ViewHolderDetailTicket vhdt, int position){
        DetailTicket detailTicket = detailTicketList.get(position);
        if (detailTicket != null){
            vhdt.Title.setText(detailTicket.title);
            vhdt.Text.setText(detailTicket.text);
            vhdt.FullName.setText(detailTicket.firstName+ " " + detailTicket.lastName);
            vhdt.CreationDate.setText(detailTicket.documentCreatedWhen);
            //vhdt.CreationTime.setText(detailTicket);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return DETAIL_TICKET;
    }


    @Override
    public int getItemCount() {
        return detailTicketList.size();
    }
}
