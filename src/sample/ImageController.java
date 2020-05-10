package sample;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import org.controlsfx.control.StatusBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class ImageController {
    int count = 0;
    public static final double ELEMENT_SIZE = 90;
    TilePane projectImageKeeper;
    ImageView bigImageView;
    StackPane bigImagePane;
    Label imageName;
    Vector<File> images;


    StatusBar status;

    public ImageController(Vector<File> images,

                           StackPane bigImagePane,
                           ImageView bigImageView,
                           TilePane projectImageKeeper,
                           StatusBar status,
                           Label imageName) {

        this.images = images;
        this.bigImagePane = bigImagePane;
        this.bigImageView = bigImageView;
        this.projectImageKeeper = projectImageKeeper;
        this.status = status;
        this.imageName = imageName;
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
            imageName.setText("Selected image: " + file.getName());
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

            status.setText(ex.getMessage());
        }
    }

    public void imageLoader() {
        try {
            do {
                projectImageKeeper.getChildren().add(createPage(count));
                count++;
            } while (images.size() > count);
            status.setText("Images loaded successfully: " + images.size());
        } catch (Exception e) {
            status.setText(e.getMessage());
        }
    }

    public void imageLoader(Vector<File> loadedImages) {
        try {
            projectImageKeeper.getChildren().clear();
            count = 0;
            do {
                projectImageKeeper.getChildren().add(createPage(count));
                count++;
            } while (loadedImages.size() > count);
            status.setText("Images loaded successfully: " + loadedImages.size());
        } catch (Exception e) {
            status.setText(e.getMessage());
        }
    }
}