package com.yangshao.loading;


import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yangshao.utils.UIUtils;
import com.yangsho.baselib.R;

/**
 * Created by Administrator on 2017/3/23.
 */

public class LoadingController {

    public BaseLoadingLayout mBaseLoadingLayout;
    public LoadingController(BaseLoadingLayout baseLoadingLayout) {
        this.mBaseLoadingLayout = baseLoadingLayout;
    }

    public BaseLoadingLayout getmBaseLoadingLayout() {
        return mBaseLoadingLayout;
    }

    public static class AlertParams{

        BaseLoadingLayout.OnReloadListener onReloadListener;
        public View contentView;
        public View loadingPage;
        public View errorPage;
        public View emptyPage;
        public View networkPage;
        public View defineLoadingPage;


        private ImageView errorImg;
        private ImageView emptyImg;
        private ImageView networkImg;

        private TextView errorText;
        private TextView emptyText;
        private TextView networkText;

        private TextView errorReloadBtn;
        private TextView networkReloadBtn;

        String emptyStr = "暂无数据";
        String errorStr = "加载失败，请稍后重试···";
        String netwrokStr = "无网络连接，请检查网络···";
        String reloadBtnStr = "点击重试";
        int emptyImgId = R.mipmap.empty;
        int errorImgId = R.mipmap.error;
        int networkImgId = R.mipmap.no_network;
        int reloadBtnId = R.drawable.selector_btn_back_gray;
        int tipTextSize = 14;
        int buttonTextSize = 14;
        int tipTextColor = R.color.base_text_color_light;
        int buttonTextColor = R.color.base_text_color_light;
        int buttonWidth = -1;
        int buttonHeight = -1;
        int loadingLayoutId = R.layout.widget_loading_page;
        View loadingView = null;
        int backgroundColor = R.color.base_loading_background;


        public final static int Success = 0;
        public final static int Empty = 1;
        public final static int Error = 2;
        public final static int No_Network = 3;
        public final static int Loading = 4;

        @IntDef({Success, Empty, Error, No_Network, Loading})
        public @interface Flavour {

        }

