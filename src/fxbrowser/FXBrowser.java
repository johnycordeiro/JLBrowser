package fxbrowser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author jpc
 */
public class FXBrowser extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLBrowserView.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setWidth(0.8*bounds.getWidth());
        stage.setHeight(0.9*bounds.getHeight());
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
