package sample;


import javafx.event.*;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.InfoOverlay;
import org.controlsfx.control.StatusBar;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class Controller {

    @FXML
    public ImageView bigImageView;
    int count = 0;


    public static final double ELEMENT_SIZE = 90;

    // file array to store read images info
    Vector<File> images = new Vector<>();



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
        Stage parent = (Stage) imageStackStage.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif"));
        images.addAll(fileChooser.showOpenMultipleDialog(parent));
        imageLoader();
        status.setText("Images loaded successfully: " + images.size());
    }

    @FXML
    void openFiles_menuItem(ActionEvent event) {
        Stage parent = (Stage) imageStackStage.getScene().getWindow();


        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(parent);

        if (selectedDirectory != null) {
            FilenameFilter filterJpg = (File dir, String name) -> name.toLowerCase().endsWith(".jpg"); // тут эта хуйня фиьтрует по расширению
            FilenameFilter filterPng = (File dir, String name) -> name.toLowerCase().endsWith(".png"); // тут эта хуйня фиьтрует по расширению
            FilenameFilter filterGif = (File dir, String name) -> name.toLowerCase().endsWith(".gif");
            // тут записывает всю отфильтрованную хуету в масив типа ФАЙЛ
            images.addAll(Arrays.asList(Objects.requireNonNull(selectedDirectory.listFiles(filterJpg))));
            images.addAll(Arrays.asList(Objects.requireNonNull(selectedDirectory.listFiles(filterPng))));
            images.addAll(Arrays.asList(Objects.requireNonNull(selectedDirectory.listFiles(filterGif))));
            imageLoader();
        }
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
        pageBox.setStyle("-fx-border-style:white");
        pageBox.setOnMouseEntered(mouseEvent -> pageBox.setStyle("-fx-border-style:blue"));
        pageBox.setOnMouseExited(mouseEvent -> pageBox.setStyle("-fx-border-style:white"));
        pageBox.setOnMouseClicked(mouseEvent -> {

            bigImageView.setImage(new Image(file.toURI().toString()));
            bigImageView.setSmooth(false);
            bigImageView.setStyle(String.valueOf(GroupLayout.Alignment.CENTER));
            bigImageView.setFitHeight(bigImagePane.getHeight());
            if (bigImageView.getFitWidth() > bigImagePane.getWidth()) {
                bigImageView.setFitWidth(bigImagePane.getWidth());
            }

            status.setText("Selected image: "+file.getName());
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


}
