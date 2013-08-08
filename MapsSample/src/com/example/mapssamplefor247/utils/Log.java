package com.example.mapssamplefor247.utils;


public class Log
{
	
	private static final boolean LOG_ENABLED = true;
	private static final boolean VERBOSE_ENABLED = true;
	private static final boolean WARNING_ENABLED = true;
	private static final boolean ERROR_ENABLED = true;
	private static final boolean DEBUG_ENABLED = true;
	private static final boolean INFO_ENABLED = true;
 
 private Log()
 {
  
 }

 @SuppressWarnings("unused")
public static void d(String tag, String msg)
 {
  if (LOG_ENABLED && DEBUG_ENABLED)
	  android.util.Log.d(""+tag, ""+msg);
 }
 
 @SuppressWarnings("unused")
public static void d(String tag, String msg, Throwable tr)
 {
  if (LOG_ENABLED && DEBUG_ENABLED)
	  android.util.Log.d(""+tag, ""+msg, tr);
 }

 @SuppressWarnings("unused")
public static void e(String tag, String msg)
 {
  if (LOG_ENABLED && ERROR_ENABLED)
	  android.util.Log.e(""+tag, ""+msg);
 }

 @SuppressWarnings("unused")
 public static void e(String tag, String msg, Throwable tr)
 {
  if (LOG_ENABLED && ERROR_ENABLED)
	  android.util.Log.e(""+tag, ""+msg, tr);
 }

 @SuppressWarnings("unused")
 public static void i(String tag, String msg)
 {
  if (LOG_ENABLED && INFO_ENABLED)
	  android.util.Log.i(""+tag, ""+msg);
 }

 @SuppressWarnings("unused")
 public static void i(String tag, String msg, Throwable tr)
 {
  if (LOG_ENABLED && INFO_ENABLED)
	  android.util.Log.i(""+tag, ""+msg, tr);
 }
 
 @SuppressWarnings("unused")
 public static void v(String tag, String msg)
 {
  if (LOG_ENABLED && VERBOSE_ENABLED)
	  android.util.Log.v(""+tag, ""+msg);
 }
 
 @SuppressWarnings("unused")
 public static void v(String tag, String msg, Throwable tr)
 {
  if (LOG_ENABLED && VERBOSE_ENABLED)
	  android.util.Log.v(""+tag, ""+msg, tr);
 }
 
 @SuppressWarnings("unused")
 public static void w(String tag, Throwable tr)
 {
  if (LOG_ENABLED && WARNING_ENABLED)
	  android.util.Log.w(""+tag, tr);
 }
 
 @SuppressWarnings("unused")
 public static void w(String tag, String msg, Throwable tr)
 {
  if (LOG_ENABLED && WARNING_ENABLED)
	  android.util.Log.w(""+tag, ""+msg, tr);
 }

 @SuppressWarnings("unused")
 public static void w(String tag, String msg)
 {
  if (LOG_ENABLED && WARNING_ENABLED)
	  android.util.Log.w(""+tag, ""+msg);
 }
 
}