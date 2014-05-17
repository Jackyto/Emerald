package com.emerald.fragments;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
			List<Song> tmp = MainActivity.getManager().getSongList();
			Collections.sort(tmp, new Comparator<Song>() {
				@Override
				public int compare(final Song object1, final Song object2) {
					return object1.getTitle().compareTo(object2.getTitle());
				}
			} );
			adapter = new SongListAdapter(getActivity().getApplicationContext(), R.layout.song_row, tmp);
		}
		else {
			if (MusicManager.getCurrentAlbum() != null) {
				adapter = new SongListAdapter(getActivity().getApplicationContext(),
						R.layout.song_row,
						MusicManager.getSongListFromAlbum(MusicManager.getCurrentAlbum()));
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
				else if (MusicManager.getCurrentAlbum() != null) {
					MusicManager.setCurrentPlaylist(new Playlist(MusicManager.getSongListFromAlbum(MusicManager.getCurrentAlbum()), pos, "current"));
				} else
					MusicManager.setCurrentPlaylist(new Playlist(MainActivity.getManager().getSongListFromArtist(MusicManager.getCurrentArtist()), pos, "current"));
				
				MusicManager.getCurrentPlaylist().setIndex(MusicManager.getSongIndex(MusicManager.getCurrentSong()));
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
				SongDialog sd = new SongDialog(getActivity(), (Song) adapter.getItemAtPosition(pos));
				sd.show();
				return true;
			}
		});

		return rootView;
	}
}
