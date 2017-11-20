package com.example.wailiantong.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wailiantong.Model.RecverModel;
import com.example.wailiantong.R;

import java.util.List;

/**
 * Created by 蔚克 on 2017/6/2.
 */

public class RecyclerRecverAdapter extends RecyclerView.Adapter<RecyclerRecverAdapter.ViewHolder> implements View.OnClickListener {
    private OnItemClickListener mOnItemClickListener = null;
    private List<RecverModel> recverList;

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener!=null){
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }
public void setmOnItemClickListener(OnItemClickListener listener){
    this.mOnItemClickListener=listener;
}
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View recverView;
        TextView name;
        TextView grade;

        public ViewHolder(View view) {
            super(view);
            recverView = view;
            name = (TextView) view.findViewById(R.id.item_recver_name);
            grade = (TextView) view.findViewById(R.id.item_recver_grade);

        }
    }

    public RecyclerRecverAdapter(List<RecverModel> list) {
        recverList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.zsy_item_recver, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecverModel recverModel = recverList.get(position);
        holder.name.setText(recverModel.getName());
        holder.grade.setText(recverModel.getCredit()+"");
        holder.recverView.setTag(position);


    }

    @Override
    public int getItemCount() {
        return recverList.size();
    }


}
