package com.emerald.containers;

import java.io.Serializable;

import android.view.View;

public class Artist implements Serializable {
	private static final long serialVersionUID = -6690962827108846833L;
	private int id;
	private String name;
	private String key;
	private int nbAlbum;
	
	public Artist(int _id, String _key, String _name, int _nbAlbum) {
		// TODO Auto-generated constructor stub
		this.id = _id;
		this.name = _name;
		this.nbAlbum = _nbAlbum;
		this.key = _key;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getNbAlbum() {
		return nbAlbum;
	}
	public void setNbAlbum(int i) {
		this.nbAlbum = i;
	}
	public View getCategory() {
		// TODO Auto-generated method stub
		return null;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
}
