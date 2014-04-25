package com.emerald.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.containers.Album;
import com.emerald.containers.AlbumListAdapter;
import com.emerald.containers.ExpandableAlbumListAdapter;
import com.emerald.containers.AlbumGroup;
import com.emerald.containers.Song;

import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AlbumFragment extends Fragment {
	AlbumListAdapter adapter = null;

	public static AlbumFragment newInstance() {
		AlbumFragment fragment = new AlbumFragment();
		return fragment;
	}

	public AlbumFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = null;

		if (!MainActivity.isFullAlbum()) {
			rootView = inflater.inflate(R.layout.album_frag, container, false);

			ListView listView = (ListView) rootView.findViewById(R.id.album_list);

			if (MainActivity.isFromDrawer()) {
				List<Album> tmp = MainActivity.getManager().getAlbumList();
				
				Collections.sort(tmp, new Comparator<Album>() {
					@Override
					public int compare(final Album object1, final Album object2) {
						return object1.getName().compareTo(object2.getName());
					}
				} );
				adapter = new AlbumListAdapter(getActivity().getApplicationContext(), R.layout.album_row, tmp);
				MainActivity.setFromDrawer(false);
			} else {
				if (MusicManager.getCurrentArtist() != null) {
					adapter = new AlbumListAdapter(getActivity().getApplicationContext(),
							R.layout.album_row,
							MainActivity.getManager().getAlbumListFromArtist(MusicManager.getCurrentArtist()));
				}
				else {
					((MainActivity) getActivity()).changeView(MainActivity.getFragmentIndex() + 1);
					return rootView;
				}
			}

			View header = inflater.inflate(R.layout.header_row, null);

			TextView label = (TextView) header.findViewById(R.id.label);
			label.setText(R.string.allAlbum);

			listView.addHeaderView(header);

			if (adapter != null)
				listView.setAdapter(adapter);

			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapter, View v, int pos,
						long arg3) {

					MainActivity.setFromDrawer(false);
					if (pos == 0) {
						MainActivity.setFullAlbum(true);
						((MainActivity) getActivity()).changeView(MainActivity.getFragmentIndex());
					}
					else {
						MusicManager.setCurrentAlbum((Album) adapter.getItemAtPosition(pos));
						MusicManager.setCurrentArtist(MusicManager.getArtistFromAlbum(MusicManager.getCurrentAlbum()));
						((MainActivity) getActivity()).changeView(MainActivity.getFragmentIndex() + 1);
					}
				}
			});
		} else {
			MainActivity.setFullAlbum(false);

			rootView = inflater.inflate(R.layout.exp_song_frag, container, false);

			SparseArray<AlbumGroup> groups = new SparseArray<AlbumGroup>();

			List<Album>			albums = new ArrayList<Album>();

			if (MusicManager.getCurrentArtist() == null
					|| MainActivity.isFromDrawer()) 
				albums = MainActivity.getManager().getAlbumList();
			else
				albums = MainActivity.getManager().getAlbumListFromArtist(MusicManager.getCurrentArtist());

			List<Song>			songs = new ArrayList<Song>();

			for (int j = 0; j < albums.size(); j++) {
				AlbumGroup group = new AlbumGroup(albums.get(j));
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