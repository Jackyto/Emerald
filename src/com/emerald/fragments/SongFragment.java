package com.emerald.fragments;

import java.util.ArrayList;
import java.util.List;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.containers.Album;
import com.emerald.containers.ExpandableAlbumListAdapter;
import com.emerald.containers.Group;
import com.emerald.containers.Playlist;
import com.emerald.containers.Song;
import com.emerald.containers.SongListAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
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

		if (!MainActivity.isFullAlbum()) {

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
						MusicManager.setPlaylist(new Playlist(MainActivity.getManager().getSongList(), pos));
					else if (MusicManager.getCurrentAlbum() != null)
						MusicManager.setPlaylist(new Playlist(MainActivity.getManager().getSongListFromAlbum(MusicManager.getCurrentAlbum()), pos));
					else
						MusicManager.setPlaylist(new Playlist(MainActivity.getManager().getSongListFromArtist(MusicManager.getCurrentArtist()), pos));

					MainActivity.setFromDrawer(false);
					MusicManager.setCurrentSong((Song) adapter.getItemAtPosition(pos));
					((MainActivity) getActivity()).play();
					((MainActivity) getActivity()).refreshPlayer();
				}

			});

		} else {
			MainActivity.setFullAlbum(false);
			
			rootView = inflater.inflate(R.layout.exp_song_frag, container, false);

			SparseArray<Group> groups = new SparseArray<Group>();

			List<Album>			albums = new ArrayList<Album>();
			albums = MainActivity.getManager().getAlbumListFromArtist(MusicManager.getCurrentArtist());

			List<Song>			songs = new ArrayList<Song>();

			for (int j = 0; j < albums.size(); j++) {
				Group group = new Group(albums.get(j));
				songs = MainActivity.getManager().getSongListFromAlbum(albums.get(j));
				for (int i = 0; i < songs.size(); i++) {
					group.children.add(songs.get(i));
				}
				groups.append(j, group);
			}

			ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.expListView);
			ExpandableAlbumListAdapter adapter = new ExpandableAlbumListAdapter(getActivity(),
					groups);
			listView.setChildIndicator(null);
			listView.setAdapter(adapter);
		}
		return rootView;
	}
}
