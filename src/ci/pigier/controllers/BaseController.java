package ci.pigier.controllers;

import java.io.IOException;
import java.net.URL;

import ci.pigier.model.Note;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class BaseController {
		protected Alert alert;
	protected static Note editNote = null;
	
	protected static ObservableList<Note> data = FXCollections.observableArrayList(
		    new Note(1, "Note 1", "Description de la note 1"),
		    new Note(2, "Note 2", "Description de la note 2"),
		    new Note(3, "Note 3", "Description de la note 3"),
		    new Note(4, "Note 4", "Description de la note 4"),
		    new Note(5, "Note 5", "Description de la note 5"));
	
			 
	protected void navigate(Event event, URL fxmlDocName) throws IOException {
		// Chargement du nouveau document FXML de l'interface utilisateur
		Parent pageParent = FXMLLoader.load(fxmlDocName);
		// Création d'une nouvelle scène
		Scene scene = new Scene(pageParent);
		// Obtention de la scène actuelle
		Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		// Masquage de l'ancienne scène (facultatif)
		appStage.hide(); // facultatif
		// Définition de la nouvelle scène pour la scène
		appStage.setScene(scene);
		// Affichage de la scène
		appStage.show();
	}
	
	
}
