
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package bus.ticketing.system;

//~--- non-JDK imports --------------------------------------------------------

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

//~--- JDK imports ------------------------------------------------------------

import java.lang.reflect.Field;

import java.net.URL;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Harvey
 */
public class HomePageController implements Initializable {

    /**
     * 
     * Interfaces to inject
     */
    public static String         BOOKING_ID   = "booking";
    public static String         BOOKING_FXML = "Bookin.fxml";
    public static String         MAIL_ID      = "mail";
    public static String         MAIL_FXML    = "Mail.fxml";
    public static String         REPORT_ID    = "report";
    public static String         REPORT_FXML  = "Report.fxml";
    public static String         query;
    public static DatabaseHelper helper;

    /**
     * ************************************************
     */
    @FXML
    private AnchorPane anchor;
    ScreensController  screensContainer;

    /**
     * initialises the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            helper = new DatabaseHelper();
        } catch (SQLException ex) {
            Logger.getLogger(HomePageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        screensContainer = new ScreensController(anchor);
        screensContainer.loadScreen(BOOKING_ID, BOOKING_FXML);
        screensContainer.loadScreen(MAIL_ID, MAIL_FXML);
        screensContainer.loadScreen(REPORT_ID, REPORT_FXML);
        screensContainer.setScreen(BOOKING_ID);

        Group root = new Group(screensContainer);
    }

    @FXML
    private void bookSeat(ActionEvent event) {
        screensContainer.setScreen(BOOKING_ID);
    }

    @FXML
    private void sendMail(ActionEvent event) {
        screensContainer.setScreen(MAIL_ID);
    }

    @FXML
    private void viewReports(ActionEvent event) {
        screensContainer.setScreen(REPORT_ID);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
