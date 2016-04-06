package com.example.musictext;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OneMusicActivity extends Activity
{
	private ImageButton returnButton;
	private TextView oneName;
	private TextView oneAuthor;
	private RelativeLayout singLayout;
	private ImageView disk;
	private ImageView pointer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.one_music_layout);
		Intent intent = getIntent();
		List<Songs> list = (List<Songs>)intent.getSerializableExtra("list");
		int position = intent.getIntExtra("position", 0);
	}

}
