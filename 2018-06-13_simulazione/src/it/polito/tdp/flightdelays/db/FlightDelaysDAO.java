package it.polito.tdp.flightdelays.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.AirportIdMap;
import it.polito.tdp.flightdelays.model.Flight;
import it.polito.tdp.flightdelays.model.Rotta;

public class FlightDelaysDAO {

	public List<Airline> loadAllAirlines() {
		String sql = "SELECT id, airline from airlines";
		List<Airline> result = new ArrayList<Airline>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.add(new Airline(rs.getString("ID"), rs.getString("airline")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirports() {
		String sql = "SELECT id, airport, city, state, country, latitude, longitude FROM airports";
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
						rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
				result.add(airport);
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights() {
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, "
				+ "arrival_date, departure_delay, arrival_delay, air_time, distance FROM flights";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				result.add(flight);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Flight> loadFlightsFromAirline(Airline airline) {
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, "
				+ "arrival_date, departure_delay, arrival_delay, air_time, distance FROM flights WHERE airline = ? "+
				"and origin_airport_id in (select id from airports) "+
				"and destination_airport_id in (select id from airports)";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, airline.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Flight f = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
						rs.getString("origin_airport_id"), rs.getString("destination_airport_id"),
						rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
						rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
						rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
				result.add(f);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Rotta> getAllRotte(Airline airline, AirportIdMap airportIdMap){
		String sql = "SELECT origin_airport_id, destination_airport_id, AVG(arrival_delay) as avg "
				+ "FROM flights WHERE airline = ? "
				+ "AND origin_airport_id IN (SELECT id FROM airports) "
				+ "AND destination_airport_id IN (SELECT id FROM airports) "
				+ "GROUP BY origin_airport_id, destination_airport_id";
		List<Rotta> result = new LinkedList<Rotta>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, airline.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Rotta r = new Rotta(airportIdMap.get(rs.getString("origin_airport_id")),
						airportIdMap.get(rs.getString("destination_airport_id")), 
						rs.getDouble("avg"), airline);
				
				result.add(r);
			}
			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Airport> loadAllAirportsFromAirline(Airline airline, AirportIdMap airportIdMap) {
		String sql = "SELECT id, airport, city, state, country, latitude, longitude " + 
				"FROM airports " + 
				"where id in (SELECT f.ORIGIN_AIRPORT_ID FROM flights f WHERE airline = ?) " + 
				"or id in (SELECT f.DESTINATION_AIRPORT_ID FROM flights f WHERE airline = ?) ";
		List<Airport> result = new ArrayList<Airport>();
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, airline.getId());
			st.setString(2, airline.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
						rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
				result.add(airportIdMap.getOrPutNew(airport));
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		
	
	}
	
	
	/*public boolean esisteVolo(Flight f, Airport a1, Airport a2) {
		String sql = "SELECT count(*) as cnt"
				+ "FROM flights as f, airports as a1, airports as a2 "
				+ "WHERE f.AIRLINE = '?' "
				+ "AND a1.ID = '?' "
				+ "AND a2.ID = '?' "
				+ "AND f.ORIGIN_AIRPORT_ID = a1.ID" 
				+ "AND f.DESTINATION_AIRPORT_ID = a2.ID";
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, f.getAirlineId());
			st.setString(2, a1.getId());
			st.setString(3, a2.getId());
			ResultSet rs = st.executeQuery();

			rs.first();
			int count = rs.getInt("cnt");
			
			conn.close();
			if(count == 0)
				return false;
			else
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}*/
	
	
	
}
