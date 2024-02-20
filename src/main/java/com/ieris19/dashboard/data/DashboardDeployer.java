package com.ieris19.dashboard.data;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import static com.ieris19.dashboard.Composer.DESTINATION_FOLDER;
@Slf4j
public class DashboardDeployer {
    private final File imagesFolder;
    private final File htmlFile;
    private final File scriptFile;
    private final File stylesheetFile;
    private final Dashboard content;

    public DashboardDeployer(Dashboard content) {
        this.content = content;
        if (!initializeFile(DESTINATION_FOLDER, true))
            throw new IllegalStateException("Destination Folder cannot be created");
        this.htmlFile = new File(DESTINATION_FOLDER, "index.html");
        this.scriptFile = new File(DESTINATION_FOLDER, "assets/scripts/dashboard.js");
        this.stylesheetFile = new File(DESTINATION_FOLDER, "assets/styles/dashboard.css");
        this.imagesFolder = new File(DESTINATION_FOLDER, "assets/images/");
        boolean successfulInitialization = true;
        if (initializeFile(htmlFile, false)) {
            log.info("Initialized HTML file");
        } else
            successfulInitialization = false;
        if (initializeFile(scriptFile, false)) {
            log.info("Initialized Script file");
        } else
            successfulInitialization = false;
        if (initializeFile(stylesheetFile, false)) {
            log.info("Initialized Stylesheet");
        } else
            successfulInitialization = false;
        if (initializeFile(imagesFolder, true)) {
            log.info("Initialized Images Folder");
        } else
            successfulInitialization = false;
        if (!successfulInitialization)
            throw new IllegalStateException("One or more required files cannot be verified");
    }

    private boolean initializeFile(File file, boolean isDirectory) {
        log.debug("Initializing file {}", file);
        try {
            if (!file.exists()) {
                log.trace("File does not exist.");
                return isDirectory ? file.mkdirs() : createFileHelper(file);
            }
            log.trace("Verifying state: file/folder");
            return isDirectory == file.isDirectory();
        } catch (IOException e) {
            log.error("Error verifying file", e);
            return false;
        }
    }

    private boolean createFileHelper(File file) throws IOException {
        log.trace("Creating file");
        File parent = file.toPath().getParent().toFile();
        parent.mkdirs();
        return file.createNewFile();
    }

    public void deploy() {
        createAssets();
        createContent();
        createMetadata();
    }

    public void createAssets() {
        try (InputStream css = this.getClass().getResourceAsStream("dashboard.css");
             InputStream js = this.getClass().getResourceAsStream("dashboard.js")) {
            if (css == null || js == null) {
                throw new IOException("Cannot find necessary static resources");
            }
            Files.copy(css, stylesheetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(js, scriptFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            File icon = this.content.getIconSource();
            if (icon != null) {
                Files.copy(icon.toPath(), new File(imagesFolder, icon.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                try (InputStream webIcon = this.getClass().getResourceAsStream(content.getIconName())) {
                    if (webIcon == null) {
                        throw new IOException("Cannot find necessary static resources");
                    }
                    Files.copy(webIcon, new File(imagesFolder, content.getIconName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Cannot create asset files", e);
        }
    }

    public void createContent() {
        try {
            Files.writeString(htmlFile.toPath(), this.content.getDocument());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot write dashboard document");
        }
    }

    public void createMetadata() {
        Document metadata = this.content.toXML();
        File metadataFile = new File(DESTINATION_FOLDER, "metadata.xml");
        try (FileOutputStream out = new FileOutputStream(metadataFile, false)) {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            t.transform(new DOMSource(metadata), new StreamResult(out));
        } catch (TransformerConfigurationException e) {
            log.error("Error creating metadata", e);
        } catch (IOException | TransformerException e) {
            throw new IllegalStateException("Cannot write metadata");
        }
    }

    public static Dashboard readMetadata() {
        File metadataFile = new File(DESTINATION_FOLDER, "metadata.xml");
        if (!metadataFile.exists()) {
            return new Dashboard("", null, true, null);
        }
            return Dashboard.fromXML(metadataFile);
    }
}
