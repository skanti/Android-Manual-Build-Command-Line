package com.example.hellojni;

import android.util.Log;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;

public class HelloJNI extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TextView  tv = new TextView(this);
        // tv.setText( stringFromJNI() );
        tv.setText("sup");
        setContentView(tv);
    }
	
  //   public native String  stringFromJNI();
	
  //   static {
		// Log.i("amo", "Trying to load shared library!");
  //       System.loadLibrary("hellojni");
  //   }
}
