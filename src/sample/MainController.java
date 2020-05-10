package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import org.controlsfx.control.StatusBar;

import java.io.File;
import java.util.Vector;


public class MainController {

    @FXML
    public ImageView bigImageView;
    int count = 0;


    public static final double ELEMENT_SIZE = 90;

    // file array to store read images info
    Vector<File> images = new Vector<>();
    @FXML
    public Label imageName;
    @FXML
    public ProgressIndicator loadingIndicator;

    @FXML
    public ScrollPane queueKeeperStage;

    @FXML
    public TilePane projectImageQueue;

    @FXML
    public MenuItem OpenFiles_btn;

    @FXML
    public MenuItem multiOpen_MenuItem;

    @FXML
    public StatusBar status;

    @FXML
    public StackPane bigImagePane;

    @FXML
    public TilePane projectImageKeeper;


    @FXML
    public ScrollPane imageStackStage;

    // ContextMenu
    ////
    @FXML
    void handleDragOverTilePane(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    void handleDragDroppedTilePane(DragEvent event) {
        images.clear();//add method
        images.addAll(event.getDragboard().getFiles());
        new ImageController(images, bigImagePane, bigImageView,
                projectImageKeeper, status, imageName).imageLoader();
        status.setText("Images loaded successfully: " + images.size());
    }

    ///
    @FXML
    void handleDragDetectedHBox(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    void openMultiFiles_menuItem(ActionEvent event) {
        new FileController(imageStackStage, images).openMultiple();
        new ImageController(images, bigImagePane, bigImageView,
                projectImageKeeper, status, imageName).imageLoader();
        status.setText("Images loaded successfully: " + images.size());
    }

    @FXML
    void openFiles_menuItem(ActionEvent event) {
        new FileController(imageStackStage, images).openFromDirectory();
        new ImageController(images, bigImagePane, bigImageView,
                projectImageKeeper, status, imageName).imageLoader();

        status.setText("Images loaded successfully: " + images.size());
    }

    @FXML
    void initialize() {
        imageStackStage.setContent(projectImageKeeper);
        imageStackStage.fitToHeightProperty();
        imageStackStage.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        loadingIndicator.setVisible(false);

    }


    public void saving(ActionEvent actionEvent) {
        try {
            status = new FileController(projectImageKeeper, images, status).save();
        } catch (Exception e) {
            status.setText(e.getMessage());
        }
    }

    public void loading(ActionEvent actionEvent) {
        try {
            images.clear();
            images = new FileController(projectImageKeeper, images, status).load();
            new ImageController(images, bigImagePane, bigImageView,
                    projectImageKeeper, status, imageName).imageLoader(images);
        } catch (Exception e) {
            status.setText(e.getMessage());
        }

    }


}
