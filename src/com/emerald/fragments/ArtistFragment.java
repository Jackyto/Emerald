package com.emerald.fragments;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.containers.Artist;
import com.emerald.containers.ArtistListAdapter;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ArtistFragment extends Fragment {

	public static ArtistFragment newInstance() {
		ArtistFragment fragment = new ArtistFragment();
		return fragment;
	}

	public ArtistFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.artist_frag, container, false);
	    
		ListView listView = (ListView) rootView.findViewById(R.id.artist_list);
		
		ArtistListAdapter adapter = new ArtistListAdapter(getActivity().getApplicationContext(), R.layout.artist_row, MainActivity.getManager().getArtistList());
		
		if (MainActivity.getManager().getArtistList() != null)
			listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View v, int pos,
					long arg3) {
				// TODO Auto-generated method stub
				MainActivity.setFromDrawer(false);
				MusicManager.setCurrentArtist((Artist) adapter.getItemAtPosition(pos));
				((MainActivity) getActivity()).changeView(MainActivity.getFragmentIndex() + 1);
			}
		});
		
		return rootView;
	}
}
