package com.yangshao.skin;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.yangshao.skin.attr.SkinAttr;
import com.yangshao.skin.attr.SkinType;
import com.yangshao.utils.L_;

import java.util.ArrayList;
import java.util.List;

/**
  * @author: gyymz1993
  * 创建时间：2017/3/30 22:02
  * @version 解析辅助类
  *
 **/
public class SkinAttrSupport {
    /**
     *
     * @param context
     * @param attrs
     * @return 解析我们需要的属性
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        /*存放我们资源集合*/
        List<SkinAttr> attrList=new ArrayList<>();
        int attrLenght=attrs.getAttributeCount();
        for (int index=0;index<attrLenght;index++) {
            String attrName = attrs.getAttributeName(index);
            String attrValue = attrs.getAttributeValue(index);
            L_.e("TAG   getSkinAttrs"+"attrName  -->" + attrName + "    :" + "attrValue   -->" + attrValue);
            //只获取我们重要和我们需要的
            SkinType skinType=getSkinType(attrName);
            if (skinType!=null){
                //资源名称  目前只有Value  而且是@int 开头类型
                String resName=getResName(context,attrValue);
                if (TextUtils.isEmpty(resName)){
                    continue;
                }
                SkinAttr skinAttr=new SkinAttr(resName,skinType);
                attrList.add(skinAttr);
            }
        }
        return attrList;
    }

    /**
     *  获取资源的名称
     * @param context
     * @param attrValue
     * @return
     */
    private static String getResName(Context context, String attrValue) {
        if (attrValue.startsWith("@")){
            attrValue = attrValue.substring(1);
            int resId=Integer.parseInt(attrValue);
            L_.e("TAG ----->getResName真实名称"+context.getResources().getResourceEntryName(resId));
            return context.getResources().getResourceEntryName(resId);
        }
        return null;
    }

    /**
     *  通过名称获取SkinType
     * @param attrName
     * @return
     */
    private static SkinType getSkinType(String attrName) {
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            if (skinType.getResName().equals(attrName)) {
                return skinType;
            }
        }
        return null;
    }
}
