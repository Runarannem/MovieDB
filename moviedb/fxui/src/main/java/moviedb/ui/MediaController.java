package moviedb.ui;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.scene.Scene;
import javafx.stage.Stage;
import moviedb.core.AbstractMedia;
import moviedb.core.Movie;
import moviedb.core.Series;

public class MediaController {

    private Stage currentStage;
    private FXMLLoader fxmlLoader;
    private AbstractMedia abstractMedia;
    private Tab tab;
    private String imagePath;
    private MenuController menuController;

    @FXML private Label nameLabel;
    @FXML private Label releaseYearLabel;
    @FXML private Label ratingLabel;
    @FXML private Label genreLabel;
    @FXML private Label runtimeSeasonsLabel;
    @FXML private ImageView imageView;
    @FXML private Button deleteButton;

    public MediaController(AbstractMedia media, Tab tab, String imagePath, MenuController menuController) {
        this.abstractMedia = media;
        this.tab = tab;
        this.imagePath = imagePath;
        this.menuController = menuController;
        currentStage = new Stage();
        fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(this.getClass().getResource("individualMedia.fxml"));
            fxmlLoader.setController(this);
            currentStage.setScene(new Scene(fxmlLoader.load()));
            currentStage.setTitle(media.getName());
            initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method for initializing
    private void initialize() {
        this.nameLabel.setText(this.abstractMedia.getName());
        this.releaseYearLabel.setText("Release year: " + this.abstractMedia.getReleaseYear());
        this.genreLabel.setText("Genres: " + this.abstractMedia.getGenres());
        this.ratingLabel.setText("Rating (1-10): " + this.abstractMedia.getRating());

        if (this.tab.getText().equals("Movies")) {
            this.runtimeSeasonsLabel.setText("Runtime (min): " + ((Movie) this.abstractMedia).getRuntime());
            this.deleteButton.setText("Delete movie");
            // TODO: could include try-catch for casting safety
        } else {
            this.runtimeSeasonsLabel.setText("Seasons: " + ((Series) this.abstractMedia).getSeasons());
            this.deleteButton.setText("Delete series");
        }
        try {
            System.out.println(imagePath);
            this.imageView.setImage(new Image(this.imagePath));
        }
        catch (Exception e) {
            try {
                this.imageView.setImage(new Image(getClass().getResource("images/defaultImage.png").toString()));
            } catch (NullPointerException event) {
                event.printStackTrace();
            }
        }
    }

    public void showStage() {
        currentStage.initModality(Modality.APPLICATION_MODAL);
        currentStage.showAndWait();
    }

    @FXML
    public void handleDelete() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirm action");
        alert.setHeaderText("");
        alert.setContentText("Are you sure you wish to delete this item from the database?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK){
            // User has confirmed they want to delete movie
            this.menuController.handleRemove(this.abstractMedia);
            handleClose();
        } 
    }

    @FXML
    public void handleClose() {
        currentStage.close();
    }

}
