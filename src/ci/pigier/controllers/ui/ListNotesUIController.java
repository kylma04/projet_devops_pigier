package ci.pigier.controllers.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ci.pigier.controllers.BaseController;
import ci.pigier.model.Note;
import ci.pigier.ui.FXMLPage;
import javafx.collections.transformation.FilteredList;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    void doDelete(ActionEvent event) {
        Note selectedNote = notesListTable.getSelectionModel().getSelectedItem();
        if (selectedNote != null) {
            data.remove(selectedNote);
            updateNotesCountLabeldelete();
        }
    }
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
    void newNote(ActionEvent event) throws IOException {
    	editNote = null;
    	navigate(event, FXMLPage.ADD.getPage());
    }
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
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

}
