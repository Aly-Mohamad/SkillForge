import Model.JsonDatabaseManager;
import gui.LoginPanel;

public static void main(String[] args) {
    JsonDatabaseManager db = new JsonDatabaseManager();
    new LoginPanel(db).setVisible(true);
}