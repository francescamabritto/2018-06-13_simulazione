package it.polito.tdp.flightdelays.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {
	
	private FlightDelaysDAO dao;
	private List<Airline> airlines;
	private List<Airport> airports;
	private List<Flight> flights;
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private FlightIdMap flightIdMap;
	
	public Model() {
		this.dao = new FlightDelaysDAO();
		this.airlines = dao.loadAllAirlines();
		this.airports = dao.loadAllAirports();
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.flightIdMap = new FlightIdMap();
	}
	
	
	public List<Airline> getAllAirline(){
		return airlines;
	}
	public List<Airport> getAllAirport(){
		return airports;
	}
	
	public Airport getAirportFromId(String id) {
		for(Airport a: this.airports) {
			if(a.getId().compareTo(id)==0)
				return a;
		}
		return null;
		
	}
	
	public void creaGrafo(Airline airline) {
		
		
		Graphs.addAllVertices(grafo, this.airports);
		
		this.flights = dao.loadFlightsFromAirline(airline, flightIdMap);
		
		
		for(Flight f: flights) {
			Airport a1 = this.getAirportFromId(f.getOriginAirportId());
			//System.out.println(a1);
			Airport a2 = this.getAirportFromId(f.getDestinationAirportId());
			//System.out.println(a2);
			grafo.addEdge(a1, a2);
		}
		
		System.out.println("vertici = "+ this.grafo.vertexSet().size() +"\narchi = "+this.grafo.edgeSet().size());
		
	}
	
	
	
}
