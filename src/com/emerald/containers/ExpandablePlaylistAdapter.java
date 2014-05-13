package com.emerald.containers;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExpandablePlaylistAdapter extends BaseExpandableListAdapter {

	private final SparseArray<PlaylistGroup> groups;
	public LayoutInflater inflater;
	public Activity activity;

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ExpandablePlaylistAdapter(SparseArray<PlaylistGroup> groups,
			Activity activity) {
		super();
		this.groups = groups;
		this.activity = activity;
		inflater = activity.getLayoutInflater();
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		final Song children = (Song) getChild(groupPosition, childPosition);
		TextView textSong, textArtist, textDuration = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.playlist_listrow_details, null);
		}

		ImageView iv = (ImageView) convertView.findViewById(R.id.expPlaylistAlbumArt);
		textSong = (TextView) convertView.findViewById(R.id.expPlaylistSongLabel);
		textArtist = (TextView) convertView.findViewById(R.id.expPlaylistArtistLabel);
		textDuration = (TextView) convertView.findViewById(R.id.expPlaylistSongDuration);

		iv.setImageBitmap(MusicManager.getAlbumFromSong(children).getArt());
		textSong.setText(children.getTitle());
		textArtist.setText(children.getArtist());
		textDuration.setText(MusicManager.getUtils().milliSecondsToClock(children.getDuration()));

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MusicManager.setCurrentPlaylist(new Playlist(groups.get(groupPosition).children, childPosition, "current"));

				System.out.println(MusicManager.getCurrentPlaylist().getPlaylist().size() + " size current");
				
				MainActivity.setFromDrawer(false);
				MusicManager.setCurrentSong(children);
				((MainActivity) activity).play();
				((MainActivity) activity).refreshPlayer();
			}
		});
		convertView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						activity);

				// set title
				// set dialog message
				alertDialogBuilder
				.setMessage("Do you really want to delete " + groups.get(groupPosition).children.get(childPosition).getTitle() + " ?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						MusicManager.fetchPlaylist(MusicManager.getPlaylistNames().get(groupPosition)).getPlaylist().remove(groupPosition);
						Toast.makeText(activity, groups.get(groupPosition).name + " deleted", Toast.LENGTH_SHORT).show();
						((MainActivity) activity).changeView(MainActivity.getFragmentIndex());			
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
		});
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).children.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.playlist_listrow_group, null);
		View v = inflater.inflate(R.layout.exp_playlist_frag, null); 
		ExpandableListView elv = (ExpandableListView) v.findViewById(R.id.playlistView);
		PlaylistGroup group = (PlaylistGroup) getGroup(groupPosition);

		TextView ctv = (TextView) convertView.findViewById(R.id.expPlaylistLabel);
		ctv.setText(group.name);

		TextView	tv = (TextView) convertView.findViewById(R.id.expNbSongs);
		tv.setText(group.children.size() + " song(s)");

		elv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						activity);

				// set title
				// set dialog message
				alertDialogBuilder
				.setMessage("Do you really want to delete " + groups.get(groupPosition).name + " ?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						MusicManager.getPlaylistNames().remove(groupPosition);
						MusicManager.getUserPlaylists().remove(groupPosition);
						Toast.makeText(activity, groups.get(groupPosition).name + " deleted", Toast.LENGTH_SHORT).show();
						((MainActivity) activity).changeView(MainActivity.getFragmentIndex());			
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
		});

		return convertView;
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	public SparseArray<PlaylistGroup> getGroups() {
		return groups;
	}

}
