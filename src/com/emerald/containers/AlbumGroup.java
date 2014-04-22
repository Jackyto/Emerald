package com.emerald.containers;

import java.util.ArrayList;
import java.util.List;

public class AlbumGroup {
	public Album album;
	public final List<Song> children = new ArrayList<Song>();

	public AlbumGroup(Album _album) {
		this.album = _album;
	}

}
