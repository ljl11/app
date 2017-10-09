package com.example.newenergyvehicle;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScanHistoryAdapt extends BaseAdapter{
	public List<String[]> list;
	private String[] names;
	private int count;
	private Context context;
	private DoubleHolder doubleHolder;
	private SingleHolder singleHolder;
	
	
	public ScanHistoryAdapt(Context context,List<String[]> list,String[] names){
		this.context = context;
		this.list = list;
		this.names = names;
		this.count = names.length;
	}
	
	public void resertData(List<String[]> list){
		this.list.clear();
		this.list.addAll(list);
		this.notifyDataSetChanged();
	}
	
	public void resertIndexData(int index){
		this.list.remove(index);
		this.notifyDataSetChanged();
	}
	
	public void addData(List<String[]> list){
		this.list.addAll(list);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.deliver_scan_list, null);
			if(count == 1){
				RelativeLayout doubleLayout = (RelativeLayout) convertView.findViewById(R.id.doubleLayout);
				doubleLayout.setVisibility(View.GONE);
				singleHolder = new SingleHolder();
				singleHolder.name = (TextView) convertView.findViewById(R.id.name);
				singleHolder.info = (TextView) convertView.findViewById(R.id.info);
				convertView.setTag(singleHolder);
			}else{
				RelativeLayout singleLayout = (RelativeLayout) convertView.findViewById(R.id.singleLayout);
				singleLayout.setVisibility(View.GONE);
				doubleHolder = new DoubleHolder();
				doubleHolder.name1 = (TextView) convertView.findViewById(R.id.name1);
				doubleHolder.info1 = (TextView) convertView.findViewById(R.id.info1);
				doubleHolder.name2 = (TextView) convertView.findViewById(R.id.name2);
				doubleHolder.info2 = (TextView) convertView.findViewById(R.id.info2);
				convertView.setTag(doubleHolder);
			}
			
		} else {
			if(count == 1){
				singleHolder = (SingleHolder) convertView.getTag();
			}else{
				doubleHolder = (DoubleHolder) convertView.getTag();
			}
		}
		String[] infos = list.get(position);
		if(count == 1){
			singleHolder.name.setText(names[0]);
			singleHolder.info.setText(infos[0]);
		}else{
			doubleHolder.name1.setText(names[0]);
			doubleHolder.info1.setText(infos[0]);
			doubleHolder.name2.setText(names[1]);
			doubleHolder.info2.setText(infos[1]);
		}
		return convertView;
	}

	static class DoubleHolder{
		TextView name1;
		TextView info1;
		TextView name2;
		TextView info2;
	}
	
	static class SingleHolder{
		TextView name;
		TextView info;
	}
}
