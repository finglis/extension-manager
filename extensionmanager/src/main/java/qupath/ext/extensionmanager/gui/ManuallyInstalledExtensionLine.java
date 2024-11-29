package qupath.ext.extensionmanager.gui;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.controlsfx.glyphfont.FontAwesome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A container displaying the name of a manually installed extension as well as a
 * button to delete it.
 */
class ManuallyInstalledExtensionLine extends HBox {

    private static final Logger logger = LoggerFactory.getLogger(ManuallyInstalledExtensionLine.class);
    private final Path jarPath;
    @FXML
    private Label name;
    @FXML
    private Button delete;

    /**
     * Create the container.
     *
     * @param jarPath the path of the manually installed extension to display
     * @throws IOException when an error occurs while creating the container
     */
    public ManuallyInstalledExtensionLine(Path jarPath) throws IOException {
        this.jarPath = jarPath;

        UiUtils.loadFXML(this, ManuallyInstalledExtensionLine.class.getResource("manually_installed_extension_line.fxml"));

        if (jarPath.getFileName() != null) {
            name.setText(jarPath.getFileName().toString());
        }

        delete.setGraphic(UiUtils.getFontAwesomeIcon(FontAwesome.Glyph.MINUS_CIRCLE));
    }

    @FXML
    private void onDeleteClicked(ActionEvent ignored) {
        var confirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                String.format(
                        "Remove %s?",
                        jarPath.getFileName()
                )
        ).showAndWait();

        if (confirmation.isPresent() && confirmation.get().equals(ButtonType.OK)) {
            try {
                Files.delete(jarPath);
            } catch (IOException | SecurityException e) {
                logger.error(String.format("Cannot delete %s extension", jarPath), e);

                new Alert(
                        Alert.AlertType.ERROR,
                        String.format(
                                "Cannot delete extension:\n%s",
                                e.getLocalizedMessage()
                        )
                ).show();
            }
        }
    }
}
