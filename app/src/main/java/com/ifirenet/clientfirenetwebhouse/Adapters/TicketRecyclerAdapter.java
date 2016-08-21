package com.ifirenet.clientfirenetwebhouse.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifirenet.clientfirenetwebhouse.R;
import com.ifirenet.clientfirenetwebhouse.Utils.Client.ClientTicket;
import com.ifirenet.clientfirenetwebhouse.Utils.Support.SupportTicket;
import com.ifirenet.clientfirenetwebhouse.Viewholders.ViewHolderClientTicket;
import com.ifirenet.clientfirenetwebhouse.Viewholders.ViewHolderSupportTicket;

import java.util.ArrayList;

/**
 * Created by Pouriya.kh on 8/8/2016.
 */
public class TicketRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CLIENT_TICKET= 1;
    private static final int SUPPORT_TICKET= 2;
    Context context;
    ArrayList<Object> objectList;
    private OnTicketRecyclerAdapterListener mListener;

    public TicketRecyclerAdapter(Context context, ArrayList<Object> objectList, OnTicketRecyclerAdapterListener mListener) {
        this.context = context;
        this.objectList = objectList;
        this.mListener = mListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case CLIENT_TICKET:
                View vc = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_view_holder_client_ticket, parent, false);
                viewHolder = new ViewHolderClientTicket(vc);
                break;
            case SUPPORT_TICKET:
                View vs = inflater.inflate(R.layout.layout_view_holder_support_ticket,parent, false);
                viewHolder = new ViewHolderSupportTicket(vs);
                break;
           /* default:
                View v = inflater.inflate(android.R.layout.simple_list_item_1,parent, false);
                viewHolder = new RecyclerViewSimpleTextViewHolder(v);
                break;*/
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case CLIENT_TICKET:
                ViewHolderClientTicket vhct= (ViewHolderClientTicket) viewHolder;
                configureViewHolderClientTicket(vhct, position);
                break;
            case SUPPORT_TICKET:
                ViewHolderSupportTicket vhst = (ViewHolderSupportTicket) viewHolder;
                configureViewHolderSupportTicket(vhst, position);
                break;
           /* default:
                RecyclerViewSimpleTextViewHolder vh = (RecyclerViewSimpleTextViewHolder) viewHolder;
                configureDefaultViewHolder(vh, position);
                break; */
        }
    }

    private void configureViewHolderSupportTicket(ViewHolderSupportTicket vhst, int position) {
        final SupportTicket supportTicket= (SupportTicket) getItem(position);
        if (supportTicket != null) {
            vhst.getTitle().setText(supportTicket.title);
            vhst.getDate().setText(supportTicket.createDate);
            vhst.getTrackingCode().setText(String.valueOf(supportTicket.trackingCode));
            vhst.getStatus().setText(String.valueOf(supportTicket.status));
            vhst.getStatus().setTextColor(Color.parseColor(supportTicket.statusColor));
            vhst.getPriority().setText(supportTicket.priority);
            vhst.getPriority().setTextColor(Color.parseColor(supportTicket.priorityColor));
            vhst.getResult().setText(supportTicket.result);
            vhst.getResult().setTextColor(Color.parseColor(supportTicket.resultColor));

            vhst.getItem().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(supportTicket);
                }
            });
        }
    }

    private void configureViewHolderClientTicket(ViewHolderClientTicket vhct, int position) {
        final ClientTicket clientTicket= (ClientTicket) getItem(position);
        if (clientTicket != null) {
            vhct.getTitle().setText(clientTicket.title);
            vhct.getDate().setText(clientTicket.createDate);
            vhct.getTrackingCode().setText(String.valueOf(clientTicket.trackingCode));
            vhct.getStatus().setText(String.valueOf(clientTicket.status));
            vhct.getStatus().setTextColor(Color.parseColor(clientTicket.statusColor));

            vhct.getItem().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemClick(clientTicket);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return objectList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(getItem(position) instanceof ClientTicket) {
            return CLIENT_TICKET;
        } else if (getItem(position) instanceof SupportTicket) {
            return SUPPORT_TICKET;
        }
        return -1;
    }

    private Object getItem(int position) {
        return objectList.get(position);
    }


    public interface OnTicketRecyclerAdapterListener {
        // TODO: Update argument type and name
        void onItemClick(Object object);
    }


}
