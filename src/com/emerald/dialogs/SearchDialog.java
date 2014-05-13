package com.emerald.dialogs;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.autocomplete.AutoCompleteSongArrayAdapter;
import com.emerald.autocomplete.CustomAutoCompleteTextChangedListener;
import com.emerald.autocomplete.CustomAutoCompleteView;
import com.emerald.containers.Playlist;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class SearchDialog extends Dialog {
	public Activity c;
	CustomAutoCompleteView autoComplete;
	AutoCompleteSongArrayAdapter adapter;
	
	public SearchDialog(Activity activity) {
		super(activity);
		this.c = activity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_dialog);

		autoComplete = (CustomAutoCompleteView) findViewById(R.id.searchView);
		adapter = new AutoCompleteSongArrayAdapter(c, R.layout.song_row, MusicManager.getSearchList());
		
		autoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(c, autoComplete, adapter));
		
		autoComplete.setAdapter(adapter);

		autoComplete.setOnItemClickListener(new OnItemClickListener() {
			 
			@Override
			public void onItemClick(AdapterView<?> adapter, View arg1, int pos,
					long arg3) {
				
				MusicManager.setCurrentSong(MusicManager.getSearchList().get(pos));
				MusicManager.setCurrentArtist(MusicManager.getArtistFromSong(MusicManager.getCurrentSong()));
				MusicManager.setCurrentAlbum(MusicManager.getAlbumFromSong(MusicManager.getCurrentSong()));
				
				if (MainActivity.isFromDrawer())
					MusicManager.setCurrentPlaylist(new Playlist(MainActivity.getManager().getSongList(), 0, "current"));
				else if (MusicManager.getCurrentAlbum() != null)
					MusicManager.setCurrentPlaylist(new Playlist(MainActivity.getManager().getSongListFromAlbum(MusicManager.getCurrentAlbum()), 0, "current"));
				else
					MusicManager.setCurrentPlaylist(new Playlist(MainActivity.getManager().getSongListFromArtist(MusicManager.getCurrentArtist()), 0, "current"));

				MusicManager.getCurrentPlaylist().setIndex(0);
				MainActivity.setFromDrawer(false);

				((MainActivity) c).play();
				((MainActivity) c).refreshPlayer();
				
				dismiss();			
			}

        });
	}
}