        private int state;
        public void setStatus(int status) {
            this.state = status;
            switch (status) {
                case Success:
                    contentView.setVisibility(View.VISIBLE);
                    emptyPage.setVisibility(View.GONE);
                    errorPage.setVisibility(View.GONE);
                    networkPage.setVisibility(View.GONE);
                    if (defineLoadingPage != null) {
                        defineLoadingPage.setVisibility(View.GONE);
                    } else {
                        loadingPage.setVisibility(View.GONE);
                    }
                    break;
                case Loading:
                    contentView.setVisibility(View.GONE);
                    emptyPage.setVisibility(View.GONE);
                    errorPage.setVisibility(View.GONE);
                    networkPage.setVisibility(View.GONE);
                    if (defineLoadingPage != null) {
                        defineLoadingPage.setVisibility(View.VISIBLE);
                    } else {
                        loadingPage.setVisibility(View.VISIBLE);
                    }
                    break;
                case Empty:
                    contentView.setVisibility(View.GONE);
                    emptyPage.setVisibility(View.VISIBLE);
                    errorPage.setVisibility(View.GONE);
                    networkPage.setVisibility(View.GONE);
                    if (defineLoadingPage != null) {
                        defineLoadingPage.setVisibility(View.GONE);
                    } else {
                        loadingPage.setVisibility(View.GONE);
                    }
                    break;
                case Error:
                    contentView.setVisibility(View.GONE);
                    loadingPage.setVisibility(View.GONE);
                    emptyPage.setVisibility(View.GONE);
                    errorPage.setVisibility(View.VISIBLE);
                    networkPage.setVisibility(View.GONE);
                    if (defineLoadingPage != null) {
                        defineLoadingPage.setVisibility(View.GONE);
                    } else {
                        loadingPage.setVisibility(View.GONE);
                    }
                    break;
                case No_Network:
                    contentView.setVisibility(View.GONE);
                    loadingPage.setVisibility(View.GONE);
                    emptyPage.setVisibility(View.GONE);
                    errorPage.setVisibility(View.GONE);
                    networkPage.setVisibility(View.VISIBLE);
                    if (defineLoadingPage != null) {
                        defineLoadingPage.setVisibility(View.GONE);
                    } else {
                        loadingPage.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }

        /**
         * 自定义加载页面，仅对当前所在的地方有效
         *
         * @param id
         * @return
         */
        public void setLoadingPage(LoadingController controller,Context mContext,@LayoutRes int id) {
            controller.getmBaseLoadingLayout().removeView(loadingPage);
            View view = LayoutInflater.from(mContext).inflate(id, null);
            defineLoadingPage = view;
            defineLoadingPage.setVisibility(View.GONE);
            controller.getmBaseLoadingLayout().addView(view);
        }


        public void setContentView(View contentView) {
            this.contentView = contentView;
        }

        public void apply(LoadingController controller,Context mContext) {
            LoadingViewHelper loadingViewHelper = new LoadingViewHelper();
            if (loadingLayoutId != 0) {
                loadingPage = LayoutInflater.from(mContext).inflate(loadingLayoutId, null);
            }
            if (loadingView != null) {
                loadingPage = loadingView;
            }
            if (loadingPage==null){
                loadingPage = LayoutInflater.from(mContext).inflate(R.layout.widget_loading_page, null);
            }
            Log.e("loadingPage",loadingLayoutId+":"+loadingPage);
            errorPage = LayoutInflater.from(mContext).inflate(R.layout.widget_error_page, null);
            emptyPage = LayoutInflater.from(mContext).inflate(R.layout.widget_empty_page, null);
            networkPage = LayoutInflater.from(mContext).inflate(R.layout.widget_nonetwork_page, null);
            defineLoadingPage = null;

            loadingPage.setBackgroundColor(UIUtils.getColor(backgroundColor));
            errorPage.setBackgroundColor(UIUtils.getColor(backgroundColor));
            emptyPage.setBackgroundColor(UIUtils.getColor(backgroundColor));
            networkPage.setBackgroundColor(UIUtils.getColor(backgroundColor));
            errorText = UIUtils.findViewById(errorPage, R.id.error_text);
            emptyText = UIUtils.findViewById(emptyPage, R.id.empty_text);
            networkText = UIUtils.findViewById(networkPage, R.id.no_network_text);
            errorImg = UIUtils.findViewById(errorPage, R.id.error_img);
            emptyImg = UIUtils.findViewById(emptyPage, R.id.empty_img);
            networkImg = UIUtils.findViewById(networkPage, R.id.no_network_img);

            errorReloadBtn = UIUtils.findViewById(errorPage, R.id.error_reload_btn);
            networkReloadBtn = UIUtils.findViewById(networkPage, R.id.no_network_reload_btn);
            errorReloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onReloadListener!=null){
                        onReloadListener.onReload(v);
                    }
                }
            });
            networkReloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onReloadListener!=null){
                        onReloadListener.onReload(v);
                    }
                }
            });
            errorText.setText(errorStr);
            emptyText.setText(emptyStr);
            networkText.setText(netwrokStr);
            errorText.setTextSize(tipTextSize);
            emptyText.setTextSize(tipTextSize);
            networkText.setTextSize(tipTextSize);
            errorText.setTextColor(UIUtils.getColor(tipTextColor));
            emptyText.setTextColor(UIUtils.getColor(tipTextColor));
            networkText.setTextColor(UIUtils.getColor(tipTextColor));
            errorImg.setImageResource(errorImgId);
            emptyImg.setImageResource(emptyImgId);
            networkImg.setImageResource(networkImgId);
            errorReloadBtn.setBackgroundResource(reloadBtnId);
            networkReloadBtn.setBackgroundResource(reloadBtnId);
            errorReloadBtn.setText(reloadBtnStr);
            networkReloadBtn.setText(reloadBtnStr);
            errorReloadBtn.setTextSize(buttonTextSize);
            networkReloadBtn.setTextSize(buttonTextSize);
            errorReloadBtn.setTextColor(UIUtils.getColor(buttonTextColor));
            networkReloadBtn.setTextColor(UIUtils.getColor(buttonTextColor));
            if (buttonHeight != -1) {
                errorReloadBtn.setHeight(UIUtils.dp2px( buttonHeight));
                networkReloadBtn.setHeight(UIUtils.dp2px(buttonHeight));
            }
            if (buttonWidth != -1) {
                errorReloadBtn.setWidth(UIUtils.dp2px(buttonWidth));
                networkReloadBtn.setWidth(UIUtils.dp2px(buttonWidth));
            }
