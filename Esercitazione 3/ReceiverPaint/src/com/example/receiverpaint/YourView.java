package com.example.receiverpaint;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.view.View;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

public class YourView extends View {
	
    
    private XMPPConnection connection; // variabile di connessione XMPP
	private Paint paint = new Paint();
    private Path path = new Path();
    private Handler myH = new Handler(); //oggetto handler per il thread
    
    // creo un oggetto Runnable con all'interno della run il metodo invalidate() da ripetere ogni secondo
   private Runnable runnable = new Runnable() {
        public void run() 
        { 
                invalidate();
                myH.postDelayed(this,1000);
        }
    };
    
    
    public YourView(Context context) {
		super(context);
		connessioneXMPP(); // Eseguo il metodo per connettermi al server XMPP
		gestisciMsg(); // Eseguo il metodo per gestire le coordinate ricevute
		
		myH.postDelayed(runnable, 1000); // esegue il thread di aggiornamento ogni secondo

    }
    
    
    
    protected void onDraw(Canvas canvas){ // metodo onDraw come per l'App SenderPaint
    	paint.setAntiAlias(true);
	 	paint.setColor(Color.RED);
	 	paint.setStyle(Paint.Style.STROKE);
	 	paint.setStrokeJoin(Paint.Join.ROUND);
	 	paint.setStrokeWidth(3f);
        canvas.drawPath(path, paint);
    }
    
    // metodo per la connessione al server XMPP. Facendo delle prove ho notato che facendolo partire con un nuovo thread
    // funziona sui dispositivi con Antdroid 4.0 ma non sugli emulatori 2.2, al contrario assegnandolo al main thread 
    // accade esattamente l'opposto. Visto che stiamo sviluppando principalmente su Android 2.2 ho preferito lasciare 
    // il metodo nel main thread, commentando le righe che servono per farlo partire in un nuovo thread !
    public void connessioneXMPP(){  
    	//new Thread(){
    		//public void run() {
    			// TODO Auto-generated method stub
    			ConnectionConfiguration config = new ConnectionConfiguration("ppl.eln.uniroma2.it",5222);
    	        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
    	        connection = new XMPPConnection(config);
    	        
    	        try {
    				connection.connect();
    			} catch (XMPPException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    			}
    	        
    	        try {
    				connection.login("fioravanti", "qwerty"); // la mia username e pw
    			} catch (XMPPException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				
    			}
    		//}
    	//}.start();
    }
    
    // Creo un metodo per gestire i dati inviati da SenderPaint, anche qui con codice commentato per Android 4.0
    public void gestisciMsg(){
    	
    	//new Thread(){
			//public void run(){
    	connection.addPacketListener(new PacketListener(){
			@SuppressLint("UseValueOf")
			public void processPacket(Packet arg0) {
				final Message msg = (Message) arg0; // esegue il casting
				final String s = msg.getBody(); // salva il corpo del mess in una strina
				
				//se inizia per "move:" prende i valori delle coordinate ed esegue il metodo moveTo
				if(s.startsWith("move:")){                	
					String xy = s.substring(5);			    
					String[] co = xy.split("/");	
					final Float moveX = Float.valueOf(co[0]);
					final Float moveY = Float.valueOf(co[1]);
					path.moveTo(moveX, moveY);

				
				//se inizia per "line:" prende i valori delle coordinate ed esegue il metodo lineTo	
				} else if(s.startsWith("line:")){
					String xy = s.substring(5);
					String[] co = xy.split("/");
					final Float lineX = Float.valueOf(co[0]);
					final Float lineY = Float.valueOf(co[1]);
					path.lineTo(lineX, lineY);
					}

				}
			
		}, new MessageTypeFilter(Message.Type.normal));	
			//}
		//}.start();
    }
    
}
