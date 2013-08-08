package com.example.mapssamplefor247.host.parsers;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.location.Location;

import com.example.mapssamplefor247.dao.Business;
import com.example.mapssamplefor247.dao.BusinessLocation;

public class BusinessesParser {
	//xml node names
	
	/*
	 * <business>
<name>Patxi's Pizza</name>
<category>Pizza</category>
<rating>4</rating>
<location>
<address>1875 S Bascom Ave, Ste 405</address>
<city>Campbell</city>
<state>CA</state>
<zip>95008</zip>
<latitude>37.289692</latitude>
<longitude>-121.932707</longitude>
</location>
<phone>+14085590700</phone>
</business>
	 * */
	private static final String TAG_ROOT = "business";
	private static final String TAG_NAME = "name";
	private static final String TAG_CATEGORY = "category";
	private static final String TAG_RATING = "rating";
	private static final String TAG_LOCATION = "location";
	private static final String TAG_ADDRESS = "address";
	private static final String TAG_CITY = "city";
	private static final String TAG_STATE = "state";
	private static final String TAG_ZIP = "zip";
	private static final String TAG_LATITUDE = "latitude";
	private static final String TAG_LONGITUDE = "longitude";
	private static final String TAG_PHONE = "phone";
	private ArrayList<Business> businesses;
	private Business business = null;
	
	public BusinessesParser (String xml){
		businesses = new ArrayList<Business>();
		try{
			XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
			parser.setInput(new StringReader(xml));
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT){				
				switch(eventType){
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					String tagName = parser.getName();
					if (tagName.equalsIgnoreCase(TAG_ROOT)){
						business = new Business();
					}else if (tagName.equalsIgnoreCase(TAG_NAME)){
						business.setName(parser.nextText());
					}else if (tagName.equalsIgnoreCase(TAG_CATEGORY)){
						business.setCategory(parser.nextText());
					}else if  (tagName.equalsIgnoreCase(TAG_RATING)){
						business.setRating(Integer.parseInt(parser.nextText()));
					} else if (tagName.equalsIgnoreCase(TAG_LOCATION)){
						business.setLocation(new BusinessLocation());
					}else if  (tagName.equalsIgnoreCase(TAG_ADDRESS)){						
						business.getLocation().setAddress(parser.nextText());
					}else if  (tagName.equalsIgnoreCase(TAG_CITY)){
						business.getLocation().setCity(parser.nextText());
					}else if  (tagName.equalsIgnoreCase(TAG_STATE)){
						business.getLocation().setState(parser.nextText());
					}else if  (tagName.equalsIgnoreCase(TAG_ZIP)){
						business.getLocation().setZip(parser.nextText());
					}else if  (tagName.equalsIgnoreCase(TAG_LATITUDE)){						
						business.getLocation().setLatitude(Double.parseDouble(parser.nextText()));
					}else if  (tagName.equalsIgnoreCase(TAG_LONGITUDE)){
						business.getLocation().setLongitude(Double.parseDouble(parser.nextText()));
					}else if  (tagName.equalsIgnoreCase(TAG_PHONE)){
						business.setPhone(parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					String tag = parser.getName();
					if (tag.equalsIgnoreCase(TAG_ROOT)){
						businesses.add(business);
					}
					break;
				}
				eventType = parser.next();
			}
		}catch (XmlPullParserException e){
			e.printStackTrace();
			business = null;
		} catch (IOException e) {
			e.printStackTrace();
			business = null;
		}
	}

	public ArrayList<Business> getBusinesses() {
		return businesses;
	}

	
}
