/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dani.java.examenuf6.view;

import dani.java.examenuf6.controller.EventDAO;
import dani.java.examenuf6.controller.EventService;
import dani.java.examenuf6.controller.Main;
import dani.java.examenuf6.model.Event;
import eu.schudt.javafx.controls.calendar.DatePicker;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author dani
 */
public class AppViews {
    
    /**
     * Llista observable per carregar dades al tableview
     */
    private static ObservableList<Event> list;
    
    /**
     * Event auxiliar per utilitzar amb els mètodes d'operacio a la base de dades
     */
    private static Event event;
    
    /**
     * Formatter per convertir les dates en Strings pel datePicker (Calendari)
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Formatter per convertir les dates a strings pel logger
     */
    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    /**
     * Servei per realitzar operacions a la base de dades
     */
    private static EventService dao;
    
    /**
     * Stage auxiliar per generar finestres pels mètodes d'inserció i modificació del programa
     */
    private static Stage stage;
    
    /**
     * Variable de control utilitzada per guardar l'estat de visibilitat del registre en el programa
     */
    private static boolean showLog = false;
    
    /**
     * Textarea on es mostra el registre d'operacions en el programa
     */
    public static TextArea area;
    
    /**
     * Objecte Date utilitzat per generar dates pel logger i el log de la interficie
     */
    private static Date date = new Date();
    
    /**
     * Metode per generar el Scene principal de la interfície del programa
     * @return Scene del menu principal
     */
    public static Scene mainScene() {
        list = FXCollections.observableArrayList();
        
        dao = new EventService(new EventDAO(null));
        
        List<Event> l = dao.getAll();
        
        for (Event e:l) {
            list.add(e);
        }
        
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(45));
        //gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(25);
	gridPane.setHgap(25);

        final TableView tableView = new TableView(list);
        
