package View;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;

public class View {
    private BorderPane borderPane;
    private MenuItem showAB, addMRB, addBM, addRB;
    private MenuItem showAC, addMRC, addCM, addRC;
    private MenuItem showAP, addPM, addRP, addMRP;
    private MenuItem addNM, addRN, addMRN;
    private MenuItem elect, showResults;
    private MenuItem clear, exit;
    private Alert alert;
    private static boolean firstLaunch = true;

    public enum Views {
        Main, Form, BallotsTable, CitizensTable, PartiesTable, ElectionResults
    }

    private Views currentView;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    public View(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();

        Menu ballotBoxesMenu = new Menu("BallotBoxes");
        showAB = new MenuItem("Show All BallotBoxes");
        addBM = new MenuItem("Add a Ballot Manually");
        addRB = new MenuItem("Add a Random Ballot");
        addMRB = new MenuItem("Add Multiple Random Ballots");
        ballotBoxesMenu.getItems().addAll(showAB, addBM, addRB, addMRB);

        Menu citizensMenu = new Menu("Citizens");
        showAC = new MenuItem("Show All Citizens");
        addCM = new MenuItem("Add a Citizen Manually");
        addRC = new MenuItem("Add a Random Citizen");
        addMRC = new MenuItem("Add Multiple Random Citizens");
        citizensMenu.getItems().addAll(showAC, addCM, addRC, addMRC);

        Menu partiesMenu = new Menu("Parties");
        showAP = new MenuItem("Show All Parties");
        addPM = new MenuItem("Add a Party Manually");
        addRP = new MenuItem("Add a Random Party");
        addMRP = new MenuItem("Add Multiple Random Parties");
        partiesMenu.getItems().addAll(showAP, addPM, addRP, addMRP);

        Menu nomineesMenu = new Menu("Nominees");
        addNM = new MenuItem("Add a Nominee Manually");
        addRN = new MenuItem("Add a Random Nominee");
        addMRN = new MenuItem("Add Multiple Random Nominees");
        nomineesMenu.getItems().addAll(addNM, addRN, addMRN);

        Menu electionMenu = new Menu("Election");
        elect = new MenuItem("Elect a Party And a Prime Minister");
        showResults = new MenuItem("Show The Election Results");
        showResults.setDisable(true);
        electionMenu.getItems().addAll(elect, showResults);

        Menu misc = new Menu("Navigate");
        clear = new MenuItem("Go to Main Page");
        exit = new MenuItem("Exit Program");
        exit.setOnAction(event -> Platform.exit());
        misc.getItems().addAll(clear, exit);

        menuBar.getMenus().addAll(misc, ballotBoxesMenu, citizensMenu, partiesMenu, nomineesMenu, electionMenu);
        borderPane = new BorderPane();
        Button backButton = new Button("<-");
        backButton.setOnAction(event -> clear.fire());
        HBox top = new HBox(backButton, menuBar);
        HBox.setHgrow(menuBar, Priority.ALWAYS);
        borderPane.setTop(top);
        alert = new Alert(Alert.AlertType.ERROR);

        Scene scene = new Scene(borderPane, 900, 700);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Election Program");
        primaryStage.show();
    }

    public void refreshCurrentView() {
        switch (currentView) {
            case BallotsTable:
                showAB.fire();
                break;
            case CitizensTable:
                showAC.fire();
                break;
            case PartiesTable:
                showAP.fire();
                break;
            case ElectionResults:
                showResults.fire();
                break;
            default:
                clear.fire();
                break;
        }
    }

    private BarChart<String, Number> buildGauge(String name, int value, String color) {
        BarChart<String, Number> barChart =
                new BarChart<>(new CategoryAxis(), new NumberAxis());
        barChart.setTitle(name);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        XYChart.Data<String, Number> data = new XYChart.Data<>(String.valueOf(value), value);
        data.nodeProperty().addListener((ov, oldNode, node) -> {
            if (node != null) {
                node.setStyle("-fx-bar-fill: " + color + ";");
            }
        });
        series.getData().add(data);
        barChart.getData().add(series);
        barChart.setLegendVisible(false);
        return barChart;
    }


