package com.ifirenet.clientfirenetwebhouse.Viewholders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ifirenet.clientfirenetwebhouse.R;

/**
 * Created by Pouriya.kh on 8/20/2016.
 */
public class ViewHolderAttachedInfo extends RecyclerView.ViewHolder {
    TextView Title, Value;
    public ViewHolderAttachedInfo(View v) {
        super(v);
        Title = (TextView) v.findViewById(R.id.txt_attached_info_title);
        Value = (TextView) v.findViewById(R.id.txt_attached_info_value);
    }
}
