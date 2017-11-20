package com.example.wailiantong.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wailiantong.Model.OrderMessageModel;
import com.example.wailiantong.R;

import java.util.List;

/**
 * Created by weike on 2017/6/12.
 */

public class RecyclerOrderMessageAdapter extends RecyclerView.Adapter<RecyclerOrderMessageAdapter.ViewHolder> {

    private List<OrderMessageModel> orderMessageList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View messageView;
        TextView people;
        TextView time;
        TextView content;

        public ViewHolder(View view) {
            super(view);
            messageView = view;
            people = (TextView) view.findViewById(R.id.item_message_people);
            time = (TextView) view.findViewById(R.id.item_message_time);
            content = (TextView) view.findViewById(R.id.item_message_content);

        }
    }

    public RecyclerOrderMessageAdapter(List<OrderMessageModel> list) {
        orderMessageList = list;
    }

    @Override
    public RecyclerOrderMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zsy_item_order_message_list, parent, false);
        final RecyclerOrderMessageAdapter.ViewHolder holder = new RecyclerOrderMessageAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerOrderMessageAdapter.ViewHolder holder, int position) {
        OrderMessageModel myMessageModel = orderMessageList.get(position);
        holder.people.setText(myMessageModel.getPeople());
        holder.time.setText(myMessageModel.getTime());
        holder.content.setText(myMessageModel.getContent());


    }

    @Override
    public int getItemCount() {
        return orderMessageList.size();
    }


}