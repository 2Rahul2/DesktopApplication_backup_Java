package org.openjfx.hellofx;

import java.awt.Desktop;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
public class LoginController implements Initializable {
	@FXML
	Button submitBtn;
	@FXML
	TextField username;
	@FXML
	TextField password;
	@FXML
	Label errorText;
	@FXML
	Hyperlink clickhere;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		username.setStyle("-fx-background-color: #464a5c; -fx-text-fill: white;");
		password.setStyle("-fx-background-color: #464a5c; -fx-text-fill: white;");
		username.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER) {
				password.requestFocus();
			}
		});
		
		password.setOnKeyPressed(e->{
			if(e.getCode() == KeyCode.ENTER) {
				checkLogin();
			}
		});
		submitBtn.setOnAction(e->{
			checkLogin();
		});
		clickhere.setOnAction(e->{
			openBrowser();
		});
	}
	void openBrowser() {
		getServerUrl geturl = new getServerUrl();
		String url_string = geturl.getRootUrl();
		try {
			Desktop.getDesktop().browse(new URI(url_string+"signin/"));
		}catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Unable to open Browser");
			alert.getDialogPane().setGraphic(null);
			Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
			Image customIcon = new Image(getClass().getResourceAsStream("no-wifi.png"));
			alertStage.getIcons().add(customIcon);
			alert.setContentText("Visit This Link : "+"https://updata.onrender.com/signin/");
			alert.showAndWait();
		}
	}
	void checkLogin() {
		String name = username.getText();
		String pass = password.getText();
		Login newLogin = Login.getInstance();
		newLogin.checkCredentials(name, pass);
		if(!newLogin.canLogin) {
			errorText.setText("User does not exist");
		}
		System.out.println("submitted " +name +"  "+pass);
	}
}
