package com.example.util.dropDownOrUp;

import android.view.MotionEvent;

public abstract class Moving {
	public abstract void down(MotionEvent arg1);
	public abstract void move(MotionEvent arg1);
	public abstract void up(MotionEvent arg1);
}
