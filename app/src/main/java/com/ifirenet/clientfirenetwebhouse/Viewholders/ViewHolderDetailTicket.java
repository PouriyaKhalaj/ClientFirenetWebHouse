package com.ifirenet.clientfirenetwebhouse.Viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ifirenet.clientfirenetwebhouse.R;

/**
 * Created by Pouriya.kh on 8/20/2016.
 */
public class ViewHolderDetailTicket extends RecyclerView.ViewHolder {

    public TextView Title, Text, FullName, CreationDate;

    public ViewHolderDetailTicket(View v) {
        super(v);
        Title = (TextView) v.findViewById(R.id.txt_detail_ticket_title);
        Text = (TextView) v.findViewById(R.id.txt_detail_ticket_text);
        FullName = (TextView) v.findViewById(R.id.txt_detail_ticket_full_name);
        CreationDate = (TextView) v.findViewById(R.id.txt_detail_ticket_creation_date);
    }
}
