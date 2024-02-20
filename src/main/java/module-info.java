module dashboard.composer {
    requires static lombok;
    requires org.slf4j;

    requires java.base;
    requires java.desktop;
    requires java.rmi;

    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;

    requires ierislib.common;
    requires ierislib.files.config;
    requires ierislib.ui;

    opens com.ieris19.dashboard.controller;
    opens com.ieris19.dashboard.model;
    opens com.ieris19.dashboard.data;
}