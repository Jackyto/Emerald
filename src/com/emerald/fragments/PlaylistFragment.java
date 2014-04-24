package com.emerald.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.containers.ExpandablePlaylistAdapter;
import com.emerald.containers.PlaylistGroup;
import com.emerald.dialogs.NewPlaylistDialog;

public class PlaylistFragment extends Fragment {
	SparseArray<PlaylistGroup> groups = new SparseArray<PlaylistGroup>();

	public static PlaylistFragment newInstance() {
		PlaylistFragment fragment = new PlaylistFragment();
		return fragment;
	}

	public PlaylistFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.exp_playlist_frag, container, false);

		View header = inflater.inflate(R.layout.header_row, null);

		TextView label = (TextView) header.findViewById(R.id.label);
		label.setText("New playlist");

		for (int i = 0; i < MusicManager.getUserPlaylists().size(); i++) {
			PlaylistGroup pg = new PlaylistGroup(MusicManager.getUserPlaylists().get(i).getName());
			pg.children = MusicManager.getUserPlaylists().get(i).getPlaylist();
			groups.append(i, pg);
		}
		
		ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.playlistView);
		ExpandablePlaylistAdapter adapter = new ExpandablePlaylistAdapter(groups,
				getActivity());
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if (pos == 0) {
					NewPlaylistDialog cdd=new NewPlaylistDialog(getActivity());
					cdd.show();
				} else {
					
				}
				
			}
		});
		return rootView;
	}

}

