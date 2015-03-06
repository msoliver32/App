package com.app.campeonatovirtualdefutebol.pojo;

public class Competidor {
	
	private int id = 0;
	private String nomeApelido = "";
    private int time = 0;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getNomeApelido() {
		return nomeApelido;
	}
	
	public void setNomeApelido(String nomeApelido) {
		this.nomeApelido = nomeApelido;
	}

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
