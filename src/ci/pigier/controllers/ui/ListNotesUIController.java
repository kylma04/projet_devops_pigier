package ci.pigier.controllers.ui;


import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.util.Collections;
import java.util.Comparator;
//import java.util.Objects;
import java.util.ResourceBundle;

import ci.pigier.DatabaseConnection;
import ci.pigier.controllers.BaseController;
import ci.pigier.model.Note;
import ci.pigier.ui.FXMLPage;
import javafx.collections.transformation.FilteredList;
//import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
//import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ListNotesUIController extends BaseController implements Initializable {

    @FXML
    private TableColumn<Note, String> descriptionTc; // Correction du type générique

    @FXML
    private Label notesCount;

    @FXML
    private TableView<Note> notesListTable;

    @FXML
    private TextField searchNotes;

    @FXML
    private TableColumn<Note, String> titleTc; // Correction du type générique
    
    @FXML
    private boolean ascendingOrder = true;

    @FXML
    void doDelete(ActionEvent event) {
        Note selectedNote = notesListTable.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            deleteNoteFromDatabase(selectedNote.getId()); // Supprimer la note sélectionnée de la base de données
            data.remove(selectedNote); // Supprimer la note de la liste observable
            updateNotesCountLabel(); // Mettre à jour le compteur de notes
        }
    }
    
    @FXML
    //
    private void updateNotesCountLabeldelete() {
        int count = notesListTable.getItems().size();
        notesCount.setText(count + (count == 1 ? " Note" : " Notes"));
    }



    @FXML
    void doEdit(ActionEvent event) throws IOException {
        editNote = notesListTable.getSelectionModel().getSelectedItem();
        if (editNote != null) {
            navigate(event, FXMLPage.ADD.getPage());
        }
    }
    @FXML
    private ObservableList<Note> data = FXCollections.observableArrayList();

    @FXML
    void newNote(ActionEvent event) throws IOException {
    	editNote = null;
    	navigate(event, FXMLPage.ADD.getPage());
    }
    
    @FXML
    void handleSortButtonClicked(ActionEvent event) {
        if (ascendingOrder) {
            // Tri par ordre croissant (ignore la casse)
            Collections.sort(data, Comparator.comparing(Note::getTitle, String.CASE_INSENSITIVE_ORDER));
        } else {
            // Tri par ordre décroissant (ignore la casse)
            Collections.sort(data, Comparator.comparing(Note::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
        }

        // Inverser l'ordre pour le prochain clic
        ascendingOrder = !ascendingOrder;

        // Rafraîchir la TableView pour refléter les changements
        notesListTable.refresh();
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	// Charger les notes depuis la base de données
    	loadNotesFromDatabase();
    	
    	// Créer un filtre pour les données de la liste
        FilteredList<Note> filteredData = new FilteredList<>(data, n -> true);
        notesListTable.setItems(filteredData);
        titleTc.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTc.setCellValueFactory(new PropertyValueFactory<>("description"));

        // Mettre à jour le compteur de notes lors du filtrage
        filteredData.addListener((ListChangeListener.Change<? extends Note> change) -> {
            updateNotesCountLabel(filteredData);
        });

        searchNotes.setOnKeyReleased(e -> {
            filteredData.setPredicate(n -> {
                if (searchNotes.getText() == null || searchNotes.getText().isEmpty())
                    return true;
                String lowerCaseFilter = searchNotes.getText().toLowerCase();
                return n.getTitle().toLowerCase().contains(lowerCaseFilter)
                        || n.getDescription().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // Mettre à jour le compteur initial
        updateNotesCountLabel(filteredData);
    }
    
    private void updateNotesCountLabel(FilteredList<Note> filteredData) {
        int count = filteredData.size();
        notesCount.setText(count + (count == 1 ? " Note" : " Notes"));
    }
    
    private void loadNotesFromDatabase() {
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
    
    private void deleteNoteFromDatabase(int noteId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM notes WHERE id = ?")) {

            statement.setInt(1, noteId);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la suppression, aucune ligne supprimée.");
            }
        } catch (SQLException e) {
            alertAndLogDatabaseError("Erreur lors de la suppression de la note : " + e.getMessage());
        }
    }
    
    private void updateNotesCountLabel() {
        int count = data.size();
        notesCount.setText(count + (count == 1 ? " Note" : " Notes"));
    }
    
    private void alertAndLogDatabaseError(String message) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Erreur de base de données");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
