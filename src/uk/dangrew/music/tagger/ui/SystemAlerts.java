package uk.dangrew.music.tagger.ui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class SystemAlerts {

    public Optional<ButtonType> showTagClearanceAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tag Clearance");
        alert.setHeaderText("Tags are about to be loaded into the system!");
        alert.setContentText("Would you like to clear the existing tags?");

        alert.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        return alert.showAndWait();
    }
}
