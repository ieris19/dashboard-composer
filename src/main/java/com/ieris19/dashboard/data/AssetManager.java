package com.ieris19.dashboard.data;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class AssetManager {
    @Setter
    @Getter
    private File icon;
    private final File scriptsFolder;
    private final File stylesFolder;
    private final File iconsFolder;
//    private final File backgroundFolder;

    public AssetManager(File destinationFolder) {
        File assetsFolder = new File(destinationFolder, "assets");
        this.scriptsFolder = new File(assetsFolder, "scripts");
        this.stylesFolder = new File(assetsFolder, "styles");
        this.iconsFolder = new File(assetsFolder, "images/icons");
//        this.backgroundFolder = new File(assetsFolder, "image/background");
    }

    public InputStream getTemplate() {
        return this.getClass().getResourceAsStream("index.html");
    }

    public String getIconPath() {
        return "assets/images/icons/" + icon.getName();
    }

    public void exportIcon() {
        if (icon != null) {
            try {
                exportResource(iconsFolder, new FileInputStream(icon), icon.getName());
                return;
            } catch (FileNotFoundException e) {
                log.error("Icon is not null but file doesn't exist", e);
            }
        }
        exportResource(iconsFolder, this.getClass().getResourceAsStream("default-icon.png"), "default-icon.png");
    }

    public void exportStylesheet() {
        exportResource(stylesFolder, this.getClass().getResourceAsStream("dashboard.css"), "dashboard.css");
    }

    public void exportScript() {
        exportResource(scriptsFolder, this.getClass().getResourceAsStream("dashboard.js"), "dashboard.js");
    }

    private void exportResource(File dir, InputStream resource, String name) {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            Path target = Paths.get(dir.getAbsolutePath(), name);
            if (!target.toFile().exists()) {
                target.toFile().createNewFile();
            }
            Files.copy(resource, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Error exporting resource: " + name, e);
        }
    }
}
