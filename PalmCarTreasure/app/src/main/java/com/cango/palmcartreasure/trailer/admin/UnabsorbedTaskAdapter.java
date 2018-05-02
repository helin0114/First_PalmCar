package com.cango.palmcartreasure.trailer.admin;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.baseAdapter.BaseAdapter;
import com.cango.palmcartreasure.baseAdapter.BaseHolder;
import com.cango.palmcartreasure.model.TaskManageList;

import java.util.List;

/**
 * 查询所有未分配的任务
 * Created by cango on 2017/4/10.
 */

public class UnabsorbedTaskAdapter extends BaseAdapter<TaskManageList.DataBean.TaskListBean> {
    private MtOnCheckedChangeThenTypeListener mtOnCheckedChangeThenTypeListener;
    private int noReadColor,ReaderColor,noReaderTvColor,ReaderTvColor;
    public UnabsorbedTaskAdapter(Context context, List<TaskManageList.DataBean.TaskListBean> datas, boolean isOpenLoadMore, MtOnCheckedChangeThenTypeListener mtOnCheckedChangeThenTypeListener) {
        super(context, datas, isOpenLoadMore);
        this.mtOnCheckedChangeThenTypeListener = mtOnCheckedChangeThenTypeListener;
        noReadColor = mContext.getResources().getColor(R.color.mtf54333);
        ReaderColor = mContext.getResources().getColor(R.color.e0e0e0);
        noReaderTvColor = mContext.getResources().getColor(R.color.mtfef6f5);
        ReaderTvColor = mContext.getResources().getColor(R.color.mt9c9c9c);
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.unabsorbed_task_item;
    }

    @Override
    protected void convert(BaseHolder holder, final TaskManageList.DataBean.TaskListBean data) {
        CheckBox cbSelect = holder.getView(R.id.checkbox_group_item);
        cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.setChecked(isChecked);
                if (isChecked) {
                    //如果所有的item都被选中，或者选中状态只有几个或一个
                    mtOnCheckedChangeThenTypeListener.mtOnCheckedChangedThenType();
                }
            }
        });
        cbSelect.setChecked(data.isChecked());

        TextView tvApplyCD = holder.getView(R.id.tv_applyCD);
        TextView tvDistance = holder.getView(R.id.tv_distance);
        TextView tvShortName = holder.getView(R.id.tv_task_item_type);
        TextView tvCustomerName = holder.getView(R.id.tv_task_item_name);
        TextView tvCarPlateNO = holder.getView(R.id.tv_task_item_plate);
        TextView tvFeerate = holder.getView(R.id.tv_feerate);
        TextView tvPrompt = holder.getView(R.id.tv_prompt);
        TextView tvRedueDays = holder.getView(R.id.tv_redueDays);
        TextView tvAgencyAmount = holder.getView(R.id.tv_agencyAmount);

        TextView tvFlowStauts = holder.getView(R.id.tv_flowStauts);
        tvFlowStauts.setText(data.getFlowStauts());

        TextView tvAddress = holder.getView(R.id.tv_address);
        tvAddress.setText(data.getAddress());

        TextView tvIsRead = holder.getView(R.id.tv_isread);
        ImageView ivIsRead = holder.getView(R.id.iv_isread);
        String isRead;
        if ("T".equals(data.getIsRead())){
            isRead="已 读";
            ivIsRead.setBackgroundColor(ReaderColor);
            tvIsRead.setTextColor(ReaderTvColor);
        }else {
            isRead="未 读";
            ivIsRead.setBackgroundColor(noReadColor);
            tvIsRead.setTextColor(noReaderTvColor);
        }
        tvIsRead.setText(isRead);



        tvApplyCD.setText(data.getApplyCD());
        tvDistance.setText(data.getDistance());
        tvShortName.setText(data.getShortName());
        tvCustomerName.setText(data.getCustomerName());
        tvCarPlateNO.setText(data.getCarPlateNO());

        String feerate = data.getFeerate();
        tvFeerate.setText(data.getFeerate() + "");
        if ("0".equals(feerate) || TextUtils.isEmpty(feerate)) {
            tvFeerate.setVisibility(View.INVISIBLE);
            tvPrompt.setVisibility(View.INVISIBLE);
        } else {
            tvFeerate.setVisibility(View.VISIBLE);
            tvPrompt.setVisibility(View.VISIBLE);
        }

        if (data.getRedueDays() == 0) {
            tvRedueDays.setText("");
        } else {
            tvRedueDays.setText(data.getRedueDays() + "天");
        }
        if ("0".equals(data.getRedueAmount()) || TextUtils.isEmpty(data.getRedueAmount())) {
            tvAgencyAmount.setText("");
        } else {
            tvAgencyAmount.setText("逾期金额:"+data.getRedueAmount() + "元");
        }

    }

    public interface MtOnCheckedChangeThenTypeListener {
        void mtOnCheckedChangedThenType();
    }
}
