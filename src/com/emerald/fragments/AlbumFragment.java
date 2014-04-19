package com.emerald.fragments;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.containers.Album;
import com.emerald.containers.AlbumListAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class AlbumFragment extends Fragment {

	public static AlbumFragment newInstance() {
		AlbumFragment fragment = new AlbumFragment();
		return fragment;
	}

	public AlbumFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.album_frag, container, false);

		ListView listView = (ListView) rootView.findViewById(R.id.album_list);

		AlbumListAdapter adapter = null;
		
		if (MainActivity.isFromDrawer()) {
			 adapter = new AlbumListAdapter(getActivity().getApplicationContext(), R.layout.album_row, MainActivity.getManager().getAlbumList());
			 MainActivity.setFromDrawer(false);
		} else {
			 adapter = new AlbumListAdapter(getActivity().getApplicationContext(),
					 			R.layout.album_row,
					 			MainActivity.getManager().getAlbumListFromArtist(MusicManager.getCurrentArtist()));
		}
		
		if (adapter != null)
			listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				MainActivity.setFromDrawer(false);
				MusicManager.setCurrentAlbum((Album) adapter.getItemAtPosition(pos));
				((MainActivity) getActivity()).changeView(MainActivity.getFragmentIndex() + 1);
			}
		});
		
		return rootView;
	}

}