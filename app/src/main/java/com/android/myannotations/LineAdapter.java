package com.android.myannotations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class LineAdapter extends RecyclerView.Adapter<LineHolder> {

    private final List<UserModel> mUsers;

    public LineAdapter(ArrayList users) {
        mUsers = users;
    }


    public void updateList(UserModel user) {
        insertItem(user);
    }

    private void insertItem(UserModel user) {
        mUsers.add(user);
        notifyItemInserted(getItemCount());
    }

    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LineHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_line_view, parent, false));
    }

    @Override
    public void onBindViewHolder(LineHolder holder, int position) {
        holder.title.setText(mUsers.get(position).getName() );
        holder.date.setText(mUsers.get(position).getDate());

//        holder.moreButton.setOnClickListener(view -> updateItem(position));
//        holder.deleteButton.setOnClickListener(view -> removerItem(position));
    }

    @Override
    public int getItemCount() {
        return mUsers != null ? mUsers.size() : 0;
    }

}
