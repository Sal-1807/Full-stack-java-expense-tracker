module expense.tracker.client {
    requires javafx.controls;
    requires com.google.gson;
    requires javafx.graphics;
    requires jdk.jshell;
//    requires expense.tracker.client;
//    requires expense.tracker.client;

    opens org.example.models to javafx.base;

    exports org.example;

}