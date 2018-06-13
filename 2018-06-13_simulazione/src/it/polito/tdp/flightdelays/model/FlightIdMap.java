package it.polito.tdp.flightdelays.model;

import java.util.HashMap;
import java.util.Map;

public class FlightIdMap {
	
	private Map<Integer, Flight> map;
	
	public FlightIdMap() {
		map = new HashMap<>();
	}
	
	public Flight get(Flight f) {
		Flight old = map.get(f.getId());
		if(old == null) {
			map.put(f.getId(), f);
			return f;
		}
		return old;
	}
	
	public void put(int id, Flight f) {
		map.put(id, f);
	}
	
	public Flight get(int id) {
		return map.get(id);
	}
	
	
	
	
	
}
