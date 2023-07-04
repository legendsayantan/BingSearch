package com.legendsayantan.bingsearch;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.util.concurrent.atomic.AtomicReference;
import java.util.prefs.Preferences;

public class Controller {
    //run when initialsied
    @FXML
    protected void initialize() {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());

        //TextFormat
        String numericPattern = "[0-9]*";

        // Create TextFormatters with the numeric pattern
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches(numericPattern)) {
                return change;
            }
            return null;
        });
        TextFormatter<String> textFormatter2 = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches(numericPattern)) {
                return change;
            }
            return null;
        });

        //default browser management
        defaultBrowser.setSelected(prefs.getBoolean("defaultBrowser", true));
        pathButton.setDisable(defaultBrowser.isSelected());
        browserPath.setVisible(!defaultBrowser.isSelected());
        browserPath.setManaged(!defaultBrowser.isSelected());
        defaultBrowser.setOnAction(event -> {
            prefs.putBoolean("defaultBrowser", defaultBrowser.isSelected());
            pathButton.setDisable(defaultBrowser.isSelected());
            browserPath.setVisible(!defaultBrowser.isSelected());
            browserPath.setManaged(!defaultBrowser.isSelected());
        });

        //set count and delay
        count.setText(prefs.get("count", "30"));
        delay.setText(prefs.get("delay", "3"));
        count.setTextFormatter(textFormatter);
        delay.setTextFormatter(textFormatter2);
        count.textProperty().addListener((observable, oldValue, newValue) -> {
            prefs.put("count", newValue);
        });
        delay.textProperty().addListener((observable, oldValue, newValue) -> {
            prefs.put("delay", newValue);
        });

        //auto close tabs
        closeTabs.setSelected(prefs.getBoolean("closeTabs", false));
        closeTabs.setOnAction(event -> {
            prefs.putBoolean("closeTabs", closeTabs.isSelected());
        });

        //open github
        openGithub.setOnMouseClicked(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create("https://github.com/legendsayantan/BingSearch"));
            } catch (java.io.IOException e) {
                System.out.println(e.getMessage());
            }
        });

        //path picker
        browserPath.setText(prefs.get("path", "Open browsername://version to find path"));
        pathButton.setOnAction(event -> {
            javafx.stage.FileChooser chooser = new javafx.stage.FileChooser();
            chooser.setTitle("Select Executable for your browser (Open browsername://version to find out)");
            chooser.setInitialFileName(prefs.get("path", System.getProperty("java.awt.browser")));
            java.io.File selectedFile = chooser.showOpenDialog(pathButton.getScene().getWindow());
            if (selectedFile != null) {
                prefs.put("path", selectedFile.getAbsolutePath());
                browserPath.setText(selectedFile.getAbsolutePath());
            }
        });

        AtomicReference<Thread> searchThread = new AtomicReference<>();
        //start searching
        startButton.setOnAction(event -> {
            if (startButton.getText().equals("Start")) {
                startButton.setText("Stop");
                outputText.setText("Starting...");
                try {
                    searchThread.set(new BingSearch(Integer.parseInt(count.getText()), Integer.parseInt(delay.getText()), browserPath.getText(), closeTabs.isSelected(), defaultBrowser.isSelected()) {
                        @Override
                        public void onProgress(int progress) {
                            Platform.runLater(() -> {
                                outputText.setText("Completed : " + progress);
                                if (progress == Integer.parseInt(count.getText())) {
                                    startButton.setText("Start");
                                }
                            });
                        }

                        @Override
                        public void onError(Exception error) {
                            Platform.runLater(() -> {
                                outputText.setText("Error: " + error.getMessage());
                                startButton.setText("Start");
                            });
                        }
                    }.createThread());
                    searchThread.get().start();
                } catch (Exception e) {
                    outputText.setText("Error: " + e.getMessage());
                }
            } else {
                searchThread.get().interrupt();
                startButton.setText("Start");
            }
        });
    }

    @FXML
    TextField count, delay;
    @FXML
    private Label outputText,openGithub,browserPath;

    @FXML
    CheckBox defaultBrowser,closeTabs;

    @FXML
    Button pathButton,startButton;



}