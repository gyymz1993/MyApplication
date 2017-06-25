package com.yangshao.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

/**
  * @author: gyymz1993
  * 创建时间：2017/3/31 15:26
  * @version
  *
 **/
public abstract class BasePage implements OnClickListener {

	protected Context context;
	protected View contentView;

	public boolean isLoadSuccess=false;
	
	public BasePage(Context context) {
		this.context = context;
		contentView = initView((LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
//		ViewUtils.inject(contentView);

	}
	protected abstract View initView(LayoutInflater inflater);

	public abstract void initData();

	public View getContentView() {
		return contentView;
	}
	@Override
	public void onClick(View v) {
	}
	
	protected void startActivity(Activity activity) {
		Intent intent = new Intent(context, activity.getClass());
		context.startActivity(intent);
	}
	
	
}
