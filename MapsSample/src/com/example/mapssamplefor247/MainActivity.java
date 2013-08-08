package com.example.mapssamplefor247;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.mapssamplefor247.dao.Business;
import com.example.mapssamplefor247.host.HttpPostHandler;
import com.example.mapssamplefor247.host.parsers.BusinessesParser;
import com.example.mapssamplefor247.utils.HelperTool;
import com.example.mapssamplefor247.views.BusinessInfoDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class MainActivity extends FragmentActivity implements OnInfoWindowClickListener{
	
	ArrayList<Business> businesses;
	GoogleMap map;
	HashMap<Marker, Business> businessMarkers;
	String xmlURL = "https://dl.dropboxusercontent.com/u/101222705/business.xml";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		businesses = new ArrayList<Business>();
		businessMarkers = new HashMap<Marker, Business>();
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                .getMap();
		map.setOnInfoWindowClickListener(this);
		if (!HelperTool.isNetworkAvailable(this))
		{
			HelperTool.createCancellableAcceptDialog(this, getString(R.string.app_title), getString(R.string.alert_no_network), "Ok", null);
			return;
		}
		HelperTool.doAsyncWithNonCancelableProgressDialog(this, getString(R.string.app_title), getString(R.string.dialog_loading_xml), new Runnable() {
			
			@Override
			public void run() {
				String xmlResponse = HttpPostHandler.doHTTPRequest(xmlURL);
				
				BusinessesParser parser = new BusinessesParser(xmlResponse);
				businesses = parser.getBusinesses();
			}
		}, new Runnable() {
			
			@Override
			public void run() {
				setupBusinessMarkersToMap();
				centerToFirstBusiness();
			}
		}, null, null, null);
		
	}
	
	private void setupBusinessMarkersToMap(){
		
		for (Business business: businesses){
			
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(new LatLng(business.getLocation().getLatitude(), business.getLocation().getLongitude()));
			markerOptions.title(business.getName());
			markerOptions.snippet(getString(R.string.snippet_text));
			
			Marker marker = map.addMarker(markerOptions);
			
			businessMarkers.put(marker, business);
		}
	}
	
	private void centerToFirstBusiness(){
		if (businesses.size() > 0){
			map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(businesses.get(0).getLocation().getLatitude(), businesses.get(0).getLocation().getLongitude()), 18));
			
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		Business business = businessMarkers.get(marker);		
		HelperTool.trace(this, "You've clicked " + business.getName());
		
		BusinessInfoDialog dialog = new BusinessInfoDialog(this, business);
		
		dialog.show();
		
		
	}

}
