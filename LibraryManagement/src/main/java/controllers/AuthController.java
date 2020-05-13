package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import libs.HibernateUtils;
import entities.Address;
import entities.Credential;
import entities.Role;
import entities.User;
import libs.PasswordMD5;
import libs.Utils;

public class AuthController implements Initializable {
	@FXML
    private Button submitLogin;
	
	@FXML
	private TextField userId;
	
	@FXML
	private TextField password;
	
	@FXML
	private Text errorMessage;
	
	@FXML
	public void submitLogin(ActionEvent event) {
		
		if(!Utils.isNumeric(userId.getText())) {
			errorMessage.setText("User ID must be number!");
			return;
		}
		
		SessionFactory factory = HibernateUtils.getSessionFactory();
		Session session = factory.getCurrentSession();
		try {
			session.getTransaction().begin();
			Credential credential = session.get(Credential.class, Integer.parseInt(userId.getText()));
			
			if(credential != null && credential.isCredential(password.getText())) {
				
				errorMessage.setText("");
				
				Stage primaryStage = (Stage) ((Node)event.getSource()).getScene().getWindow();
				
				FXMLLoader loader = null;
				
				loader = new FXMLLoader(getClass().getResource("/views/Main.fxml"));
				
				Scene scene = new Scene((BorderPane)loader.load(),1300,920);
				
				MainController controller = loader.<MainController>getController();
				
				controller.setUser(credential.getUser());
				scene.getStylesheets().add(getClass().getResource("/assets/css/application.css").toExternalForm());
				primaryStage.setScene(scene);
				session.close();
			} else {
				errorMessage.setText("Invalid User id and Password!");
				session.close();
			}
		} catch (Exception e) {
			errorMessage.setText("Something was wrong!");
			session.close();
		}
	}


	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
}
