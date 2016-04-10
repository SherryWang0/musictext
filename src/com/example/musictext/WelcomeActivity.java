package com.example.musictext;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class WelcomeActivity extends Activity
{
	private final long LENGTH = 1000;
	Handler handler = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome_layout);
		handler.postDelayed(new Runnable()
		{
			@Override
			public void run()
			{
				Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		}, LENGTH);
	}
}
