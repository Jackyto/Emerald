package com.emerald.containers;

import java.util.ArrayList;
import java.util.List;

public class PlaylistGroup {
	public String name;
	public final List<String> children = new ArrayList<String>();

	public PlaylistGroup(String string) {
		this.name = string;
	}
}
