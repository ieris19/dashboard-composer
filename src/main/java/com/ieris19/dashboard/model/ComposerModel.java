package com.ieris19.dashboard.model;

import com.ieris19.dashboard.data.Dashboard;
import com.ieris19.dashboard.data.DashboardDeployer;
import com.ieris19.dashboard.data.DashboardLink;
import com.ieris19.lib.ui.mvvm.Model;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

@Slf4j
public class ComposerModel implements Model {

    public ComposerModel() {
    }

    public void submit(String title, File icon, boolean hasSearch, List<DashboardLink> links) {
        Dashboard dashboard = new Dashboard(title, icon, hasSearch, links);
        DashboardDeployer deployer = new DashboardDeployer(dashboard);
        deployer.deploy();
    }

    public Dashboard getCurrentDashboard() {
        return DashboardDeployer.readMetadata();
    }
}
