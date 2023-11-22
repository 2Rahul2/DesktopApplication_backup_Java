package org.openjfx.hellofx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

public class LoginController implements Initializable {
	@FXML
	Button submitBtn;
	@FXML
	TextField username;
	@FXML
	TextField password;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		submitBtn.setOnAction(e->{
			String name = username.getText();
			String pass = password.getText();
			Login loginObject = Login.getInstance();
			loginObject.checkCredentials(name, pass);
			System.out.println("submitted " +name +"  "+pass);
		});
	}
	public void createNewButton() {
		String url = "http://127.0.0.1:8000/signin/";
		App.getInstance().getHostServices().showDocument(url);
//		HostServices hostService = getHostServices();
	}
}
