package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.StatusBar;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;


public class MainController {

    @FXML
    public ImageView bigImageView;
    int count = 0;
    int position_count = 0;

    public static final double ELEMENT_SIZE = 90;
    public static final double QUEUED_ELEMENT_SIZE = 110;

    // file array to store read images info
    Vector<File> images = new Vector<>();
    Vector<File> queuedImages = new Vector<>();

    @FXML
    private Button backButton;

    @FXML
    private Button nextButton;

    @FXML
    public Label imageName;
    @FXML
    public ProgressIndicator loadingIndicator;

    @FXML
    public ScrollPane queueKeeperStage;

    @FXML
    public HBox projectImageQueue;

    @FXML
    private Menu mainMenu;

    @FXML
    private MenuItem OpenFiles_btn;

    @FXML
    private MenuItem multiOpen_MenuItem;

    @FXML
    private Menu recentProjectsMenu;

    @FXML
    private MenuItem expSave;

    @FXML
    private MenuItem saveAlbumMenu;

    @FXML
    private MenuItem expLoad;

    @FXML
    private MenuItem exitMenu;

    @FXML
    private MenuItem undoMenu;

    @FXML
    private MenuItem redoMenu;

    @FXML
    private MenuItem deleteMenu;

    @FXML
    private MenuItem aboutMenu;

    @FXML
    public StatusBar status;

    @FXML
    public StackPane bigImagePane;

    @FXML
    public TilePane projectImageKeeper;


    @FXML
    public ScrollPane imageStackStage;
    @FXML
    ContextMenu contextMenu;

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
        images.addAll(event.getDragboard().getFiles());
        imageLoader();
        status.setText("Images loaded successfully: " + images.size());
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
        projectImageKeeper.setAlignment(Pos.TOP_LEFT);
        imageStackStage.fitToHeightProperty();
        imageStackStage.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        loadingIndicator.setVisible(false);


