package com.badminton.management.controller;

import com.badminton.management.view.MainFrame;

public class MainController {
    private MainFrame view;

    public MainController(MainFrame view) {
        this.view = view;

        this.view.addDashboardListener(e -> handleDashboard());
    }

    private void handleDashboard() {
        if (view.getDashboardController() != null) {
            view.getDashboardController().refreshData();
        }
    }
}