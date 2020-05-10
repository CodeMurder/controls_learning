package sample;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.TilePane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Vector;

public class FileController {
    ScrollPane root;
    TilePane projectImageKeeper;
    Vector<File> image;
    StatusBar status;

    protected FileController(TilePane projectImageKeeper, Vector<File> images, StatusBar status) {
        this.projectImageKeeper = projectImageKeeper;
        this.image = images;
        this.status = status;
    }

    public FileController(ScrollPane root, Vector<File> img) {
        this.root = root;
        this.image = img;
    }

    protected StatusBar save() {
        return new ExpSaver().save(projectImageKeeper, image, status);
    }

    protected Vector<File> load() {
        return new ExpLoader().load(projectImageKeeper, image);
    }

    protected Vector<File> openMultiple() {
        Stage parent = (Stage) root.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.gif"));
        image.addAll(fileChooser.showOpenMultipleDialog(parent));
        return image;
    }

    protected Vector<File> openFromDirectory() {
        //image.clear();
        Stage parent = (Stage) root.getScene().getWindow();


        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(parent);

        if (selectedDirectory != null) {
            FilenameFilter filterJpg = (File dir, String name) -> name.toLowerCase().endsWith(".jpg");
            FilenameFilter filterPng = (File dir, String name) -> name.toLowerCase().endsWith(".png");
            FilenameFilter filterGif = (File dir, String name) -> name.toLowerCase().endsWith(".gif");

            image.addAll(Arrays.asList(Objects.requireNonNull(selectedDirectory.listFiles(filterJpg))));
            image.addAll(Arrays.asList(Objects.requireNonNull(selectedDirectory.listFiles(filterPng))));
            image.addAll(Arrays.asList(Objects.requireNonNull(selectedDirectory.listFiles(filterGif))));
        }
        return image;
    }

}
