package com.example.receiverpaint;



import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ReceiverPaint extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new YourView(this));
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_receiver_paint, menu);
        return true;
    }
}
