package com.wanny.workease.system.workease_business.business.bus_main_mvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanny.workease.system.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 文件名： BusMainAdapter
 * 功能：
 * 作者： wanny
 * 时间： 16:39 2017/8/10
 */
public class BusMainAdapter extends RecyclerView.Adapter<BusMainAdapter.BusWorkViewHolder> {


    private ArrayList<WorkPeopleEntity> dataList;

    private Context context;

    public BusMainAdapter(ArrayList<WorkPeopleEntity> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public BusWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bus_main_item_view, parent, false);
        return new BusWorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusWorkViewHolder holder, final int position) {
        if (position != -1) {
            WorkPeopleEntity entity = dataList.get(position);
            if (entity != null) {
                if (!TextUtils.isEmpty(entity.getUserName())) {
                    holder.busMainItemName.setText(entity.getUserName());
                }else{
                    holder.busMainItemName.setText("姓名：--");
                }
                if (!TextUtils.isEmpty(entity.getMobile())) {
                    holder.busMainItemPhone.setText(entity.getMobile());
//                    if(entity.getMobile().length() >= 11){
//
//                        holder.busMainItemPhone.setText(entity.getMobile().substring(0,3) + "****" + entity.getMobile().substring(7,entity.getMobile().length()));
//                    }else{
//                        holder.busMainItemPhone.setText(entity.getMobile());
//                    }
                }else{
                    holder.busMainItemPhone.setText("电话号码：--");
                }
                if (!TextUtils.isEmpty(entity.getSenior())) {
                    holder.busMainItemSkill.setText(entity.getSenior());
                }else{
                    holder.busMainItemSkill.setText("");
                }
                if (entity.getUserState() == 0) {
                    holder.busMainItemState.setText("空闲中");
                }else{
                    holder.busMainItemState.setText("忙碌中");
                }
                if (!TextUtils.isEmpty(entity.getWorkyear())) {
                    holder.busMainItemWorktime.setText("工龄: " +entity.getWorkyear() + "年");
                }else{
                    holder.busMainItemWorktime.setText("工龄:--");
                }

                if (!TextUtils.isEmpty(entity.getJobTypeName())) {
                    holder.busMainItemWorktype.setText("工种: " +entity.getJobTypeName());
                }else{
                    holder.busMainItemWorktype.setText("工种:--");
                }
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(workListListener != null){
                        workListListener.click(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class BusWorkViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.bus_main_item_name)
        TextView busMainItemName;
        @BindView(R.id.bus_main_item_phone)
        TextView busMainItemPhone;
        @BindView(R.id.bus_main_item_state)
        TextView busMainItemState;
        @BindView(R.id.bus_main_item_worktype)
        TextView busMainItemWorktype;
        @BindView(R.id.bus_main_item_skill)
        TextView busMainItemSkill;
        @BindView(R.id.bus_main_item_worktime)
        TextView busMainItemWorktime;

        public BusWorkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private WorkListListener workListListener;

    public void setWorkListListener(WorkListListener workListListener) {
        this.workListListener = workListListener;
    }

    public interface  WorkListListener{
        void click(int position);
    }
}
