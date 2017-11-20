package com.example.wailiantong.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wailiantong.Model.OrderModel;
import com.example.wailiantong.R;

import java.util.List;

/**
 * Created by 蔚克 on 2017/6/2.
 */

public class RecyclerAllOrderAdapter extends RecyclerView.Adapter<RecyclerAllOrderAdapter.ViewHolder> implements View.OnClickListener {
    private OnItemClickListener mOnItemClickListener = null;
    private List<OrderModel> orderList;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setmOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View orderView;
        TextView title;
        TextView price;
        TextView content;
        TextView grade;


        public ViewHolder(View view) {
            super(view);
            orderView = view;
            title = (TextView) view.findViewById(R.id.item_order_title);
            price = (TextView) view.findViewById(R.id.item_order_price);
            content= (TextView) view.findViewById(R.id.item_order_content);
            grade= (TextView) view.findViewById(R.id.item_order_grade);

        }
    }

    public RecyclerAllOrderAdapter(List<OrderModel> list) {
        orderList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zsy_item_order_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderModel orderModel = orderList.get(position);
        holder.grade.setText(orderModel.getGrade());
        holder.content.setText(orderModel.getContent());
        holder.title.setText(orderModel.getTitle());
        holder.price.setText(orderModel.getPrice());
        holder.orderView.setTag(position);



    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


}
