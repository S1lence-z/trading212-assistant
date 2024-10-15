package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DocumentsController extends BaseController {
    @FXML
    private CheckBox DividendsBox;

    @FXML
    private ListView<?> DocumentsList;

    @FXML
    private Button DownloadButton;

    @FXML
    private VBox FormAndListSection;

    @FXML
    private Label FromDateLabel;

    @FXML
    private DatePicker FromDatePicker;

    @FXML
    private Label IncludeLabel;

    @FXML
    private CheckBox InterestBox;

    @FXML
    private CheckBox OrdersBox;

    @FXML
    private HBox PageLayout;

    @FXML
    private Label ToDateLabel;

    @FXML
    private DatePicker ToDatePicker;

    @FXML
    private CheckBox TransactionsBox;

}
