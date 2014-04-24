package com.emerald.containers;

import java.io.Serializable;
import java.util.List;


public class Playlist implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6424861753337600028L;
	private String		name;
	private List<Song>	playlist;
	private int			index;
	
	public Playlist(List<Song> playlist, int index, String name) {
		super();
		this.playlist = playlist;
		this.index = index;
		this.name = name;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
