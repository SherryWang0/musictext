package com.example.musictext;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter
{
	private List<Songs> list;
	private Context context;
	public MyAdapter(List<Songs> list, Context context)
	{
		this.list = list;
		this.context = context;
	}
	@Override
	public int getCount()
	{
		return list.size();
	}
	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View v;
		if(convertView == null)
			v = ((LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_layout, null);
		else
			v = convertView;
		TextView listName = (TextView)v.findViewById(R.id.list_name);
		TextView listSinger = (TextView)v.findViewById(R.id.list_singer);
		TextView listTime = (TextView)v.findViewById(R.id.list_time);
		String name = list.get(position).getTitle() + "-" + list.get(position).getAlbum();
		listName.setText(name);
		listSinger.setText(list.get(position).getArtist());
		listTime.setText(list.get(position).getDuration()+"");
		return v;
	}

}
