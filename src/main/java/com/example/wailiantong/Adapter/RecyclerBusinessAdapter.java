package com.example.wailiantong.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wailiantong.Model.BusinessModel;
import com.example.wailiantong.R;

import java.util.List;

/**
 * Created by 蔚克 on 2017/6/2.
 */

public class RecyclerBusinessAdapter extends RecyclerView.Adapter<RecyclerBusinessAdapter.ViewHolder> implements View.OnClickListener {
    private OnItemClickListener mOnItemClickListener = null;
    private List<BusinessModel> BusinessList;

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
        View businessView;
        TextView party;
        TextView ID;

        public ViewHolder(View view) {
            super(view);
            businessView = view;
            party = (TextView) view.findViewById(R.id.business_party);
            ID = (TextView) view.findViewById(R.id.business_ID);

        }
    }

    public RecyclerBusinessAdapter(List<BusinessModel> list) {
        BusinessList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_business_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BusinessModel businessModel = BusinessList.get(position);
        holder.ID.setText(businessModel.getCertificate_num());
        holder.party.setText(businessModel.getParty_name());
        holder.businessView.setTag(position);


    }

    @Override
    public int getItemCount() {
        return BusinessList.size();
    }


}
