package com.yangshao.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.yangshao.skin.attr.SkinView;
import com.yangshao.utils.L_;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 皮肤管理类
 *          把View交给它管理
 * @author: gyymz1993
 * 创建时间：2017/3/30 22:01
 **/
public class SkinManager {
    private Context mContext;
    private static SkinManager mInstance;
    private SkinResource mSkinResource;
    private Map<ISkinChangeListener, List<SkinView>> mSkinViewsListener = new HashMap<>();

    static {
        mInstance = new SkinManager();
    }

    public static SkinManager getInstance() {
        return mInstance;
    }

    private SkinManager() {
    }

    /**
     * 初始化 一般都在Application中配置
     *
     * @param context
     */
    public void init(Context context) {
        this.mContext = context.getApplicationContext();
        String skinPath = SkinPreUtils.getInstance().getSkinPath();
        if (TextUtils.isEmpty(skinPath)) {
            return;
        }
        File skinFile = new File(skinPath);
        if (!skinFile.exists()) {
         /*不存在*/
            clearSkinInfo();
            return;
        }
        String packgeName = mContext.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES).packageName;
        if (TextUtils.isEmpty(packgeName)) {
         /*皮肤文件错误*/
            clearSkinInfo();
            return;
        }
        initSkinResource(skinPath);
    }

    /**
     * 加载皮肤
     *
     * @param skinPaht 当前皮肤路径
     * @return
     */
    public int loadSkin(String skinPaht) {
        /*当前皮肤一样就不用换肤*/
        String currentSkinPath = SkinPreUtils.getInstance().getSkinPath();
        if (skinPaht.equals(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NONTHING;
        }

        File skinFile = new File(skinPaht);
        if (!skinFile.exists()) {
            return SkinConfig.SKIN_PATH_ERROR;
        }
        // 判断签名是否正确，后面增量更新再说
        String packgeName = mContext.getPackageManager().getPackageArchiveInfo(skinPaht, PackageManager.GET_ACTIVITIES).packageName;
        if (TextUtils.isEmpty(packgeName)) {
            return SkinConfig.SKIN_PATH_ERROR;
        }
        initSkinResource(skinPaht);
        changeSkin();
        saveSkinInfo(skinPaht);
        // 加载成功
        return SkinConfig.SKIN_LOAD_SUCCESS;
    }

    public void checkChangeSkin(SkinView skinView) {
        //如果偶皮肤就换肤
        String currentSkin = SkinPreUtils.getInstance().getSkinPath();
        if (!TextUtils.isEmpty(currentSkin)) {
            skinView.skin();
        }
    }

    /**
     * 切换皮肤
     *
     * @param
     */
    private void changeSkin() {
        for (ISkinChangeListener skinChangeListener : mSkinViewsListener.keySet()) {
            List<SkinView> skinViews = mSkinViewsListener.get(skinChangeListener);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
            skinChangeListener.changeSkin(getSkinResources());
        }
    }

    /**
     * 初始化皮肤的Resource
     *
     * @param path
     */
    private void initSkinResource(String path) {
        mSkinResource = new SkinResource(mContext, path);
    }

    /**
     * 保存当前皮肤的信息
     *
     * @param path 当前皮肤的路径
     */
    private void saveSkinInfo(String path) {
        SkinPreUtils.getInstance().saveSkinPath(path);
    }

    /**
     * 恢复默认皮肤
     */
    public int restoreDefault() {
        /*判断当前皮肤  如果没有就不要执行*/
        String currentSkinPath = SkinPreUtils.getInstance().getSkinPath();
        if (TextUtils.isEmpty(currentSkinPath)) {
            return SkinConfig.SKIN_CHANGE_NONTHING;
        }
        String path = mContext.getPackageResourcePath();
        initSkinResource(path);
        changeSkin();
        clearSkinInfo();
        return SkinConfig.SKIN_LOAD_SUCCESS;
    }

    /**
     * 清空皮肤信息
     */
    private void clearSkinInfo() {
        SkinPreUtils.getInstance().clearSkinInfo();
    }

    public SkinResource getSkinResources() {
        return mSkinResource;
    }

    /**
     * 注册监听回调
     */
    public void registerListener(ISkinChangeListener skinChangeListener, List<SkinView> skinViews) {
        mSkinViewsListener.put(skinChangeListener, skinViews);
    }

    /**
     * 移除回调，怕引起内存泄露
     */
    public void unregisterListener(ISkinChangeListener skinChangeListener) {
        mSkinViewsListener.remove(skinChangeListener);
    }

    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViewsListener.get(activity);
    }

}
