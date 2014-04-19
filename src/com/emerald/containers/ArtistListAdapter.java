package com.emerald.containers;

import java.util.List;

import com.emerald.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArtistListAdapter extends ArrayAdapter<Artist> {
	Context c;
	public ArtistListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		c = context;
	}

	public ArtistListAdapter(Context context, int resource, List<Artist> items) {
		super(context, resource, items);
		c= context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.artist_row, null);
		}

		Artist p = getItem(position);

		if (p != null) {

			TextView tt = (TextView) v.findViewById(R.id.artistLabel);
			TextView tt1 = (TextView) v.findViewById(R.id.nbAlbumLabel);
			
			if (tt != null) {
				tt.setText(p.getName());
			}
			if (tt1 != null) {

				tt1.setText(p.getNbAlbum() + " Album(s)");
			}

		}

		return v;

	}
}
