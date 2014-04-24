package com.emerald.containers;

import com.emerald.MainActivity;
import com.emerald.MusicManager;
import com.emerald.R;

import android.app.Activity;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableAlbumListAdapter extends BaseExpandableListAdapter {

	private final SparseArray<AlbumGroup> groups;
	public LayoutInflater inflater;
	public Activity activity;

	public ExpandableAlbumListAdapter(Activity act, SparseArray<AlbumGroup> groups) {
		activity = act;
		this.groups = groups;
		inflater = act.getLayoutInflater();
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).children.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		final Song children = (Song) getChild(groupPosition, childPosition);
		
		TextView text, dur = null;
		
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.song_listrow_details, null);
		}
		
		text = (TextView) convertView.findViewById(R.id.expSongLabel);
		text.setText(children.getTitle());
		
		dur = (TextView) convertView.findViewById(R.id.expSongDuration);
		dur.setText(MusicManager.getUtils().milliSecondsToClock(children.getDuration()));
		
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		
				MusicManager.setCurrentPlaylist(new Playlist(MainActivity.getManager().getSongListFromArtist(MusicManager.getCurrentArtist()), childPosition, "current"));
				MainActivity.setFromDrawer(false);
				MusicManager.setCurrentSong(children);
				MusicManager.setCurrentAlbum(groups.get(groupPosition).album);
				((MainActivity) activity).play();
				((MainActivity) activity).refreshPlayer();
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
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.song_listrow_group, null);

		AlbumGroup group = (AlbumGroup) getGroup(groupPosition);

		TextView tv = (TextView) convertView.findViewById(R.id.expAlbumLabel);
		tv.setText(group.album.getName());

		ImageView iv = (ImageView) convertView.findViewById(R.id.expAlbumArt);
		iv.setImageBitmap(group.album.getArt());

		((CheckedTextView) tv).setChecked(isExpanded);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}
}
