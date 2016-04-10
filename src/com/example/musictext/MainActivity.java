package com.example.musictext;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private RelativeLayout layout;
	private ProgressBar progressBar;
	private ImageView image;
	private TextView songName;
	private TextView singer;
	private ImageButton next;
	private ImageButton start;
	private ImageButton last;
	private ListView listView;
	private List<Songs> list;
	private MusicUtils utils;
	private MyAdapter adapter;
	private MediaPlayer mediaPlayer = new MediaPlayer();
	private MyHandler handler;
	private int p;
	private boolean isStop = false; 
	private int way = 0;
	private MyBroadcast mbc;
	private IntentFilter ifi;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_layout);
		layout = (RelativeLayout)findViewById(R.id.layout);
		progressBar = (ProgressBar)findViewById(R.id.progress_bar);
		image = (ImageView)findViewById(R.id.image);
		songName = (TextView)findViewById(R.id.song_name);
		singer = (TextView)findViewById(R.id.singer);
		next = (ImageButton)findViewById(R.id.next);
		start = (ImageButton)findViewById(R.id.start);
		last = (ImageButton)findViewById(R.id.last);
		listView = (ListView)findViewById(R.id.list_view);
		utils = new MusicUtils(this);
		list = utils.getAllMusic();
		adapter = new MyAdapter(list, this);
		listView.setAdapter(adapter);
		songName.setText(list.get(0).getTitle());
		singer.setText(list.get(0).getArtist());
		progressBar.setMax(list.get(0).getDuration());
		start(0);
		handler = new MyHandler();
		mediaPlayer.pause();
		MyListener l = new MyListener(0);
		layout.setOnClickListener(l);
		next.setOnClickListener(l);
		last.setOnClickListener(l);
		start.setImageResource(R.drawable.playbar_btn_play);
		start.setOnClickListener(l);
		listView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id)
			{
				p = position;
				songName.setText(list.get(position).getTitle());
				singer.setText(list.get(position).getArtist());
				progressBar.setMax(list.get(position).getDuration());
				MyListener l = new MyListener(p);
				layout.setOnClickListener(l);
				next.setOnClickListener(l);
				last.setOnClickListener(l);
				start.setOnClickListener(l);
				try
				{
					if(mediaPlayer.isPlaying())
						stop();
					start(position);
				}catch(IllegalArgumentException e)
				{
					e.printStackTrace();
				}catch(SecurityException e)
				{
					e.printStackTrace();
				}catch(IllegalStateException e)
				{
					e.printStackTrace();
				}
			}
		});
		listView.setOnItemLongClickListener(new MyLongListener(MainActivity.this, list, adapter));
		sendBroad();
		mbc = new MyBroadcast();
		ifi = new IntentFilter("com.example.musictext.onetomain");
		registerReceiver(mbc, ifi);
	}
	class MyBroadcast extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			int state = intent.getIntExtra("state", -1);
			if(state == OneMusicActivity.IS_PLAY)
				buttonStart(p);
			else if(state == OneMusicActivity.NEXT_SONG)
				next(p);
			else if(state == OneMusicActivity.LAST_SONG)
				last(p);
			else if(state == OneMusicActivity.PLAY_LOVE)
				list.get(p).setLove(intent.getBooleanExtra("love", false));
			else if(state == OneMusicActivity.PLAY_WAY)
				way = intent.getIntExtra("way", 0);
			else if(state == OneMusicActivity.PLAY_POSITION)
				mediaPlayer.seekTo(intent.getIntExtra("pro", 0));
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
					int pro = progressBar.getProgress();
					boolean isPlay = mediaPlayer.isPlaying();
					Intent intent = new Intent("com.example.musictext.maintoone");
					intent.putExtra("progress", pro);
					intent.putExtra("id", p);
					intent.putExtra("isplay", isPlay);
					sendBroadcast(intent);
				}
			}
		}).start();
	}
	class MyListener implements OnClickListener
	{
		private int p;
		public MyListener()
		{
			super();
		}
		public MyListener(int p)
		{
			this.p = p;
		}
		@Override
		public void onClick(View v)
		{
			
			switch(v.getId())
			{
			case R.id.next:
				next(p);
				break;
			case R.id.start:
				buttonStart(p);
				break;
			case R.id.last:
				last(p);
				break;
			case R.id.layout:
				oneMusic(list, p);
				break;
			default:
				break;
			}
		}
	}
	private void stop()
	{
		start.setImageResource(R.drawable.playbar_btn_play);
		if(mediaPlayer != null)
			mediaPlayer.stop();
		try
		{
			mediaPlayer.prepare();
		}catch(IllegalStateException e)
		{
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	public void last(int p2)
	{
		try
		{
			if (mediaPlayer.isPlaying())
				stop();
			if (p2 == 0)
				p = list.size() - 1;
			else
				p = p2 - 1;
			start(p);
			songName.setText(list.get(p).getTitle());
			singer.setText(list.get(p).getArtist());
			progressBar.setMax(list.get(p).getDuration());
		}catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}catch(SecurityException e)
		{
			e.printStackTrace();
		}catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
	}
	public void next(int p2)
	{
		try
		{
			if(mediaPlayer.isPlaying())
				stop();
			if(p2 == list.size() - 1)
				p = 0;
			else
				p = p2 + 1;
			start(p);
			songName.setText(list.get(p).getTitle());
			singer.setText(list.get(p).getArtist());
			progressBar.setMax(list.get(p).getDuration());
		}catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}catch(SecurityException e)
		{
			e.printStackTrace();
		}catch(IllegalStateException e)
		{
			e.printStackTrace();
		}
	}
	public void buttonStart(int position)
	{
		if(mediaPlayer.isPlaying())
		{
			mediaPlayer.pause();
			start.setImageResource(R.drawable.playbar_btn_play);
		}
			
		else if(!mediaPlayer.isPlaying())
		{
			mediaPlayer.start();
			listen(position);
			start.setImageResource(R.drawable.playbar_btn_pause);
		}
	}
	private void oneMusic(List<Songs> list, int p)
	{
		Intent i = new Intent(MainActivity.this, OneMusicActivity.class);
		i.putExtra("list", (Serializable)list);
		i.putExtra("position", p);
		startActivity(i);
	}
