package com.selvaraj.buyerapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.selvaraj.buyerapp.R;
import com.selvaraj.buyerapp.model.UserMessage;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVE = 2;
    private List<UserMessage> messageList;

    public MessageAdapter(List<UserMessage> messageList) {
        this.messageList = messageList;
    }

    @Override
    public int getItemViewType(int position) {
        UserMessage message = messageList.get(position);
        if (message.isUser()) {
            return VIEW_TYPE_RECEIVE;
        }
        return VIEW_TYPE_SENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        switch (i) {
            case VIEW_TYPE_SENT:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_sent, null);
                return new ViewHolderSent(view);
            case VIEW_TYPE_RECEIVE:
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_message_receive, null);
                return new ViewHolderReceive(view);

        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        UserMessage message = messageList.get(i);
        switch (viewHolder.getItemViewType()) {
            case VIEW_TYPE_SENT:
                ViewHolderSent viewHolderSent = (ViewHolderSent) viewHolder;
                viewHolderSent.tvSent.setText(message.getMessage());
                break;
            case VIEW_TYPE_RECEIVE:
                ViewHolderReceive viewHolderReceive = (ViewHolderReceive) viewHolder;
                viewHolderReceive.tvReceive.setText(message.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolderSent extends RecyclerView.ViewHolder {
        private TextView tvSent;

        public ViewHolderSent(@NonNull View itemView) {
            super(itemView);
            tvSent = itemView.findViewById(R.id.text_message_sent);
        }
    }

    public class ViewHolderReceive extends RecyclerView.ViewHolder {
        private TextView tvReceive;

        public ViewHolderReceive(@NonNull View itemView) {
            super(itemView);
            tvReceive = itemView.findViewById(R.id.text_message_received);
        }
    }

    public void updateDetails(List<UserMessage> messageList) {
        this.messageList = messageList;
        notifyDataSetChanged();
    }

}
