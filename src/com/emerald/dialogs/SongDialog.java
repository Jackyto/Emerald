package com.emerald.dialogs;
import com.emerald.MusicManager;
import com.emerald.R;
import com.emerald.containers.PlaylistAdapter;
import com.emerald.containers.Song;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class SongDialog extends Dialog {
	public Activity c;
	public Song		s;

	public SongDialog(Activity a, Song s) {
		super(a);
		// TODO Auto-generated constructor stub
		this.c = a;
		this.s = s;
	}

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.song_dialog);

		ListView lv = (ListView) findViewById(R.id.addPlaylistListview);
		PlaylistAdapter adapter = new PlaylistAdapter(c,
				R.layout.playlist_row, MusicManager.getUserPlaylists());
		
		Button b = (Button) findViewById(R.id.newPlaylistButton);
		
		b.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NewPlaylistDialog cdd=new NewPlaylistDialog(c, s);
				cdd.show();
				dismiss();
			}
		});
		lv.setAdapter(adapter);
		
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if (!MusicManager.isSongInPlaylist(MusicManager.fetchPlaylist(MusicManager.getPlaylistNames().get(pos)), s)) {
					MusicManager.fetchPlaylist(MusicManager.getPlaylistNames().get(pos)).getPlaylist().add(s);
					Toast.makeText(c, "Added to " + MusicManager.getPlaylistNames().get(pos), Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(c, "Song already in list", Toast.LENGTH_SHORT).show();
				dismiss();

			}
		});
	}
}
