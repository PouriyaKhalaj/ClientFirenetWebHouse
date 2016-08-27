package com.ifirenet.clientfirenetwebhouse.Viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ifirenet.clientfirenetwebhouse.R;

/**
 * Created by Pouriya.kh on 8/8/2016.
 */
public class ViewHolderClientTicket extends RecyclerView.ViewHolder {
    private TextView Title, Status, Date, TrackingCode;
    private CardView Item;
    private FrameLayout BorderStatus;
    public ViewHolderClientTicket(View v) {
        super(v);
        Title = (TextView) v.findViewById(R.id.txt_client_ticket_title);
        Status = (TextView) v.findViewById(R.id.txt_client_ticket_status);
        Date = (TextView) v.findViewById(R.id.txt_client_ticket_date);
        TrackingCode = (TextView) v.findViewById(R.id.txt_client_ticket_tracking_code);
        Item = (CardView) v.findViewById(R.id.card_view_client_ticket_item);
        BorderStatus = (FrameLayout) v.findViewById(R.id.fl_client_ticket_status_border);
    }
    public TextView getTitle(){
        return this.Title;
    }

    public TextView getStatus(){
        return this.Status;
    }

    public TextView getDate(){
        return this.Date;
    }

    public TextView getTrackingCode(){
        return this.TrackingCode;
    }

    public CardView getItem() {return this.Item;}

    public FrameLayout getBorderStatus() {return this.BorderStatus;}
}
