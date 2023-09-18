package edu.planuj.client;

import edu.planuj.Presentation.database.DatabaseFactory;
import edu.planuj.Server.database.Database;
import edu.planuj.clientConnection.ClientConnectionFactory;
import edu.planuj.clientConnection.ClientConnectionManager;
import edu.planuj.clientConnection.NotificationObserverImplementation;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ConnectException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RealApplication extends Application {
    private static Database database;
    private static Stage stage;
    private static ClientConnectionManager connectionManager;

    public static Database getDatabase() {
        return database;
    }

    @Override
    public void start(Stage stage) throws IOException, DatabaseFactory.DatabaseFactoryException {

        RealApplication.stage = stage;

        //Notifications
        LoggingNotificationSubscriber loggingNotificationSubscriber = new LoggingNotificationSubscriber();
        NotificationObserverImplementation.getInstance().subscribe(loggingNotificationSubscriber);

        connectionManager = ClientConnectionFactory.getInstance().getClientConnection();
        database = DatabaseFactory.getInstance().getServerDatabase(connectionManager.getRequestHandler());

        FXMLLoader fxmlLoader = new FXMLLoader(RealApplication.class.getResource("base-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setResizable(true);
        stage.setMinHeight(400);
        stage.setMinWidth(600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();



        try {
            connectionManager.startService();
        } catch (ConnectException e) {
            setScene("retry-connection.fxml");
            Logger.getAnonymousLogger().log(Level.SEVERE, "Exception while connecting to server: ", e);
            //throw new RuntimeException(e);
        }
    }

    public static void retryConnection(){
        try {
            connectionManager.startService();
            setScene("login-view.fxml");
        }catch (Exception ignored){

        }
    }

    public static void setScene(String viewName) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(RealApplication.class.getResource(viewName));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("projektUJ");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws Exception {
        launch();
    }
}
