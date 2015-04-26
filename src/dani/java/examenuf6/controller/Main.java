/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dani.java.examenuf6.controller;

import dani.java.examenuf6.view.AppViews;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author dani
 */
public class Main extends Application {
    
    /**
     * Stage principal del programa
     */
    public static Stage stage;
    
    @Override
    public void start(Stage primaryStage) {
        stage = new Stage();
        stage.setScene(AppViews.mainScene());
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
