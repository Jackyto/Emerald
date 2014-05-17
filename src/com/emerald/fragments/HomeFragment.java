package com.emerald.fragments;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.Utilities;
import com.emerald.autocomplete.AutoCompleteSongArrayAdapter;
import com.emerald.autocomplete.CustomAutoCompleteTextChangedListener;
import com.emerald.containers.Playlist;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnSeekBarChangeListener {
	private SeekBar				seekBar;
	private Handler				mHandler = new Handler();
	private Utilities			utils;

	private TextView			startTime;
	private TextView			endTime;

	private TextView			idLabel;
	private TextView			idArtist;
	private TextView			idAlbum;
	private TextView			idDuration;
	private ImageView			iv;

	AutoCompleteTextView 		autoComplete;
	AutoCompleteSongArrayAdapter adapter;
	Activity					act;


	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}

	public HomeFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_frag, container, false);

		act = getActivity();

		utils = new Utilities();

		seekBar = (SeekBar) rootView.findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(this);

		startTime = (TextView) rootView.findViewById(R.id.startTime);
		endTime = (TextView) rootView.findViewById(R.id.endTime);

		idLabel = (TextView) rootView.findViewById(R.id.idSongLabel);
		idArtist = (TextView) rootView.findViewById(R.id.idSongArtist);
		idAlbum = (TextView) rootView.findViewById(R.id.idSongAlbum);
		idDuration = (TextView) rootView.findViewById(R.id.idSongDuration);
		iv = (ImageView) rootView.findViewById(R.id.idAlbumArt);

		if (MusicManager.getCurrentSong() != null) {

			MainActivity.getManager().refreshCurrents();
			
			idLabel.setText(MusicManager.getCurrentSong().getTitle());
			idArtist.setText(MusicManager.getCurrentSong().getArtist());
			idAlbum.setText(MusicManager.getCurrentSong().getAlbum());
			idDuration.setText(utils.milliSecondsToTimer(MusicManager.getCurrentSong().getDuration()));
			iv.setImageBitmap(MusicManager.getAlbumFromSong(MusicManager.getCurrentSong()).getArt());	
		}

		if (MainActivity.getmService() != null
				&& MainActivity.getmService().getPlayer() != null) 
			updateProgressBar();

		autoComplete = (AutoCompleteTextView) rootView.findViewById(R.id.searchView);
		adapter = new AutoCompleteSongArrayAdapter(act, R.layout.song_row, MusicManager.getSearchList());

		autoComplete.addTextChangedListener(new CustomAutoCompleteTextChangedListener(act, autoComplete, adapter));
		
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

				((MainActivity) act).play();
				((MainActivity) act).refreshPlayer();

				InputMethodManager imm = (InputMethodManager) act.getSystemService(
						Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(autoComplete.getWindowToken(), 0);
				
				MusicManager.getSearchList().clear();
			}

		});

		return rootView;
	}

	public void refresh() {
		if (MusicManager.getCurrentSong() != null) {
			MainActivity.getManager();

			idLabel.setText(MusicManager.getCurrentSong().getTitle());
			idArtist.setText(MusicManager.getCurrentSong().getArtist());
			idAlbum.setText(MusicManager.getCurrentSong().getAlbum());
			idDuration.setText(utils.milliSecondsToTimer(MusicManager.getCurrentSong().getDuration()));

			iv.setImageBitmap(MusicManager.getAlbumFromSong(MusicManager.getCurrentSong()).getArt());

			updateProgressBar();
		}

	}

	public void updateProgressBar() {
		mHandler.postDelayed(mUpdateTimeTask, 100);
	}

	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			if (MainActivity.getmService().getPlayer() != null) {
				long totalDuration = MainActivity.getmService().getPlayer().getDuration();
				long currentDuration = MainActivity.getmService().getPlayer().getCurrentPosition();

				endTime.setText(""+ utils.milliSecondsToTimer(totalDuration - currentDuration));
				startTime.setText(""+ utils.milliSecondsToTimer(currentDuration));

				int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
				seekBar.setProgress(progress);

				mHandler.postDelayed(this, 100);
			}
		}
	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = MainActivity.getmService().getPlayer().getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

		MainActivity.getmService().getPlayer().seekTo(currentPosition);

		updateProgressBar();
	}



	@Override
	public void onDestroy() {
		mHandler.removeCallbacks(mUpdateTimeTask);
		super.onDestroy();
	}

	@Override
	public void onPause() {
		mHandler.removeCallbacks(mUpdateTimeTask);
		super.onPause();
	}

	@Override
	public void onResume() {
		mHandler.postDelayed(mUpdateTimeTask, 1000);
		super.onResume();
	}
}
