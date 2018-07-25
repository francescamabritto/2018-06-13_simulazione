package it.polito.tdp.flightdelays.model;

public class Passeggero {
	
	private int id;
	private Flight flight;
	private int ritardo;
	private int numVoli;

	public Passeggero(int id, Flight flight, int ritardo, int numVoli) {
		super();
		this.id = id;
		this.flight = flight;
		this.ritardo = ritardo;
		this.numVoli = numVoli;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Flight getFlight() {
		return flight;
	}
	public void setFlight(Flight flight) {
		this.flight = flight;
	}
	public int getRitardo() {
		return ritardo;
	}
	public void setRitardo(int ritardo) {
		this.ritardo = ritardo;
	}
	public int getNumVoli() {
		return numVoli;
	}
	public void setNumVoli(int numVoli) {
		this.numVoli = numVoli;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Passeggero other = (Passeggero) obj;
		if (id != other.id)
			return false;
		return true;
	}
	@Override
	public String toString() {
		//return id + ", " + flight + ", " + ritardo;
		return id + " ritardo =  "+ritardo;
	}
	
	
}
