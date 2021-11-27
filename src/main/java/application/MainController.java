package application;


import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.List;

import javafx.scene.control.*;
import Ethereum.ProcessTemplate;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import translator.Translator;
import utils.BlockchainUtils;


public class MainController implements Initializable {



	@FXML
	private Button  Button_load_model,Button_set_contract, Button_execute, button_default_model, Button_update_model, Button_rule_list, Button_getRule, Button_getVariable, Button_messageState;

	@FXML
	private TextField loaded_model_path, Text_messageID_query, Text_input_query, updated_model_path,
			TextField_contract_address,Textfield_messageID_get, Textfield_variable_name, Textfield_variable_results,
			TextField_monitor_results;

	@FXML
	private TextArea Text_area;

	@FXML
	private ProgressIndicator progress;

	@FXML
	private ChoiceBox<String> Choice_variable_type;
	
	@FXML
	private VBox boxID;

	Alert a = new Alert(AlertType.NONE);
	BlockchainUtils u = new BlockchainUtils();

	public void getPastMessages(ActionEvent event) throws Exception {

		a.setContentText("You selected contract:  " + u.getContractAddress());
		a.setAlertType(AlertType.CONFIRMATION);
		//String messageId = Textfield_messageID_get.getText();
		HashMap<String, List<String>> pastRules = u.pastRules();
		String finalText = " ";
		for (Map.Entry<String, List<String>> set  : pastRules.entrySet()) {
			finalText+="Message sent with ID: " + set.getKey();
			finalText+=" and with value: ";
			for (String singleValue : set.getValue()) {
				finalText+= singleValue + ", ";
			}

		}
		//0xa0b2Dca9F0bFe557707Cb863b44D4eAC1367C214
		Text_area.setText(finalText);
		a.show();
	}

	public void getPastUpdates(ActionEvent event) throws Exception {
		a.setContentText("You selected contract:  " + u.getContractAddress());
		a.setAlertType(AlertType.CONFIRMATION);
		//String messageId = Textfield_messageID_get.getText();
		HashMap<List<String>, List<String>> pastRules = u.pastUpdates();
		String finalText = " ";

		for (Map.Entry<List<String>, List<String>> set  : pastRules.entrySet()) {
			for (int i = 0; i < set.getKey().size(); i++){
				finalText+= "MessageID: " + set.getKey().get(i) + "With rule: \n" + set.getValue().get(i) + "\n";
				Text_area.setText(finalText);
				//Textfield_variable_results.setText(finalText);
			}
			String newLine = System.getProperty("line.separator");

		}
		String p = "caa\n" + "ddd";

		a.show();
	}

