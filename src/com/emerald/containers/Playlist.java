package com.emerald.containers;

import java.util.List;

public class Playlist {
	private List<Song>	playlist;
	private int			index;
	
	public Playlist(List<Song> playlist, int index) {
		super();
		this.playlist = playlist;
		this.index = index;
	}
	public int		getSize() {
		return playlist.size();
	}
	public  List<Song> getPlaylist() {
		return playlist;
	}
	public void setPlaylist(List<Song> playlist) {
		this.playlist = playlist;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
}
