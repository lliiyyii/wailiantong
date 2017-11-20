package com.example.wailiantong.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wailiantong.Model.MessageModel;
import com.example.wailiantong.R;

import java.util.List;

/**
 * Created by weike on 2017/6/12.
 */

public class RecyclerMessageAdapter extends RecyclerView.Adapter<RecyclerMessageAdapter.ViewHolder> {

    private List<MessageModel> MessageList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View messageView;
        TextView title;
        TextView time;
        TextView content;

        public ViewHolder(View view) {
            super(view);
            messageView = view;
            title = (TextView) view.findViewById(R.id.message_title);
            time = (TextView) view.findViewById(R.id.message_time);
            content = (TextView) view.findViewById(R.id.message_content);

        }
    }

    public RecyclerMessageAdapter(List<MessageModel> list) {
        MessageList = list;
    }

    @Override
    public RecyclerMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message_list, parent, false);
        final RecyclerMessageAdapter.ViewHolder holder = new RecyclerMessageAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerMessageAdapter.ViewHolder holder, int position) {
        MessageModel myMessageModel = MessageList.get(position);
        holder.title.setText(myMessageModel.getTitle());
        holder.time.setText(myMessageModel.getTimestamp());
        holder.content.setText(myMessageModel.getContent());


    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }


}