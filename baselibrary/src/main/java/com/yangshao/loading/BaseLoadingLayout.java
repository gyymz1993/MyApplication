package com.yangshao.loading;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import com.yangsho.baselib.R;

/**
  * @author: gyymz1993
  * 创建时间：2017/4/26 22:16
  * @version
  *
 **/

public class BaseLoadingLayout extends FrameLayout{

    private View contentView;
    private boolean isFirstVisible; //是否一开始显示contentview，默认不显示
    private LoadingController mAlert;
    private static LoadConfiger mLoadConfiger=new LoadConfiger();
    private Context mContent;

    public interface OnReloadListener {
        void onReload(View v);
    }

    public BaseLoadingLayout(Context context) {
        this(context,null);
    }

    public BaseLoadingLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
    public BaseLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContent=context;
        mAlert=new LoadingController(this);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadingLayout);
        isFirstVisible = a.getBoolean(R.styleable.LoadingLayout_isFirstVisible, false);
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("LoadingLayout can host only one direct child");
        }
        contentView = this.getChildAt(0);
        if (!isFirstVisible) {
            contentView.setVisibility(View.GONE);
        }
        mLoadConfiger.P.setContentView(contentView);
        mLoadConfiger.P.apply(mAlert,mContent);
    }

    public void setStatus(@LoadingController.AlertParams.Flavour int status){
        mLoadConfiger.P.setStatus(status);
    }


    /**
     * 设置ReloadButton的监听器
     *
     * @param listener
     * @return
     */
    public BaseLoadingLayout setOnReloadListener(OnReloadListener listener) {
        mLoadConfiger.P.onReloadListener=listener;
        return this;
    }


    public static LoadConfiger getmLoadConfiger() {
        return mLoadConfiger;
    }

    /**
     * 自定义加载页面，仅对当前所在的地方有效
     * @param id
     * @return
     */
    public BaseLoadingLayout setLoadingPage(@LayoutRes int id) {
        mLoadConfiger.P.setLoadingPage(mAlert,mContent,id);
        return this;
    }

    public BaseLoadingLayout setLoadingPage(View view) {
        mLoadConfiger.P.loadingView=view;
        return this;
    }

    public static class LoadConfiger{
        public  LoadingController.AlertParams P;

        /**
         * 自定义加载页面，仅对当前所在的Activity有效
         * @return
         */

        public LoadConfiger(){
            P = new LoadingController.AlertParams();
            P.setContentView(P.loadingView);
        }

        public LoadConfiger setErrorText(@NonNull String text) {
            P.errorStr = text;
            return this;
        }

        public LoadConfiger setEmptyText(@NonNull String text) {
            P.emptyStr = text;
            return this;
        }

        public LoadConfiger setNoNetworkText(@NonNull String text) {
            P.netwrokStr = text;
            return this;
        }

        public LoadConfiger setReloadButtonText(@NonNull String text) {
            P.reloadBtnStr = text;
            return this;
        }

        /**
         * 设置所有提示文本的字体大小
         *
         * @param sp
         * @return
         */
        public LoadConfiger setAllTipTextSize(int sp) {
            P.tipTextSize = sp;
            return this;
        }

        /**
         * 设置所有提示文本的字体颜色
         *
         * @param color
         * @return
         */
        public LoadConfiger setAllTipTextColor(@ColorRes int color) {
            P.tipTextColor = color;
            return this;
        }

        public LoadConfiger setReloadButtonTextSize(int sp) {
            P.buttonTextSize = sp;
            return this;
        }

        public LoadConfiger setReloadButtonTextColor(@ColorRes int color) {
            P.buttonTextColor = color;
            return this;
        }

        public LoadConfiger setReloadButtonBackgroundResource(@DrawableRes int id) {
            P.reloadBtnId = id;
            return this;
        }

        public LoadConfiger setReloadButtonWidthAndHeight(int width_dp, int height_dp) {
            P.buttonWidth = width_dp;
            P.buttonHeight = height_dp;
            return this;
        }

        public LoadConfiger setErrorImage(@DrawableRes int id) {
            P.errorImgId = id;
            return this;
        }

        public LoadConfiger setEmptyImage(@DrawableRes int id) {
            P.emptyImgId = id;
            return this;
        }

        public LoadConfiger setNoNetworkImage(@DrawableRes int id) {
            P.networkImgId = id;
            return this;
        }

        public LoadConfiger setLoadingPageLayout(@LayoutRes int id) {
            P.loadingLayoutId = id;
            return this;
        }

        public LoadConfiger setLoadingPageView(View view){
            P.loadingView  = view;
            return this;
        }

        public LoadConfiger setAllPageBackgroundColor(@ColorRes int color) {
            P.backgroundColor = color;
            return this;
        }
    }

}
