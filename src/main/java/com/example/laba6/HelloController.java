package com.example.laba6;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Callback;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    @FXML
    private Button changeButton;

    @FXML
    private ColorPicker colorField;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField modelField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField vinField;

    @FXML
    private TableView<Car> carTable;

    @FXML
    private TableColumn<Car, Car> deleteColumn;

    @FXML
    private TableColumn<Car, String> colorColumn;

    @FXML
    private TableColumn<Car, Car> dateColumn;

    @FXML
    private TableColumn<Car, String> modelColumn;

    @FXML
    private TextField nameFindField;

    @FXML
    private TableColumn<Car, String> nameColumn;

    @FXML
    private TableColumn<Car, String> vinColumn;

    private final ObservableList<Car> data =Car.readFromFile();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeButton.setDisable(true);
        nameField.setDisable(true);
        colorField.setDisable(true);
        dateField.setDisable(true);
        vinField.setDisable(true);
        modelField.setDisable(true);

        nameField.textProperty().addListener(getListener());
        modelField.textProperty().addListener(getListener());
        vinField.textProperty().addListener(getListener());

        dateField.setEditable(false);
        dateField.setDayCellFactory(new Callback<>() {
            @Override
            public DateCell call(DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate localDate, boolean b) {
                        super.updateItem(localDate, b);
                        if (localDate.isAfter(LocalDate.now())) {
                            this.setDisable(true);
                        }
                    }
                };
            }
        });

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("Model"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("Color"));
        colorColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String color, boolean b) {
                super.updateItem(color, b);
                if (color!=null) {
                    setBackground(Background.fill(Paint.valueOf(color)));
                    setText("");
                }
            }
        });
        vinColumn.setCellValueFactory(new PropertyValueFactory<>("VIN"));
        deleteColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(Car car, boolean empty) {
                super.updateItem(car, empty);

                if (car == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(deleteButton);

                deleteButton.setOnAction(event -> {
                    car.deleteFromFile(data);
                    data.remove(car);
                });
            }
        });
        carTable.setItems(data);

        FilteredList<Car> filteredData=new FilteredList<>(data, b -> true);
        nameFindField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(car -> {
            if (newValue == null || newValue.isEmpty()) {
                return  true;
            }
            return car.getName().toLowerCase().contains(newValue.toLowerCase());
        }));
        SortedList<Car> sortedData=new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(carTable.comparatorProperty());
        carTable.setItems(sortedData);

        carTable.setRowFactory(param -> {
            TableRow<Car> row = new TableRow<>();
            row.setOnMouseClicked(event -> Optional.ofNullable(row.getItem()).ifPresent(rowData-> {
                if(event.getClickCount() == 2 && rowData.equals(carTable.getSelectionModel().getSelectedItem())) {
                    changeButton.setDisable(false);
                    nameField.setDisable(false);
                    modelField.setDisable(false);
                    dateField.setDisable(false);
                    colorField.setDisable(false);
                    vinField.setDisable(false);

                    nameField.setText(rowData.getName());
                    modelField.setText(rowData.getModel());
                    dateField.setValue(rowData.getDate());
                    colorField.setValue(Color.valueOf(rowData.getColor()));
                    vinField.setText(rowData.getVIN());
                    data.remove(rowData);
                    changeButton.setOnAction(buttonEvent -> {
                        rowData.setName(nameField.getText());
                        rowData.setModel(modelField.getText());
                        rowData.setDate(dateField.getValue());
                        rowData.setColor(String.valueOf(colorField.getValue()));
                        rowData.setVIN(vinField.getText());
                        data.add(rowData);
                        Car.updateFile(data);

                        nameField.setText("");
                        colorField.setValue(null);
                        dateField.setValue(null);
                        vinField.setText("");
                        modelField.setText("");

                        nameField.setDisable(true);
                        colorField.setDisable(true);
                        dateField.setDisable(true);
                        vinField.setDisable(true);
                        modelField.setDisable(true);
                    });
                }
            }));
            return row;
        });
    }

    ChangeListener<String> getListener() {
        return (observable, oldValue, newValue) -> changeButton.setDisable(nameField.getText().isEmpty() ||
                modelField.getText().isEmpty() ||
                vinField.getText().isEmpty());
    }
}