package it.polito.tdp.flightdelays.model;

import java.util.HashMap;

public class AirportIdMap extends HashMap<String, Airport> {
	
	public Airport getOrPutNew(Airport cercato) {
		Airport old = this.get(cercato.getId());
		if(old != null) {
			return old;
		}else {
			this.put(cercato.getId(), cercato);
			return cercato;
		}
	}
	
}
