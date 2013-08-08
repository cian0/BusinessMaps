package com.example.mapssamplefor247.utils;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;


import com.example.mapssamplefor247.utils.Log;
import com.example.mapssamplefor247.utils.dialogs.CustomProgressDialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class HelperTool {
	public static void CheckEnableGPS(final Context ctxt ){
	    String provider = Settings.Secure.getString(ctxt.getContentResolver(),
	      Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
	       if(!provider.equals("")){
	       }else{
	    	   HelperTool.createCancellableAcceptDialog(ctxt, 
	    			   "GPS Enabler", 
	    			   "Your GPS is currently disabled, turn it on?",
	    			   "Yes", 
	    			   
	    			   new Runnable() {
						@Override
						public void run() {
							Intent intent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
							ctxt.startActivity(intent);
						}
			});
	       }

	   }
	public static boolean isDeviceInchesGreaterThanOrEqualTo (double inches, Activity a){
		//http://stackoverflow.com/questions/15055458/detect-7-inch-and-10-inch-tablet-programmatically
		DisplayMetrics metrics = new DisplayMetrics();
		a.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int widthInPixels = metrics.widthPixels;
		int heightInPixels = metrics.heightPixels;
		float scaleFactor = metrics.density;
		float widthDP = widthInPixels/scaleFactor;
		float heightDP = heightInPixels/scaleFactor;
		float widthDPI = metrics.xdpi;
		float heightDPI = metrics.ydpi;
		
		float widthInInches = widthInPixels/widthDPI;
		float heightInInches = heightInPixels/heightDPI;
		
		double diagonalInches = Math.sqrt((widthInInches * widthInInches) + (heightInInches + heightInInches));
		HelperTool.trace(a, "''diagonalInches is " + diagonalInches);
		
		if (diagonalInches >= inches){
			return true;
		}
		
		return false;
	}
	private static CustomProgressDialog progressDialog;
	public static void trace (Object o, String msg){
//		Log.i("trace", "tracing...." + o);
		Log.i(o.getClass().getSimpleName() + "", msg + "");
	}
	public static void traceErr (Object o, String msg){
		Log.e(o.getClass().getSimpleName() + "", msg + "");
	}
	public static Date stringToDate_yyyyMMdd(String aStrDate) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		if (aStrDate !=null)
    			return dateFormat.parse(aStrDate);
    	} catch (java.text.ParseException ex) {
    		return null;
    	}
    	return null;
    }
	public static String dateToYYYYmmDDString(Date d){
		String date;
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		date = formatter.format(d);
		return date;
	}
	

	public static void addDelimitersAndLimitToEditText (final EditText eText, final int limit, final char [] chars){
		eText.setFilters(new InputFilter[] {
			    new InputFilter() {
					@Override
					public CharSequence filter(CharSequence source, int start, int end,
							Spanned dest, int dstart, int dend) {
						
						for (int i = start; i < end; i++) {
							if (eText.getText().length() > limit -1)
								return "";
							for(char delimiter : chars){
								if (source.charAt(i) == delimiter){
									return "";
								}
								
							}
						}
						return null;
					}
				}
			    
			});
	}
	public static void runDelayed(Runnable r, long delayMillis){
		new Handler().postDelayed(r, delayMillis);
	}
	public static void createNonCancellableAcceptDialog(Context context, String title, String message, String acceptButtonText, final Runnable onAccept ){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton(acceptButtonText, new DialogInterface.OnClickListener() {
		  @Override
		  public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    
		    
		    if (onAccept != null)
		    	onAccept.run();
		  }
		});
		AlertDialog alert = builder.create();
		alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
		    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		        if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
		            return true; // Pretend we processed it
		        }
		        return false; // Any other keys are still processed as normal
		    }
		});
		alert.show();
	}
	public static int getPixels(Context c, int dipValue){
        Resources r = c.getResources();
        int px = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, 
        r.getDisplayMetrics());
        return px;
	}
	public static void showNonCancelableProgressDialog(Activity context, final String progressTitle, final String progressMessage){
		progressDialog = new CustomProgressDialog(context);
		progressDialog.progressDialog = CustomProgressDialog.show(context, progressTitle, progressMessage);
		progressDialog.setCancelable(false);
	}
	
	public static void showCancelableProgressDialog(Activity context, final String progressTitle, final String progressMessage, Runnable onCancel){
		progressDialog = new CustomProgressDialog(context, onCancel);

//		progressDialog.setCancelable(true);
//		progressDialog.show(context, progressTitle, progressMessage, onCancel);
//		progressDialog.progressDialog = CustomProgressDialog.show(context, progressTitle, progressMessage, onCancel);
		progressDialog.progressDialog = CustomProgressDialog.showCancelable(context, progressTitle, progressMessage, onCancel);
	}
	public static void dismissProgressDialog(){
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	public static int convertStringToInt(String input){
		
		int valueToInt = 0;
		
		try {
			valueToInt = Integer.parseInt(input);
		} catch (Exception e) {
			valueToInt = 0;
		}
		
		return valueToInt;
	}
	
	public static String convertIntToString(int input){
		
		String valueToString = "0";
		
		try {
			valueToString = String.valueOf(input);
		} catch (Exception e) {
			valueToString = "0";
		}
		
		return valueToString;
	}
	
	public static Date getDate(final Date aDate) {
	    try {
		    Calendar calDate = Calendar.getInstance();
            calDate.setTime(aDate);
		    
		    return calDate.getTime();
		    
	    } catch (Exception ex) {
            System.out.println("HelperTool.getDate(): " 
                + ex.toString());
            return null;
	    }
	}
	
	public static Date stringToDate(String aStrDate) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	try {
    		return dateFormat.parse(aStrDate);
    	} catch (java.text.ParseException ex) {
    		return null;
    	}
    }

	public static void toastMessage(Context onItemClickListener, String message){
		Toast.makeText(onItemClickListener, message, Toast.LENGTH_SHORT).show();
	}
	
	
	public static double roundOff(double amount) {
		return Double.valueOf(new DecimalFormat("0.00").format(amount));
	}
	
	public static String formatToCurrency(double amount, String sign) {
		DecimalFormat df;
		df = new DecimalFormat((sign.length() > 0 ? sign+" " : "")+"#,##0.00");
		return df.format(amount);
	}
	
	public static void createNonCancellableAcceptOrCancelDialog(Context context, String title, String message, String acceptButtonText, String cancelButtonText, final Runnable onAccept, final Runnable onCancel ){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton(acceptButtonText, new DialogInterface.OnClickListener() {
		  @Override
		  public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    if (onAccept != null)
		    	onAccept.run();
		  }
		});
		
		builder.setNegativeButton(cancelButtonText, new DialogInterface.OnClickListener() {
			  @Override
			  public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			    if (onCancel != null)
			    	onCancel.run();
			  }
			});
		AlertDialog alert = builder.create();
		alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
			@Override
		    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		        if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
		            return true; // Pretend we processed it
		        }
		        return false; // Any other keys are still processed as normal
		    }
		});
		alert.show();
	}
	
	public static void createCancellableAcceptDialog(Context context, String title, String message, String acceptButtonText, final Runnable onAccept ){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setInverseBackgroundForced(true);
		builder.setPositiveButton(acceptButtonText, new DialogInterface.OnClickListener() {
		  @Override
		  public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    if (onAccept!= null)
		    	onAccept.run();
		  }
		});
		AlertDialog alert = builder.create();
		alert.show();
	}
	private static boolean cancelableProgressShouldFinish = true;
	public static void doAsyncWithCancelableProgressDialog(final Activity context, final String progressTitle, final String progressMessage, final Runnable task, final Runnable onFinish, final Runnable onCancel, final Runnable onProgressUpdate, final Runnable onPreExecute, final boolean leaveTaskRunning){

		cancelableProgressShouldFinish = true;
		Runnable cancelCallback = new Runnable() {
			
			@Override
			public void run() {
				if (leaveTaskRunning)
					return;
				cancelableProgressShouldFinish = false;
				
				if (onCancel != null)
					onCancel.run();
			}
		};
		final CustomProgressDialog customProgressDialog = new CustomProgressDialog(context);
		customProgressDialog.progressDialog = CustomProgressDialog.showCancelable(context, progressTitle, progressMessage, cancelCallback);
		
//		customProgressDialog.progressDialog = CustomProgressDialog.show(context, progressTitle, progressMessage);
//		customProgressDialog.progressDialog.setCancelable(true);
//		customProgressDialog.progressDialog.setOnKeyListener(null);
		
		Runnable onTaskFinish = new Runnable() {
			@Override
			public void run() {
				if (!cancelableProgressShouldFinish)
					return;
				customProgressDialog.dismiss();
				if (onFinish!= null)
					onFinish.run();
				
			}
		};
		doAsync(context, task, onTaskFinish, null, onProgressUpdate, onPreExecute);		
	}
	
	
	public static void doAsyncWithCancelableProgressDialog(final Activity context, final String progressTitle, final String progressMessage, final Runnable task, final Runnable onFinish, final Runnable onCancel, final Runnable onProgressUpdate, final Runnable onPreExecute){

		cancelableProgressShouldFinish = true;
		Runnable cancelCallback = new Runnable() {
			
			@Override
			public void run() {
				cancelableProgressShouldFinish = false;
				
				if (onCancel != null)
					onCancel.run();
			}
		};
		final CustomProgressDialog customProgressDialog = new CustomProgressDialog(context);
		customProgressDialog.progressDialog = CustomProgressDialog.showCancelable(context, progressTitle, progressMessage, cancelCallback);
		
//		customProgressDialog.progressDialog = CustomProgressDialog.show(context, progressTitle, progressMessage);
//		customProgressDialog.progressDialog.setCancelable(true);
//		customProgressDialog.progressDialog.setOnKeyListener(null);
		
		Runnable onTaskFinish = new Runnable() {
			@Override
			public void run() {
				if (!cancelableProgressShouldFinish)
					return;
				customProgressDialog.dismiss();
				if (onFinish!= null)
					onFinish.run();
				
			}
		};
		doAsync(context, task, onTaskFinish, null, onProgressUpdate, onPreExecute);		
	}
	
	public static void doAsyncWithNonCancelableProgressDialog(final Activity context, final String progressTitle, final String progressMessage, final Runnable task, final Runnable onFinish, final Runnable onCancel, final Runnable onProgressUpdate, final Runnable onPreExecute){
		final CustomProgressDialog customProgressDialog = new CustomProgressDialog(context);
		customProgressDialog.progressDialog = CustomProgressDialog.show(context, progressTitle, progressMessage);
		Runnable onTaskFinish = new Runnable() {
			@Override
			public void run() {
				customProgressDialog.dismiss();
				if (onFinish!= null)
					onFinish.run();
				
			}
		};
		doAsync(context, task, onTaskFinish, onCancel, onProgressUpdate, onPreExecute);		
	}
	
	public static void doAsync (Context context, final Runnable task, final Runnable onFinish, final Runnable onCancel, final Runnable onProgressUpdate, final Runnable onPreExecute){
		AsyncTask<String, Void, String> asyncTask = new AsyncTask<String, Void, String>() {
			
			@Override
			protected String doInBackground(String ...params) {
				task.run();
				return null;
			}
	
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (onPreExecute!= null)
					onPreExecute.run();
			}
	
			@Override
			protected void onProgressUpdate(Void... values) {
				super.onProgressUpdate(values);
				if (onProgressUpdate!= null)
					onProgressUpdate.run();
			}
	
			@Override
			protected void onCancelled() {
				super.onCancelled();
				if (onCancel!= null)
					onCancel.run();
			}
	
			@Override
			protected void onPostExecute(String result) {
				super.onPostExecute(result);
				if (onFinish != null)
					onFinish.run();
			}
		};
		
		asyncTask.execute(new String ());
	}
	
	public static String dateToString(Date d){
		String date;
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyy-MM-dd");
		date = formatter.format(d);
		return date;
	}
	
	public static String makeTimestamp(Date d){
		String date;
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("yyyyMMddhhmmss");
		date = formatter.format(d);
		return date;
	}
	
	public static String dateToString_MMddyyyy(Date d){
		String date;
		SimpleDateFormat formatter;
		formatter = new SimpleDateFormat("MM-dd-yyyy");
		date = formatter.format(d);
		return date;
	}
	
	public static Date stringMM_dd_yyyyToDate(String aStrDate) {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    	try {
    		return dateFormat.parse(aStrDate);
    	} catch (java.text.ParseException ex) {
    		return null;
    	}
    }
	
	public static void addDelimitersAndLimitToEditText (final EditText eText, final int limit){
		char delimiters [] = {'=', '^', '\\' , '|' };
		HelperTool.addDelimitersAndLimitToEditText(eText, limit, delimiters);
	}
	
	public static boolean isNetworkAvailable(Context c){
		ConnectivityManager conManager = (ConnectivityManager) c
		.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conManager.getActiveNetworkInfo() != null) {
			return true;
		} else {
			HelperTool.createCancellableAcceptDialog(c, "Connection Manager", "Network connection unavailable.", "OK", null);
			return false;
		}
		
	}
	//http://dzone.com/snippets/store-image#related
	public static boolean StoreByteImage(Context mContext, byte[] imageData,
			int quality, String expName) {

                File sdImageMainDirectory = new File("/sdcard/myImages");
		FileOutputStream fileOutputStream = null;
		String nameFile = "";
		try {

			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize = 5;
			
			Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0,
					imageData.length,options);

			
			fileOutputStream = new FileOutputStream(
					sdImageMainDirectory.toString() +"/" + nameFile + ".jpg");
							
  
			BufferedOutputStream bos = new BufferedOutputStream(
					fileOutputStream);

			myImage.compress(CompressFormat.JPEG, quality, bos);

			bos.flush();
			bos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	//FROM ANDROID SNIPPETS (http://www.androidsnippets.com/multipart-http-requests)
	public static String multipartRequest(String urlTo, String post, String filepath, String filefield) throws ParseException, IOException {
		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;
		InputStream inputStream = null;
		
		String twoHyphens = "--";
		String boundary =  "*****"+Long.toString(System.currentTimeMillis())+"*****";
		String lineEnd = "\r\n";
		
		String result = "";
		
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1*1024*1024;
		
		String[] q = filepath.split("/");
		int idx = q.length - 1;
		
		try {
			File file = new File(filepath);
			FileInputStream fileInputStream = new FileInputStream(file);
			
			URL url = new URL(urlTo);
			connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("User-Agent", "Android Multipart HTTP Client 1.0");
			connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
			
			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + q[idx] +"\"" + lineEnd);
			outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
			outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
			outputStream.writeBytes(lineEnd);
			
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			while(bytesRead > 0) {
				outputStream.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}
			
			outputStream.writeBytes(lineEnd);
			
			// Upload POST Data
			String[] posts = post.split("&");
			int max = posts.length;
			for(int i=0; i<max;i++) {
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				String[] kv = posts[i].split("=");
				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + kv[0] + "\"" + lineEnd);
				outputStream.writeBytes("Content-Type: text/plain"+lineEnd);
				outputStream.writeBytes(lineEnd);
				outputStream.writeBytes(kv[1]);
				outputStream.writeBytes(lineEnd);
			}
			
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			
			inputStream = connection.getInputStream();
			result = convertStreamToString(inputStream);
			
			fileInputStream.close();
			inputStream.close();
			outputStream.flush();
			outputStream.close();
			
			return result;
		} catch(Exception e) {
			Log.e("MultipartRequest","Multipart Form Upload Error");
			e.printStackTrace();
			return "error";
		}
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public static String parseMWDate (String unparsedDate){
		String cleaned = unparsedDate.replace("\\/", "-");
//		HelperTool.trace(cleaned, cleaned);
		String splitted [] = cleaned.split("\\/");
//		HelperTool.trace(cleaned, splitted.length + "");
		//"2\/14\/2013"
//		HelperTool.trace(cleaned, splitted[0] + "");
//		HelperTool.trace(cleaned, splitted[1] + "");
//		HelperTool.trace(cleaned, splitted[2] + "");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.YEAR, Integer.parseInt(splitted[2]));
		cal.set(Calendar.MONTH, Integer.parseInt(splitted[0]) - 1);
		cal.set(Calendar.DATE, Integer.parseInt(splitted[1]));
//		cal.set(Calendar., value)
		return dateToString(cal.getTime());
		
	}
	static Pattern ptr = Pattern.compile("(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*))*)?;\\s*)");
	
	public static boolean validateEmail(String emailAddress){
		return (ptr.matcher(emailAddress).matches());
	}
}
