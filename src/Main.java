import Model.UserDatabase;
import gui.LoginPanel;

public static void main(String[] args) {
    UserDatabase db = new UserDatabase();
    new LoginPanel(db).setVisible(true);
}