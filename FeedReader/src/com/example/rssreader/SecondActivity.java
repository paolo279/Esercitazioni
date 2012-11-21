package com.example.rssreader;




import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class SecondActivity extends RssReader {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//Settiamo come layout "second".
		
		setContentView(R.layout.second);
		
		// Otteniamo i riferimenti alle textview.
		
		TextView titleView = (TextView) findViewById(R.id.titolo);
		TextView dateView = (TextView) findViewById(R.id.data); 
		TextView linkView = (TextView) findViewById(R.id.descrizione);
		
		// Creiamo un array di stringhe con i dati passati dalla main activity.
		
		String[] news= getIntent().getExtras().getStringArray("notizia");
		
		// Impostiamo il testo delle varie Textview con i dati dell'array.
		
		titleView.setText(news[0]);
		dateView.setText(news[1]);
		linkView.setText("Leggi la news: "+news[2]);
		
		// per il link utiliziamo il metodo addLinks della classe Linkify che al tocco
		// lancerà il browser e aprirà il sito linkato.
		
		Linkify.addLinks(linkView, Linkify.WEB_URLS);
		
			
			
	}

}
