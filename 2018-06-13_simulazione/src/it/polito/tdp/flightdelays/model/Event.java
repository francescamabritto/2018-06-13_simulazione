package it.polito.tdp.flightdelays.model;

import it.polito.tdp.flightdelays.model.Simulator.EventType;

public class Event implements Comparable <Event>{
	
	private Passeggero passeggero;
	private EventType tipo;
	
	public Event(Passeggero passeggero, EventType tipo) {
		super();
		this.passeggero = passeggero;
		this.tipo = tipo;
	}

	public Passeggero getPasseggero() {
		return passeggero;
	}

	public EventType getTipo() {
		return tipo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((passeggero == null) ? 0 : passeggero.hashCode());
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
		Event other = (Event) obj;
		if (passeggero == null) {
			if (other.passeggero != null)
				return false;
		} else if (!passeggero.equals(other.passeggero))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return passeggero + ", " + tipo;
	}

	@Override
	public int compareTo(Event o) {
		return this.passeggero.getNumVoli()-o.passeggero.getNumVoli();
	}

	
	
	
}

