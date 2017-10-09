package com.example.util.EditTextSub;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.example.newenergyvehicle.R;

public class SubEditText extends EditText implements OnFocusChangeListener,TextWatcher{
	private Drawable mClearDrawable;  
    private boolean hasFoucs;  
  
    public SubEditText(Context context) {  
        this(context, null);  
    }  
  
    public SubEditText(Context context, AttributeSet attrs) {  
        this(context, attrs, android.R.attr.editTextStyle);  
    }  
  
    public SubEditText(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init();  
    }  
  
    private void init() {  
        mClearDrawable = getCompoundDrawables()[2];  
        if (mClearDrawable == null) {  
            mClearDrawable = getResources().getDrawable(R.drawable.clean); 
           
        }  
  
//        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(),  
//                mClearDrawable.getIntrinsicHeight());
        mClearDrawable.setBounds(0, 0, 40, 40);
        setClearIconVisible(false);  
        setOnFocusChangeListener(this);  
        addTextChangedListener(this);  
    }  
          
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        if (event.getAction() == MotionEvent.ACTION_UP) {  
            if (getCompoundDrawables()[2] != null) {  
                int x = (int)event.getX();  
                int y = (int)event.getY();  
                Rect rect = getCompoundDrawables()[2].getBounds();  
                int height = rect.height();  
                int distance = (getHeight() - height)/2;  
                boolean isInnerWidth = x > (getWidth() - getTotalPaddingRight()) && x < (getWidth() - getPaddingRight());  
                boolean isInnerHeight = y > distance && y <(distance + height);  
                if (isInnerWidth && isInnerHeight) {  
                    this.setText("");  
                }  
            }  
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
        	getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(event);  
    }  
  
    @Override  
    public void onFocusChange(View v, boolean hasFocus) {  
        this.hasFoucs = hasFocus;  
        if (hasFocus) {  
            setClearIconVisible(getText().length() > 0);  
        } else {  
            setClearIconVisible(false);  
        }  
    }  
  
    protected void setClearIconVisible(boolean visible) {  
        Drawable right = visible ? mClearDrawable : null;  
        setCompoundDrawables(getCompoundDrawables()[0],  
                getCompoundDrawables()[1], right, getCompoundDrawables()[3]);  
    }  
  
    @Override  
    public void onTextChanged(CharSequence s, int start, int count, int after) {  
        if (hasFoucs) {  
            setClearIconVisible(s.length() > 0);  
        }  
    }  
  
    @Override  
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {  
  
    }  
  
    @Override  
    public void afterTextChanged(Editable s) {  
  
    }  
}
