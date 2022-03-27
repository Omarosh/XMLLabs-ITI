/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package finalxml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author EGC
 */
public class Finalxml extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml"));
        Parent root = (Parent) loader.load();
        FXMLDocumentController controller = (FXMLDocumentController) loader.getController();
        // FXMLDocumentController.FILENAME = controller.openNewFile(stage);
        controller.mystage = stage;
        // Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        // FXMLDocumentController controller=new FXMLDocumentController();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
