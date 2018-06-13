package it.polito.tdp.flightdelays.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {
	
	private FlightDelaysDAO dao;
	private List<Airline> airlines;
	private List<Airport> airports;
	private List<Flight> flights;
	private Graph<Airport, DefaultWeightedEdge> grafo;
	private FlightIdMap flightIdMap;
	private AirportIdMap airportIdMap;
	
	public Model() {
		this.dao = new FlightDelaysDAO();
		this.airlines = dao.loadAllAirlines();
		this.flightIdMap = new FlightIdMap();
		this.airportIdMap = new AirportIdMap();
	}
	
	
	public List<Airline> getAllAirline(){
		return airlines;
	}
	public List<Airport> getAirports(){
		return airports;
	}
	
	public void creaGrafo(Airline airline) {
		
		this.grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

		this.airports = dao.loadAllAirportsFromAirline(airline, airportIdMap);
		Graphs.addAllVertices(grafo, this.airports);
		
		this.flights = dao.loadFlightsFromAirline(airline);
		
		for(Flight f: flights) {
			Airport a1 = airportIdMap.get(f.getOriginAirportId());
			Airport a2 = airportIdMap.get(f.getDestinationAirportId());
			DefaultWeightedEdge edge = grafo.addEdge(a1, a2);
			if(edge!=null) {
			LatLng partenza = new LatLng(a1.getLatitude(), a1.getLongitude());
			LatLng arrivo = new LatLng(a2.getLatitude(), a2.getLongitude());
			
			double distanza = LatLngTool.distance(partenza, arrivo, LengthUnit.KILOMETER);
			double avgRitardo=0;
			int numFlightPerTratta =0;
			for(Flight f_delay: this.flights) {
				if(f_delay.getOriginAirportId().compareTo(a1.getId())==0 &&
						f_delay.getDestinationAirportId().compareTo(a2.getId())==0) {
					numFlightPerTratta++;
					avgRitardo += f_delay.getArrivalDelay();
				}	
			}
			avgRitardo = avgRitardo/numFlightPerTratta;
			// TODO 
			double peso = avgRitardo/distanza;
			
			grafo.setEdgeWeight(edge, peso);
			}
		}
		
		
		System.out.println("vertici = "+ this.grafo.vertexSet().size() +"\narchi = "+this.grafo.edgeSet().size());
		
	}
	
	
	
}
