package com.example.testpaint;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;


public class MyView extends View {


	// Creo il paint e il path da utilizzare nel metodo drawPath del Canvas
    private Paint paint = new Paint(); 
    private Path path = new Path();
    
    // variabile di connessione XMPP
    private XMPPConnection connection; 
    


    
    public MyView(Context context) {
		super(context);
		
		connessioneXMPP(); // Richiamo il metodo per far partire la connessione	
    }


    @Override
    protected void onDraw(Canvas canvas) { // Nel metodo onDraw imposto il paint con i vari setting: colore, stile...ecc.
    	 	paint.setAntiAlias(true);
    	 	paint.setColor(Color.BLUE);
    	 	paint.setStyle(Paint.Style.STROKE);
    	 	paint.setStrokeJoin(Paint.Join.ROUND);
    	 	paint.setStrokeWidth(3f);
            canvas.drawPath(path, paint); //nel metodo drawPath del canvas dò come riferimento i due oggetti creati di tipo Paint e Path
    }

    
    @Override
    public boolean onTouchEvent(MotionEvent event) { //imposto l'evento di onTouch
            float eventX = event.getX(); //prendo le coordinate X e Y del tocco
            float eventY = event.getY();
            switch (event.getAction()) { 
            case MotionEvent.ACTION_DOWN: 
                    path.moveTo(eventX, eventY);
                    sentMove(eventX,eventY);
                    // se l'azione è di tipo ACTION_DOWN sposto il path con moveTo
                    // e invio le coordinate alla seconda app con il metodo sentMove
                    return true;

            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                    // In questo modo imposto un ciclo for dove vengono memorizzate
                    // tutte le coordinate che vengono toccate, faccio impostare al path
                    // un lineTo e invio tutte le coordinate con sentLine alla seconda app
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                            float historicalX = event.getHistoricalX(i);
                            float historicalY = event.getHistoricalY(i);
                            path.lineTo(historicalX, historicalY);
                            sentLine(historicalX, historicalY);
                    }

                    // Dopo aver memorizzato la storia invio l'ultimo lineTo e sentLine con le prime coordinate di tocco.
                    path.lineTo(eventX, eventY);
                    sentLine(eventX,eventY);
                    break;

            }

            // a questo punto posso invocare l'onDraw grazie al metodo invalidate
            invalidate();


            return true;
    }

    //con questo metodo imposto la connessione al server XMPP come visto nell'esercizio svolto in aula	
    public void connessioneXMPP(){
    	
    	new Thread(){
    		public void run() {
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
    				connection.login("colucci", "qwerty"); // la mia username e pw
    			} catch (XMPPException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
    		}
    	}.start();
    	
    }
    
    //metodo per inviare le coordinate per il moveTo alla seconda app
    public void sentMove(float x, float y){  
    	
    	Message msg = new Message(); 
    	msg.setTo("all@broadcast.ppl.eln.uniroma2.it");
    	msg.setBody("move:"+x+"/"+y); 
    	connection.sendPacket(msg);
    	//oltre alle coordinate aggiungo inizialmente una stringa "move:"
    	//per far capire alla seconda app che metodo deve invocare
    }
    
    //metodo per inviare le coordinate per il lineTo alla seconda app simile al precedente
    public void sentLine(float x, float y){
    
    	Message msg = new Message();
    	msg.setTo("all@broadcast.ppl.eln.uniroma2.it");
    	msg.setBody("line:"+x+"/"+y);
      	connection.sendPacket(msg);
    }
   


}


