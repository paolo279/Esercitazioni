package com.example.rssreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class RssHandler extends DefaultHandler{
	boolean inTitle = false;
	boolean inLink = false;
	boolean inDate = false;
	ArrayList<String> title = new ArrayList<String>();
	ArrayList<String> link = new ArrayList<String>();
	ArrayList<String> date = new ArrayList<String>();
	int i=0;
	int j=0;
	int k=0;
	boolean inItem = false;
	boolean inEnclosure = false;
	String imageUrl = null;


	public Bitmap getImage(){
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(imageUrl).openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream is = connection.getInputStream();
			Bitmap bitmap= BitmapFactory.decodeStream(is);
			is.close();
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		//System.out.println("inizio documento");
	}
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
		//System.out.println("fine documento");
	}



	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		System.out.println("inizio elemento:"+qName);
		if(qName.equals("title")){
			inTitle=true;
		} 
		if(qName.equals("link")){
			inLink=true;
		}
		if(qName.equals("pubDate")){
			inDate=true;
		}
		else if(qName.equalsIgnoreCase("item")){
			inItem = true;
		}
		else if(qName.equalsIgnoreCase("enclosure")){
			inEnclosure = true;
		}

		if(inItem & inEnclosure){
			for (int i = 0; i < attributes.getLength(); i++){
				//System.out.println("attributo: "+attributes.getQName(i)+" valore: "+attributes.getValue(i));
				if(attributes.getQName(i).equalsIgnoreCase("url")){
					imageUrl=attributes.getValue(i);
				}

			}
		}

		for (int i = 0; i < attributes.getLength(); i++){
			//System.out.println("attributo: "+attributes.getQName(i)+" valore: "+attributes.getValue(i));

		}

	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		//System.out.println("fine elemento:"+qName);
		if(qName.equals("title")){
			inTitle=false;
		}if(qName.equals("pubDate")){
			inDate=false;
		}else if(qName.equals("link")){
			inLink=false;
		}else if(qName.equalsIgnoreCase("item")){
			inItem = false;
		}
		else if(qName.equalsIgnoreCase("enclosure")){
			inEnclosure = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		String s = new String (ch, start, length);
		System.out.println("testo:"+s);
		if (inTitle & inItem){
				title.add(s);
				i++;	
		}else if (inLink & inItem){
				link.add(s);
				j++;
		}else if (inDate & inItem){
				date.add(s);
				k++;
		}
	}
}