        TableColumn<Event, String> nameColumn = new TableColumn<>("Nom");
        TableColumn<Event, String> descColumn = new TableColumn<>("Descripció");
        TableColumn<Event, Double> priceColumn = new TableColumn<>("Preu");
        TableColumn<Event, Boolean> ticketsColumn = new TableColumn<>("Entrades Disponibles");
        TableColumn<Event, String> dateColumn = new TableColumn<>("Data");
        
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("Name"));
        descColumn.setCellValueFactory(new PropertyValueFactory<>("Description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));
        ticketsColumn.setCellValueFactory(new PropertyValueFactory<>("TicketsAvailable"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("Data"));
        
        tableView.getColumns().addAll(nameColumn, descColumn, priceColumn, ticketsColumn, dateColumn);
        tableView.setMinSize(450, 300);
        tableView.setMaxSize(450, 300);
        
        tableView.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent x) {
                if (x.isPrimaryButtonDown()) {
                    System.out.println(tableView.getSelectionModel().getSelectedItem());
                    event = (Event) tableView.getSelectionModel().getSelectedItem();
                }
            }
        });
        
        gridPane.add(tableView, 0, 0);
        
        Button addButton = new Button("Nou");
        addButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                insertEvent();
            }
        });
        addButton.setMinWidth(80);
        
        Button updateButton = new Button("Modificar");
        updateButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                updateEvent();
            }
        });
        updateButton.setMinWidth(80);
        
        Button deleteButton = new Button("Esborrar");
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                deleteEvent();
            }
        });
        deleteButton.setMinWidth(80);
        
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(addButton, updateButton, deleteButton);
        gridPane.add(vbox, 1, 0);
        
        area = new TextArea("");
        area.setMinHeight(200);
        area.setMaxHeight(200);
        area.setVisible(false);
        area.setEditable(false);
        GridPane.setColumnSpan(area, 2);
        gridPane.add(area, 0, 2);        
        
        Button logButton = new Button("Log");
        logButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (showLog == false) {
                    Main.stage.setMinHeight(700);
                    Main.stage.setMaxHeight(700);
                    area.setVisible(true);
                    showLog = true;
                } else {
                    Main.stage.setMinHeight(450);
                    Main.stage.setMaxHeight(450);
                    area.setVisible(false);
                    showLog = false;
                }
            }
        });
        logButton.setMinWidth(80);
        gridPane.add(logButton, 0, 1);
        
        Button exitButton = new Button("Sortir");
        exitButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                dao.closeService();
                Main.stage.close();
            }
        });
        exitButton.setMinWidth(80);
        gridPane.add(exitButton, 1, 1);

        Scene scene = new Scene(gridPane, 640, 450);
        return scene;        
    }
    
    /**
     * Mètode que crea la pantalla per inserir nous registres a la taula
     */
    private static void insertEvent() {
        VBox vbox = new VBox(15);
        vbox.setAlignment(Pos.CENTER);
        
        Label nameLabel = new Label("Nom");
        final TextField nameField = new TextField();
        nameField.setMaxWidth(200);
        
        Label descLabel = new Label("Descripció");
        final TextField descField = new TextField();
        descField.setMaxWidth(200);
        
        Label priceLabel = new Label("Preu");
        final TextField priceField = new TextField();
        priceField.setMaxWidth(200);
        
        Label ticketLabel = new Label("Entrades disponibles");
        final CheckBox ticketBox = new CheckBox();
        
        HBox hbox1 = new HBox(20);
        hbox1.setAlignment(Pos.CENTER);
        hbox1.getChildren().addAll(ticketLabel, ticketBox);
        
        Label dateLabel = new Label("Data");
        final DatePicker datePicker = new DatePicker(Locale.getDefault());
        datePicker.setDateFormat(sdf);
        datePicker.getCalendarView().todayButtonTextProperty().set("Avui");
        datePicker.getCalendarView().setShowWeeks(false);
        datePicker.getStylesheets().add("file:///" + new File("DatePicker.css").getAbsolutePath());
        datePicker.getCalendarView().setShowTodayButton(false);
        datePicker.setMaxWidth(200);
        
        Button okButton = new Button("Acceptar");
        okButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Event e = new Event(nameField.getText(), descField.getText(), Double.valueOf(priceField.getText()), ticketBox.isSelected(), datePicker.getSelectedDate());
                    dao.insert(e);
                    list.add(e);
                } catch (Exception e) {
                    errorDialog(e.getMessage());
                    EventDAO.logger.log(Level.SEVERE, e.getMessage());
                    area.appendText(sdf2.format(date) + " " + Event.class.getName() + "\n"+ Level.SEVERE + ": " + e.getMessage());
                }
                stage.close();
            }
        });
        
        Button cancelButton = new Button("Cancelar");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                stage.close();
            }
        });
        
        HBox hbox2 = new HBox(20);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.getChildren().addAll(okButton, cancelButton);
        
        vbox.getChildren().addAll(nameLabel, nameField, descLabel, descField, priceLabel, priceField, hbox1, dateLabel, datePicker, hbox2);
        
        Scene scene = new Scene(vbox, 300, 480);
        stage = new Stage();
        
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(Main.stage);
        stage.show();
    }
    
    /**
     * Mètode per modificar registres de la taula
     */
    private static void updateEvent() {
        if (event != null) {
            VBox vbox = new VBox(15);
            vbox.setAlignment(Pos.CENTER);

            Label nameLabel = new Label("Nom");
            final TextField nameField = new TextField(event.getName());
            nameField.setMaxWidth(200);

            Label descLabel = new Label("Descripció");
            final TextField descField = new TextField(event.getDescription());
            descField.setMaxWidth(200);

            Label priceLabel = new Label("Preu");
            final TextField priceField = new TextField(String.valueOf(event.getPrice()));
            priceField.setMaxWidth(200);

            Label ticketLabel = new Label("Entrades disponibles");
            final CheckBox ticketBox = new CheckBox();

            HBox hbox1 = new HBox(20);
            hbox1.setAlignment(Pos.CENTER);
            hbox1.getChildren().addAll(ticketLabel, ticketBox);

            Label dateLabel = new Label("Data");
            final DatePicker datePicker = new DatePicker(Locale.getDefault());
            datePicker.setDateFormat(sdf);
            datePicker.getCalendarView().todayButtonTextProperty().set("Avui");
            datePicker.getCalendarView().setShowWeeks(false);
            datePicker.getStylesheets().add("file:///" + new File("DatePicker.css").getAbsolutePath());
            datePicker.getCalendarView().setShowTodayButton(false);
            datePicker.setMaxWidth(200);
            datePicker.setSelectedDate(event.getData());

            Button okButton = new Button("Acceptar");
            okButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent x) {
                    try {
                        event.setName(nameField.getText());
                        event.setDescription(descField.getText());
                        event.setPrice(Double.valueOf(priceField.getText()));
                        event.setTicketsAvailable(ticketBox.isSelected());
                        event.setData(datePicker.getSelectedDate());
                        dao.update(event);
                        list.remove(event);
                        list.add(event);
                    } catch (Exception e) {
                        errorDialog(e.getMessage());
                        EventDAO.logger.log(Level.SEVERE, e.getMessage());
                        area.appendText(sdf2.format(date) + " " + Event.class.getName() + "\n"+ Level.SEVERE + ": " + e.getMessage());
                    }
                    stage.close();
                }
            });

            Button cancelButton = new Button("Cancelar");
            cancelButton.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    stage.close();
                }
            });

            HBox hbox2 = new HBox(20);
            hbox2.setAlignment(Pos.CENTER);
            hbox2.getChildren().addAll(okButton, cancelButton);

            vbox.getChildren().addAll(nameLabel, nameField, descLabel, descField, priceLabel, priceField, hbox1, dateLabel, datePicker, hbox2);

            Scene scene = new Scene(vbox, 300, 480);
            stage = new Stage();

            stage.setScene(scene);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(Main.stage);
            stage.show();
        }
    }
    
    /**
     * Mètode per eliminar registres de la taula
     */
    private static void deleteEvent() {
        if (event != null) {
            list.remove(event);
            dao.delete(event);
            event = null;
        }
    }
    
    /**
     * Mètode per mostrar per pantalla els missatges d'error que llenci el programa en una finestra nova
     * @param msg Missatge d'error a mostrar per pantalla
     */
    public static void errorDialog(String msg) {
		final Stage dialogStage = new Stage();
        Text text = new Text(msg);
        text.setLineSpacing(5);
        VBox vbox = new VBox(20);
        Button acceptButton = new Button("Acceptar");
        acceptButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent t) {
                dialogStage.close();
            }
        });
        vbox.getChildren().addAll(text,acceptButton);
        vbox.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox, 270, 125, Color.web("eee"));
        dialogStage.initOwner(Main.stage);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setScene(scene);
        dialogStage.show();
	}
    
}
