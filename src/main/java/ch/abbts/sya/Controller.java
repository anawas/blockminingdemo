package ch.abbts.sya;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import jdk.nashorn.internal.ir.ReturnNode;
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

        Task<ReturnValues> aTask = createTask();
        new Thread(aTask).start();

        aTask.setOnSucceeded(e -> {
            progressIndicator.setVisible(false);
            miningButton.setDisable(false);
            ReturnValues response = (ReturnValues)e.getSource().getValue();
            hashField.setText(response.getHash());
            nonceField.setText(response.getNonce());
        });

    }

    private String assembleStringToHash() {
        String message = blockNumberField.getText();
        message += dataField.getText();
        return message;
    }

    private Task<ReturnValues>createTask() {

        final Task<ReturnValues> task = new Task<ReturnValues>() {
            Miner miner = new Miner();
            String hash = "";
            String nonce = "";

            @Override
            protected ReturnValues call() {
                miner.setDifficulty(Integer.parseInt(difficultySpinner.getValue().toString()));
                try {
                    hash = miner.mineHash(assembleStringToHash());
                    nonce = miner.getNonce().toString();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                ReturnValues retrn = new ReturnValues();
                retrn.setHash(hash);
                retrn.setNonce(nonce);
                return retrn;
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
