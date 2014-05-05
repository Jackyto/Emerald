package com.emerald.fragments;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.containers.ExpandablePlaylistAdapter;
import com.emerald.containers.PlaylistGroup;
import com.emerald.containers.Song;
import com.emerald.dialogs.NewPlaylistDialog;

public class PlaylistFragment extends Fragment implements OnItemLongClickListener{
	SparseArray<PlaylistGroup> groups = new SparseArray<PlaylistGroup>();
	ExpandablePlaylistAdapter adapter;
	ExpandableListView listView;
	Button b;
	
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

		b = (Button) rootView.findViewById(R.id.newPlaylistFragButton);
		
		groups.clear();
		for (int i = 0; i < MusicManager.getUserPlaylists().size(); i++) {
			PlaylistGroup pg = new PlaylistGroup(MusicManager.getUserPlaylists().get(i).getName());
			if (MusicManager.getUserPlaylists().get(i).getPlaylist() != null) {
				pg.children = MusicManager.getUserPlaylists().get(i).getPlaylist();
			} else {
				pg.children = new ArrayList<Song>();
			}
			groups.append(i, pg);
		}

		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NewPlaylistDialog cdd=new NewPlaylistDialog(getActivity(), null);
				cdd.show();
			}
		});

		listView = (ExpandableListView) rootView.findViewById(R.id.playlistView);
		adapter = new ExpandablePlaylistAdapter(groups,
				getActivity());
		listView.setAdapter(adapter);

		listView.setOnItemLongClickListener(this);

		return rootView;
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2,
			long arg3) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set title
		// set dialog message
		alertDialogBuilder
		.setMessage("Do you really want to delete " + groups.get(arg2).name + " ?")
		.setCancelable(false)
		.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				String toToast = MusicManager.getPlaylistNames().get(arg2);
				MusicManager.getPlaylistNames().remove(arg2);
				MusicManager.getUserPlaylists().remove(arg2);
				Toast.makeText(getActivity(), toToast + " deleted", Toast.LENGTH_SHORT).show();
				((MainActivity) getActivity()).changeView(MainActivity.getFragmentIndex());			
			}
		})
		.setNegativeButton("No",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});

		AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();

		return true;
	}
}