//	public void next(int p)
//	{
//		if(p == list.size() - 1)
//			p = 0;
//		else
//			p = p + 1;
//		start(p);
//		Log.d("....", p + "");
//		songName.setText(list.get(p).getTitle());
//		singer.setText(list.get(p).getArtist());
//	}
	private void start(final int position)
	{
		start.setImageResource(R.drawable.playbar_btn_pause);
		try
		{
			mediaPlayer.reset();
			mediaPlayer.setDataSource(list.get(position).getData());
			mediaPlayer.prepare();
			mediaPlayer.start();
			progressBar.setMax(mediaPlayer.getDuration());
			listen(position);
		}catch(IllegalArgumentException e)
		{
			e.printStackTrace();
		}catch(SecurityException e)
		{
			e.printStackTrace();
		}catch(IllegalStateException e)
		{
			e.printStackTrace();
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	class MyHandler extends Handler
	{
		Songs s;
		@Override
		public void handleMessage(Message msg)
		{
			progressBar.setProgress(msg.what);
			if(msg.obj != null)
			{
				s = (Songs)msg.obj;
				songName.setText(s.getTitle());
				singer.setText(s.getArtist());
			}
		}
	}
	public void listen(final int position)
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				while(mediaPlayer.isPlaying())
				{
					int current = mediaPlayer.getCurrentPosition();
					Message msg = new Message();
					msg.what = current;
					if(mediaPlayer.getCurrentPosition() == mediaPlayer.getDuration())
					{
						if(way == 0)
						{
							if(position == list.size() - 1)
								p = 0;
							else
								p = position + 1;
						}
						else if(way == 1)
							p = position;
						start(p);
						last.setOnClickListener(new MyListener(p));
						next.setOnClickListener(new MyListener(p));
						layout.setOnClickListener(new MyListener(p));
						Songs song = list.get(p);
						msg.obj = song;
					}
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
	@Override
	protected void onDestroy()
	{
		
		mediaPlayer.release();
		list.clear();
		super.onDestroy();
	}
	@Override
	public void onBackPressed()
	{
		isStop = true;
		super.onBackPressed();
		onDestroy();
	}
}