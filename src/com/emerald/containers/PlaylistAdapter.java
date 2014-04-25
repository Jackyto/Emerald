package com.emerald.containers;

import java.util.List;

import com.emerald.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class PlaylistAdapter extends ArrayAdapter<Playlist> {
	Context c;
	public PlaylistAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		c = context;
	}

	public PlaylistAdapter(Context context, int resource, List<Playlist> items) {
		super(context, resource, items);
		c= context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.playlist_row, null);
		} else
			v = convertView;

		Playlist p = getItem(position);

		if (p != null) {
	
			TextView tt = (TextView) v.findViewById(R.id.playlistRowLabel);
			TextView tt1 = (TextView) v.findViewById(R.id.playlistRowNbSong);

			if (tt != null) {
				tt.setText(p.getName());
			}
			if (tt1 != null) {

				tt1.setText(p.getPlaylist().size() + " Song(s)");
			}

		}

		return v;

	}
}
