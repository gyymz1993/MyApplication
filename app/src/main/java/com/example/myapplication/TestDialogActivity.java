package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yangshao.base.BaseActivity;
import com.yangshao.loading.BaseLoadingLayout;
import com.yangshao.loading.LoadingController;
import com.yangshao.title.DefaultTitleBar;
import com.yangshao.utils.UIUtils;

import static android.view.Window.ID_ANDROID_CONTENT;

/**
 * 创建人：gyymz1993
 * 创建时间：$date$ $time$
 **/
public class TestDialogActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initSetContentView() {
        setContentView(R.layout.dialog_mian);
    }

    @Override
    public void initView() {
        new DefaultTitleBar.Builder(this,
            (ViewGroup)findViewById(ID_ANDROID_CONTENT))
            .setLeftIcon(R.drawable.back_h).
            setTitleText("发表").setRightText("发表").onCreate();
        final BaseLoadingLayout  loading = (BaseLoadingLayout) findViewById(R.id.loading);
        UIUtils.getHandler().post(new Runnable() {
            @Override
            public void run() {
                loading.setStatus(LoadingController.AlertParams.Success);
            }
        });
        loading.setLoadingPage(R.layout.define_loading_page).
            setOnReloadListener(new BaseLoadingLayout.OnReloadListener() {
                @Override
                public void onReload(View v) {
                    Toast.makeText(TestDialogActivity.this, "重试", Toast.LENGTH_SHORT).show();
                }
            });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setStatus(LoadingController.AlertParams.Empty);
            }
        }, 2000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setStatus(LoadingController.AlertParams.Error);
            }
        }, 4000);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                loading.setStatus(LoadingController.AlertParams.No_Network);
            }
        }, 6000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setStatus(LoadingController.AlertParams.Loading);
            }
        }, 8000);

        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setStatus(LoadingController.AlertParams.Success);
            }
        }, 10000);

    }

    @Override
    public void processLogic(Bundle saveInstanceState) {

    }

    @Override
    public void setListener() {

    }
}
