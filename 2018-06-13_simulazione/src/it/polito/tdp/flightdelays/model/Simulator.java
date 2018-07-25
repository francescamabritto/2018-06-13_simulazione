package it.polito.tdp.flightdelays.model;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;



public class Simulator {

	
	enum EventType{
		PARTENZA,
		ARRIVO
	}
	
	// Coda degli eventi
	private PriorityQueue<Event> queue = new PriorityQueue<>();
		
	// Parametri di simulazione 
	private List<Passeggero> listaPasseggeri;
	
	
	// Modello del mondo
	private int passeggeri;
	private int voli;
	private List<Flight> listaVoli;
	
	// Variabili output
	private List<Integer> ritardoTot;
	
	public void init(int k , int v, List<Flight> listaVoliInput) {
		
		this.passeggeri = k;
		this.voli = v;
		this.listaPasseggeri = new LinkedList<>();
		this.listaVoli = listaVoliInput;
		this.ritardoTot = new LinkedList<>();
		
//		posizionare K passeggeri in modo casuale tra gli aeroporti disponibili
		for(int i=0; i<this.passeggeri; i++) {
			Flight f = this.generateRandomFlight();
			Passeggero p = new Passeggero(i, f, f.getArrivalDelay(), 1);
			this.listaPasseggeri.add(p);
			Event e = new Event(p, EventType.ARRIVO);
			queue.add(e);			
		}
		System.out.println("fine inizializzazione");
		
	}
	
	public void run() {
		Event e;
		while((e = queue.poll()) != null) {
			if(e.getPasseggero().getNumVoli() == voli || this.voliDisponibili(e.getPasseggero().getFlight())==false)
				break;
			processEvent(e);
			System.out.println(e);
		}
	}
	
	private void processEvent(Event e) {
		switch(e.getTipo()) {
		case PARTENZA:
			Flight f = this.nextFlight(e.getPasseggero().getFlight());
			e.getPasseggero().setFlight(f);
			e.getPasseggero().setNumVoli(e.getPasseggero().getNumVoli()+1);
			Event viaggio = new Event(e.getPasseggero(), EventType.ARRIVO);
			queue.add(viaggio);
			break;
			
		case ARRIVO:
			for(Passeggero p: this.listaPasseggeri) {
				if(p.getId() == e.getPasseggero().getId()) {
					int ritardo = e.getPasseggero().getFlight().getArrivalDelay() + e.getPasseggero().getRitardo();	
					p.setRitardo(ritardo);
				}
			}
			Event riparte = new Event(e.getPasseggero(), EventType.PARTENZA);
			queue.add(riparte);
			break;
		}
	}

	private boolean voliDisponibili(Flight flight) {
		
		for(Flight f: this.listaVoli) {
			LocalDateTime oraPartenzaVolo = f.getScheduledDepartureDate().plusMinutes(f.getDepartureDelay()) ; 
			if(flight.getDestinationAirportId().compareTo(f.getDestinationAirportId())==0
					&& flight.getArrivalDate().isBefore(oraPartenzaVolo)) {
				return true;
			}
		}
		return false;
	}


/**
 * Trova il prossimo volo che parte dall'aeroporto di arrivo del volo precedente
 * @param old
 * @return
 */
	private Flight nextFlight(Flight old) {
		LocalDateTime oraArrivo = old.getArrivalDate();
		LocalDateTime nuovaOraPartenza = LocalDateTime.MAX;
		Flight newFlight = null;
		for(Flight f: this.listaVoli) {
			LocalDateTime oraPartenzaVolo = f.getScheduledDepartureDate().plusMinutes(f.getDepartureDelay()) ; 
			if(old.getDestinationAirportId().compareTo(f.getDestinationAirportId())==0) {
				if(oraPartenzaVolo.isBefore(nuovaOraPartenza) && oraPartenzaVolo.isAfter(oraArrivo)) {
					nuovaOraPartenza = oraPartenzaVolo;
					newFlight = f;
				}
			}
		}
		return newFlight;
	}

	public Flight generateRandomFlight() {
		int n = (int) (Math.random() * listaVoli.size());
		return listaVoli.get(n);
	}
	
	public List<Integer> getRitardoTotPerPasseggero(){
		for(Passeggero p: this.listaPasseggeri) {
//			System.out.println(p.getRitardo());
//			System.out.println(p.getFlight());
			ritardoTot.add(p.getRitardo());
		}
		return ritardoTot;
	}
}
