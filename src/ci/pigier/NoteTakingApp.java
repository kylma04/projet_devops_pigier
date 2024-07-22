package ci.pigier;

import java.sql.Connection;
import java.sql.SQLException;

import ci.pigier.ui.FXMLPage;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NoteTakingApp extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
		Connection connection = DatabaseConnection.getConnection();

        try {
            // Fermer la connexion
            connection.close();
            System.out.println("Connexion fermée avec succès.");

        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
        }
    }
	
	@Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(FXMLPage.LIST.getPage());
        
        Scene scene = new Scene(root);
     
        stage.setScene(scene);
        stage.setTitle("Application DE PRISE DE NOTE v1.0.0");
        stage.setResizable(false);
        stage.show();
    }
    
}