	public void loadModel(ActionEvent event) throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Files");
		//		fileChooser.setInitialDirectory(new File("CollaborationRepository/"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Choreography", "*.bpmn"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {
			this.loaded_model_path.setText(selectedFile.getName());
			Translator t = new Translator();
			t.readModel(selectedFile);
			String finalRule = t.flowNodeSearch();
			t.createFile(selectedFile.getName(), finalRule);
			String address = t.deployAndUpload();
			System.out.println(address);
			//Textfield_variable_results.setText("New contract deployed at: " + address);
		}



	}
	
	
	public void updateModel(ActionEvent event) throws Exception {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Files");
		//		fileChooser.setInitialDirectory(new File("CollaborationRepository/"));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Choreography", "*.bpmn"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null) {

			this.updated_model_path.setText(selectedFile.getName());

			
			this.boxID.setDisable(true);
			this.progress.setVisible(true);

			Translator t = new Translator();
			t.readModel(selectedFile);
			String finalRule = t.flowNodeSearch();
			t.createFile(selectedFile.getName(), finalRule);
			t.updateRules(u.getContractAddress());
			//Textfield_variable_results.setText("New contract deployed at: " + address);
			//System.out.println(updated_model_path.getText());
			this.boxID.setDisable(false);
			this.progress.setVisible(false);

		}

	}
	
	public void button_default_model(ActionEvent event) {
		//LOAD DEFAULT MODEL
		 this.a.setContentText("You selected Load Default Model");
		 this.a.setAlertType(AlertType.CONFIRMATION);
		 this.a.show();
	}

	
	
	public void ruleList(ActionEvent event) throws Exception {
		//LOAD DEFAULT MODEL
		 this.a.setContentText("You selected GetMessage List");
		 this.a.setAlertType(AlertType.CONFIRMATION);
		 this.a.show();
		 List idList = u.getIDs();
		 String result = "";
		for (Object id: idList) {
			result += " "+id+",";
		}
        if(result == null || result.isEmpty()){
            System.out.println("Guarda che result e' null (o vuota)!!!\n");
        }
        else {
            System.out.println("Result non e' null\n");
        }
        System.out.println("Ecco result: " + result);
        this.Text_area.setText(result);
        //Textfield_variable_results.setText(result);
	}
	public void executeQuery(ActionEvent event) throws Exception {
		String message="You selected Execute query: "+ Text_messageID_query.getText()+ " -->" + Text_input_query.getText();
		String content = this.Text_input_query.getText();
		this.a.setContentText(message);
		this.a.setAlertType(AlertType.CONFIRMATION);
		this.a.show();
		String[] splitted = content.split(",");
		List<String> parameters = new ArrayList<>();
		for (String elem: splitted) {
			parameters.add(elem);
		}
		this.u.executeMessage(this.Text_messageID_query.getText(), parameters);

	}
	public void setContract(ActionEvent event) {
		//TextField_contract_address.setText("0x4041d79f597a341d760d1c250cc6835d0b30ab3d1893214801adc1eb39a4738e");
		String address = this.TextField_contract_address.getText();
		this.a.setContentText("You selected SetContract Address query: " + address);
		this.a.setAlertType(AlertType.CONFIRMATION);
		ProcessTemplate contract = this.u.loadContract(address);
		this.a.show();
		this.Text_area.setText("Contract loaded at: " + contract.getContractAddress());

	}

	public void getRule(ActionEvent event) throws Exception {
		
		this.a.setContentText("You selected getRule: " + this.Textfield_messageID_get.getText());
		this.a.setAlertType(AlertType.CONFIRMATION);
		String messageId = this.Textfield_messageID_get.getText();
		String rule = this.u.getRule(messageId);
		this.Text_area.setText(rule);
		this.a.show();

	}

		
	public void getVariable(ActionEvent event) throws Exception {

		this.a.setContentText("You selected getRule: "+this.Textfield_variable_name.getText()+"-->"+this.Choice_variable_type.getSelectionModel().getSelectedItem());
		this.a.setAlertType(AlertType.CONFIRMATION);
		this.a.show();
		String type = (String)this.Choice_variable_type.getSelectionModel().getSelectedItem();
		String varName = this.Textfield_variable_name.getText();
		String result = "";

		if(type.equals("String")){
			 result = this.u.getStringFromContract(varName);

		} else if(type.equals("Integer")){
			result = String.valueOf(this.u.getIntFromContract(varName));

		} else if(type.equals("Boolean")){
			result = String.valueOf(this.u.getBoolFromContract(varName));

		}

		this.Textfield_variable_results.setText(result);

	}

	public void getMessageState(ActionEvent event) throws Exception {

		this.a.setContentText("You selected getRule: get message name");
		this.a.setAlertType(AlertType.CONFIRMATION);
		this.a.show();
		String messageId = this.Textfield_messageID_get.getText();
		BigInteger rule = this.u.getState(messageId);
		String state = "";
		System.out.println(rule.intValue());
		switch (rule.intValue()){
			case 0:
				state = "ENABLED";
				break;
			case 1:
				state = "DISABLED";
				break;
			case 2:
				state = "COMPLETED";
				break;
		}

        System.out.println("State: " + state);
		//this.Textfield_variable_results.setText(state);

        this.Text_area.setText(state);

	}


	@FXML
	public void initializeChoice() {
		this.Choice_variable_type.getItems().addAll(FXCollections.observableArrayList("String", "Integer","Boolean"));
		this.Choice_variable_type.setValue("Select a Variable Type");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		initializeChoice();
		
	}




}