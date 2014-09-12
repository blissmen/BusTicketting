
/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package bus.ticketing.system;

//~--- non-JDK imports --------------------------------------------------------

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

//~--- JDK imports ------------------------------------------------------------

import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author Harvey
 */
public class Seats_70Controller implements Initializable, ControlledScreen {
    public static populator pop;
    @FXML
    private ToggleButton    three;
    @FXML
    private ToggleButton    two;
    @FXML
    private ToggleButton    one;
    @FXML
    private ToggleButton    four;
    @FXML
    private ToggleButton    five;
    @FXML
    private ToggleButton    nine;
    @FXML
    private ToggleButton    thirteen;
    @FXML
    private ToggleButton    sixty_three;
    @FXML
    private ToggleButton    forty_five;
    @FXML
    private ToggleButton    forty_four;
    @FXML
    private ToggleButton    thirty_eight;
    @FXML
    private ToggleButton    thirty_nine;
    @FXML
    private ToggleButton    forty;
    @FXML
    private ToggleButton    forty_one;
    @FXML
    private ToggleButton    thirty_six;
    @FXML
    private ToggleButton    thirty_seven;
    @FXML
    private ToggleButton    thirty_two;
    @FXML
    private ToggleButton    thirty_four;
    @FXML
    private ToggleButton    thirty_five;
    @FXML
    private ToggleButton    thirty_three;
    @FXML
    private ToggleButton    twenty_eight;
    @FXML
    private ToggleButton    twenty_nine;
    @FXML
    private ToggleButton    twenty_five;
    @FXML
    private ToggleButton    twenty_four;
    @FXML
    private ToggleButton    thirty;
    @FXML
    private ToggleButton    thirty_one;
    @FXML
    private ToggleButton    twenty_six;
    @FXML
    private ToggleButton    twenty_seven;
    @FXML
    private ToggleButton    twenty_two;
    @FXML
    private ToggleButton    twenty_three;
    @FXML
    private ToggleButton    eighteen;
    @FXML
    private ToggleButton    nineteen;
    @FXML
    private ToggleButton    fifteen;
    @FXML
    private ToggleButton    sixteen;
    @FXML
    private ToggleButton    twenty;
    @FXML
    private ToggleButton    fourty_eight;
    @FXML
    private ToggleButton    fifty_seven;
    @FXML
    private ToggleButton    fourteen;
    @FXML
    private ToggleButton    twenty_one;
    @FXML
    private ToggleButton    seventeen;
    @FXML
    private ToggleButton    ten;
    @FXML
    private ToggleButton    six;
    @FXML
    private ToggleButton    eleven;
    @FXML
    private ToggleButton    twelve;
    @FXML
    private ToggleButton    eight;
    @FXML
    private ToggleButton    fifty_nine;
    @FXML
    private ToggleButton    sixty_six;
    @FXML
    private ToggleButton    sixty_two;
    @FXML
    private ToggleButton    fifty_eight;
    @FXML
    private ToggleButton    fifty_four;
    @FXML
    private ToggleButton    fifty;
    @FXML
    private ToggleButton    fourty_six;
    @FXML
    private ToggleButton    fourty_two;
    @FXML
    private ToggleButton    fifty_one;
    @FXML
    private ToggleButton    fourty_seven;
    @FXML
    private ToggleButton    fourty_three;
    @FXML
    private ToggleButton    sixty_four;
    @FXML
    private ToggleButton    seventy;
    @FXML
    private ToggleButton    sixty_nine;
    @FXML
    private ToggleButton    sixty_eight;
    @FXML
    private ToggleButton    sixty_seven;
    @FXML
    private ToggleButton    fifty_five;
    @FXML
    private ToggleButton    fifty_six;
    @FXML
    private ToggleButton    sixty_five;
    @FXML
    private ToggleButton    sixty_one;
    @FXML
    private ToggleButton    sixty;
    @FXML
    private ToggleButton    fifty_two;
    @FXML
    private ToggleButton    fifty_three;
    @FXML
    private ToggleButton    fourty_nine;
    ArrayList               Selectedseates;
    @FXML
    private ToggleButton    seven;
    @FXML
    private GridPane        panel;
    ArrayList               buttons;
    private Functions       function;
    private ArrayList       seatsTacken;

    /**
     * Initializes the controller class.
     */
    public Seats_70Controller() {
        this.Selectedseates = new ArrayList();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        function    = new Functions();
        pop         = new populator();
        seatsTacken = function.getAllData("SeatNo", "PASSENGERS");

        for (int i = 0; i < panel.getChildren().size(); i++) {

            // buttons.add(panel.getChildren().get(0).getId());
            System.out.println(panel.getChildren().get(i).getId());
        }
    }

    void Populate(ArrayList seatTacken) {

        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try {
            if (seatTacken.size() > 1) {
                for (Object sd : seatTacken) {
                    for (int i = 0; i < panel.getChildren().size(); i++) {
                        System.out.println(panel.getChildren().get(i).getId() + ":" + sd);

                        if ((panel.getChildren().get(i).getId() == null)
                            ? sd.toString() == null
                            : panel.getChildren().get(i).getId().equals(sd.toString())) {
                            panel.getChildren().get(i).setDisable(true);
                            System.out.println("found");
                        }
                    }
                }
            }
        } catch (Exception gh) {
            System.out.println(gh.getCause());
        }
    }

    @Override
    public void setScreenParent(ScreensController pane) {}

    @FXML
    private void AddSeat(javafx.event.ActionEvent event) {
        ToggleButton seat     = (ToggleButton) event.getSource();
        Action       tackSeat = Dialogs.create().title("Select A Seat").masthead(
                                    "The Selected Seat Is: " + "" + seat.getText()).message(
                                    "Are You Sure thats the Seat you want because It is A One time deal").showConfirm();

        if (tackSeat.equals(Dialog.Actions.YES)) {
            BookingController.chosean.choseSeat(seat.getId());
            BookingController.AllselectedSeats.add(seat.getId());
            seat.setDisable(true);
        } else {
            return;
        }
    }

    class populator {
        public void populating() {
            Populate(seatsTacken);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
