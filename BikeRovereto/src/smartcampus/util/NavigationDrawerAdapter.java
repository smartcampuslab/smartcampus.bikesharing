package smartcampus.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import eu.trentorise.smartcampus.bikesharing.R;

public class NavigationDrawerAdapter extends ArrayAdapter<String> {
	private int[] images;
	private int selectedItem;
	private String[] extraTitles;
	private int[] extraImages;
	private int itemLenght;
	public NavigationDrawerAdapter(Context context, String[] titles, int[] images,
									String[] extraTitles, int[] extraImages) {
		super(context, 0, titles);
		this.itemLenght=titles.length;
		this.images=images;
		this.extraTitles=extraTitles;
		this.extraImages=extraImages;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		
		if (convertView == null){
			LayoutInflater inflater = LayoutInflater.from(getContext());
			if (getItemViewType(position)==0)
				convertView = inflater.inflate(R.layout.navigation_drawer_model, parent, false);
			else
				convertView = inflater.inflate(R.layout.navigation_drawer_model_extra, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.title=(TextView)convertView.findViewById(R.id.text);
			viewHolder.icon=(ImageView)convertView.findViewById(R.id.icon);
			convertView.setTag(viewHolder);
		}
		else {
			viewHolder=(ViewHolder)convertView.getTag();
		}
		Log.d("pos",position+"");
		if (getItemViewType(position)==0){
			viewHolder.title.setText(getItem(position));
			viewHolder.icon.setImageDrawable(getContext().getResources().getDrawable(images[position]));
			if (position==selectedItem)
			{
				convertView.setBackgroundColor(getContext().getResources().getColor(R.color.nav_backcolor_selected));
			}
			else
			{
				convertView.setBackgroundColor(getContext().getResources().getColor(R.color.nav_backcolor_normal));			
			}
		}
		else
		{
			viewHolder.title.setText(extraTitles[position-itemLenght]);
			viewHolder.icon.setImageDrawable(getContext().getResources().getDrawable(extraImages[position-itemLenght]));
		}
		
		
		return convertView;		
	}
	
	@Override
	public int getCount() {
		return itemLenght + extraTitles.length;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		return position < itemLenght ? 0 : 1;
	}
	
	private static class ViewHolder {
		TextView title;
		ImageView icon;
	}
	
	public void setItemChecked(int position){
		selectedItem=position;
	}
}
