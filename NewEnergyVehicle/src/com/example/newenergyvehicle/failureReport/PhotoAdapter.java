package com.example.newenergyvehicle.failureReport;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.newenergyvehicle.R;

public class PhotoAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private LinkedList<Bitmap> list;

	public int getMax() {
		return list.size();
	}

	
	public PhotoAdapter(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new LinkedList<Bitmap>();
	}

	public LinkedList<Bitmap> getList() {
		return list;
	}

	public void resetData(LinkedList<Bitmap> list) {
		if (list != null && list.size() > 0) {
			int length = list.size();
			for (int i = 0; i < length; i++) {
				this.list.add(list.get(i));
			}
		}
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final int index = position;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.photo_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		setItem(holder, position);
		return convertView;
	}

	private void setItem(ViewHolder holder, int position) {
		holder.item.setImageBitmap(list.get(position));
	}

	class ViewHolder {
		ImageView item;
		ImageView cancel;

		public ViewHolder(View view) {
			item = (ImageView) view.findViewById(R.id.picture_item);
			cancel = (ImageView) view.findViewById(R.id.cancel_picture);
		}
	}

	public void resetSingleData(LinkedList<Bitmap> list) {
		list.clear();
		resetData(list);
	}


}
