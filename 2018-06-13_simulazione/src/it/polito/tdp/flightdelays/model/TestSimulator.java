package it.polito.tdp.flightdelays.model;


public class TestSimulator {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Simulator sim = new Simulator();
		Model model = new Model();
		
		Airline airline = new Airline ("AA", "American Airlines Inc.");
		model.inizListe(airline);
		//System.out.println(model.getFlights().size());
		
		sim.init(3, 4, model.getFlights());
		System.out.println("inizializzazione avvenuta con successo");
		sim.run();
		System.out.println("run avvenuto con successo");

		System.out.println("Lista ritardi: " + sim.getRitardoTotPerPasseggero());
		
	}

}
