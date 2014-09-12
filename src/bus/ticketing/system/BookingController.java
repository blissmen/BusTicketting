
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package bus.ticketing.system;

//~--- non-JDK imports --------------------------------------------------------

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import static bus.ticketing.system.HomePageController.helper;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;

import java.sql.SQLException;
import java.sql.Timestamp;

import java.time.Instant;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Harvey
 */
public class BookingController implements Initializable, ControlledScreen {
    protected static ArrayList AllselectedSeats = new ArrayList();

    /*
     *  Buses to load
     */
    public static String      SEATS_30_ID   = "seats30";
    public static String      SEAT_30_FXML  = "Seats_30.fxml";
    public static String      SEATS_55_ID   = "seats55";
    public static String      SEATS_55_FXML = "Seats_55.fxml";
    public static String      SEATS_70_ID   = "seats70";
    public static String      SEATS_70_FXML = "Seats_70.fxml";
    public static setSeat     chosean;
    @FXML
    private TextField         chosenSeats;
    @FXML
    private TextField         totalAmt;
    @FXML
    private DatePicker        expDate;
    @FXML
    private TextField         from;
    @FXML
    private ComboBox<String>  depTerminal;
    @FXML
    private TextField         to;
    @FXML
    private ComboBox          arrivTerminal;
    @FXML
    private AnchorPane        seatsAnchor;
    @FXML
    private TextField         driver;
    @FXML
    private ComboBox          departure;
    @FXML
    private ComboBox          destination;
    @FXML
    private DatePicker        date;
    @FXML
    private ComboBox          bus;
    @FXML
    private TextField         seats;
    ArrayList                 departr, destinatn, busNum;
    private String            query;
    @FXML
    private TextField         phone;
    @FXML
    private TextField         name;
    @FXML
    private TextField         idNumber;
    ScreensController         busContainer;
    LoadBus                   newBus;
    String                    dept, dest, passName, id, passNumber;
    String                    depTerm, destTerm, selectedSeats;
    LocalDate                 exDate;
    Timestamp                 depDate;
    private ScreensController busController;
    private int               busseats;
    private Functions         function;
    private ArrayList         seatsTacken;
    private String            seat;
    private String            BusNumber;

    void chosenSeat(String text) {

        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        chosenSeats.setText(text);
        System.out.println(text);
    }

    /*
     *
     *
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            function      = new Functions();
            chosean       = new setSeat();
            busController = new ScreensController(seatsAnchor);
            busController.loadScreen(SEATS_30_ID, SEAT_30_FXML);
            busController.loadScreen(SEATS_55_ID, SEATS_55_FXML);
            busController.loadScreen(SEATS_70_ID, SEATS_70_FXML);
            busController.setScreen(SEATS_70_ID);
            newBus      = new LoadBus();
            departr     = new ArrayList<>();
            destinatn   = new ArrayList<>();
            busNum      = new ArrayList<>();
            seatsTacken = function.getAllData("SeatNo", "PASSENGERS");
            System.out.println(seatsTacken);

            if (seatsTacken.get(0) != "0") {
                Seats_70Controller.pop.populating();
            }

            AnchorPane gh = (AnchorPane) seatsAnchor.getChildren().get(0);

            System.out.println(gh.getChildrenUnmodifiable().toString());
            query = "SELECT LOCATION FROM TERMINALS";
            helper.setQuery(query);

            if (helper.resultSet.first()) {
                do {
                    departr.add(helper.resultSet.getString("LOCATION"));
                    destinatn.add(helper.resultSet.getString("LOCATION"));
                } while (helper.resultSet.next());

                departure.getItems().addAll(departr);

//              departure.getSelectionModel().selectFirst();
                destination.getItems().addAll(destinatn);

//              destination.getSelectionModel().selectFirst();
            }

            query = "SELECT BUSNUMBER FROM BUSES";
            helper.setQuery(query);
            bus.getItems().clear();

            if (helper.resultSet.first()) {
                do {
                    busNum.add(helper.resultSet.getString("BUSNUMBER"));
                } while (helper.resultSet.next());

                bus.getItems().addAll(busNum);
            }

            bus.getItems().add("Select Bus Number");
            bus.getSelectionModel().select("Select Bus Number");
            populateBusInfo(null);
        } catch (SQLException | IllegalStateException ex) {
            Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * 
         * setting up combo boxes
         */
        departure.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    from.setText(newValue);
                    query = "SELECT TERMINAL FROM TERMINALS";
                    helper.setQuery(query);
                    depTerminal.getItems().clear();

