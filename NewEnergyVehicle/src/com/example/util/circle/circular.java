package com.example.util.circle;

import android.content.Context;  
import android.graphics.Canvas;  
import android.graphics.Paint;  
import android.util.AttributeSet;  
import android.view.View;  
  
public class circular extends View {  
  
    private final  Paint paint;  
    private final Context context;  
      
    public circular(Context context) {  
          
        // TODO Auto-generated constructor stub  
        this(context, null);  
    }  
  
    public circular(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        // TODO Auto-generated constructor stub  
        this.context = context;  
        this.paint = new Paint();  
        this.paint.setAntiAlias(true); //�������  
        this.paint.setStyle(Paint.Style.STROKE); //���ƿ���Բ   
    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {  
        // TODO Auto-generated method stub  
        int center = getWidth()/2;  
        int innerCircle = dip2px(context, 83); //������Բ�뾶  
        int ringWidth = dip2px(context, 5); //����Բ�����  
          
        //������Բ  
        this.paint.setARGB(155, 167, 190, 206);  
        this.paint.setStrokeWidth(2);  
        canvas.drawCircle(center,center, innerCircle, this.paint);  
          
        //����Բ��  
        this.paint.setARGB(255, 212 ,225, 233);  
        this.paint.setStrokeWidth(ringWidth);  
        canvas.drawCircle(center,center, innerCircle+1+ringWidth/2, this.paint);  
          
        //������Բ  
        this.paint.setARGB(155, 167, 190, 206);  
        this.paint.setStrokeWidth(2);  
        canvas.drawCircle(center,center, innerCircle+ringWidth, this.paint);  
  
          
        super.onDraw(canvas);  
    }  
      
      
    /** 
     * �����ֻ��ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
}  
