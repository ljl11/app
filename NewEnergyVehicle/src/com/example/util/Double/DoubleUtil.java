package com.example.util.Double;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class DoubleUtil {

	//保留2位小数
	public static double get2Double(double a){
	    DecimalFormat df=new DecimalFormat("0.00");
	    return new Double(df.format(a).toString());
	}
	
	//判断一个double值是否为0
	public static boolean judgeDoubleIsZero(double number){
		double zero = 0.0;
		BigDecimal data1 = new BigDecimal(zero);
		BigDecimal data2 = new BigDecimal(number);
		// 为0
		int result = data1.compareTo(data2);
		if (result == 0){
			return true;
		}else{
			return false;
		}
	}
}
