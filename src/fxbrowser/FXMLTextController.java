package fxbrowser;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;

/**
 * FXML Controller class
 *
 * @author jpc
 */
public class FXMLTextController implements Initializable {
    
    @FXML
    private TextArea texto;
    
    
    public void setText(List<String> t) {
        texto.clear();
        for (String s : t) {
            texto.appendText(s+'\n');
        }
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
