package com.cango.palmcartreasure.trailer.admin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.util.CommUtil;

/**
 * 管理员任务页面
 */
public class AdminTasksActivity extends BaseActivity {

    public static final int ACTIVITY_ARRANGE_REQUEST_CODE=1001;
    private AdminTaskPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tasks);
        String type = getIntent().getStringExtra(AdminTasksFragment.TYPE);
        int[] groupIds = getIntent().getIntArrayExtra(AdminTasksFragment.GROUPIDS);
        String applyId = getIntent().getStringExtra(AdminTasksFragment.SEARCH_APPLYID);
        String mobile = getIntent().getStringExtra(AdminTasksFragment.SEARCH_MOBILE);
        String plateNo =getIntent().getStringExtra(AdminTasksFragment.SEARCH_PLATENO);
        AdminTasksFragment adminTasksFragment = (AdminTasksFragment) getSupportFragmentManager().findFragmentById(R.id.fl_admin_contains);
        if (CommUtil.checkIsNull(adminTasksFragment)) {
            if (getIntent().hasExtra(AdminTasksFragment.GROUPIDS)){
                if (groupIds.length>0){
                    adminTasksFragment=AdminTasksFragment.newInstance(type,groupIds);
                }else {
                    adminTasksFragment = AdminTasksFragment.newInstance(type,groupIds,applyId,mobile,plateNo);
                }
            }else {
                adminTasksFragment = AdminTasksFragment.newInstance(type);
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_admin_contains, adminTasksFragment);
            transaction.commit();
        }
        mPresenter = new AdminTaskPresenter(adminTasksFragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==ACTIVITY_ARRANGE_REQUEST_CODE){
            if (resultCode== Activity.RESULT_OK){
                AdminTasksFragment adminTasksFragment = (AdminTasksFragment) getSupportFragmentManager().findFragmentById(R.id.fl_admin_contains);
                if (adminTasksFragment!=null){
                    adminTasksFragment.onRefresh();
                }
            }
        }
    }
}