    public void setToMainView(int ballotsNum, int citizensNum, int partiesNum) {
        VBox defaultPage = new VBox();
        Text text = new Text();
        if (firstLaunch)
            text.setText("Welcome to The Election Program!");
        else text.setText("The Election Program");
        text.setFont(Font.font("Tahoma Bold", FontWeight.BOLD, 30));
        text.setFill(Color.GOLDENROD);
        text.setStroke(Color.BLACK);
        text.setFontSmoothingType(FontSmoothingType.LCD);
        defaultPage.getChildren().add(text);
        VBox.setMargin(text, new Insets(0, 0, 15, 0));

        if (firstLaunch) {
            Text instruction = new Text("Please Choose One of The Items from The Top MenuBar" +
                    "\n\tOr Click One of The Status Gauges Below");
            instruction.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 15));
            defaultPage.getChildren().add(instruction);
            firstLaunch = false;
        }
        
        BarChart<String, Number> ballotsGauge = buildGauge("Ballot Boxes", ballotsNum, "TAN");
        ballotsGauge.setOnMousePressed(event -> showAB.fire());
        BarChart<String, Number> citizensGauge = buildGauge("Citizens", citizensNum, "DARKSLATEGRAY");
        citizensGauge.setOnMousePressed(event -> showAC.fire());
        BarChart<String, Number> partiesGauge = buildGauge("Political Parties", partiesNum, "MAROON");
        partiesGauge.setOnMousePressed(event -> showAP.fire());
        
        HBox gauges = new HBox(ballotsGauge, citizensGauge, partiesGauge);
        VBox.setMargin(gauges, new Insets(200, 0, 0, 0));
        gauges.setAlignment(Pos.CENTER);
        gauges.setSpacing(10);
        gauges.setMaxHeight(100);
        gauges.setMaxWidth(300);
        defaultPage.getChildren().add(gauges);
        
        defaultPage.setAlignment(Pos.CENTER);
        borderPane.setCenter(defaultPage);
        setCurrentView(Views.Main);
    }

    public void setCurrentView(Views currentView) {
        this.currentView = currentView;
    }

    public void setCenterToVBox(VBox vBox) {
        borderPane.setCenter(vBox);
    }

    public static void alignTextField(String name, TextField textField, HBox hb) {
        Text txt = new Text(name);
        hb.getChildren().addAll(txt, textField);
        hb.setAlignment(Pos.CENTER_RIGHT);
        HBox.setMargin(txt, new Insets(10, 0, 10, 0));
    }

    public void enableVotesResults() {
        showResults.setDisable(false);
    }

    //In our use case it's safe to use, no runtime failures.
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> void setToBallotsTable(List<T> ballots) {
        Label label = new Label("All Ballots: (Double Click To View Details)");
        label.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 14));
        TableView tableView = new TableView();
        TableColumn serialNum = new TableColumn<>("#");
        serialNum.setCellValueFactory(new PropertyValueFactory<>("serialNum"));
        
        TableColumn address = new TableColumn<>("Address");
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        
        TableColumn type = new TableColumn<>("Type");
        type.setCellValueFactory(new PropertyValueFactory<>("typeToString"));
        
        TableColumn designatedCitizensAmount = new TableColumn<>("Voters");
        designatedCitizensAmount.setCellValueFactory(new PropertyValueFactory<>("designatedCitizensAmount"));
        
        serialNum.setStyle("-fx-alignment: CENTER;");
        designatedCitizensAmount.setStyle("-fx-alignment: CENTER;");
        serialNum.setMaxWidth(100);
        serialNum.setMinWidth(50);
        designatedCitizensAmount.setMaxWidth(100);
        designatedCitizensAmount.setMinWidth(50);
        type.setMaxWidth(70);
        type.setMinWidth(70);
        
        tableView.getColumns().addAll(serialNum, type, address, designatedCitizensAmount);
        tableView.getItems().addAll(ballots);
        addDetailsAlertToTableView(tableView,810);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setMaxWidth(300);
        
        VBox result = new VBox(label, tableView);
        result.setSpacing(5);
        result.setAlignment(Pos.CENTER);
        borderPane.setCenter(result);
        setCurrentView(Views.BallotsTable);
    }

    //In our use case it's safe to use, no runtime failures.
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> void setToCitizensTable(List<T> citizens) {
        Label label = new Label("All Citizens:");
        label.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 14));
        TableView tableView = new TableView();
        TableColumn type = new TableColumn<>("Type");
        type.setCellValueFactory(new PropertyValueFactory<>("typeToString"));
        
        TableColumn name = new TableColumn<>("First Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn ID = new TableColumn<>("ID");
        ID.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        TableColumn yearOfBirth = new TableColumn<>("Born");
        yearOfBirth.setCellValueFactory(new PropertyValueFactory<>("yearOfBirth"));
        yearOfBirth.setMaxWidth(50);
        yearOfBirth.setMinWidth(50);
        
        TableColumn canVote = new TableColumn<>("Can Vote");
        canVote.setCellValueFactory(new PropertyValueFactory<>("canVote"));
        canVote.setMaxWidth(80);
        canVote.setMinWidth(80);
        
        TableColumn numOfSicknessDays = new TableColumn<>("Sickness Days");
        numOfSicknessDays.setCellValueFactory(new PropertyValueFactory<>("numOfSicknessDays"));
        
        TableColumn wearingProtectionSuit = new TableColumn<>("Protection Suit");
        wearingProtectionSuit.setCellValueFactory(new PropertyValueFactory<>("wearingProtectionSuit"));
        
        TableColumn carryWeapon = new TableColumn<>("Weapon");
        carryWeapon.setCellValueFactory(new PropertyValueFactory<>("carryWeapon"));
        carryWeapon.setMaxWidth(80);
        carryWeapon.setMinWidth(80);
        
        TableColumn partyToString = new TableColumn<>("Political Party");
        partyToString.setCellValueFactory(new PropertyValueFactory<>("partyToString"));
        partyToString.setMaxWidth(120);
        partyToString.setMinWidth(120);
        
        tableView.getColumns().addAll(type, name, ID, yearOfBirth, canVote, numOfSicknessDays,
                wearingProtectionSuit, carryWeapon, partyToString);
        tableView.getItems().addAll(citizens);
        tableView.getSortOrder().addAll(type);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        VBox result = new VBox(label, tableView);
        result.setAlignment(Pos.CENTER);
        result.setSpacing(5);
        borderPane.setCenter(result);
        setCurrentView(Views.CitizensTable);
    }

    //In our use case it's safe to use, no runtime failures.
    @SuppressWarnings({"rawtypes", "unchecked"})
    public <T> void setToPartiesTable(List<T> parties) {
        Label label = new Label("All Parties: (Double Click To View Details)");
        label.setFont(Font.font("Tahoma", FontWeight.SEMI_BOLD, 14));
        TableView tableView = new TableView();
        TableColumn name = new TableColumn<>("Name");
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        TableColumn wing = new TableColumn<>("Wing");
        wing.setCellValueFactory(new PropertyValueFactory<>("wingToString"));
        
        TableColumn formedDate = new TableColumn<>("Formed Date");
        formedDate.setCellValueFactory(new PropertyValueFactory<>("formedDate"));
        
        TableColumn nomineesAmount = new TableColumn<>("Nominees");
        nomineesAmount.setCellValueFactory(new PropertyValueFactory<>("nomineesAmount"));
        nomineesAmount.setMaxWidth(80);
        nomineesAmount.setMinWidth(80);
        nomineesAmount.setStyle("-fx-alignment: CENTER;");
        
        wing.setMaxWidth(50);
        wing.setMinWidth(50);
        name.setMinWidth(70);
        tableView.getColumns().addAll(name, wing, formedDate, nomineesAmount);
        tableView.getItems().addAll(parties);
        addDetailsAlertToTableView(tableView,500);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setMaxWidth(350);
        
        VBox result = new VBox(label, tableView);
        result.setAlignment(Pos.CENTER);
        result.setSpacing(5);
        borderPane.setCenter(result);
        setCurrentView(Views.PartiesTable);
    }

    //In our use case it's safe to use, no runtime failures.
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void setToElectionResults(List<String> electionResults, double votingPercentage, String pMinister,
                                     String winningPartyName) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        electionResults.forEach(result -> pieChartData.add(
                new PieChart.Data(result.substring(0, result.lastIndexOf('-')),
                        Integer.parseInt(result.substring(result.lastIndexOf('-') + 1)))));
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Election Results");
        TableView tableView = new TableView();
        TableColumn<String, String> party = new TableColumn<>("Party");
        party.setCellValueFactory(str ->
                new SimpleStringProperty(str.getValue().substring(0, str.getValue().lastIndexOf('-'))));
        
        TableColumn<String, String> votes = new TableColumn<>("Votes");
        votes.setCellValueFactory(str ->
                new SimpleStringProperty(str.getValue().substring(str.getValue().lastIndexOf('-') + 1)));
        
        tableView.getColumns().addAll(party, votes);
        tableView.getItems().addAll(electionResults);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        votes.setComparator(Comparator.comparingInt(Integer::parseInt));
        votes.setSortType(TableColumn.SortType.DESCENDING);
        votes.setMaxWidth(100);
        votes.setMinWidth(60);
        votes.setStyle("-fx-alignment: CENTER;");
        tableView.getSortOrder().addAll(votes);
        tableView.setMaxHeight(220);
        
        Label votingPercent = new Label(">Total percentage of voting: " + df2.format(votingPercentage) + '%');
        Label winningParty = new Label(">The Winning Party is: " + winningPartyName.replace('-', '\0'));
        Label primeMinister = new Label(pMinister);
        primeMinister.setWrapText(true);
        votingPercent.setWrapText(true);
        winningParty.setWrapText(true);
        
        VBox textDetails = new VBox(winningParty, primeMinister, votingPercent);
        textDetails.setSpacing(15);
        textDetails.setPadding(new Insets(30, 0, 0, 20));
        textDetails.setMaxWidth(500);
        
        HBox details = new HBox(tableView, textDetails);
        details.setSpacing(10);
        details.setAlignment(Pos.CENTER);
        
        VBox result = new VBox(pieChart, details);
        result.setSpacing(30);
        result.setAlignment(Pos.CENTER);
        borderPane.setCenter(result);
        setCurrentView(Views.ElectionResults);
    }
    
    //In our use case it's safe to use, no runtime failures.
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addDetailsAlertToTableView(TableView tableView, int width){
        tableView.setRowFactory(tView -> {
            TableRow row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Alert detailsView = new Alert(Alert.AlertType.INFORMATION);
                    TextArea textArea = new TextArea(row.getItem() + "");
                    textArea.setEditable(false);
                    textArea.setMinWidth(width);
                    detailsView.setHeaderText("Details: ");
                    detailsView.setGraphic(textArea);
                    detailsView.showAndWait();
                }
            });
            return row;
        });
    }

    public void showAlert(Alert.AlertType type, String message) {
        alert.setAlertType(type);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.show();
    }


    public void triggerShowResults() {
        showResults.fire();
    }

    public void addEventToAddBM(EventHandler<ActionEvent> addBBM_EventHandler) {
        addBM.setOnAction(addBBM_EventHandler);
    }

    public void addEventToAddCM(EventHandler<ActionEvent> addCM_EventHandler) {
        addCM.setOnAction(addCM_EventHandler);
    }

    public void addEventToAddNM(EventHandler<ActionEvent> addNM_eventHandler) {
        addNM.setOnAction(addNM_eventHandler);
    }

    public void addEventToAddPM(EventHandler<ActionEvent> addPM_EventHandler) {
        addPM.setOnAction(addPM_EventHandler);
    }

    public void addEventToAddRC(EventHandler<ActionEvent> addRC_EventHandler) {
        addRC.setOnAction(addRC_EventHandler);
    }

    public void addEventToAddRN(EventHandler<ActionEvent> addRN_EventHandler) {
        addRN.setOnAction(addRN_EventHandler);
    }

    public void addEventToAddRP(EventHandler<ActionEvent> addRP_EventHandler) {
        addRP.setOnAction(addRP_EventHandler);
    }

    public void addEventToAddRB(EventHandler<ActionEvent> addRB_EventHandler) {
        addRB.setOnAction(addRB_EventHandler);
    }

    public void addEventToShowBB(EventHandler<ActionEvent> showBB_EventHandler) {
        showAB.setOnAction(showBB_EventHandler);
    }

    public void addEventToShowAC(EventHandler<ActionEvent> showAC_EventHandler) {
        showAC.setOnAction(showAC_EventHandler);
    }

    public void addEventToShowAP(EventHandler<ActionEvent> showAP_EventHandler) {
        showAP.setOnAction(showAP_EventHandler);
    }

    public void addEventToAddMRB(EventHandler<ActionEvent> addMRB_EventHandler) {
        addMRB.setOnAction(addMRB_EventHandler);
    }

    public void addEventToAddMRC(EventHandler<ActionEvent> addMRC_EventHandler) {
        addMRC.setOnAction(addMRC_EventHandler);
    }

    public void addEventToAddMRP(EventHandler<ActionEvent> addMRP_EventHandler) {
        addMRP.setOnAction(addMRP_EventHandler);
    }

    public void addEventToAddMRN(EventHandler<ActionEvent> addMRN_EventHandler) {
        addMRN.setOnAction(addMRN_EventHandler);
    }

    public void addEventToElect(EventHandler<ActionEvent> elect_EventHandler) {
        elect.setOnAction(elect_EventHandler);
    }

    public void addEventToShowResults(EventHandler<ActionEvent> showResults_EventHandler) {
        showResults.setOnAction(showResults_EventHandler);
    }

    public void addEventToClearScene(EventHandler<ActionEvent> clearScene_EventHandler) {
        clear.setOnAction(clearScene_EventHandler);
    }
}