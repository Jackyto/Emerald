package com.emerald.containers;

import java.util.ArrayList;
import java.util.List;

public class PlaylistGroup {
	public String name;
	public List<Song> children;

	public PlaylistGroup(String string) {
		this.name = string;
		this.children = new ArrayList<Song>();

	}
}
