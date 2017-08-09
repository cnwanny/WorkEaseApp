package com.wanny.workease.system.workease_business.customer.main_mvp;

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
 * 文件名： WordListAdapter
 * 功能：
 * 作者： wanny
 * 时间： 14:00 2017/7/19
 */
public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WorkViewHolder> {

    private ArrayList<WorkInfoEntity> dataList;
    private Context context;
    public WordListAdapter(ArrayList<WorkInfoEntity> dataList, Context context) {
        this.dataList = dataList;
        this.context = context;
    }

    @Override
    public WorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.work_item_view, parent, false);
        return new WorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WorkViewHolder holder, final int position) {
        if (position != -1) {
            WorkInfoEntity entity = dataList.get(position);
            if (!TextUtils.isEmpty(entity.getPrice())) {
                holder.workItemPrice.setText("价格：" + entity.getPrice());
            } else {
                holder.workItemPrice.setText("价格：");
            }
            if (entity.getJobType() != null) {
                holder.workItemType.setText("工种：" + entity.getJobType().getName());
            }else{
                holder.workItemType.setText("工种：");
            }
            if (!TextUtils.isEmpty(entity.getName())) {
                holder.workItemProjectname.setText("楼盘名称:"+entity.getName());
            }else{
                holder.workItemProjectname.setText("楼盘名称：");
            }
            if (!TextUtils.isEmpty(entity.getRecruitNum())) {
                holder.workItemNeednumber.setText("人数：" + entity.getRecruitNum());
            }else{
                holder.workItemNeednumber.setText("人数：");
            }
            if(entity.getCity() != null){
                if(!TextUtils.isEmpty(entity.getCity().getName())){
                    holder.workItemArea.setText("区域：" + entity.getCity().getName());
                }else{
                    holder.workItemArea.setText("区域：");
                }
            }else{
                holder.workItemArea.setText("区域：");
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workClickListener != null){
                    workClickListener.click(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class WorkViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.work_item_type)
        TextView workItemType;
        @BindView(R.id.work_item_price)
        TextView workItemPrice;
        @BindView(R.id.work_item_area)
        TextView workItemArea;
        @BindView(R.id.work_item_neednumber)
        TextView workItemNeednumber;
        @BindView(R.id.work_item_projectname)
        TextView workItemProjectname;

        public WorkViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private WorkClickListener workClickListener;


    public void setWorkClickListener(WorkClickListener workClickListener) {
        this.workClickListener = workClickListener;
    }

    public interface  WorkClickListener{
        void click(int position);
    }
}
