package com.cango.adpickcar.fc.message;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.cango.adpickcar.R;
import com.cango.adpickcar.base.BaseActivity;
import com.cango.adpickcar.util.CommUtil;

/**
 * 消息界面
 */
public class MessageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        MessageFragment messageFragment = (MessageFragment) getSupportFragmentManager().findFragmentById(R.id.fl_message_contains);
        if (CommUtil.checkIsNull(messageFragment)) {
            messageFragment = MessageFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.fl_message_contains, messageFragment);
            transaction.commit();
        }
        new MessagePresenter(messageFragment);
    }
}
