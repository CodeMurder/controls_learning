package sample;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.WindowEvent;
import org.controlsfx.control.StatusBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import static sample.ExpLoader.deleteDirectory;


public class MainController {

    @FXML
    public ImageView bigImageView;
    int count = 0;


    public static final double ELEMENT_SIZE = 90;

    // file array to store read images info
    Vector<File> images = new Vector<>();

    @FXML
    private ProgressIndicator loadingIndicator;

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
    void handleDragDroppedTilePane(DragEvent event) {                       //add method
        images.addAll(event.getDragboard().getFiles());
        imageLoader();
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
        imageLoader();
        status.setText("Images loaded successfully: " + images.size());
    }

    @FXML
    void openFiles_menuItem(ActionEvent event) {
        new FileController(imageStackStage, images).openFromDirectory();
        imageLoader();

        status.setText("Images loaded successfully: " + images.size());
    }

    @FXML
    void initialize() {
        imageStackStage.setContent(projectImageKeeper);
        imageStackStage.fitToHeightProperty();
        imageStackStage.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

    }


    public HBox createPage(int index) {
        ImageView imageView = new ImageView();
        File file = images.get(index);
        setImage(imageView, file);
        return getHBox(imageView, file);
    }

    public HBox getHBox(ImageView imageView, File file) {
        HBox pageBox = new HBox();
        pageBox.getChildren().add(imageView);
        pageBox.setStyle("-fx-border-color: #292929;");
        pageBox.setStyle("-fx-background-color: rgba(0, 0, 0,0.7)");
        pageBox.setAlignment(Pos.CENTER);

        imageView = null;
        pageBox.setStyle("-fx-border-color:white");
        pageBox.setOnMouseEntered(mouseEvent -> pageBox.setStyle("-fx-border-color:blue"));
        pageBox.setOnMouseExited(mouseEvent -> pageBox.setStyle("-fx-border-color:white"));
        pageBox.setOnMouseClicked(mouseEvent -> {

            bigImageView.setImage(new Image(file.toURI().toString()));
            bigImageView.setSmooth(false);
            bigImageView.setFitHeight(bigImagePane.getHeight());
            if (bigImageView.getFitWidth() > bigImagePane.getWidth()) {
                bigImageView.setFitWidth(bigImagePane.getWidth());
            }

            status.setText("Selected image: " + file.getName());
        });
        return pageBox;
    }

    public void setImage(ImageView imageView, File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            imageView.setFitWidth(ELEMENT_SIZE);
            imageView.setFitHeight(ELEMENT_SIZE);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            imageView.setCursor(Cursor.HAND);
        } catch (IOException ex) {


        }
    }

    public void imageLoader() {
        do {
            projectImageKeeper.getChildren().add(createPage(count));
            count++;
        } while (images.size() > count);

    }

    public void imageLoader(Vector<File> loadedImages) {
        projectImageKeeper.getChildren().clear();
        count = 0;
        do {
            projectImageKeeper.getChildren().add(createPage(count));
            count++;
        } while (loadedImages.size() > count);
        status.setText("Images loaded successfully: " + loadedImages.size());
    }


    public void saving(ActionEvent actionEvent) {
        status = new FileController(projectImageKeeper, images, status).save();
    }

    public void loading(ActionEvent actionEvent) {
        images.clear();
        imageLoader(new FileController(projectImageKeeper, images, status).load());

    }

    private final EventHandler<WindowEvent> closeEventHandler = event -> {
        deleteDirectory(ExpLoader.temp);
    };

    public EventHandler<WindowEvent> getCloseEventHandler() {
        return closeEventHandler;
    }
}
