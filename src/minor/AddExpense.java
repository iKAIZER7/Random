package minor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AddExpense {

    //private TabPane tabPane;
    //private TextField expenseNameField;
    //private TextField expenseAmountField;
    private ComboBox<String> categoryComboBox;
    private TextField amountField;
    private Button addButton;
    private TextArea expenseListArea;

    public void addExpenseCategoryTab(TabPane tabPane) {
        // Create the UI components for the "Expense Category" tab
       VBox expenseVbox3 = new VBox(10); // Create a new VBox for the second expense tab
       expenseVbox3.setPadding(new Insets(20));
       expenseVbox3.setAlignment(Pos.CENTER);
       Label expenseTitleLabel2 = new Label("Expense Category");
       expenseTitleLabel2.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
       categoryComboBox = new ComboBox<>();
       categoryComboBox.setItems(FXCollections.observableArrayList( "Food", "Travel", "Movie", "Education", "Stationary", "Fuel", "Grocery","Movie" ));
       categoryComboBox.setPromptText("Select Category");
       amountField = new TextField();
       amountField.setPromptText("Amount");
       addButton = new Button("Add Expense");
       addButton.setOnAction(e -> expenseCategory());
       expenseListArea = new TextArea();
       expenseListArea.setEditable(false);
       expenseListArea.setPrefSize(300, 200);
       expenseVbox3.getChildren().addAll(expenseTitleLabel2, categoryComboBox, amountField, addButton, expenseListArea);

        // Create the "Expense Category" tab
        Tab expenseTab3 = new Tab("Expense Category", expenseVbox3);

        // Add the tab to the TabPane
        tabPane.getTabs().add(expenseTab3);
    }

    public void expenseCategory() {
        String selectedCategory = categoryComboBox.getValue();
        String amount = amountField.getText();
        if (selectedCategory == null || selectedCategory.isEmpty() || amount.isEmpty()) {
            showAlert("Error", "Please select a category and enter an amount.");
            return;
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_manager", "root", "root");
            String query = "INSERT INTO expensess (category, amount) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, selectedCategory);
            preparedStatement.setString(2, amount);
            preparedStatement.executeUpdate();
            updateExpenseList(selectedCategory, Double.parseDouble(amount));
            amountField.clear();
            showAlert("Expense Added", "Expense successfully added!");
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while adding the expense.");
        }
    }

    /*public void saveExpenseToDatabase(String name, double amount) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_manager", "root", "root")) {
            String query = "INSERT INTO expenses (name, amount) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save expense to the database.");
        }
    }*/
    public void updateExpenseList(String name, double amount) {
        expenseListArea.appendText(name + ": Rs." + amount + "\n");
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
}
