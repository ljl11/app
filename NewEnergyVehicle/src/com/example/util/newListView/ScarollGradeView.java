package com.example.util.newListView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class ScarollGradeView extends GridView{

	public ScarollGradeView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.setFocusable(false);
	}

	public ScarollGradeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setFocusable(false);
	}

	public ScarollGradeView(Context context) {
		super(context);
		this.setFocusable(false);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

		MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);

		}
}
