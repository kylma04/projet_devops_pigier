package ci.pigier.controllers.ui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Comparator;

import ci.pigier.model.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class NoteTests {


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
