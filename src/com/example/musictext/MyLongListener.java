package com.example.musictext;

import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

public class MyLongListener implements OnItemLongClickListener
{
	private Context context;
	private List<Songs> list;
	private MyAdapter adapter;
	private static final int RINGTONE_PICKED = 0;

	public MyLongListener(Context context, List list, MyAdapter adapter)
	{
		this.context = context;
		this.list = list;
		this.adapter = adapter;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
	{
		Builder builder = new Builder(context);
		builder.setSingleChoiceItems(new String[]
		{ "查看详细信息", "删除歌曲", "从列表中删除歌曲", "设为手机铃声" }, 0, new OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
				switch (which)
				{
				case 0:
					Builder ad = new AlertDialog.Builder(context);
					ad.setTitle("歌曲详细信息");
					String message = list.get(position).toString();
					ad.setMessage(message);
					ad.setCancelable(true);
					ad.show();
					break;
				case 1:
					File f = new File(list.get(position).getData());
					if (f.exists())
					{
						f.delete();
						context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
								MediaStore.Audio.Media._ID + "=?", new String[]
						{ list.get(position).getId() + "" });
						Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(context, "文件不存在", Toast.LENGTH_SHORT).show();
				case 2:
					list.remove(position);
					adapter.notifyDataSetChanged();
					Toast.makeText(context, "已从列表中删除", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					// goto
					String data = list.get(position).getData();
//					setRington(data);
					break;
				default:
					break;
				}
			}
		});
		builder.show();
		return false;
	}

//	private void setRington(String data)
//	{
//		Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
//		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
//		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
//		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
//		Uri ringtoneUri;
//		if (data != null)
//		{
//			ringtoneUri = Uri.parse(data);
//		} else
//		{
//			ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
//		}
//
//		intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, ringtoneUri);
//		context.startActivityForResult(intent, RINGTONE_PICKED);
//	}
}
