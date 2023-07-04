module com.legendsayantan.bingsearch {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.prefs;
    requires java.desktop;


    opens com.legendsayantan.bingsearch to javafx.fxml;
    exports com.legendsayantan.bingsearch;
}