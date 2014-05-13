package com.emerald.autocomplete;

import java.util.List;

import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.Utilities;
import com.emerald.containers.Song;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AutoCompleteSongArrayAdapter extends ArrayAdapter<Song> {
 
    final String TAG = "AutoCompleteSongArrayAdapter.java";
         
    Context mContext;
    int layoutResourceId;
 
    public AutoCompleteSongArrayAdapter(Context mContext, int layoutResourceId,	List<Song> data) {
 
        super(mContext, layoutResourceId, data);
         
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
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
}