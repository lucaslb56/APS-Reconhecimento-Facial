import org.opencv.core.Core;

import dataBase.DBConnection;
import interfaces.MainWindow;


public class Main {
    public static void main(String[] args) {
        DBConnection.startDB();
        MainWindow mainWindow = new MainWindow();
        mainWindow.start();
    }
}
