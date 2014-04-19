package com.emerald.containers;

import android.graphics.Bitmap;

public class Album {
	private int		id;
	private String	key;
	private String	name;
	private String 	artist;
	private int		nbSong;
	private Bitmap	art;
	
	public Album(int id, String key, String name, String artist, int nbSong) {
		super();
		this.id = id;
		this.key = key;
		this.name = name;
		this.artist = artist;
		this.nbSong = nbSong;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNbSong() {
		return nbSong;
	}
	public void setNbSong(int nbSong) {
		this.nbSong = nbSong;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Bitmap getArt() {
		return art;
	}
	public void setArt(Bitmap art) {
		this.art = art;
	}
	
}
