package com.ieris19.dashboard.controller;

import com.ieris19.dashboard.data.DashboardLink;
import com.ieris19.dashboard.view.HubViewModel;
import com.ieris19.lib.ui.core.UIComponent;
import com.ieris19.lib.ui.core.ViewManager;
import com.ieris19.lib.ui.mvvm.ViewController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class HubController extends ViewController {
    private HubViewModel viewModel;

    // region FXML Properties
    @FXML
    private TextField dashboardTitleField;
    @FXML
    private Button dashboardIconButton;
    @FXML
    private Label dashboardIconText;
    @FXML
    private CheckBox searchCheckBox;
    @FXML
    private TextField linkTitleField;
    @FXML
    private TextField linkAddressField;
    @FXML
    private Button addLinkButton;
    @FXML
    private ListView<DashboardLink> existingLinkList;
    @FXML
    private Button removeLinkButton;
    @FXML
    private Button submitButton;
    @FXML
    private Label statusLabel;

    // endregion

    @Override
    public void init(ViewManager viewManager, UIComponent view, Region root) {
        super.init(viewManager, view, root);
        this.viewModel = (HubViewModel) view.getViewModel();
        setBindings();
        this.existingLinkList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        reset();
    }

    @Override
    public void reset() {
        this.viewModel.reset();
    }

    @Override
    public void setBindings() {
        viewModel.bind("DASHBOARD_TITLE", dashboardTitleField.textProperty());
        viewModel.bind("DASHBOARD_ICON_TEXT", dashboardIconText.textProperty());
        viewModel.bind("DASHBOARD_SEARCH_BOX", searchCheckBox.selectedProperty());
        viewModel.bind("LINK_TITLE", linkTitleField.textProperty());
        viewModel.bind("LINK_ADDRESS", linkAddressField.textProperty());
        viewModel.bind("LINK_LIST", existingLinkList.itemsProperty());
        viewModel.bind("STATUS", statusLabel.textProperty());
    }

    public void selectIconDialog() {
        log.debug("Selecting a dialog");
        FileChooser fileSelector = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Image files", "ico", "png", "gif", "jpeg", "jpg", "svg");
        fileSelector.setSelectedExtensionFilter(filter);
        fileSelector.setTitle("Select an icon for your dashboard");
        File file = fileSelector.showOpenDialog(viewManager.getStage());
        if (file != null) {
            viewModel.setIconFile(file);
        } else {
            log.debug("No file selected");
        }
    }

    public void addLink() {
        log.debug("Adding a link");
        viewModel.addLink();
    }

    public void clearEditLink() {
        log.debug("Clearing a link");
        this.existingLinkList.getSelectionModel().clearSelection();
        viewModel.clearLinkEdit();
    }

    public void removeLink() {
        log.debug("Removing a link");
        viewModel.removeLink(getSelectedItem());
    }

    public void editNode() {
        int index = getSelectedItem();
        if (index >= 0) {
            log.debug("Editing a node");
            viewModel.editNode(index);
        }
    }

    public void submit() {
        log.debug("Submitting dashboard config");
        viewModel.submit();
    }

    private int getSelectedItem() {
        return existingLinkList.getSelectionModel().getSelectedIndex();
    }
}
