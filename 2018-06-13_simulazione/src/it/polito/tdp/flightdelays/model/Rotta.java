package it.polito.tdp.flightdelays.model;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class Rotta {
	private Airport origin_airport;
	private Airport destination_airport;
	private double distanza;
	private double ritardoMedio;
	private Airline airline;
	
	public Rotta(Airport origin_airport, Airport destination_airport, double ritardoMedio, Airline airline) {
		super();
		this.origin_airport = origin_airport;
		this.destination_airport = destination_airport;
		this.ritardoMedio = ritardoMedio;
		this.airline = airline;
		LatLng partenza = new LatLng(this.origin_airport.getLatitude(), this.origin_airport.getLongitude());
		LatLng arrivo = new LatLng(this.destination_airport.getLatitude(), this.destination_airport.getLongitude());
		this.distanza = LatLngTool.distance(partenza, arrivo, LengthUnit.KILOMETER);
	}

	public Airport getOrigin_airport() {
		return origin_airport;
	}

	public Airport getDestination_airport() {
		return destination_airport;
	}

	public double getDistanza() {
		return distanza;
	}

	public double getRitardoMedio() {
		return ritardoMedio;
	}

	public Airline getAirline() {
		return airline;
	}

	@Override
	public String toString() {
		return origin_airport.getId() + ", " + destination_airport.getId() + ", " + distanza + ", " + ritardoMedio + ", " + airline;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((airline == null) ? 0 : airline.hashCode());
		result = prime * result + ((destination_airport == null) ? 0 : destination_airport.hashCode());
		result = prime * result + ((origin_airport == null) ? 0 : origin_airport.hashCode());
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
		Rotta other = (Rotta) obj;
		if (airline == null) {
			if (other.airline != null)
				return false;
		} else if (!airline.equals(other.airline))
			return false;
		if (destination_airport == null) {
			if (other.destination_airport != null)
				return false;
		} else if (!destination_airport.equals(other.destination_airport))
			return false;
		if (origin_airport == null) {
			if (other.origin_airport != null)
				return false;
		} else if (!origin_airport.equals(other.origin_airport))
			return false;
		return true;
	}
	
	
	
	
}