                    if (helper.resultSet.first()) {
                        departr.clear();

                        do {
                            System.out.println(helper.resultSet.getString("TERMINAL"));
                            departr.add(helper.resultSet.getString("TERMINAL"));
                        } while (helper.resultSet.next());

                        depTerminal.getItems().addAll(departr);
                        depTerminal.getSelectionModel().selectFirst();
                    }
                } catch (SQLException | IllegalStateException ex) {
                    Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        destination.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    to.setText(newValue);
                    query = "SELECT TERMINAL FROM TERMINALS";
                    helper.setQuery(query);

                    if (helper.resultSet.first()) {
                        destinatn.clear();

                        do {
                            destinatn.add(helper.resultSet.getString("TERMINAL"));
                        } while (helper.resultSet.next());

                        arrivTerminal.getItems().addAll(departr);
                        arrivTerminal.getSelectionModel().selectFirst();
                    }
                } catch (SQLException | IllegalStateException ex) {
                    Logger.getLogger(BookingController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        this.date.setValue(LocalDate.now());
        expDate.setValue(LocalDate.now());

        /**
         * ****************End of combo box setup
         */
    }

    @Override
    public void setScreenParent(ScreensController pane) {}

    @FXML
    private void submit(ActionEvent event) {
        dept       = this.from.getText();
        dest       = this.to.getText();
        passName   = this.name.getText();
        id         = this.idNumber.getText();
        passNumber = this.phone.getText();
        BusNumber  = this.bus.getSelectionModel().getSelectedItem().toString();
        seat       = chosenSeats.getText();

        Formatter format   = new Formatter();
        Calendar  date     = Calendar.getInstance();
        String    CurrDate = format.format("%tF", date).toString();

        exDate = this.expDate.getValue();

        try {
            query = "INSERT INTO PASSENGERS VALUES(" + Integer.valueOf(id) + "," + "'" + passName + "'," + "'" + dept
                    + "'," + "'" + dest + "'," + "'" + passNumber + "'," + "'" + CurrDate + "'," + "'" + seat + "')";

            Action action = Dialogs.create().title("Insert New Passenegr").masthead(
                                "Inserting Passeger with Data").message(
                                "Passenegr Name : 7" + "" + passName + "\n Passenegr Number : " + passNumber
                                + "\n Bus Number :" + BusNumber + " \n Destination : " + dest + "  \n Depature :"
                                + dept + "\n Seat Number :" + this.chosenSeats.getText() + "").showConfirm();

            if (action.equals(Dialog.Actions.YES)) {
                try {
                    helper.setQuery(query);
                    Dialogs.create().title("Success").message("Passenger Succesfully registered ").showInformation();
                    seatsTacken.add(seat);
                    clear();
                } catch (Exception asd) {
                    Dialogs.create().title("Error").message("Error in Validating Data").showError();
                    asd.printStackTrace();
                }
            } else {
                return;
            }
        } catch (IllegalStateException ex) {
            Dialogs.create().title("Error ").masthead("Error In Database Connection").message(
                "Error In Inserting Passenger " + passName).showError();
            ex.printStackTrace();
        }
    }

    @FXML
    private void idNumber(ActionEvent event) {}

    private void busFull() throws SQLException {
        depTerm  = this.depTerminal.getSelectionModel().getSelectedItem();
        destTerm = (String) this.arrivTerminal.getSelectionModel().getSelectedItem();
        depDate  = Timestamp.from(Instant.now());
        query    = "INSERT INTO ITINERARY VALUES(" + "'" + dept + "'" + "'" + depTerm + "'" + "'" + dest + "'" + "'"
                   + destTerm + "'" + "'" + depDate + "'" + ")";
        helper.setQuery(query);

        // print Travel List
        // should contain mails bus has to carry
    }

    private void checkBusStatus() {
        GridPane seat;

        /*
         * get the seats index in bus
         * loop through indices
         * if an index is a toggle button and is not checked
         * notify the cashier that a seat is empty
         * else print all necessary documents and load next bus
         */
    }

    private void clear() {
        name.clear();
        phone.clear();
        idNumber.clear();
        chosenSeats.clear();
        totalAmt.clear();
        expDate.setValue(LocalDate.now());
    }

    private void clearAll() {
        clear();
        seatsAnchor.getChildren().clear();
        from.clear();
        to.clear();
        depTerminal.getItems().clear();
        arrivTerminal.getItems().clear();
    }

    @FXML
    private void populateBusInfo(ActionEvent event) {
        if (bus.getSelectionModel().getSelectedItem() == "Select Bus Number") {    // busController.setScreen(null);
            return;
        } else {
            String newValue = bus.getSelectionModel().getSelectedItem().toString();
            String qu       = "select NUMBEROFSEATS from buses where BUSNUMBER ='" + newValue + "'";

            try {
                helper.setQuery(qu);
                System.out.println(helper.resultSet.getString(1));
                seats.setText("" + helper.resultSet.getString(1));

                // setBus(Integer.parseInt(helper.resultSet.getString(1)));
                busseats = helper.resultSet.getInt(1);
            } catch (Exception df) {
                System.out.println(df.getMessage());
                seats.setText("Not Defined");
            }

            try {
                newBus.setBus(busseats);
            } catch (Exception s) {
                System.out.println(s.getMessage() + "::" + s.getCause() + "::" + s.getClass());
            }
        }
    }

    private void selectsea() {

        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class LoadBus implements ControlledScreen {
        public ScreensController screen;

        @Override
        public void setScreenParent(ScreensController pane) {
            screen = pane;
        }

        public void setBus(int size) {
            switch (size) {
            case 30 :
                busController.setScreen(SEATS_30_ID);

                break;

            case 55 :
                busController.setScreen(SEATS_55_ID);

                break;

            case 70 :
                busController.setScreen(SEATS_70_ID);

                break;
            }
        }
    }


    class setSeat {
        public void choseSeat(String sea) {
            chosenSeat(sea);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
