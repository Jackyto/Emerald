package com.emerald.fragments;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.containers.Playlist;
import com.emerald.containers.Song;
import com.emerald.containers.SongListAdapter;
import com.emerald.dialogs.SongDialog;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

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
		View rootView = null;

		rootView = inflater.inflate(R.layout.song_frag, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.song_list);

		SongListAdapter adapter = null;
		if (MainActivity.isFromDrawer()) {
			adapter = new SongListAdapter(getActivity().getApplicationContext(), R.layout.song_row, MainActivity.getManager().getSongList());
		}
		else {
			if (MusicManager.getCurrentAlbum() != null) {
				adapter = new SongListAdapter(getActivity().getApplicationContext(),
						R.layout.song_row,
						MainActivity.getManager().getSongListFromAlbum(MusicManager.getCurrentAlbum()));
			}
			else {
				adapter = new SongListAdapter(getActivity().getApplicationContext(),
						R.layout.song_row,
						MainActivity.getManager().getSongListFromArtist(MusicManager.getCurrentArtist()));
			}
		}

		if (adapter != null)
			listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				if (MainActivity.isFromDrawer())
					MusicManager.setCurrentPlaylist(new Playlist(MainActivity.getManager().getSongList(), pos, "current"));
				else if (MusicManager.getCurrentAlbum() != null)
					MusicManager.setCurrentPlaylist(new Playlist(MainActivity.getManager().getSongListFromAlbum(MusicManager.getCurrentAlbum()), pos, "current"));
				else
					MusicManager.setCurrentPlaylist(new Playlist(MainActivity.getManager().getSongListFromArtist(MusicManager.getCurrentArtist()), pos, "current"));
				
				MusicManager.getCurrentPlaylist().setIndex(pos);
				MainActivity.setFromDrawer(false);
				MusicManager.setCurrentSong((Song) adapter.getItemAtPosition(pos));
				((MainActivity) getActivity()).play();
				((MainActivity) getActivity()).refreshPlayer();
			}

		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View arg1,
					int pos, long arg3) {
				Toast.makeText(getActivity(), "caca", Toast.LENGTH_SHORT).show();
				SongDialog sd = new SongDialog(getActivity(), (Song) adapter.getItemAtPosition(pos));
				sd.show();
				return true;
			}
		});

		return rootView;
	}
}
