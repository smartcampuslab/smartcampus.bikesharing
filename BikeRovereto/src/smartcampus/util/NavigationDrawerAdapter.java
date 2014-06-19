package smartcampus.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikerovereto.R;

public class NavigationDrawerAdapter extends ArrayAdapter<String> {
	private int[] images;
	private int selectedItem;
	public NavigationDrawerAdapter(Context context, String[] titles, int[] images) {
		super(context, 0, titles);
		this.images=images;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			convertView = inflater.inflate(R.layout.navigation_drawer_model, parent, false);
			
			viewHolder=new ViewHolder();
			viewHolder.title=(TextView)convertView.findViewById(R.id.text);
			viewHolder.icon=(ImageView)convertView.findViewById(R.id.icon);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.title.setText(getItem(position));
		viewHolder.icon.setImageDrawable(getContext().getResources().getDrawable(images[position]));
		Log.d("pos","position: "+position+" selected: "+selectedItem);
		if (position==selectedItem)
		{
			convertView.setBackgroundColor(getContext().getResources().getColor(R.color.nav_backcolor_selected));
		}
		else
		{
			convertView.setBackgroundColor(getContext().getResources().getColor(R.color.nav_backcolor_normal));			
		}
		return convertView;		
	}
	
	private static class ViewHolder {
		TextView title;
		ImageView icon;
	}
	
	public void setItemChecked(int position){
		selectedItem=position;
	}
}
