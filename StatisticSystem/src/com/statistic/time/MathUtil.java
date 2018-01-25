package com.statistic.time;

public class MathUtil {
 public static float floatParseTwo(float a) {
	 float b = Math.round(a * 100) * 0.01f;
	 String c = String.format("%.2f", b);
	 float d = Float.parseFloat(c);
	 return d;
 } 
 public static float floatParseOne(float a) {
	 float b = Math.round(a * 100) * 0.01f;
	 String c = String.format("%.1f", b);
	 float d = Float.parseFloat(c);
	 return d;
 } 
 
 public static int floatParseInt(float a) {
	 float b = Math.round(a * 100) * 0.01f;
	 int d = (int) b;
	 return d;
 } 
 
}
