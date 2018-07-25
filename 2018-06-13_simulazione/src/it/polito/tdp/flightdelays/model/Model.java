package it.polito.tdp.flightdelays.model;

import java.util.Collections;
import java.util.LinkedList;
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
	private List<Rotta> rotte;
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private AirportIdMap airportIdMap;
	List<Double> listaPesi;
	List<DefaultWeightedEdge> archiPeggiori;
	
	public Model() {
		this.dao = new FlightDelaysDAO();
		this.airlines = dao.loadAllAirlines();
		this.airportIdMap = new AirportIdMap();
		this.archiPeggiori = new LinkedList<>();
		this.listaPesi = new LinkedList<>();
	}
	
	
	public List<Airline> getAllAirline(){
		return airlines;
	}
	public List<Airport> getAirports(){
		return airports;
	}
	public List<Flight> getFlights(){
		return flights;
	}
	
	public void inizListe(Airline airline) {
		this.flights = dao.loadFlightsFromAirline(airline);
	}
	
	public void creaGrafo(Airline airline) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.airports = dao.loadAllAirportsFromAirline(airline, airportIdMap);
		this.rotte = dao.getAllRotte(airline, airportIdMap);
		this.flights = dao.loadFlightsFromAirline(airline);
		
		Graphs.addAllVertices(grafo, this.airports);
		
		for(Rotta r: rotte) {
			Airport a1 = r.getOrigin_airport();
			Airport a2 = r.getDestination_airport();
			DefaultWeightedEdge edge = grafo.addEdge(a1, a2);
			if(edge != null) {
				double peso = r.getRitardoMedio()/r.getDistanza();
				grafo.setEdgeWeight(edge, peso);
				listaPesi.add(peso);
			}
		}
		
		Collections.sort(listaPesi);

		for(int i=listaPesi.size()-1; i>listaPesi.size()-10-1; i--) {
			for(DefaultWeightedEdge e: grafo.edgeSet()) {
				if(grafo.getEdgeWeight(e) == listaPesi.get(i)) {
					archiPeggiori.add(e);
				}
			}	
		}
		
	}
	public List<DefaultWeightedEdge> getDieciArchiPeggiori() {
		return this.archiPeggiori;
	}
	
	
	
	
}