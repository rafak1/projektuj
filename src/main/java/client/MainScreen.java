package client;

import Utils.TaskInfo;
import Utils.TeamInfo;
import Utils.TeamUser;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class MainScreen implements Initializable {
    @FXML
    public HBox pane;
    @FXML
    public StackPane main;
    @FXML
    public Button teamsButton;
    private boolean isTeamsButtonPressed;
    @FXML
    public Button logInButton;
    private boolean isLogInButtonPressed;
    @FXML
    public TeamsView teamsView;
    @FXML
    public MembersView membersView;
    @FXML
    public TasksView tasksView;
    public BorderPane logInView;
    public LoginView loginController;
    static MainScreen instance;



    public static MainScreen getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        AppHandler.setMainScreen(this);

        instance = this;
        HBox.setHgrow(main, Priority.ALWAYS);
        teamsView = new TeamsView();
        isTeamsButtonPressed = false;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));

        try {
            logInView = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        loginController = loader.getController();
    }



    public void setTasks(Collection<TaskInfo> tasks) {
        tasksView.setTasks(tasks);
    }

    public void setMembers(Collection<TeamUser> members) {
        membersView.setMembers(members);
    }

    public void setTeams(Collection<TeamInfo> teams) {
        teamsView.setTeams(teams);
    }
    public void addTask(TaskInfo task) {
        tasksView.addTask(task);
    }
    public void addUserToTask(TaskInfo task, String user) {

    }

    public void addMember(TeamUser name) {
        membersView.addMember(name);
    } // ?


    public void handleTeamsButton(ActionEvent actionEvent) {
        if (isTeamsButtonPressed) {
            // operations on button

            // operations on pane
            isTeamsButtonPressed = false;
            main.getChildren().remove(teamsView);
        } else {
            // operations on button

            // operations on pane

            isTeamsButtonPressed = true;
            main.getChildren().add(teamsView);
        }
    }

    public void handleLogInButton(ActionEvent actionEvent) {
        if (isLogInButtonPressed) {
           closeLogInView();
        }
        else {
            showLogInView();
        }
    }

    public void reportError(Exception e) {
        Logger.getAnonymousLogger().info("Error: " + e.getMessage());
    }

    public void setLogInViewExitable(boolean flag) {
        loginController.setExitable(flag);
    }

    public void showLogInView() {
        if (!main.getChildren().contains(logInView)) {
            main.getChildren().add(logInView);
            isLogInButtonPressed = true;
        }
    }

    public void closeLogInView() {
        if (main.getChildren().contains(logInView) && loginController.getExitable()) {
            main.getChildren().remove(logInView);
            isLogInButtonPressed = false;
        }
    }

    public void showTeams() {
        if (!main.getChildren().contains(teamsView)) {
            main.getChildren().add(teamsView);
            isTeamsButtonPressed = true;
        }
    }

    public void closeTeams() {
        if (main.getChildren().contains(teamsView)) {
            main.getChildren().remove(teamsView);
            isTeamsButtonPressed = false;
        }
    }
}