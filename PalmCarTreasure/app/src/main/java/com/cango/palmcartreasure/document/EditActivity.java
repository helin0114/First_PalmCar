package com.cango.palmcartreasure.document;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.cango.palmcartreasure.R;
import com.cango.palmcartreasure.base.BaseActivity;
import com.cango.palmcartreasure.model.TypeTaskData;
import com.cango.palmcartreasure.util.CommUtil;

public class EditActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {

        }

        TypeTaskData.DataBean.TaskListBean bean = getIntent().getParcelableExtra(EditFragment.BEAN);
        EditFragment editFragment = (EditFragment) getSupportFragmentManager().findFragmentById(R.id.fl_edit_contains);
        if (CommUtil.checkIsNull(editFragment)) {
            editFragment = EditFragment.newInstance(bean);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_edit_contains, editFragment);
            transaction.commit();
        }
    }
}
