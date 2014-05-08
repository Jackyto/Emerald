package com.emerald.containers;

import java.util.List;
import com.emerald.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumListAdapter extends ArrayAdapter<Album> {
	Context c;
	public AlbumListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		c = context;
	}

	public AlbumListAdapter(Context context, int resource, List<Album> items) {
		super(context, resource, items);
		c= context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.album_row, parent, false);

		Album p = getItem(position);

		if (p != null) {

			ImageView	iv = (ImageView) v.findViewById(R.id.albumArt);
			iv.setImageBitmap(p.getArt());

			TextView tt = (TextView) v.findViewById(R.id.albumLabel);
			TextView tt1 = (TextView) v.findViewById(R.id.nbSongLabel);

			if (tt != null) {
				tt.setText(p.getName());
			}
			if (tt1 != null) {
				tt1.setText(p.getNbSong() + " Song(s)");
			}

		}

		return v;

	}
}
