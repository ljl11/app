package com.example.newenergyvehicle.homePage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.example.newenergyvehicle.R;
import com.example.newenergyvehicle.myWork.MyWork;
import com.example.service.pushService.DemoIntentService;
import com.example.service.pushService.Notice;
import com.example.util.menu.MenuContainer;
import com.example.util.menu.MenuItemInfo;
import com.example.util.menu.MenuList;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NavigationAdapater extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
	private List<NavigationItem> list;
	private DrawerLayoutMenu menu;
	private int noticeNum;
	private Handler handler = new Handler();

	public int getMax() {
		return list.size();
	}

	public NavigationAdapater(Context context) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		list = new ArrayList<NavigationItem>();
	}

	public NavigationAdapater(Context context, DrawerLayoutMenu menu) {
		this.context = context;
		inflater = LayoutInflater.from(context);
		this.menu = menu;
		list = new ArrayList<NavigationItem>();
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
			convertView = inflater.inflate(R.layout.navigation_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			
			
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		setItem(holder, position);

		return convertView;
	}

	class ViewHolder {
		private ImageView user_head;
		private TextView function_module;
		private TextView num;
		private LinearLayout item;

		public ViewHolder(View view) {
			user_head = (ImageView) view.findViewById(R.id.item_pic);
			function_module = (TextView) view
					.findViewById(R.id.navigation_name);
			num = (TextView) view.findViewById(R.id.Navigation_num_notie);
			item = (LinearLayout) view.findViewById(R.id.navigation_item);
		}
	}

	private void setItem(ViewHolder holder, final int position) {
		final NavigationItem item = list.get(position);
		holder.user_head.setImageResource(item.getImg());
		holder.function_module.setText(item.getModule());
		int num = item.getNum();
		if (num > 0) {
			holder.num.setVisibility(View.VISIBLE);
		} else {
			holder.num.setVisibility(View.INVISIBLE);
		}
		holder.item.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				List<Fragment> list = ((DrawerLayoutMenu) context)
						.getNotNullFragmentList();
				Fragment changeView = menu.getMenuFragment(position);
				FragmentTransaction fm = ((DrawerLayoutMenu) context)
						.getSupportFragmentManager().beginTransaction();
				for (int i = list.size() - 1; i > 0; i--) {
					Fragment fragment = list.get(i);
					if (fragment.getClass() != changeView.getClass()) {
						fm = ((DrawerLayoutMenu) context)
								.getSupportFragmentManager().beginTransaction();
						fm.remove(fragment).show(list.get(i - 1)).commit();
					}
				}

				if (list.contains(changeView)) {
					fm = ((DrawerLayoutMenu) context)
							.getSupportFragmentManager().beginTransaction();
					fm.show(changeView).commit();
				} else {
					fm = ((DrawerLayoutMenu) context)
							.getSupportFragmentManager().beginTransaction();
					List<Fragment> test = ((DrawerLayoutMenu) context)
							.getNotNullFragmentList();
					fm.remove(test.get(0))
							.add(R.id.id_framelayout2, changeView).commit();
				}

				MenuItemInfo info = MenuList.getMenuList().getMenuItemInfo(
						item.getId());
				((DrawerLayoutMenu) context).changeHeader(info.getName(),
						info.isMore(), info.getMoreString());
				((DrawerLayoutMenu) context).closeDrawer();
				clearNotification(changeView);
			}
		});
	}

	public NavigationItem getEntity(MenuItemInfo info) {
		NavigationItem navigationItem = new NavigationItem();

		try {
			navigationItem.setId(info.getId());
		} catch (Exception e) {
		}

		try {
			navigationItem.setImg(MenuList.getMenuList().getFragmetImage(
					info.getId()));
		} catch (Exception e) {
		}

		try {
			navigationItem.setModule(info.getName());
		} catch (Exception e) {
			navigationItem.setModule("暂无数据");
		}
		navigationItem.setNum(0);
		if (info.getId() == "2" && MyWork.haveUnread == true) {
			navigationItem.setNum(1);
		}
		if (info.getId() == "8") {
			navigationItem.setNum(DrawerLayoutMenu.noticeNum);
		}

		return navigationItem;
	}

	public void resetSingleData(String[] menu) {
		list.clear();
		resetData(menu);
	}

	public void resetData(String[] menu) {
		List<Fragment> fragment = new ArrayList<Fragment>();
		if (menu != null) {
			int length = menu.length;
			MenuItemInfo info = null;
			for (int i = 0; i < length; i++) {
				info = MenuList.getMenuList().getMenuItemInfo(menu[i]);
				list.add(getEntity(info));

				fragment.add(MenuContainer.getMenuContainer().getFragment(
						info.getId()));
			}
		}

		this.menu.setMenuFragment(fragment);
	}

	public void changeNoite(String id) {
		if (id == null)
			return;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId().equals(id)) {
				list.get(i).setNum(list.get(i).getNum() - 1);
				notifyDataSetChanged();
				break;
			}
		}
	}
	
	private void  clearNotification(Fragment f){
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE );
		for (Entry<Integer, Notice> en : DemoIntentService.map.entrySet()) { 
			Class<?> cla = en.getValue().getCla();
            if ( f.getClass() ==  cla && !(f instanceof MyWork) ) {  
            	manager.cancel(en.getKey());//nofify中第一个参数使用的id
            	DemoIntentService.map.remove(en.getKey());
            	return;
            }  
        }  
	}
	

}
