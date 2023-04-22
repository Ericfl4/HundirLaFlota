module com.example.hundirlaflota {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.logging;

    opens com.example.hundirlaflota to javafx.fxml;
    exports com.example.hundirlaflota;
    exports com.example.hundirlaflota.sockets;
    opens com.example.hundirlaflota.sockets to javafx.fxml;
}