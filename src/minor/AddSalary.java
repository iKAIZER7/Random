package minor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AddSalary {
    private Button addSalaryButton;
    private TextArea salaryListArea;
    private TextField salaryAmountField;

    public VBox addSalaryTab(TabPane tabPane) {
        VBox expenseVbox1 = new VBox(10);
        expenseVbox1.setPadding(new Insets(20));
        expenseVbox1.setAlignment(Pos.CENTER);
        Label salaryTitleLabel = new Label("Add Salary");
        salaryTitleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        salaryAmountField = new TextField();
        salaryAmountField.setPromptText("Salary Amount");
        addSalaryButton = new Button("Add Salary");
        addSalaryButton.setOnAction(e -> addSalary());
        salaryListArea = new TextArea();
        salaryListArea.setEditable(false);
        salaryListArea.setPrefSize(300, 200);
        expenseVbox1.getChildren().addAll(salaryTitleLabel, salaryAmountField, addSalaryButton, salaryListArea);
        Tab expenseTab1 = new Tab("Add Salary", expenseVbox1);

        // Add the tab to the TabPane
        tabPane.getTabs().add(expenseTab1);
        return expenseVbox1;
    }

    public void addSalary() {
        String salaryAmountStr = salaryAmountField.getText();

        if (!salaryAmountStr.isEmpty()) {
            try {
                double salaryAmount = Double.parseDouble(salaryAmountStr);
                saveSalaryToDatabase(salaryAmount);
                updateSalaryList(salaryAmount);
                salaryAmountField.clear();
                showAlert("Salary Added", "Salary successfully added!");
            } catch (NumberFormatException e) {
                showAlert("Invalid Amount", "Please enter a valid numeric amount.");
            }
        } else {
            showAlert("Missing Information", "Please enter the salary amount.");
        }
    }
    public void saveSalaryToDatabase(double amount) {
        try {Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_manager", "root", "root");
            String query = "INSERT INTO salary (amount) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save salary to the database.");
        }
    }

    public void updateSalaryList(double amount) {
        salaryListArea.appendText("Salary: Rs." + amount + "\n");
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
}
