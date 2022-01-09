import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;

public class TitledSpinner<T> extends Spinner<T> {
    private Label titleLabel;
    VBox vBox;

    public TitledSpinner(int start, int end, int step, String title) {
        super(start, end, step);
        this.titleLabel = new Label(title);
        setVBox();
    }

    private void setVBox() {
        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(titleLabel, this);
    }

    public VBox getAsVBox() {
        return vBox;
    }
}
