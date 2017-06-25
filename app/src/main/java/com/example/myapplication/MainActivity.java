package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.yangshao.base.BaseActivity;
import com.yangshao.hook.HookStartActivity;
import com.yangshao.loading.BaseLoadingLayout;
import com.yangshao.title.DefaultTitleBar;
import com.yangshao.utils.UIUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.id_btn_dialog)
    Button btn_post;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSetContentView();
        HookStartActivity.hook();
        //btn_post.setOnClickListener();
        //(ViewGroup)findViewById(R.id.loading)
       // ViewGroup contentParent = (ViewGroup)findViewById(ID_ANDROID_CONTENT);
        //contentParent=(ViewGroup)findViewById(R.id.loading);
        new DefaultTitleBar.Builder(this).setLeftIcon(R.drawable.back_h).
            setTitleText("发表").setRightText("发表").onCreate();
        ImageView imageView=new ImageView(this);
        imageView.setImageResource(R.drawable.common_full_open_on_phone);
    }

    //等同于@NetWorkState(value={R.id.btn_get,R.id.btn_post},type=View.OnClickListener.class)
    @Event(value={R.id.id_btn_dialog})
    private void getEvent(View view){
        switch(view.getId()){
            case R.id.id_btn_dialog:
               // Toast.makeText(MainActivity.this, 2/0+"保存", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    public void setListener() {
       //  final Button button = (Button) findViewById(R.id.id_btn_dialog);

//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, 2/0+"保存", Toast.LENGTH_SHORT).show();
//
////                new AlertDialog.Builder(MainActivity.this)
////                        .setContentView(R.layout.dialog)
////                        .setText(R.id.id_save,"保存")
////                        .setOnclickListener(R.id.id_save, new View.OnClickListener() {
////                            @Override
////                            public void onClick(View v) {
////                                Toast.makeText(MainActivity.this, "保存", Toast.LENGTH_SHORT).show();
////                            }
////                        }).show();
//            }
//        });
        UIUtils.runInWindows(getWindow()).post(new Runnable() {
            @Override
            public void run() {
               // HookViewClickUtil.hookView(button);
            }
        });

    }

    BaseLoadingLayout loading;


    public void initSetContentView() {
        //setContentView(R.layout.activity_main);
        x.view().inject(this);


    }

    @Override
    public void initView() {

    }


    public void processLogic(Bundle saveInstanceState) {
    }
}
