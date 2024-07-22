package ci.pigier.controllers.ui;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.control.TableView;


import ci.pigier.DatabaseConnection;
import ci.pigier.controllers.BaseController;
import ci.pigier.model.Note;
import ci.pigier.ui.FXMLPage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;


public class AddEditUIController extends BaseController implements Initializable {

    @FXML
    private TextArea descriptionTxtArea;

    @FXML
    private Button saveBtn;
    
    @FXML
    private TableView<Note> notesListTable;

    @FXML
    private TextField titleTxtFld;

    @FXML
    void doBack(ActionEvent event) throws IOException {
    	navigate(event, FXMLPage.LIST.getPage());
    }

    @FXML
    void doClear(ActionEvent event) {
        titleTxtFld.clear();
        descriptionTxtArea.clear();
    }

    @FXML
    void doSave(ActionEvent event) throws IOException {
        // Supprimer l'ancienne note de la liste si elle existe
        if (Objects.nonNull(editNote)) {
            data.remove(editNote);
        }
        
        // Vérifier si les champs sont vides
        if (titleTxtFld.getText().trim().isEmpty() || descriptionTxtArea.getText().trim().isEmpty()) {
            alert = new Alert(AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Invalid data to save or update!");
            alert.setContentText("Note title or description cannot be empty!");
            alert.showAndWait();
            return;
        }
        
        // Créer ou mettre à jour la note dans la base de données
        if (Objects.nonNull(editNote)) {
            updateNoteInDatabase();
        } else {
            insertNoteInDatabase();
        }
        
        // Actualiser la liste observable data avec les données de la base de données
        loadNotesFromDatabase();
        
        // Navigation vers la liste après l'opération
        navigate(event, FXMLPage.LIST.getPage());
        

    }
    
    private void loadNotesFromDatabase() {
        data.clear(); // Effacer les données actuelles de la liste observable
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM notes")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String description = resultSet.getString("description");
                data.add(new Note(id, title, description));
            }
        } catch (SQLException e) {
            alertAndLogDatabaseError("Erreur lors du chargement des notes : " + e.getMessage());
        }
    }
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    if (Objects.nonNull(editNote)) {
	        titleTxtFld.setText(editNote.getTitle());
	        descriptionTxtArea.setText(editNote.getDescription());
	        saveBtn.setText("Mettre à jour");
	    }
	}
	
	private void insertNoteInDatabase() {
	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(
	                 "INSERT INTO notes (title, description) VALUES (?, ?)",
	                 Statement.RETURN_GENERATED_KEYS)) {

	        statement.setString(1, titleTxtFld.getText());
	        statement.setString(2, descriptionTxtArea.getText());

	        int affectedRows = statement.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Échec de l'insertion, aucune ligne ajoutée.");
	        }

	        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                int id = generatedKeys.getInt(1);
	                data.add(new Note(id, titleTxtFld.getText(), descriptionTxtArea.getText()));
	            } else {
	                throw new SQLException("Échec de la récupération de l'ID généré.");
	            }
	        }
	    } catch (SQLException e) {
	        alertAndLogDatabaseError("Erreur lors de l'insertion de la note : " + e.getMessage());
	    }
	}
	private void updateNoteInDatabase() {
	    try (Connection connection = DatabaseConnection.getConnection();
	         PreparedStatement statement = connection.prepareStatement(
	                 "UPDATE notes SET title = ?, description = ? WHERE id = ?")) {

	        statement.setString(1, titleTxtFld.getText());
	        statement.setString(2, descriptionTxtArea.getText());
	        statement.setInt(3, editNote.getId());

	        int affectedRows = statement.executeUpdate();
	        if (affectedRows == 0) {
	            throw new SQLException("Échec de la mise à jour, aucune ligne modifiée.");
	        }

	        editNote.setTitle(titleTxtFld.getText());
	        editNote.setDescription(descriptionTxtArea.getText());
	    } catch (SQLException e) {
	        alertAndLogDatabaseError("Erreur lors de la mise à jour de la note : " + e.getMessage());
	    }
	}

	private void alertAndLogDatabaseError(String message) {
	    alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Erreur");
	    alert.setHeaderText("Erreur de base de données");
	    alert.setContentText(message);
	    alert.showAndWait();
	}

}
