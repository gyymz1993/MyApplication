package com.example.myapplication;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yangshao.base.BaseActivity;
import com.yangshao.base.BaseApplication;
import com.yangshao.utils.T_;

import java.io.File;
import java.io.IOException;

/**
 * 创建人：gyymz1993
 * 创建时间：$date$ $time$
 **/
public class TestFixActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final Button button = (Button) findViewById(R.id.id_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T_.showToastReal( 2/0+"保存");
            }
        });
    }

    @Override
    public void initSetContentView() {
        try {
            File mFile = new File(Environment.getExternalStorageDirectory(), "fix.apatch");
            //     /data/fix.apatch
            Log.e("tag",mFile.getAbsolutePath());
            if (mFile.exists()) {
                BaseApplication.instance().patchManager.addPatch(mFile.getAbsolutePath());
                T_.showToastReal("修复成功");
            }else {
                T_.showToastReal("文件不存在");
            }
        } catch (IOException e) {
            e.printStackTrace();
            T_.showToastReal("修复失败");
        }


        try {
            File mFile = new File(Environment.getExternalStorageDirectory(), "fix.dex");
            //     /data/fix.apatch
            Log.e("tag",mFile.getAbsolutePath());
            if (mFile.exists()) {
                //BaseApplication.instance().patchManager.addPatch(mFile.getAbsolutePath());
                BaseApplication.instance().mDexFixUtils.addFixDex(mFile.getAbsolutePath());
                T_.showToastReal("修复成功");
            }else {
                T_.showToastReal("文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            T_.showToastReal("修复失败");
        }

    }

    @Override
    public void initView() {

    }

    @Override
    public void processLogic(Bundle saveInstanceState) {

    }

    @Override
    public void setListener() {

    }
}