        queueKeeperStage.setContent(projectImageQueue);
        queueKeeperStage.fitToWidthProperty();
        projectImageQueue.setAlignment(Pos.CENTER_LEFT);

    }


    public HBox createPage(int index) {
        ImageView imageView = new ImageView();
        File file = images.get(index);
        setImage(imageView, file, 0);
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

            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                bigImageView.setImage(new Image(file.toURI().toString()));
                setSizes();
                pageBox.setStyle("-fx-background-color: #79f3ff");
                if (mouseEvent.getClickCount() == 2) {
                    queueLoader(file);
                    queuedImages.add(file);
                }
            }
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                contextMenu = new ContextMenu(deleteMenu);
                contextMenu.show(pageBox, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                deleteMenu.setOnAction(actionEvent -> updateKeeper(deleteMenuOperation(file)));
                deleteMenu.setText("Delete");
            }
            imageName.setText(file.getName());
        });
        pageBox.setOnMouseReleased(mouseEvent -> {
            pageBox.setStyle("-fx-background-color: none");
        });

        return pageBox;
    }


    public void setImage(ImageView imageView, File file, int key) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Image image = new Image(file.toURI().toString());
            imageView.setImage(image);
            if (key == 0) {
                imageView.setFitWidth(ELEMENT_SIZE);
                imageView.setFitHeight(ELEMENT_SIZE);
            } else {
                imageView.setFitWidth(QUEUED_ELEMENT_SIZE);
                imageView.setFitHeight(QUEUED_ELEMENT_SIZE);
            }

            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            imageView.setCache(true);
            imageView.setCursor(Cursor.HAND);

        } catch (IOException ex) {

            status.setText(ex.getMessage());
        }
    }

    public void queueLoader(File file) {
        projectImageQueue.getChildren().add(createQueuePage(file));
    }

    public VBox createQueuePage(File file) {
        ImageView imageView = new ImageView();
        setImage(imageView, file, 1);
        VBox queuePageBox = new VBox();
        queuePageBox.getChildren().add(getQueueVBox(imageView, file));
        return queuePageBox;
    }

    public HBox getQueueVBox(ImageView imageView, File file) {
        HBox queuePageBox = new HBox();
        queuePageBox.getChildren().add(imageView);

        queuePageBox.setAlignment(Pos.CENTER);

        imageView = null;
        queuePageBox.setStyle("-fx-border-color:black");
        queuePageBox.setOnMouseEntered(mouseEvent -> queuePageBox.setStyle("-fx-border-color:blue"));
        queuePageBox.setOnMouseExited(mouseEvent -> queuePageBox.setStyle("-fx-border-color:black"));
        queuePageBox.setOnMouseClicked(mouseEvent -> {
            bigImageView.setImage(new Image(file.toURI().toString()));
            setSizes();
            queuePageBox.setStyle("-fx-background-color: #79f3ff");
            imageName.setText(file.getName());
            position_count = queuedImages.indexOf(file);
            if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                contextMenu = new ContextMenu(deleteMenu);
                contextMenu.show(queuePageBox, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                deleteMenu.setOnAction(actionEvent -> updateQueue(deleteMenuOperation()));
                deleteMenu.setText("Delete");
            }
        });
        queuePageBox.setOnMouseReleased(mouseEvent -> {
            queuePageBox.setStyle("-fx-background-color: none");
        });

        return queuePageBox;
    }

    public void updateQueue(Vector<File> loadedImages) {
        try {
            projectImageQueue.getChildren().clear();
            count = 0;
            do {
                projectImageQueue.getChildren().add(createQueuePage(loadedImages.elementAt(count)));
                count++;
            } while (loadedImages.size() > count);
            status.setText("Updated. ");
        } catch (Exception e) {
            status.setText("All deleted from queue.");
        }
    }

    private void updateKeeper(Vector<File> loadedImages) {
        imageLoader(loadedImages);
    }

    public void imageLoader() {
        try {
            do {
                projectImageKeeper.getChildren().add(createPage(count));
                count++;
            } while (images.size() > count);
            status.setText("Images: " + images.size());
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
            status.setText("Images: " + loadedImages.size());
        } catch (Exception e) {
            status.setText(e.getMessage());
        }
    }

    public void setSizes() {
        bigImageView.setSmooth(false);
        bigImageView.setFitHeight(bigImagePane.getHeight());
        if (bigImageView.getFitWidth() > bigImagePane.getWidth()) {
            bigImageView.setFitWidth(bigImagePane.getWidth());
        }
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
            imageLoader(new FileController(projectImageKeeper, images, status).load());
        } catch (Exception e) {
            status.setText(e.getMessage());
        }

    }


    public void saveAlbum(ActionEvent actionEvent) {
        try {
            status = new FileController(projectImageQueue, queuedImages, status).saveAlb();


        } catch (Exception e) {
            status.setText(e.getMessage());
        }
    }

    public void exiting(ActionEvent actionEvent) throws Exception {

        System.exit(0);
    }


    public Vector<File> deleteMenuOperation() {
        queuedImages.remove(queuedImages.elementAt(position_count));
        return queuedImages;
    }

    public Vector<File> deleteMenuOperation(File file) {
        images.remove(images.elementAt(images.indexOf(file)));
        return images;
    }

    public void handleNextButton(MouseEvent mouseEvent) {


        if (position_count >= 0 && position_count < queuedImages.size() - 1) {
            position_count++;
            bigImageView.setImage(new Image(queuedImages.elementAt(position_count).toURI().toString()));

        } else if (position_count == queuedImages.size() - 1) {

            bigImageView.setImage(new Image(queuedImages.elementAt(position_count).toURI().toString()));

        }
    }

    public void handleBackButton(MouseEvent mouseEvent) {
        if (position_count > 0 || position_count == queuedImages.size() - 1) {
            position_count--;
            bigImageView.setImage(new Image(queuedImages.elementAt(position_count).toURI().toString()));

        } else if (position_count == 0) {

            bigImageView.setImage(new Image(queuedImages.elementAt(position_count).toURI().toString()));

        }
    }
}
