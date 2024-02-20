package com.ieris19.dashboard;

import com.ieris19.dashboard.controller.HubController;
import com.ieris19.dashboard.model.ComposerModel;
import com.ieris19.dashboard.view.HubViewModel;
import com.ieris19.lib.ui.core.IerisFX;
import com.ieris19.lib.ui.core.UIComponent;
import com.ieris19.lib.ui.core.ViewMap;
import com.ieris19.lib.ui.mvvm.Model;
import lombok.Getter;

import java.io.File;

public class Composer {
    public final static File DESTINATION_FOLDER = new File(System.getProperty("user.home") + "/Documents/Dashboard");
    private static Model composerModel;

    public static void main(String... args) {
        IerisFX.setStart(Composer::start);

        IerisFX.launch(IerisFX.class);
    }

    public static void start(String... args) {
        for (View view : View.values()) {
            ViewMap.add(view.name().toLowerCase(), view.getUiComponent());
        }
    }

    public static Model getComposerModel() {
        if (composerModel == null)
            composerModel = new ComposerModel();
        return composerModel;
    }

    @Getter
    public enum View {
        HUB(new UIComponent
                ("hub.fxml", getComposerModel(), new HubViewModel(), new HubController()));

        private final UIComponent uiComponent;

        View(UIComponent view) {
            this.uiComponent = view;
        }
    }
}
