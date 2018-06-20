package com.are.reparto;

public class Configuracion {
	private String nombre;
	private int posInicial;
	private int posFinal;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getPosInicial() {
		return posInicial;
	}
	public void setPosInicial(int posInicial) {
		this.posInicial = posInicial;
	}
	public int getPosFinal() {
		return posFinal;
	}
	public void setPosFinal(int posFinal) {
		this.posFinal = posFinal;
	}
	public Configuracion(String nombre, int posInicial, int posFinal) {
		super();
		this.nombre = nombre;
		this.posInicial = posInicial;
		this.posFinal = posFinal;
	}
	public Configuracion() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
