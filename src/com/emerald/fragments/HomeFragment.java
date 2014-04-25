package com.emerald.fragments;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.Utilities;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
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
	private Button				butArtist, butAlbum, butPlaylist, butSong;

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

		butArtist = (Button) rootView.findViewById(R.id.artistButton);
		butAlbum = (Button) rootView.findViewById(R.id.albumButton);
		butPlaylist = (Button) rootView.findViewById(R.id.playlistButton);
		butSong = (Button) rootView.findViewById(R.id.songsButton);
		
		
		
		butArtist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).changeView(1);
			}
		});
		
		butAlbum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MusicManager.getCurrentArtist() == null)
					MainActivity.setFromDrawer(true);
				((MainActivity) getActivity()).changeView(2);
			}
		});
		
		butPlaylist.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).changeView(4);
			}
		});
		
		butSong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MusicManager.getCurrentAlbum() == null)
					MainActivity.setFromDrawer(true);
				((MainActivity) getActivity()).changeView(3);
			}
		});
		
		if (MusicManager.getCurrentSong() != null) {
			MainActivity.getManager();

			idLabel.setText(MusicManager.getCurrentSong().getTitle());
			idArtist.setText(MusicManager.getCurrentSong().getArtist());
			idAlbum.setText(MusicManager.getCurrentSong().getAlbum());
			idDuration.setText(utils.milliSecondsToTimer(MusicManager.getCurrentSong().getDuration()));
			
			iv.setImageBitmap(MusicManager.getAlbumFromSong(MusicManager.getCurrentSong()).getArt());
		}

		if (MainActivity.getmService() != null
				&& MainActivity.getmService().getPlayer() != null) 
			updateProgressBar();

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
