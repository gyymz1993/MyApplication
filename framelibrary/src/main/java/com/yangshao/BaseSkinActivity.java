package com.yangshao;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import com.yangshao.base.BaseActivity;
import com.yangshao.skin.ISkinChangeListener;
import com.yangshao.skin.SkinAttrSupport;
import com.yangshao.skin.SkinManager;
import com.yangshao.skin.SkinResource;
import com.yangshao.skin.SkinViewInflater;
import com.yangshao.skin.attr.SkinAttr;
import com.yangshao.skin.attr.SkinView;
import com.yangshao.utils.L_;
import com.yangshao.utils.T_;

import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 换肤框架
 * @author: gyymz1993
 * 创建时间：2017/3/31 20:54
 **/
public abstract class BaseSkinActivity extends BaseActivity implements LayoutInflaterFactory, ISkinChangeListener {

    private SkinViewInflater mSkinViewInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        LayoutInflaterCompat.setFactory(layoutInflater, this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // If the Factory didn't handle it, let our createView() method try
        //创建View
        View view = createView(parent, name, context, attrs);
        L_.e("TAG onCreateView----->" + view);
        //得到一个activity的所有Skinview
        if (view != null) {
            //解析属性 src textColor
            List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
            SkinView skinView = new SkinView(view, skinAttrs);
            addSkinManager(skinView);
              /*切换皮肤*/
            SkinManager.getInstance().checkChangeSkin(skinView);
        }
        return view;
    }

    /**
     * 添加到皮肤管理器
     * @param
     */
    protected void addSkinManager(SkinView skinView) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if (skinViews == null) {
            skinViews = new ArrayList<>();
            SkinManager.getInstance().registerListener(this, skinViews);
        }
        skinViews.add(skinView);
    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;
        if (mSkinViewInflater == null) {
            mSkinViewInflater = new SkinViewInflater();
        }
        // We only want the View to inherit its context if we're running pre-v21
        final boolean inheritContext = isPre21 && shouldInheritContext((ViewParent) parent);
        return mSkinViewInflater.createView(parent, name, context, attrs, inheritContext,
            isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
            true, /* Read read app:theme as a fallback at all times for legacy reasons */
            isPre21 /* Only tint wrap the context pre-L */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    @Override
    public void initSetContentView() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().unregisterListener(this);
    }

    @Override
    public void changeSkin(SkinResource skinResource) {
        T_.showToastReal("我开始换肤了，");
    }
}
