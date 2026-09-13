package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Operation;
import model.OperationDAO;

import java.sql.SQLException;

public class MainController {

    @FXML private TextField fieldA;
    @FXML private TextField fieldB;
    @FXML private TextField resultField;
    @FXML private TableView<Operation> historyTable;
    @FXML private TableColumn<Operation, Integer> colId;
    @FXML private TableColumn<Operation, Integer> colA;
    @FXML private TableColumn<Operation, Integer> colB;
    @FXML private TableColumn<Operation, Integer> colC;
    @FXML private Button btnCalculate;
    @FXML private Button btnClear;
    @FXML private TextField filterA;
    @FXML private TextField filterB;
    @FXML private Button btnFilter;

    private final OperationDAO dao = new OperationDAO();
    private ObservableList<Operation> operations = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colA.setCellValueFactory(new PropertyValueFactory<>("a"));
        colB.setCellValueFactory(new PropertyValueFactory<>("b"));
        colC.setCellValueFactory(new PropertyValueFactory<>("c"));
        historyTable.setItems(operations);

        loadHistory();

        btnCalculate.setOnAction(e -> calculateAndSave());
        btnClear.setOnAction(e -> clearHistory());
        btnFilter.setOnAction(e -> applyFilter());
    }

    private void loadHistory() {
        try {
            operations.setAll(dao.getAllOperations());
        } catch (SQLException e) {
            showAlert("Ошибка БД", "Не удалось загрузить историю: " + e.getMessage());
        }
    }

    private void calculateAndSave() {
        String aText = fieldA.getText().trim();
        String bText = fieldB.getText().trim();
        if (aText.isEmpty() || bText.isEmpty()) {
            showAlert("Ошибка ввода", "Введите оба числа.");
            return;
        }
        try {
            int a = Integer.parseInt(aText);
            int b = Integer.parseInt(bText);
            Operation op = dao.insertOperation(a, b);
            if (op != null) {
                operations.add(op);          // добавляем в таблицу
                resultField.setText(String.valueOf(op.getC()));
                fieldA.clear();
                fieldB.clear();
            }
        } catch (NumberFormatException e) {
            showAlert("Ошибка ввода", "Введите целые числа.");
        } catch (SQLException e) {
            showAlert("Ошибка БД", "Не удалось выполнить операцию: " + e.getMessage());
        }
    }

    private void clearHistory() {
        try {
            dao.clearHistory();
            operations.clear();
            resultField.clear();
        } catch (SQLException e) {
            showAlert("Ошибка БД", "Не удалось очистить историю: " + e.getMessage());
        }
    }

    private void applyFilter() {
        String aText = filterA.getText().trim();
        String bText = filterB.getText().trim();
        Integer a = aText.isEmpty() ? null : Integer.parseInt(aText);
        Integer b = bText.isEmpty() ? null : Integer.parseInt(bText);
        try {
            operations.setAll(dao.filterOperations(a, b));
        } catch (SQLException e) {
            showAlert("Ошибка БД", "Ошибка фильтрации: " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Ошибка ввода", "Фильтры должны быть целыми числами.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}