package com.emerald.containers;

import java.util.ArrayList;
import java.util.List;

public class Group {
	public Album album;
	public final List<Song> children = new ArrayList<Song>();

	public Group(Album _album) {
		this.album = _album;
	}

}
