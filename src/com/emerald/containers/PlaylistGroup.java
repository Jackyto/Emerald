package com.emerald.containers;

import java.util.ArrayList;
import java.util.List;

public class PlaylistGroup {
	public String name;
	public List<Song> children = new ArrayList<Song>();

	public PlaylistGroup(String string) {
		this.name = string;
	}
}
