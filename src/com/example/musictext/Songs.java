package com.example.musictext;

import java.io.Serializable;

public class Songs implements Serializable
{
	private int id;
	private String title;
	private String album;
	private String artist;
	private String data;
	private String display_name;
	private String year;
	private int duration;
	private int size;
	private boolean isLove;
	
	public boolean isLove()
	{
		return isLove;
	}
	public void setLove(boolean isLove)
	{
		this.isLove = isLove;
	}
	public int getId()
	{
		return id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getAlbum()
	{
		return album;
	}
	public void setAlbum(String album)
	{
		this.album = album;
	}
	public String getArtist()
	{
		return artist;
	}
	public void setArtist(String artist)
	{
		this.artist = artist;
	}
	public String getData()
	{
		return data;
	}
	public void setData(String data)
	{
		this.data = data;
	}
	public String getDisplay_name()
	{
		return display_name;
	}
	public void setDisplay_name(String display_name)
	{
		this.display_name = display_name;
	}
	public String getYear()
	{
		return year;
	}
	public void setYear(String year)
	{
		this.year = year;
	}
	public int getDuration()
	{
		return duration;
	}
	public void setDuration(int duration)
	{
		this.duration = duration;
	}
	public int getSize()
	{
		return size;
	}
	public void setSize(int size)
	{
		this.size = size;
	}
	public String toString()
	{
		return "������: " + getTitle() + "\n����: " + getArtist() + "\nר����: " + getAlbum() + "\n·��: " + getData() + "\nʱ��: " + getDuration() + "\n��С: " + getSize() + "\n��������: " + getYear() + "\nisLove:" + isLove;
	}
}
