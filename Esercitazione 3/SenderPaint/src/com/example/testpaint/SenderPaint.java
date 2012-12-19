package com.example.testpaint;






import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;




public class SenderPaint extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(new MyView(this));
		}



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_prova_paint, menu);
        return true;
    }
}
