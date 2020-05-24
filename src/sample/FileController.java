package sample;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import org.controlsfx.control.StatusBar;

import java.io.File;
import java.util.Vector;

public class FileController {
    ScrollPane root;
    TilePane projectImageKeeper;
    Vector<File> image;
    StatusBar status;
    private HBox projectImageQueue;
    private Vector<File> queuedImages;

    protected FileController(TilePane projectImageKeeper, Vector<File> images, StatusBar status) {
        this.projectImageKeeper = projectImageKeeper;
        this.image = images;
        this.status = status;
    }

    public FileController(ScrollPane root, Vector<File> img) {
        this.root = root;
        this.image = img;
    }

    public FileController(HBox projectImageQueue, Vector<File> queuedImages, StatusBar status) {

        this.projectImageQueue = projectImageQueue;
        this.queuedImages = queuedImages;
        this.status = status;
    }

    protected StatusBar saveAlb() {
        return new ExpSaver().overloadStack(projectImageQueue, queuedImages, status);
    }

    protected StatusBar savePrj() {
        return new ExpSaver().overloadStack(projectImageKeeper, image, status);
    }

    protected Vector<File> load() {
        return new ExpLoader().load(projectImageKeeper, image);
    }

    protected Vector<File> openMultiple() {
        Stage parent = (Stage) root.getScene().getWindow();
        image = new ExpLoader().open(parent, "multi");
        return image;
    }



    protected Vector<File> openFromDirectory() {
        //image.clear();
        Stage parent = (Stage) root.getScene().getWindow();
        image = new ExpLoader().open(parent, "folder");
        return image;
    }

}
