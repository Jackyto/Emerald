package com.emerald.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.emerald.R;
import com.emerald.containers.ExpandablePlaylistAdapter;
import com.emerald.containers.PlaylistGroup;

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
		createData();
		
		View header = inflater.inflate(R.layout.header_row, null);

		TextView label = (TextView) header.findViewById(R.id.label);
		label.setText("New playlist");

		ExpandableListView listView = (ExpandableListView) rootView.findViewById(R.id.playlistView);
		ExpandablePlaylistAdapter adapter = new ExpandablePlaylistAdapter(groups,
				getActivity());
		listView.addHeaderView(header);
		listView.setAdapter(adapter);
		return rootView;
	}

	public void createData() {
		for (int j = 0; j < 5; j++) {
			PlaylistGroup group = new PlaylistGroup("Test " + j);
			for (int i = 0; i < 5; i++) {
				group.children.add("Sub Item" + i);
			}
			groups.append(j, group);
		}
	}
}
