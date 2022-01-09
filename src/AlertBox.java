import javafx.scene.control.Alert;

public class AlertBox extends Alert {
    public AlertBox(AlertType alertType, String title, String header, String message) {
        super(alertType);
        this.setTitle(title);
        this.setHeaderText(header);
        this.setContentText(message);
    }
}
