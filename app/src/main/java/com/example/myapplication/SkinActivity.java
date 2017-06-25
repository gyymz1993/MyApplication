package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import com.yangshao.BaseSkinActivity;
import com.yangshao.skin.ISkinChangeListener;
import com.yangshao.skin.SkinManager;
import com.yangshao.utils.T_;
import java.io.File;

/**
 * 创建人：gyymz1993
 * 创建时间：2017/3/30/17:49
 **/
public class SkinActivity extends BaseSkinActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initSetContentView() {
        setContentView(R.layout.skin_layout);
    }

    public void skin(View view) {
        // 1. 首先去网上下载资源皮肤
        // 2. 下载后保存在本地
        mSrc = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "skin1.skin";
        File mFile = new File(Environment.getExternalStorageDirectory(), "skin1.skin");
        if (!mFile.exists()){
            T_.showToastReal("文件不存在");
        }else {
            T_.showToastReal("文件存在");
        }
        // 3. 调用该方法去加载皮肤 不需要重启
        SkinManager.getInstance().loadSkin(mSrc);
        // 可以判断result是否换肤成功，不同的状态对应不同的原因
    }

    public void skin1(View view) {
        // 恢复默认皮肤
        SkinManager.getInstance().restoreDefault();
    }

    // 跳转activity
    public void skin2(View view) {
        Intent intent = new Intent(this, SkinActivity.class);
        startActivity(intent);
    }

    String mSrc;
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
