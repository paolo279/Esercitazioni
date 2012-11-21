package com.example.rssreader;





import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class RssReader extends Activity {
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prova_parser);
		
		// Selezioniamo il feed xml
		
		String url= "http://corrieredellosport.feedsportal.com/c/34176/f/619144/index.rss";
		
		
		
		try {
			// Inizializiamo e lanicamo il parser  
			SAXParserFactory factory=SAXParserFactory.newInstance();
    		SAXParser parser=factory.newSAXParser();
			InputStream in = new URL(url).openStream(); //connessione http al browser
			final RssHandler handler=new RssHandler();	
			XMLReader reader = parser.getXMLReader();
			reader.setContentHandler(handler);
			reader.parse(new InputSource(in));
			
			//Facciamo il riferimento alla listview dove inserire i dati parsati
			
			ListView listView = (ListView) findViewById(R.id.ListView1);
			

			// Creiamo una mappa arraylist con il titolo e la data della news parsate
			// e le inseriamo con un ciclo for 
			
			ArrayList<HashMap<String, Object>> data= new ArrayList<HashMap<String,Object>>();
			
			for(int i=0; i< handler.title.size(); i++){
				HashMap<String, Object> datamap = new HashMap<String,Object>();
				datamap.put("title",handler.title.get(i));
				datamap.put("data", handler.date.get(i));
				data.add(datamap);
			}
			
			// creiamo i due array con i valori delle chiavi da trasferire
			// dal "from" al "to" nel simpleadapter
			String[] from = {"title","data"};
			int[] to = {R.id.textView1,R.id.textView2};
			
			
			// Facciamo l'adapter per trasferire nella ListView i titoli e le date
			// delle news, utilizzando la mappa e gli array delle chiavi create in precedenza
			
			SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),data,R.layout.rox,from,to);
			
			// settiamo l'adapter nella listview
			
			listView.setAdapter(adapter);
			
			// Impostiamo l'evento di onClik negli elementi della ListView
			
			listView.setOnItemClickListener(new OnItemClickListener() {
				
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					// Creiamo un array di stringhe in cui inseriamo i dati della TextView selezionata
					
					String news[]=new String[]{handler.title.get(arg2),handler.date.get(arg2),handler.link.get(arg2)};
					
					// Creiamo un intent per far partire la nuova Activity inserendo i dati da passargli tramite
					// il metodo putExtra()
					
					Intent intent = new Intent(RssReader.this, SecondActivity.class);
					intent.putExtra("notizia", news);
					
					// lanciamo la seconda Activity
					
					startActivity(intent);
					
				}
		});
			
		}catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_prova_parser, menu);
		return true;
	}

}
