package it.polito.tdp.flightdelays.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model model = new Model();
		Airline airline = new Airline ("AA", "American Airlines Inc.");
		
		// System.out.println(model.getAllAirport());
		// System.out.println("ok");
		model.creaGrafo(airline);
		System.out.println(model.getFlights());
	}

}
