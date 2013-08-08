package com.example.mapssamplefor247.views;


import com.example.mapssamplefor247.R;
import com.example.mapssamplefor247.dao.Business;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

public class BusinessInfoDialog extends Dialog{
	
	public BusinessInfoDialog(final Context context, Business business) {
		super(context);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.place_info_dialog);
		
		TextView name = (TextView) findViewById(R.id.textViewName);
		TextView category = (TextView) findViewById(R.id.textViewCategory);
		final TextView phone = (TextView) findViewById(R.id.textViewPhone);
		TextView address = (TextView) findViewById(R.id.textViewAddress);
		TextView city = (TextView) findViewById(R.id.TextViewCity);
		TextView state = (TextView) findViewById(R.id.textViewState);
		TextView zip = (TextView) findViewById(R.id.textViewZip);
		RatingBar rating = (RatingBar) findViewById(R.id.ratingBar);
		
		name.setText(business.getName());
		category.setText(business.getCategory());
		phone.setText(business.getPhone());
		address.setText(business.getLocation().getAddress());
		city.setText(business.getLocation().getCity());
		state.setText(business.getLocation().getState());
		zip.setText(business.getLocation().getZip());
		
		phone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent callIntent = new Intent(Intent.ACTION_CALL);
				callIntent.setData(Uri.parse("tel:+"+phone.getText().toString().trim()));
				context.startActivity(callIntent);
			}
		});
		rating.setRating(business.getRating());
		
	}
	
}
