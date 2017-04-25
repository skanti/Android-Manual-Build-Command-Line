package com.example.hellojni;

import android.util.Log;
import android.widget.TextView;
import android.app.Activity;
import android.os.Bundle;

public class HelloJni extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TextView  tv = new TextView(this);
        tv.setText( stringFromJNI() );
        setContentView(tv);
    }
	
    public native String  stringFromJNI();
	
    static {
		Log.i("amo", "Trying to load shared library!");
        System.loadLibrary("hello");
    }
}
