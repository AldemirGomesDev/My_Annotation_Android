package com.android.myannotations;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class LineHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView date;

    public LineHolder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.tvNameRecycleView);
        date = (TextView) itemView.findViewById(R.id.tvDate);
    }
}