package minor;
import javafx.application.Application;
//import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;
//import minor.*;
public class Login extends Application{
    private TabPane tabPane;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private AddSalary addSalary;
    private AddExpense addExpense;


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
        
        addSalary = new AddSalary();
        addExpense = new AddExpense();

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
                addSalary.addSalaryTab(tabPane);
                addExpense.addExpenseCategoryTab(tabPane);
                primaryStage.setTitle("Expense Manager - Logged In as " + username);
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    
}
