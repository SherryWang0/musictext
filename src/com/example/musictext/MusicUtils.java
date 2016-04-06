package com.example.musictext;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

public class MusicUtils
{
	private Context context;
	
	public MusicUtils(Context context)
	{
		this.context = context;
	}

	public List<Songs> getAllMusic()
	{
		Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		List<Songs> list = new ArrayList<Songs>();
		while(cursor.moveToNext())
		{
			Songs song = new Songs();
			song.setAlbum(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
			song.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
			song.setData(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
			song.setDisplay_name(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)));
			song.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
			song.setId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
			song.setSize(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)));
			song.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
			song.setYear(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.YEAR)));
			list.add(song);
		}
		return list;
	}
}
