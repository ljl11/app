package com.example.util.newListView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class ScarollExpanderListView extends ExpandableListView{

	public ScarollExpanderListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		this.setFocusable(false);
	}

	public ScarollExpanderListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setFocusable(false);
	}

	public ScarollExpanderListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setFocusable(false);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

		MeasureSpec.AT_MOST);

		super.onMeasure(widthMeasureSpec, expandSpec);

		}
}
