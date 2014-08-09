package de.fivestrikes.pro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

public class TabMenuListAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;

	public TabMenuListAdapter(Context context, String[] values) {
		super(context, R.layout.row_spiel, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = null;
		String s = values[position];
		if (s.startsWith("Tor")) {
			rowView = inflater.inflate(R.layout.row_spiel, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.rowSpielHeim);
			textView.setText(values[position]);
		} 
		if (s.startsWith("Fehlwurf")) {
			rowView = inflater.inflate(R.layout.row_player, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.rowMannschaftName);
			textView.setText(values[position]);
		}
		
		
		//ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		
		/*
		// Change the icon for Windows and iPhone
		String s = values[position];
		if (s.startsWith("Windows7") || s.startsWith("iPhone")
				|| s.startsWith("Solaris")) {
			imageView.setImageResource(R.drawable.no);
		} else {
			imageView.setImageResource(R.drawable.ok);
		}
		*/

    return rowView;
	}
} 