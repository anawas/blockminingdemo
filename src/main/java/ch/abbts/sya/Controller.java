package ch.abbts.sya;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import sun.security.provider.ConfigFile;

import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;

import static javafx.scene.control.ProgressIndicator.INDETERMINATE_PROGRESS;

public class Controller implements Initializable {


    @FXML
    public TextField blockNumberField;
    @FXML
    public TextField nonceField;
    @FXML
    public TextArea dataField;
    @FXML
    public TextField hashField;
    @FXML
    public Button miningButton;
    @FXML
    public Spinner difficultySpinner;
    public ProgressIndicator progressIndicator;

    //Miner miner = new Miner();

    public void miningButtonPressed(ActionEvent actionEvent) {
        miningButton.setDisable(true);
        progressIndicator.setVisible(true);
        progressIndicator.setProgress(INDETERMINATE_PROGRESS);

        Task<String> aTask = createTask();
        new Thread(aTask).start();

        aTask.setOnSucceeded(e -> {
            progressIndicator.setVisible(false);
            miningButton.setDisable(false);
            String response = e.getSource().getValue().toString();
            String[] tokens = response.split(",");

            if (tokens[0].isEmpty()) {
                hashField.setText("FEHLER");
            } else {
                hashField.setText(tokens[0]);
            }

            if (tokens[1].isEmpty()) {
                nonceField.setText("-1");
            } else {
                nonceField.setText(tokens[1]);
            }
        });

    }

    private String assembleStringToHash() {
        String message = blockNumberField.getText();
        message += dataField.getText();
        return message;
    }

    private Task<String>createTask() {

        final Task<String> task = new Task<String>() {
            Miner miner = new Miner();
            String hash = "";
            String nonce = "";

            @Override
            protected String call() {
                miner.setDifficulty(Integer.parseInt(difficultySpinner.getValue().toString()));
                try {
                    hash = miner.mineHash(assembleStringToHash());
                    nonce = miner.getNonce().toString();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                return hash+","+nonce;
            }
        };
        return task;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,6, 3);
        difficultySpinner.setValueFactory(valueFactory);
        progressIndicator.setVisible(false);
    }
}
