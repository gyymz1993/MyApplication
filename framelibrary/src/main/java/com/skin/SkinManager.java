package com.skin;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.skin.attr.SkinView;
import com.yangshao.utils.L_;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
  * @author: gyymz1993
  * 创建时间：2017/3/30 22:01
  * @version 皮肤管理类
  *
 **/
public class SkinManager {
    private static SkinManager mInstance;
    private SkinResource mSkinResource;
    private Map<ISkinChangeListener, List<SkinView>> mSkinViewsListener = new HashMap<>();
    private Map<Activity,List<SkinView>> mSkinViews=new HashMap<>();
    // 把View交给它管理
    private Context mContext;

    static {
        mInstance=new SkinManager();
    }
    public static SkinManager getInstance() {
        return mInstance;
    }
    private SkinManager(){
    }
    /**
     * 初始化 一般都在Application中配置
     * @param context
     */
    public void init(Context context) {
        this.mContext = context.getApplicationContext();
//        String skinPath = SkinPreUtils.getmInstance().getSkinPath();
//        if (TextUtils.isEmpty(skinPath)) {
//            return;
//        }
//        File skinFile = new File(skinPath);
//        if (!skinFile.exists()) {
//            clearSkinInfo();
//            return;
//        }
        //initSkinResource(skinPath);
    }

    /**
     * 加载皮肤
     * @param skinPaht 当前皮肤路径
     * @return
     */
    public int loadSkin(String skinPaht) {
         mSkinResource=new SkinResource(mContext,skinPaht);
        //改变皮肤
        Set<Activity> keys=mSkinViews.keySet();
        for (Activity key:keys){
            List<SkinView> sknViews=mSkinViews.get(key);
            for (SkinView skinView:sknViews){
                L_.e("TAG 执行改变皮肤loadSkin   skinView.skin()");
                skinView.skin();
            }
        }
            return 0;
    }


    /**
     * 加载皮肤
     * @param skinPaht 当前皮肤路径
     * @return
     */
//    public int loadSkin(String skinPaht) {
//        String currentSkinPath = SkinPreUtils.getmInstance().getSkinPath();
//        if (currentSkinPath.equals(skinPaht)) {
//            return SkinConfig.SKIN_LOADED;
//        }
//
//        File skinFile = new File(skinPaht);
//        if(!skinFile.exists()){
//            return SkinConfig.SKIN_PATH_ERROR;
//        }
//
//        // 判断签名是否正确，后面增量更新再说
//        initSkinResource(skinPaht);
//        changeSkin(skinPaht);
//        saveSkinInfo(skinPaht);
//        // 加载成功
//        return SkinConfig.SKIN_LOAD_SUCCESS;
//    }


    /**
     * 切换皮肤
     *
     * @param path 当前皮肤的路径
     */
    private void changeSkin(String path) {
        for (ISkinChangeListener skinChangeListener : mSkinViewsListener.keySet()) {
            List<SkinView> skinViews = mSkinViews.get(skinChangeListener);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
            skinChangeListener.changeSkin(path);
        }
    }

    /**
     * 初始化皮肤的Resource
     *
     * @param path
     */
//    private void initSkinResource(String path) {
//        mSkinResource = new SkinResource(mContext, path);
//    }

    /**
     * 保存当前皮肤的信息
     *
     * @param path 当前皮肤的路径
     */
    private void saveSkinInfo(String path) {
        SkinPreUtils.getmInstance().saveSkinPath(path);
    }

    /**
     * 恢复默认皮肤
     */
    public void restoreDefault() {
        String currentSkinPath = SkinPreUtils.getmInstance().getSkinPath();
        if (TextUtils.isEmpty(currentSkinPath)) {
            return;
        }

        String path = mContext.getPackageResourcePath();
       // initSkinResource(path);
        changeSkin(path);
        clearSkinInfo();
    }

    /**
     * 清空皮肤信息
     */
    private void clearSkinInfo() {
        SkinPreUtils.getmInstance().clearSkinInfo();
    }

    public SkinResource getSkinResources() {
        return mSkinResource;
    }

    /**
     * 注册监听回调
     */
    public void registerListener(List<SkinView> skinViews, ISkinChangeListener skinChangeListener) {
        mSkinViewsListener.put(skinChangeListener, skinViews);
    }

    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    public boolean isChangeSkin() {
        return SkinPreUtils.getmInstance().needChangeSkin();
    }

    public void changeSkin(SkinView skinView) {
        skinView.skin();
    }

    /**
     * 移除回调，怕引起内存泄露
     */
    public void unregisterListener(ISkinChangeListener skinChangeListener) {
        mSkinViews.remove(skinChangeListener);
    }


    public boolean needChangeSkin() {
        return true;
    }

    public void registerSkinView(Activity activity, List<SkinView> skinViews) {
        mSkinViews.put(activity,skinViews);
    }


}
