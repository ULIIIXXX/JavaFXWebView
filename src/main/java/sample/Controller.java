package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller  implements Initializable {

    @FXML
    private Button btn_exit;
    @FXML
    private Button btn_util;
    @FXML
    private WebView web_content;
    @FXML
    private TextField txt_area;

    private String address;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            web_content.getEngine().load("http://google.com");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void btn_util(ActionEvent actionEvent) {
       loadLocalHtml();
    }

    public void btn_exit(ActionEvent actionEvent) {
        address = txt_area.getText();
        System.out.println("address = " + address);
        web_content.getEngine().load(address);
    }

    public void btn_reload(ActionEvent actionEvent){
        web_content.getEngine().reload();
    }

    public void btn_zoom(ActionEvent actionEvent){
        web_content.setZoom(1.25);
    }

    public void btn_font(ActionEvent actionEvent){
        web_content.setFontScale(1.25);
    }

    public void btn_user(ActionEvent actionEvent){
        web_content.getEngine().setUserAgent("Myapp Web Browser 1.0");
    }

    public void btn_context(ActionEvent actionEvent){
        web_content.setContextMenuEnabled(false);
    }

    public void btn_history(ActionEvent actionEvent){
        WebHistory history = web_content.getEngine().getHistory();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Historial");
        alert.setContentText(history.getEntries().toString());
        alert.showAndWait();
    }

    public void btn_browse(ActionEvent actionEvent){
        ObservableList<WebHistory.Entry> historial = web_content.getEngine().getHistory().getEntries();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Historial");
        alert.setContentText(historial.get(0).getTitle() + "\n"
                + historial.get(0).getUrl()+"\n" +
                historial.get(0).getLastVisitedDate());
        alert.showAndWait();
    }

    public void js(ActionEvent actionEvent){
      /*  String HTML = ""
                + "<html>"
                + "<body>"
                + "<button onclick='alert(\"Javascript Alert\")'>Submit</button>"
                + "</body>"
                + "</html>";
        WebEngine webEngine = web_content.getEngine();
        webEngine.setOnAlert(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Message from the web page");
            alert.setContentText(event.getData());
            alert.showAndWait();
        });
        webEngine.loadContent(HTML); */

        final WebEngine engine = web_content.getEngine();
       // engine.load("https://stackoverflow.com/questions/14029964/execute-a-javascript-function-for-a-webview-from-a-javafx-program");
        engine.load("https://www.lipsum.com/");

        engine.getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue ov, Worker.State oldState, Worker.State newState) {
                        if (newState == Worker.State.SUCCEEDED) {
                            engine.executeScript(
                                    "function highlightWord(root,word){"
                                            + "  textNodesUnder(root).forEach(highlightWords);"
                                            + ""
                                            + "  function textNodesUnder(root){"
                                            + "    var n,a=[],w=document.createTreeWalker(root,NodeFilter.SHOW_TEXT,null,false);"
                                            + "    while(n=w.nextNode()) a.push(n);"
                                            + "    return a;"
                                            + "  }"
                                            + ""
                                            + "  function highlightWords(n){"
                                            + "    for (var i; (i=n.nodeValue.indexOf(word,i)) > -1; n=after){"
                                            + "      var after = n.splitText(i+word.length);"
                                            + "      var highlighted = n.splitText(i);"
                                            + "      var span = document.createElement('span');"
                                            + "      span.style.backgroundColor='#f00';"
                                            + "      span.appendChild(highlighted);"
                                            + "      after.parentNode.insertBefore(span,after);"
                                            + "    }"
                                            + "  }"
                                            + "}"
                                            + "\n"
                                            + "highlightWord(document.body,'text');");

                        }
                    }
                });
    }

    public void btn_java(ActionEvent actionEvent){
        web_content.getEngine().getLoadWorker().stateProperty().addListener(
                new ChangeListener<Worker.State>() {
                    @Override
                    public void changed(ObservableValue<? extends Worker.State> observableValue, Worker.State state, Worker.State t1) {
                        if(t1 == Worker.State.SUCCEEDED){return;}

                        JSObject window = (JSObject) web_content.getEngine().executeScript(
                                "window"
                        );
                        window.setMember("myobject", new MyObject());

                    }
                }
        );
    }

    public void loadLocalHtml(){
        String  content = "<!doctype html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\"\n" +
                "          name=\"viewport\">\n" +
                "    <meta content=\"ie=edge\" http-equiv=\"X-UA-Compatible\">\n" +
                "    <title>Test Page</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<h1>Este es un titulo</h1>\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        web_content.getEngine().loadContent(content,"text/html");
    }

    public static class MyObject{
        public void doit(){
            System.out.println("Doit methos called");
        }
    }





}
