
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package bus.ticketing.system;

//~--- non-JDK imports --------------------------------------------------------

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;

import java.time.LocalDate;

import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Harvey
 */
public class MailController implements Initializable, ControlledScreen {
    @FXML
    private TextField        sender;
    @FXML
    private TextField        senderPhone;
    @FXML
    private TextField        id;
    @FXML
    private ComboBox<String> itype;
    @FXML
    private TextArea         desc;
    @FXML
    private TextField        recipient;
    @FXML
    private TextField        recPhone;
    @FXML
    private TextField        dest;
    private String           query;
    private ComboBox<String> depTerminal;
    private ComboBox<String> destTerminal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        itype.getItems().add("Mail");
        itype.getItems().add("Cargo");
    }

    @Override
    public void setScreenParent(ScreensController pane) {}

    @FXML
    private void save() {
        String ID, SENDERNAME, SENDERPHONE, FROM, TYPE, DESC, TO, NAME, PHONE;

        ID          = id.getText();
        SENDERNAME  = sender.getText();
        SENDERPHONE = senderPhone.getText();
        DESC        = desc.getText();
        TYPE        = itype.getSelectionModel().getSelectedItem();
        FROM        = depTerminal.getSelectionModel().getSelectedItem();
        TO          = destTerminal.getSelectionModel().getSelectedItem();
        NAME        = recipient.getText();
        PHONE       = recPhone.getText();
        query       = "INSER INTO MAIL VALUES (" + "'" + ID + "'" + "'" + SENDERNAME + "'" + "'" + SENDERPHONE + "'"
                      + "'" + DESC + "'" + "'" + FROM + "'" + "'" + NAME + "'" + "'" + PHONE + "'" + "'" + TO + "'"
                      + "'" + LocalDate.now() + "'" + ")";
    }

    private void clear() {
        id.clear();
        sender.clear();
        senderPhone.clear();
        desc.clear();
        itype.getItems().clear();
        depTerminal.getItems().clear();
        destTerminal.getItems().clear();
        recipient.clear();
        recPhone.clear();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
