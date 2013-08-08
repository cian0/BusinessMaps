package com.example.mapssamplefor247.utils.dialogs;

import com.example.mapssamplefor247.utils.HelperTool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;

public class CustomProgressDialog{
	private Activity context;
	public ProgressDialog progressDialog;
	private Runnable onCancel = null;
	public CustomProgressDialog(Activity context, Runnable onCancel) {
		progressDialog = new ProgressDialog(context);
		this.context = context;
		this.onCancel = onCancel;
		// TODO Auto-generated constructor stub
		
	}
	
	public static ProgressDialog showCancelable(Activity context, CharSequence title, CharSequence message, final Runnable onCancel){
		if (!context.isFinishing() )
			return ProgressDialog.show(context, title, message, true, (onCancel != null), new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					if (onCancel != null)
						onCancel.run();
				}
			});
		
		else
			return null;
	}
	public CustomProgressDialog(Activity context) {
		progressDialog = new ProgressDialog(context);
		this.context = context;
		// TODO Auto-generated constructor stub
		
	}
	public void setCancelable(boolean cancelable){
		progressDialog.setCancelable(cancelable);
		if (!cancelable){
			
			progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
			    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			        if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
			            return true; // Pretend we processed it
			        }
			        if (keyCode == KeyEvent.KEYCODE_BACK){
			        	if (onCancel != null)
			        		onCancel.run();
			        }
			        return false; // Any other keys are still processed as normal
			    }
			});
		}else{
			progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface arg0) {
					// TODO Auto-generated method stub
					
				}
			});
			progressDialog.setOnKeyListener(null);
		}
	}
	public static ProgressDialog show (Activity context, CharSequence title, CharSequence message) {	
		if (!context.isFinishing() ){
			ProgressDialog p =ProgressDialog.show(context, title, message);
			p.setCancelable(true);
			p.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
			    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			        if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
			            return true; // Pretend we processed it
			            
			        }
			        return false; // Any other keys are still processed as normal
			    }
			});
			return p;
		}
		return null;
	}
	public static ProgressDialog show (Activity context, CharSequence title, CharSequence message, final Runnable onCancel) {	
		if (!context.isFinishing() ){
			ProgressDialog p =ProgressDialog.show(context, title, message);
			if (onCancel != null)
				p.setCancelable(true);
			p.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
			    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
			        if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
			            return true; // Pretend we processed it
			            
			        }
			        if (keyCode == KeyEvent.KEYCODE_BACK){
			        	HelperTool.trace(this, "Canceling progress dialog");
			        	if (onCancel != null)
			        		onCancel.run();
			        }
			        return false; // Any other keys are still processed as normal
			    }
			});
			return p;
		}
		return null;
	}
	public void show() {
		// TODO Auto-generated method stub
		if (!context.isFinishing() && progressDialog!= null)
			progressDialog.show();
	}
	
	public void dismiss() {
		if (!context.isFinishing() && progressDialog!= null)
			progressDialog.dismiss();
	}
}