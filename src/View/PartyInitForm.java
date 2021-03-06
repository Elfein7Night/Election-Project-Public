package View;

import Model.MyException;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Arrays;

public class PartyInitForm extends Form {
    public final TextField name, year, month, day;
    public final RadioButton leftWing, rightWing, centerWing;

    public PartyInitForm() {
        super();
        name = new TextField();
        year = new TextField();
        year.setPrefWidth(80);
        month = new TextField();
        month.setPrefWidth(30);
        day = new TextField();
        day.setPrefWidth(30);
        ToggleGroup toggleGroup = new ToggleGroup();
        leftWing = new RadioButton("Left");
        leftWing.setSelected(true);
        rightWing = new RadioButton("Right");
        centerWing = new RadioButton("Center");
        HBox nameBox = new HBox();
        View.alignTextField("Party Name:    ", name, nameBox);
        Text txt = new Text("Formed Date: ");
        HBox dateBox = new HBox();
        dateBox.getChildren().addAll(txt, day, new Text(" / "), month, new Text(" / "), year);
        dateBox.setAlignment(Pos.CENTER_RIGHT);

        VBox.setMargin(nameBox, new Insets(0, 350, 0, 0));
        VBox.setMargin(dateBox, new Insets(0, 350, 0, 0));
        Text text = new Text("Please Fill all fields:");
        VBox.setMargin(text, new Insets(0, 0, 0, 400));
        Text wingText = new Text("Wing:");
        VBox.setMargin(wingText, new Insets(0, 0, 0, 400));
        formView.getChildren().addAll(text, nameBox, dateBox, wingText, leftWing, rightWing, centerWing, submitButton);
        Arrays.asList(leftWing, rightWing, centerWing)
                .forEach(rb -> {
                    rb.setToggleGroup(toggleGroup);
                    View.setCursorAsSelect(rb);
                });
        formView.setAlignment(Pos.CENTER_LEFT);
        formView.setSpacing(10);
        VBox.setMargin(leftWing, new Insets(0, 0, 0, 400));
        VBox.setMargin(rightWing, new Insets(0, 0, 0, 400));
        VBox.setMargin(centerWing, new Insets(0, 0, 0, 400));
        VBox.setMargin(submitButton, new Insets(0, 0, 0, 450));
    }

    public String getWing() throws MyException {
        if (leftWing.isSelected())
            return "Left";
        if (rightWing.isSelected())
            return "Right";
        if (centerWing.isSelected())
            return "Center";
        throw new MyException("No wing selected!");
    }

    public boolean getAllFilled() {
        if (name.getText().trim().isEmpty())
            return false;
        if (year.getText().trim().isEmpty())
            return false;
        if (month.getText().trim().isEmpty())
            return false;
        if (day.getText().trim().isEmpty())
            return false;
        return leftWing.isSelected() || rightWing.isSelected() || centerWing.isSelected();
    }
}
