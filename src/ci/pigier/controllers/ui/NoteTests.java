package ci.pigier.controllers.ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Comparator;

import ci.pigier.controllers.ui.ListNotesUIController;
import ci.pigier.model.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class NoteTests {

    private ListNotesUIController controller;

//    @BeforeEach
//    public void setUp() {
//        // Initialisation du contrôleur ou des dépendances nécessaires
//        controller = new ListNotesUIController();
//    }
//
//    @Test
//    public void testLoadNotesFromDatabase() {
//        controller.loadNotesFromDatabase();
//        // Vérifier si les notes ont été chargées correctement depuis la base de données
//        assertFalse(controller.getData().isEmpty());
//    }
//
//    @Test
//    public void testInsertNoteInDatabase() {
//        // Simuler l'insertion d'une nouvelle note
//        Note newNote = new Note("Test Title", "Test Description");
//        controller.getData().add(newNote);
//        // Vérifier si l'insertion dans la base de données s'est déroulée correctement
//        assertTrue(controller.getData().contains(newNote));
//    }
//
//    @Test
//    public void testUpdateNoteInDatabase() {
//        // Simuler la mise à jour d'une note existante
//        Note noteToUpdate = controller.getData().get(0); // Supposons que la première note soit mise à jour
//        String newDescription = "Updated Description";
//        noteToUpdate.setDescription(newDescription);
//        controller.updateNoteInDatabase(); // Appel à la méthode de mise à jour
//        // Vérifier si la description de la note a été mise à jour dans la base de données
//        assertEquals(newDescription, noteToUpdate.getDescription());
//    }
//
//    @Test
//    public void testDeleteNoteFromDatabase() {
//        // Supprimer une note existante de la base de données
//        Note noteToDelete = controller.getData().get(0); // Supposons que la première note soit supprimée
//        controller.deleteNoteFromDatabase(noteToDelete.getId()); // Appel à la méthode de suppression
//        // Vérifier si la note a été correctement supprimée de la base de données
//        assertFalse(controller.getData().contains(noteToDelete));
//    }
//
//    @Test
//    public void testSortNotesByTitleAndDescription() {
//        // Simuler le tri des notes par titre et description
//        controller.sortNotesByTitleAndDescription(true); // Tri croissant
//        // Vérifier si les notes sont triées par titre et description dans l'ordre croissant
//        assertTrue(isSorted(controller.getData(), true));
//
//        controller.sortNotesByTitleAndDescription(false); // Tri décroissant
//        // Vérifier si les notes sont triées par titre et description dans l'ordre décroissant
//        assertTrue(isSorted(controller.getData(), false));
//    }
//
//    // Méthode utilitaire pour vérifier si la liste de notes est triée par titre et description
//    private boolean isSorted(ObservableList<Note> notes, boolean ascending) {
//        boolean sorted = true;
//        for (int i = 1; i < notes.size(); i++) {
//            Note prev = notes.get(i - 1);
//            Note current = notes.get(i);
//            if (ascending) {
//                if (prev.getTitle().compareTo(current.getTitle()) > 0 ||
//                    (prev.getTitle().equals(current.getTitle()) && prev.getDescription().compareTo(current.getDescription()) > 0)) {
//                    sorted = false;
//                    break;
//                }
//            } else {
//                if (prev.getTitle().compareTo(current.getTitle()) < 0 ||
//                    (prev.getTitle().equals(current.getTitle()) && prev.getDescription().compareTo(current.getDescription()) < 0)) {
//                    sorted = false;
//                    break;
//                }
//            }
//        }
//        return sorted;
//    }
    //Test d'ajout d'une note
    @Test
    public void testAddNote() {
        Note note = new Note("Titre de test", "Description de test");

        assertNotNull(note);
        assertEquals("Titre de test", note.getTitle());
        assertEquals("Description de test", note.getDescription());
    }
    
    //Test de suppression d'une note
    @Test
    public void testRemoveNote() {
        ObservableList<Note> data = FXCollections.observableArrayList();
        Note noteToRemove = new Note("Titre à supprimer", "Description à supprimer");
        data.add(noteToRemove);

        assertTrue(data.contains(noteToRemove));
        data.remove(noteToRemove);
        assertFalse(data.contains(noteToRemove));
    }
    //Test de mise à jour d'une note
    @Test
    public void testUpdateNote() {
        Note note = new Note("Titre initial", "Description initiale");
        assertEquals("Titre initial", note.getTitle());
        assertEquals("Description initiale", note.getDescription());

        note.setTitle("Nouveau titre");
        note.setDescription("Nouvelle description");

        assertEquals("Nouveau titre", note.getTitle());
        assertEquals("Nouvelle description", note.getDescription());
    }
    
    //Test de tri des notes
    @Test
    public void testSortNotes() {
        ObservableList<Note> data = FXCollections.observableArrayList();
        Note note1 = new Note("B", "Description B");
        Note note2 = new Note("A", "Description A");
        Note note3 = new Note("C", "Description C");

        data.addAll(note1, note2, note3);

        // Test de tri par ordre croissant des titres
        Collections.sort(data, Comparator.comparing(Note::getTitle));
        assertEquals("A", data.get(0).getTitle());
        assertEquals("B", data.get(1).getTitle());
        assertEquals("C", data.get(2).getTitle());

        // Test de tri par ordre décroissant des titres
        Collections.sort(data, Comparator.comparing(Note::getTitle).reversed());
        assertEquals("C", data.get(0).getTitle());
        assertEquals("B", data.get(1).getTitle());
        assertEquals("A", data.get(2).getTitle());
    }
    
    //Test de filtrage des notes
    
    @Test
    public void testFilterNotes() {
        ObservableList<Note> data = FXCollections.observableArrayList();
        data.add(new Note("Titre1", "Description1"));
        data.add(new Note("Titre2", "Description2"));
        data.add(new Note("Titre3", "Description3"));

        FilteredList<Note> filteredData = new FilteredList<>(data, n -> true);
        assertEquals(3, filteredData.size());

        // Filtrer par titre
        filteredData.setPredicate(n -> n.getTitle().contains("2"));
        assertEquals(1, filteredData.size());
        assertEquals("Titre2", filteredData.get(0).getTitle());

        // Filtrer par description
        filteredData.setPredicate(n -> n.getDescription().contains("3"));
        assertEquals(1, filteredData.size());
        assertEquals("Titre3", filteredData.get(0).getTitle());
    }
    
    
}
