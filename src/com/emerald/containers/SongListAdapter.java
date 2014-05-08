package com.emerald.containers;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.Utilities;

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
			Utilities utils = new Utilities(); 

			ImageView	iv = (ImageView) v.findViewById(R.id.songArt);

			if (MusicManager.getAlbumFromSong(p).getArt() != null)
				iv.setImageBitmap(MusicManager.getAlbumFromSong(p).getArt());

			TextView tt = (TextView) v.findViewById(R.id.songLabel);
			TextView tt1 = (TextView) v.findViewById(R.id.songDuration);
			TextView tt2 = (TextView) v.findViewById(R.id.songArtist);

			if (tt != null) 
				tt.setText(p.getTitle());

			if (tt1 != null) 
				tt1.setText(utils.milliSecondsToClock(p.getDuration()));

			if (tt2 != null)
				tt2.setText(p.getArtist());
		}

		return v;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public Song getItem(int position) {
		// TODO Auto-generated method stub
		return super.getItem(position);
	}

}
