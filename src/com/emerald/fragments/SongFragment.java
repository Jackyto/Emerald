package com.emerald.fragments;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.containers.Playlist;
import com.emerald.containers.Song;
import com.emerald.containers.SongListAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SongFragment extends Fragment {
	
	public static SongFragment newInstance() {
		SongFragment fragment = new SongFragment();
		return fragment;
	}

	public SongFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		getActivity().getApplicationContext();
		
		View rootView = inflater.inflate(R.layout.song_frag, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.song_list);

		SongListAdapter adapter = null;
		if (MainActivity.isFromDrawer()) {
			 adapter = new SongListAdapter(getActivity().getApplicationContext(), R.layout.song_row, MainActivity.getManager().getSongList());
		}
		else {
			 adapter = new SongListAdapter(getActivity().getApplicationContext(),
					 			R.layout.song_row,
					 			MainActivity.getManager().getSongListFromAlbum(MusicManager.getCurrentAlbum()));
		}

		if (adapter != null)
			listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				if (MainActivity.isFromDrawer())
					MusicManager.setPlaylist(new Playlist(MainActivity.getManager().getSongList(), pos));
				else
					MusicManager.setPlaylist(new Playlist(MainActivity.getManager().getSongListFromAlbum(MusicManager.getCurrentAlbum()), pos));
				MainActivity.setFromDrawer(false);
				MusicManager.setCurrentSong((Song) adapter.getItemAtPosition(pos));
				((MainActivity) getActivity()).play();
				((MainActivity) getActivity()).refreshPlayer();
			}
			
		});
		
		return rootView;
	}
}
