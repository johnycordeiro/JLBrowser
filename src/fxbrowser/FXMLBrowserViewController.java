package fxbrowser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;



public class FXMLBrowserViewController implements Initializable {
    @FXML private ProgressBar progress;
    @FXML private ComboBox    cmbAddress;
    @FXML private Button      butGo;
    @FXML private Button      butBack;
    @FXML private TextField   txfAddress;
    @FXML private WebView     webView;
    
    WebEngine wengine;
    
    
    @FXML
    private void butBackAction(ActionEvent event) {
        Platform.runLater(() -> {
            wengine.executeScript("history.back()");
        });
    }
    
    @FXML
    private void butGoAction(ActionEvent event) {
        if (butGo.getText().equals("Go")) {
            String address = "";
            File f = new File(txfAddress.getText());
            if (f.exists()) {
                address = f.toURI().toString();
            } else {
                address = txfAddress.getText();
            }

            webView.getScene().setCursor(Cursor.WAIT);
            wengine.load(address);
            butGo.setText("X");
        }
        else {
            webView.getEngine().getLoadWorker().cancel();
            webView.getScene().setCursor(Cursor.DEFAULT);
            butGo.setText("Go");
        }
    }
    
    @FXML
    private void butTextAction(ActionEvent event) {
        List<String> list= extractTextFromPage();
        //list.forEach(s -> System.out.println(s));
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FXMLText.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage(StageStyle.UTILITY);
            stage.setTitle("Texto");
            stage.setScene(new Scene(root));
            stage.show();
            
            FXMLTextController controller = fxmlLoader.getController();
            controller.setText(list);
        } catch (IOException e) {
            System.err.println(e);
        }
    }
    
    
    List<String> extractTextFromPage() {
        Document d= webView.getEngine().getDocument();
        W3CDom   dom= new W3CDom();
        org.jsoup.nodes.Document doc= Jsoup.parse(dom.asString(d));
        Elements textElements= doc.select("p,li,h1,h2,h3,h4");
        ArrayList<String> list= new ArrayList<>();
        for (Element e : textElements) {
            if ( e.hasText() ) {
                String t= e.text().trim();
                if ( "h1|h2|h3|h4".contains(e.tag().toString())) {
                    list.add("\n["+t+"]\n");
                }
                else {
                    if (".;!?".indexOf(t.charAt(t.length() - 1)) >= 0) {
                        list.add(t);
                    }
                }
            }
        }
        return list;
    }   
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /**/
        wengine= webView.getEngine();
        wengine.getLoadWorker().stateProperty().addListener((obs,oldState,newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                // new page has loaded.
                webView.getScene().setCursor(Cursor.DEFAULT);
                butGo.setText("Go");
                //Sound.beep(500, 520, 0.3);
            }
        });
        /**/
        ObservableList<String> addresses = FXCollections.observableArrayList(
                "/Users/jpc/∑/kunstler.html",
                "http://bbc.com",
                "http://www.cnn.com",
                "https://www.nytimes.com",
                "http://www.google.com"
        );
        cmbAddress.setItems(addresses);
        cmbAddress.setValue(addresses.get(0));
        cmbAddress.setOnAction((event) -> {
            txfAddress.setText(cmbAddress.getValue().toString());
            butGoAction(null);
        });
        
        progress.progressProperty().bind(wengine.getLoadWorker().progressProperty());
        progress.setStyle("-fx-accent: yellow;");
        //progress.setProgress(0);
    }    
    
}
