package it.polito.tdp.flightdelays;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Model;
import it.polito.tdp.flightdelays.model.Simulator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FlightDelaysController {
	
	private Model model;
	Simulator sim = new Simulator();

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxLineaAerea"
    private ComboBox<Airline> cmbBoxLineaAerea; // Value injected by FXMLLoader

    @FXML // fx:id="caricaVoliBtn"
    private Button caricaVoliBtn; // Value injected by FXMLLoader

    @FXML // fx:id="numeroPasseggeriTxtInput"
    private TextField numeroPasseggeriTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="numeroVoliTxtInput"
    private TextField numeroVoliTxtInput; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML
    void doCaricaVoli(ActionEvent event) {
	    	Airline airline = this.cmbBoxLineaAerea.getValue();
	    	if(airline == null) {
	    		this.txtResult.setText("Selezionare una compagnia aerea!!");
	    		return;
	    	}
	    	model.creaGrafo(airline);
	    	this.txtResult.setText("Le 10 rotte peggiori sono:\n"+model.getDieciArchiPeggiori().toString());
    }

    @FXML
    void doSimula(ActionEvent event) {
    		String passeggeri = this.numeroPasseggeriTxtInput.getText();
    		String voli = this.numeroVoliTxtInput.getText();
    		if(passeggeri.isEmpty() || voli.isEmpty()) {
    			this.txtResult.setText("Inserire un valore sia per i passeggeri che per i voli\n");
    			return;
    		}
    		try {
    			int numPasseggeri = Integer.parseInt(passeggeri);
    			int numVoli = Integer.parseInt(voli);
    			sim.init(numPasseggeri, numVoli, model.getFlights());
    			this.txtResult.appendText(sim.getRitardoTotPerPasseggero().toString());
    			
    		}catch(NumberFormatException nfe) {
    			nfe.printStackTrace();
    			this.txtResult.setText("Inserire un valore corretto per i passeggeri o per i voli\n");
    			return;
    		}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxLineaAerea != null : "fx:id=\"cmbBoxLineaAerea\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert caricaVoliBtn != null : "fx:id=\"caricaVoliBtn\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroPasseggeriTxtInput != null : "fx:id=\"numeroPasseggeriTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert numeroVoliTxtInput != null : "fx:id=\"numeroVoliTxtInput\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'FlightDelays.fxml'.";

    }

	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
		this.cmbBoxLineaAerea.getItems().setAll(model.getAllAirline());
	}
    
    
}
