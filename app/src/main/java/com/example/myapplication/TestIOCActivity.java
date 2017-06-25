package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yangshao.dialog.AlertDialog;
import com.yangshao.base.BaseActivity;
import com.yangshao.ioc.NetWorkState;
import com.yangshao.ioc.ViewById;
import com.yangshao.ioc.ViewDagger;
import com.yangshao.ioc.ViewOnClick;
import com.yangshao.title.DefaultTitleBar;
import com.yangshao.utils.T_;

/**
 * 创建人：gyymz1993
 * 创建时间：$date$ $time$
 **/
public class TestIOCActivity extends BaseActivity {

    @ViewById(R.id.id_btn)
    Button btn;
    @ViewById(R.id.id_btn1)
    Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initSetContentView() {
        setContentView(R.layout.test);
        ViewDagger.inject(this);

        DefaultTitleBar navigationBar = new DefaultTitleBar.Builder(this,
            (ViewGroup) findViewById(android.R.id.content))
            .setLeftText("投稿").
            setTitleText("发表").setRightText("发表").onCreate();


        btn.setText("我是第一");
        btn1.setText("我是第二");
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                T_.showToastReal("被点击");
//            }
//        });
    }




    @NetWorkState
    @ViewOnClick(value={R.id.id_btn})
    private void btnOnClick(){
        T_.showToastReal("被点击");
    }

    @ViewOnClick(value={R.id.id_btn1})
    private void showAlertDialog(){
        new AlertDialog.Builder(TestIOCActivity.this)
            .setContentView(R.layout.dialog)
            .setCancelable(true)
            .setText(R.id.id_save,"保存")
            .setOnclickListener(R.id.id_save, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(MainActivity.this, "保存", Toast.LENGTH_SHORT).show();
                }
            }).show();
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
