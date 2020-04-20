package ch.abbts.sya;

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

    Miner miner = new Miner();

    public void miningButtonPressed(ActionEvent actionEvent) {
        miningButton.setDisable(true);
        progressIndicator.setVisible(true);
        progressIndicator.setProgress(INDETERMINATE_PROGRESS);
        miner.setDifficulty(Integer.parseInt(difficultySpinner.getValue().toString()));

        String hash = "";

        try {
            hash = miner.mineHash(assembleStringToHash());
            hashField.setText(hash);
            nonceField.setText(miner.getNonce().toString());
        } catch (NoSuchAlgorithmException e) {
            nonceField.setText("-1");
            e.printStackTrace();
        } finally {
            miningButton.setDisable(false);
            progressIndicator.setProgress(0.0);
        }

    }

    private String assembleStringToHash() {
        String message = blockNumberField.getText();
        message += dataField.getText();
        return message;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,6, 3);
        difficultySpinner.setValueFactory(valueFactory);
        progressIndicator.setVisible(false);
    }
}
