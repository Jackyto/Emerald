package com.emerald.dialogs;

import java.util.ArrayList;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.containers.Playlist;
import com.emerald.containers.Song;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class NewPlaylistDialog extends Dialog implements
android.view.View.OnClickListener {

	public Activity c;
	public Dialog d;
	public Button yes, no;
	public EditText ed;
	public Playlist p;
	public Song s;

	public NewPlaylistDialog(Activity a, Song s) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		this.s = s;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.new_playlist_dialog);
		yes = (Button) findViewById(R.id.btn_yes);
		no = (Button) findViewById(R.id.btn_no);
		ed = (EditText) findViewById(R.id.playlistLabel);
		yes.setOnClickListener(this);
		no.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_yes:
			if (ed.getText() != null) {
				p = new Playlist(new ArrayList<Song>(), 0, ed.getText().toString());
				if (!MusicManager.isPlaylistExists(p)) {
					if (s != null)
						p.getPlaylist().add(s);
					MusicManager.getPlaylistNames().add(p.getName());
					MusicManager.getUserPlaylists().add(p);
					Toast.makeText(c, "New playlist added : " + p.getName(), Toast.LENGTH_SHORT).show();
					
					if (MainActivity.getFragmentIndex() == 4)
						((MainActivity) c).changeView(MainActivity.getFragmentIndex());
				} else
					Toast.makeText(c, "Playlist already exists : " + p.getName(), Toast.LENGTH_SHORT).show();
			}
			dismiss();
			break;
		case R.id.btn_no:
			dismiss();
			break;
		default:
			break;
		}
		dismiss();
	}
}
