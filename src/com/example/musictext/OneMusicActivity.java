package com.example.musictext;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class OneMusicActivity extends Activity
{
	private ImageButton returnButton;
	private TextView oneName;
	private TextView oneAuthor;
	private RelativeLayout singLayout;
	private ImageView disk;
	private ImageView pointer;
	private ImageButton playLove;
	private ImageButton playWay;
	private ImageButton playLast;
	private ImageButton playStart;
	private ImageButton playNext;
	private TextView currentTime;
	private TextView allTime;
	private SeekBar seekBar;
	private List<Songs> list;
	private int lastPosition;
	private int position = -1;
	protected MediaPlayer media;
	private MyBroadcast mbc;
	
	private int way = 0;
	private boolean isLove;
	private boolean isPlay;
	private boolean isStop = false;
	private boolean lastState;
	private int pro;
	private int state = -1;
	
	public static final int PLAY_POSITION = 0;
	public static final int IS_PLAY = 1;
	public static final int LAST_SONG = 2;
	public static final int NEXT_SONG = 3;
	public static final int PLAY_WAY = 4;
	public static final int PLAY_LOVE = 5;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.one_music_layout);
		Intent intent = getIntent();
		list = (List<Songs>)intent.getSerializableExtra("list");
		position = intent.getIntExtra("position", -1);
		init();
		getValue(position);
		MyListener myListener = new MyListener();
		playStart.setOnClickListener(myListener);
		playNext.setOnClickListener(myListener);
		playLast.setOnClickListener(myListener);
		returnButton.setOnClickListener(myListener);
		playLove.setOnClickListener(myListener);
		playWay.setOnClickListener(myListener);
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
			{
				if(fromUser)
				{
					seekBar.setProgress(progress);
					pro = progress;
					state = PLAY_POSITION;
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
			}
		});
		singLayout.setOnTouchListener(new OnTouchListener()
		{
			float x1, x2;
			@Override
			public boolean onTouch(View v, MotionEvent event)
			{
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					x1 = event.getX();
				//实现点击出现歌词的功能	
					return true;
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					x2 = event.getX();
					if(x2 > x1)
						state = LAST_SONG;
					else if(x2 <x1)
						state = NEXT_SONG;
					return true;
				}
				else
					return false;
			}
		});
		sendBroad();
	
		mbc = new MyBroadcast();
		IntentFilter ifi = new IntentFilter("com.example.musictext.maintoone");
		registerReceiver(mbc, ifi);
	}
	class MyListener implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
			case R.id.play_start:
				state = IS_PLAY;
				break;
			case R.id.play_next:
				state = NEXT_SONG;
				break;
			case R.id.play_last:
				state = LAST_SONG;
				break;
			case R.id.return_button:
				onBackPressed();
				break;
			case R.id.play_love:
				isLove = !isLove;
				list.get(position).setLove(isLove);
				if (isLove)
					playLove.setImageResource(R.drawable.play_icn_loved_prs);
				else
					playLove.setImageResource(R.drawable.play_icn_love);
				state = PLAY_LOVE;
				break;
			case R.id.play_way:
				if(way == 0)
				{
					playWay.setImageResource(R.drawable.play_icn_one);
					way = 1;
				}
				else if(way == 1)
				{
					playWay.setImageResource(R.drawable.play_icn_loop);
					way = 0;
				}
				state = PLAY_WAY;
				break;
			default:
				break;
			}
		}
	}
	private void sendBroad()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(!isStop)
				{
					if(state != -1)
					{
						Intent intent = new Intent("com.example.musictext.onetomain");
						intent.putExtra("state", state);
						if(state == PLAY_LOVE)
							intent.putExtra("love", isLove);
						else if(state == PLAY_WAY)
							intent.putExtra("way", way);
						else if(state == PLAY_POSITION)
							intent.putExtra("pro", pro);
						sendBroadcast(intent);
						state = -1;
					}
				}
			}
		}).start();
	}

	private void getValue(int position)
	{
		oneName.setText(list.get(position).getTitle());
		oneAuthor.setText(list.get(position).getArtist());
		allTime.setText(list.get(position).getTime());
		seekBar.setMax(list.get(position).getDuration());
		isLove = list.get(position).isLove();
		if (isLove)
			playLove.setImageResource(R.drawable.play_icn_loved_prs);
		else
			playLove.setImageResource(R.drawable.play_icn_love);
	}

	private void init()
	{
		returnButton = (ImageButton) findViewById(R.id.return_button);
		oneName = (TextView) findViewById(R.id.one_name);
		oneAuthor = (TextView) findViewById(R.id.one_author);
		singLayout = (RelativeLayout) findViewById(R.id.sing_layout);
		disk = (ImageView) findViewById(R.id.disk);
		pointer = (ImageView) findViewById(R.id.pointer);
		playLove = (ImageButton) findViewById(R.id.play_love);
		playWay = (ImageButton) findViewById(R.id.play_way);
		playLast = (ImageButton) findViewById(R.id.play_last);
		playStart = (ImageButton)findViewById(R.id.play_start);
		playNext = (ImageButton)findViewById(R.id.play_next);
		currentTime = (TextView)findViewById(R.id.current_time);
		allTime = (TextView)findViewById(R.id.all_time);
		seekBar = (SeekBar)findViewById(R.id.seek_bar);
		seekBar.setEnabled(true);
	}
	
	class MyBroadcast extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			lastPosition = position;
			position = intent.getIntExtra("id", -1);
			if(lastPosition != position)
				getValue(position);
			pro = intent.getIntExtra("progress", 0);
			lastState = isPlay;
			isPlay = intent.getBooleanExtra("isplay", false);
			if(lastState && !isPlay)
			{
				changeImage1();
			}
			else if(!lastState && isPlay)
				changeImage2();
			
			seekBar.setProgress(pro);
			currentTime.setText(getTime(pro));
			if(isPlay)
			{
				playStart.setImageResource(R.drawable.desk_pause);
			}
			else
			{
				playStart.setImageResource(R.drawable.desk_play);
			}
		}
	}
	private String getTime(int duration)
	{
		int all = duration / 1000;
		int mi = all / 60;
		int se = all % 60;
		if(se < 10)
			return mi + ":0" + se;
		else
			return mi + ":" + se;
	}
	public void changeDisk()
	{
		AnimationSet aSet = new AnimationSet(true);
		RotateAnimation aRotateDisk = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		aRotateDisk.setDuration(1000);
		aRotateDisk.setFillAfter(true);
		aSet.addAnimation(aRotateDisk);
		disk.setAnimation(aSet);
	}

	private void changeImage1()
	{
		AnimationSet aSet = new AnimationSet(true);
		RotateAnimation aRotatePointer = new RotateAnimation(0, 25, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		aRotatePointer.setDuration(500);
		aRotatePointer.setFillAfter(true);
		aSet.addAnimation(aRotatePointer);
		pointer.setAnimation(aSet);
		
	}
	private void changeImage2()
	{
		AnimationSet aSet = new AnimationSet(true);
		RotateAnimation aRotatePointer = new RotateAnimation(0, -25, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
		aRotatePointer.setDuration(500);
		aRotatePointer.setFillAfter(true);
		aSet.addAnimation(aRotatePointer);
		pointer.setAnimation(aSet);
	}
	@Override
	public void onBackPressed()
	{
		isStop = true;
		super.onBackPressed();
	}
}
