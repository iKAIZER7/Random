package minor;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
public class BUDG extends Application {

    private TabPane tabPane;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private TextField expenseNameField;
    private TextField expenseAmountField;
    private ComboBox<String> categoryComboBox;
    private TextField amountField;
    private Button addButton;
    private TextArea expenseListArea;
    private TextField salaryAmountField;
    private Button addSalaryButton;
    private TextArea salaryListArea;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Expense Manager");

        // Create the login tab
        VBox loginVbox = createLoginTab(primaryStage);
        Tab loginTab = new Tab("Login", loginVbox);

        // Create the TabPane and add the login tab initially
        tabPane = new TabPane(loginTab);

        Scene scene = new Scene(tabPane, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createLoginTab(Stage primaryStage) {
        VBox loginVbox = new VBox(10);
        loginVbox.setPadding(new Insets(20));
        loginVbox.setAlignment(Pos.CENTER);
        Label loginTitleLabel = new Label("Budget Meter Login");
        loginTitleLabel.getStyleClass().add("title-label");
        loginTitleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: roboto");
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.getStyleClass().add("text-field");
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.getStyleClass().add("text-field");
        // ... (Login UI components setup)
        
        loginButton = new Button("Login");
        loginButton.getStyleClass().add("login-button");
        loginButton.setOnAction(e -> handleLogin(primaryStage));

        // ... (Login UI components layout)
        loginVbox.getChildren().addAll(loginTitleLabel, usernameField, passwordField, loginButton);
        VBox expenseVbox = new VBox(10);
        expenseVbox.setPadding(new Insets(20));
        expenseVbox.setAlignment(Pos.CENTER);

        return loginVbox;
    }

    private void handleLogin(Stage primaryStage) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Database connection and authentication logic here
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_manager", "root", "root");
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Logged in successfully and remove the login tab, add the "Add Expense" and "Expense Category" tabs
                tabPane.getTabs().clear();
                addSalaryTab();
                addExpenseTab();
                addExpenseCategoryTab();
                primaryStage.setTitle("Expense Manager - Logged In as " + username);
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private VBox addSalaryTab() {
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
    private void addExpenseTab() {
        // Create the UI components for the "Add Expense" tab
    VBox expenseVbox2 = new VBox(10); // Create a new VBox for the first expense tab
    expenseVbox2.setPadding(new Insets(20));
    expenseVbox2.setAlignment(Pos.CENTER);
    Label expenseTitleLabel1 = new Label("Add Expenses");
    expenseTitleLabel1.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
    expenseNameField = new TextField();
    expenseNameField.setPromptText("Expense Name");
    expenseAmountField = new TextField();
    expenseAmountField.setPromptText("Expense Amount");
    addButton = new Button("Add Expense");
    addButton.setOnAction(e -> addExpense());

    expenseListArea = new TextArea();
    expenseListArea.setEditable(false);
    expenseListArea.setPrefSize(300, 200);

    expenseVbox2.getChildren().addAll(expenseTitleLabel1, expenseNameField, expenseAmountField, addButton, expenseListArea);
        // Create the "Add Expense" tab
        Tab expenseTab2 = new Tab("Add Expense", expenseVbox2);

        // Add the tab to the TabPane
        tabPane.getTabs().add(expenseTab2);
    }

    private void addExpenseCategoryTab() {
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
    private void addSalary() {
        String salaryAmountStr = salaryAmountField.getText();

        if (!salaryAmountStr.isEmpty()) {
            try {
                double salaryAmount = Double.parseDouble(salaryAmountStr);
                saveSalaryToDatabase(salaryAmount);
                updateSalaryList(salaryAmount);
                salaryAmountField.clear();
            } catch (NumberFormatException e) {
                showAlert("Invalid Amount", "Please enter a valid numeric amount.");
            }
        } else {
            showAlert("Missing Information", "Please enter the salary amount.");
        }
    }
    private void saveSalaryToDatabase(double amount) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/expense_manager", "root", "root")) {
            String query = "INSERT INTO salary (amount) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDouble(1, amount);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save salary to the database.");
        }
    }

    private void updateSalaryList(double amount) {
        salaryListArea.appendText("Salary: Rs." + amount + "\n");
    }
    private void addExpense() {
        // Implement the expense addition logic here
        String expenseName = expenseNameField.getText();
        String expenseAmountStr = expenseAmountField.getText();

        if (!expenseName.isEmpty() && !expenseAmountStr.isEmpty()) {
            try {
                double expenseAmount = Double.parseDouble(expenseAmountStr);
                saveExpenseToDatabase(expenseName, expenseAmount);
                updateExpenseList(expenseName, expenseAmount);
                expenseNameField.clear();
                expenseAmountField.clear();
            } catch (NumberFormatException e) {
                showAlert("Invalid Amount", "Please enter a valid numeric amount.");
            }
        } else {
            showAlert("Missing Information", "Please enter both expense name and amount.");
        }
    }
    private void expenseCategory() {
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
    private void saveExpenseToDatabase(String name, double amount) {
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
    }
    private void updateExpenseList(String name, double amount) {
        expenseListArea.appendText(name + ": Rs." + amount + "\n");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
