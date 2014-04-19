package com.emerald.containers;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.emerald.R;

public class SongListAdapter extends ArrayAdapter<Song> {
	Context c;
	public SongListAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
		c = context;
	}

	public SongListAdapter(Context context, int resource, List<Song> items) {
		super(context, resource, items);
		c= context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.song_row, null);
		}

		Song p = getItem(position);

		if (p != null) {

			TextView tt = (TextView) v.findViewById(R.id.songLabel);

			if (tt != null) {
				tt.setText(p.getTitle());
			}

		}

		return v;
	}
}
