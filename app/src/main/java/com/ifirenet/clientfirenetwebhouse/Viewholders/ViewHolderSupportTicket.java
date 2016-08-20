package com.ifirenet.clientfirenetwebhouse.Viewholders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ifirenet.clientfirenetwebhouse.R;

/**
 * Created by Pouriya.kh on 8/14/2016.
 */
public class ViewHolderSupportTicket extends RecyclerView.ViewHolder {
    private TextView Title, Status, Date, TrackingCode, Priority, Result;
    private CardView Item;
    public ViewHolderSupportTicket(View v) {
        super(v);
        Title = (TextView) v.findViewById(R.id.txt_support_ticket_title);
        Status = (TextView) v.findViewById(R.id.txt_support_ticket_status);
        Date = (TextView) v.findViewById(R.id.txt_support_ticket_date);
        TrackingCode = (TextView) v.findViewById(R.id.txt_support_ticket_code);
        Priority = (TextView) v.findViewById(R.id.txt_support_tiket_priority);
        Result = (TextView) v.findViewById(R.id.txt_support_tiket_result);
        Item = (CardView) v.findViewById(R.id.card_view_support_ticket_item);
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

    public TextView getPriority(){ return this.Priority;}

    public TextView getResult(){ return this.Result;}

    public CardView getItem() {return this.Item;}
}
