package com.google.mediapipe.examples.facemesh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageListAdapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

//    private Context mContext;
//    private List<BaseMessage> mMessageList;
    private ArrayList<HashMap<String,String>> arrayList;

    public MessageListAdapter(ArrayList<HashMap<String,String>> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        HashMap<String, String> message = arrayList.get(position);

        if (message.get("user").equals("me")) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_me, parent, false);
            return new SentMessageHolder(view);
        }
        else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chat_other, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        HashMap<String, String> message = arrayList.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        private TextView Message, SendTime, SendData;

        public SentMessageHolder(@NonNull View itemView) {
            super(itemView);
            Message = itemView.findViewById(R.id.text_gchat_message_me);
            SendTime = itemView.findViewById(R.id.text_gchat_timestamp_me);
//            SendData = itemView.findViewById(R.id.text_gchat_date_me);
        }

        void bind(HashMap<String, String> message) {
            Message.setText(message.get("message"));
            SendTime.setText(message.get("time"));
        }
    }
    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private TextView Message, SendTime, SendData;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            Message = itemView.findViewById(R.id.text_gchat_message_other);
            SendTime = itemView.findViewById(R.id.text_gchat_timestamp_other);
//            SendData = itemView.findViewById(R.id.text_gchat_date_other);
        }

        void bind(HashMap<String, String> message) {
            Message.setText(message.get("message"));
            SendTime.setText(message.get("time"));
        }
    }
}