//
//
//            loadingViewHelper.setViewBackgroundColor(loadingPage, backgroundColor);
//            loadingViewHelper.setViewBackgroundColor(errorPage, backgroundColor);
//            loadingViewHelper.setViewBackgroundColor(emptyPage, backgroundColor);
//            loadingViewHelper.setViewBackgroundColor(networkPage, backgroundColor);
//            loadingViewHelper.setText(errorPage, R.id.error_text, errorStr);
//            loadingViewHelper.setText(emptyPage, R.id.empty_text, emptyStr);
//            loadingViewHelper.setText(networkPage, R.id.no_network_text, netwrokStr);
//            loadingViewHelper.setTextSize(errorPage, R.id.error_text, tipTextSize);
//            loadingViewHelper.setTextSize(emptyPage, R.id.empty_text, tipTextSize);
//            loadingViewHelper.setTextSize(networkPage, R.id.no_network_text, tipTextSize);
//            loadingViewHelper.setTextColor(errorPage, R.id.error_text, tipTextColor);
//            loadingViewHelper.setTextColor(emptyPage, R.id.empty_text, tipTextColor);
//            loadingViewHelper.setTextColor(networkPage, R.id.no_network_text, tipTextColor);
//            loadingViewHelper.setImageResource(errorPage, R.id.error_img, errorImgId);
//            loadingViewHelper.setImageResource(emptyPage, R.id.empty_img, emptyImgId);
//            loadingViewHelper.setImageResource(networkPage, R.id.no_network_img, networkImgId);
//
//            loadingViewHelper.setTextBackgroundResource(errorPage, R.id.error_reload_btn, reloadBtnId);
//            loadingViewHelper.setText(errorPage, R.id.error_reload_btn, reloadBtnStr);
//            loadingViewHelper.setTextSize(errorPage, R.id.error_reload_btn, buttonTextSize);
//            loadingViewHelper.setTextColor(errorPage, R.id.error_reload_btn, buttonTextColor);
//            loadingViewHelper.setTextBackgroundResource(networkPage, R.id.no_network_reload_btn, reloadBtnId);
//            loadingViewHelper.setText(networkPage, R.id.no_network_reload_btn, reloadBtnStr);
//            loadingViewHelper.setTextSize(networkPage, R.id.no_network_reload_btn, buttonTextSize);
//            loadingViewHelper.setTextColor(networkPage, R.id.no_network_reload_btn, buttonTextColor);
//
//            if (buttonHeight != -1) {
//                loadingViewHelper.setHeight(errorPage, R.id.error_reload_btn, buttonHeight);
//                loadingViewHelper.setHeight(networkPage, R.id.no_network_reload_btn, buttonHeight);
//            }
//            if (buttonWidth != -1) {
//                loadingViewHelper.setWidth(errorPage, R.id.error_reload_btn, buttonHeight);
//                loadingViewHelper.setWidth(networkPage, R.id.no_network_reload_btn, buttonHeight);
//            }
//
//            loadingViewHelper.setOnclickListener(errorPage, R.id.error_reload_btn, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//            loadingViewHelper.setOnclickListener(networkPage, R.id.no_network_reload_btn, new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
            controller.getmBaseLoadingLayout().addView(networkPage);
            controller.getmBaseLoadingLayout().addView(emptyPage);
            controller.getmBaseLoadingLayout().addView(errorPage);
            controller.getmBaseLoadingLayout().addView(loadingPage);
        }
    }
}
