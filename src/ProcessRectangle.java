import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;


public class ProcessRectangle extends Label {

    public ProcessRectangle(String text, Color color) {
        super(text);

        this.setStyle(String.format("-fx-background-color: linear-gradient(to top, %s, transparent)", color.toString().substring(2)));
        this.setPadding(new Insets(20, 20, 20, 20));
        this.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }
}
