package com.ieris19.dashboard.view;

import com.ieris19.dashboard.data.Dashboard;
import com.ieris19.dashboard.data.DashboardLink;
import com.ieris19.dashboard.model.ComposerModel;
import com.ieris19.lib.ui.mvvm.Model;
import com.ieris19.lib.ui.mvvm.ViewModel;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
public class HubViewModel extends ViewModel {
    private final StringProperty dashboardTitle;
    private final StringProperty dashboardIconText;
    private final BooleanProperty dashboardSearchBox;
    private final StringProperty linkTitle;
    private final StringProperty linkAddress;
    private final BooleanProperty validateAddress;
    private final ListProperty<DashboardLink> dashboardLinks;
    private final StringProperty status;
    private ComposerModel model;
    private File dashboardIcon;
    private int currentlyEditing = -1;

    public HubViewModel() {
        this.dashboardIcon = null;
        this.dashboardTitle = new SimpleStringProperty("");
        this.dashboardIconText = new SimpleStringProperty("");
        this.linkTitle = new SimpleStringProperty("");
        this.linkAddress = new SimpleStringProperty("");
        this.validateAddress = new SimpleBooleanProperty(true);
        this.dashboardSearchBox = new SimpleBooleanProperty(true);
        this.dashboardLinks = new SimpleListProperty<>();
        this.status = new SimpleStringProperty();
    }

    @Override
    public void setModel(Model model) {
        this.model = (ComposerModel) model;
    }

    @Override
    public void bind(String propertyName, Property<?> property) {
        switch (propertyName) {
            case "DASHBOARD_TITLE" ->
                    dashboardTitle.bindBidirectional((StringProperty) property);
            case "DASHBOARD_ICON_TEXT" ->
                    dashboardIconText.bindBidirectional((StringProperty) property);
            case "LINK_TITLE" ->
                    linkTitle.bindBidirectional((StringProperty) property);
            case "LINK_ADDRESS" ->
                    linkAddress.bindBidirectional((StringProperty) property);
            case "LINK_VALIDATE" ->
                    validateAddress.bindBidirectional((BooleanProperty) property);
            case "DASHBOARD_SEARCH_BOX" ->
                    dashboardSearchBox.bindBidirectional((BooleanProperty) property);
            case "LINK_LIST" ->
                    dashboardLinks.bind((ObservableValue<ObservableList<DashboardLink>>) property);
            case "STATUS" ->
                    status.bindBidirectional((StringProperty) property);
            default ->
                    throw new IllegalArgumentException("Unknown property: " + propertyName);
        }
    }

    public void reset() {
        Dashboard current = model.getCurrentDashboard();
        this.dashboardIcon = current.getIconSource();
        this.dashboardTitle.set(current.getDashboardTitle().isBlank() ? "" : current.getDashboardTitle());
        this.dashboardIconText.set(current.getIconSource() == null ? "Icon Path" : current.getIconSource().getAbsolutePath());
        clearLinkForm();
        this.dashboardSearchBox.set(current.isSearchBox());
        this.validateAddress.set(true);
        if (current.getLinkList() != null)
            this.dashboardLinks.setAll(current.getLinkList());
        else
            this.dashboardLinks.clear();
        this.status.set("");
    }

    public void clearLinkForm() {
        this.linkTitle.set("");
        this.linkAddress.set("https://");
    }

    public void setIconFile(File file) {
        log.info("Chosen file = {}", file.getAbsolutePath());
        String errorMessage = "The chosen file is not a valid image";
        if (!validateImageFile(file.getName())) {
            status.set(errorMessage);
            return;
        }
        clearStatus(errorMessage);
        this.dashboardIcon = file;
        this.dashboardIconText.set(file.getAbsolutePath());
    }

    private boolean validateImageFile(String filename) {
        String[] validImageExtensions = {"ico", "png", "gif", "jpeg", "jpg", "svg"};
        boolean validImage = false;
        int extensionIndex = filename.lastIndexOf('.');
        String fileExtension = filename.substring(extensionIndex + 1);
        log.debug("Extension: {}", fileExtension);
        for (String s : validImageExtensions) {
            if (s.equals(fileExtension)) {
                return true;
            }
        }
        return false;
    }

    public void addLink() {
        String errorMessage = "Invalid URL provided";
        try {
            if (validateAddress.get()) {
                String logMessage = "Verifying link...";
                status.set(logMessage);
                log.debug(logMessage);
                if (DashboardLink.verifyURL(this.linkAddress.getValueSafe()))
                    throw new MalformedURLException("Invalid URL");
            }
            DashboardLink link = new DashboardLink(
                    this.linkTitle.getValueSafe(),
                    this.linkAddress.getValueSafe());
            status.set("");
            if (currentlyEditing >= 0) {
                dashboardLinks.set(currentlyEditing, link);
                currentlyEditing = -1;
                return;
            }
            this.dashboardLinks.add(link);
            clearStatus(errorMessage);
        } catch (MalformedURLException e) {
            status.set(errorMessage);
        }
    }

    public void removeLink(int index) {
        dashboardLinks.remove(index);
    }

    public void editNode(int selectedItem) {
        currentlyEditing = selectedItem;
        DashboardLink link = dashboardLinks.get(selectedItem);
        linkTitle.set(link.getTitle());
        linkAddress.set(link.getUrl());
    }

    public void submit() {
        String title = dashboardTitle.getValueSafe().isBlank() ? "Homepage" : dashboardTitle.getValueSafe();
        boolean hasSearch = dashboardSearchBox.get();
        List<DashboardLink> dashLinks = dashboardLinks.stream().toList();
        try {
            this.model.submit(title, dashboardIcon, hasSearch, dashLinks);
            String logMessage = "Dashboard Saved Successfully";
            log.info(logMessage);
            this.status.set(logMessage);
        } catch (Exception e) {
            log.error("Fatal error submitting: ", e);
            this.status.set(e.getMessage() != null ? e.getMessage() : "An unforeseen error has occurred");
        }
    }

    private void clearStatus(String errorMessage) {
        if (status.getValueSafe().equals(errorMessage)) {
            status.set("");
        }
    }
}
