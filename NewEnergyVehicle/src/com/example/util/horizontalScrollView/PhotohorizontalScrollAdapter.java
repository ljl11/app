package com.example.util.horizontalScrollView;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.example.newenergyvehicle.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;


public class PhotohorizontalScrollAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private LinkedList<Bitmap> mDatas;

	public PhotohorizontalScrollAdapter(Context context, LinkedList<Bitmap> mDatas){
		this.mContext = context;
		mInflater = LayoutInflater.from(context);
		this.mDatas = mDatas;
	}

	public int getCount()
	{
		return mDatas.size();
	}

	public Object getItem(int position)
	{
		return mDatas.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.photo_item, parent, false);
			viewHolder.item = (ImageView) convertView.findViewById(R.id.picture_item);
			viewHolder.cancel = (ImageView) convertView.findViewById(R.id.cancel_picture);

			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.item.setImageBitmap(mDatas.get(position));
		viewHolder.cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mDatas.remove(position);
			}
		});

		return convertView;
	}

	private class ViewHolder{
		ImageView item;
		ImageView cancel;
	}
	
	public void updataImages(LinkedList<Bitmap> list){
		mDatas.addAll(list);
		
	}
}
