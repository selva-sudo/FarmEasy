package com.selvaraj.buyerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.selvaraj.buyerapp.Interface.RecyclerViewItemClickListener;
import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.model.ProfileItems;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private List<ProfileItems> profileItemsList;
    private RecyclerViewItemClickListener listener;

    public ProfileAdapter(List<ProfileItems> profileItemsList, RecyclerViewItemClickListener listener) {
        this.profileItemsList = profileItemsList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_profile_tab, null);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ProfileItems item = profileItemsList.get(i);
        viewHolder.tvItemName.setText(item.getItemName());
        viewHolder.ivIcon.setImageResource(item.getResPath());
    }

    @Override
    public int getItemCount() {
        return profileItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIcon;
        private TextView tvItemName;

        public ViewHolder(@NonNull final View itemView, final RecyclerViewItemClickListener listener) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_profile_item);
            tvItemName = itemView.findViewById(R.id.tv_profile_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public void updateDetails(List<ProfileItems> profileItemsList) {
        this.profileItemsList = profileItemsList;
        notifyDataSetChanged();
    }
}
