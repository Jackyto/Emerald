package com.emerald.autocomplete;

import java.util.ArrayList;
import java.util.List;
import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.Utilities;
import com.emerald.containers.Song;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class AutoCompleteSongArrayAdapter extends ArrayAdapter<Song> implements Filterable {

	final String TAG = "AutoCompleteSongArrayAdapter.java";
	private List<Song> songList;
	private List<Song> sortList;
	Context mContext;
	int layoutResourceId;

	public AutoCompleteSongArrayAdapter(Context mContext, int layoutResourceId,	List<Song> data) {

		super(mContext, layoutResourceId, data);

		this.layoutResourceId = layoutResourceId;
		this.mContext = mContext;
		this.songList = MainActivity.getManager().getSongList();
		this.sortList = new ArrayList<Song>();
	}
	
	@Override
	public int getCount() {
	    return sortList.size();
	}


	@Override
	public Filter getFilter() {
		return myFilter;
	}
	
	@Override
	public Song getItem(int position) {
	    return sortList.get(position);
	}

	Filter myFilter = new Filter() {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();

	        if (constraint == null || constraint.length() == 0) {
	            ArrayList<Song> list = new ArrayList<Song>(songList);
	            results.values = list;
	            results.count = list.size();
	        } else {
	            ArrayList<Song> newValues = new ArrayList<Song>();
	            for(int i = 0; i < songList.size(); i++) {
	                Song item = songList.get(i);
	                if(item.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
	                    newValues.add(item);
	                }
	            }
	            results.values = newValues;
	            results.count = newValues.size();
	        }       

	        return results;
		}


		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence contraint, FilterResults results) {
			sortList = (List<Song>) results.values;
			MusicManager.setSearchList(sortList);
	        Log.d("CustomArrayAdapter", String.valueOf(results.values));
	        Log.d("CustomArrayAdapter", String.valueOf(results.count));
	        notifyDataSetChanged();
		}
	};
	
	@Override
	public void notifyDataSetChanged() {
	    super.notifyDataSetChanged();
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