package org.example.dialogs;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.example.models.MonthlyFinance;
import org.example.models.User;

import java.math.BigDecimal;

public class ViewChartDialog extends CustomDialog {

    public ViewChartDialog(User user, ObservableList<MonthlyFinance> monthlyFinances) {
        super(user);
        setTitle("View Chart");
        setWidth(700);
        setHeight(595);

        VBox chartBox = new VBox(10);
        chartBox.setPadding(new Insets(15));

        ComboBox<String> monthSelector = new ComboBox<>();
        monthSelector.getItems().add("All Months");
        for (MonthlyFinance mf : monthlyFinances)
            monthSelector.getItems().add(mf.getMonth());
        monthSelector.setValue("All Months");

        Text title = new Text("Income vs Expense - All Months");
        title.getStyleClass().add("text-size-md");

        PieChart pieChart = new PieChart();
        pieChart.setPrefHeight(getHeight() - 120);
        pieChart.setLabelsVisible(true);

        Text noDataText = new Text("No data available for this month.");
        noDataText.getStyleClass().add("text-size-md");
        noDataText.setVisible(false);

        StackPane chartContainer = new StackPane(pieChart, noDataText);

        Runnable updateChart = () -> {
            pieChart.getData().clear();
            noDataText.setVisible(false);

            String selectedMonth = monthSelector.getValue();
            title.setText("Income vs Expense - " + selectedMonth.toUpperCase());

            BigDecimal totalIncome = BigDecimal.ZERO;
            BigDecimal totalExpense = BigDecimal.ZERO;

            if (selectedMonth.equals("All Months")) {
                for (MonthlyFinance mf : monthlyFinances) {
                    totalIncome = totalIncome.add(mf.getIncome());
                    totalExpense = totalExpense.add(mf.getExpense());
                }
            } else {
                for (MonthlyFinance mf : monthlyFinances) {
                    if (mf.getMonth().equalsIgnoreCase(selectedMonth)) {
                        totalIncome = mf.getIncome();
                        totalExpense = mf.getExpense();
                        break;
                    }
                }
            }

            // Check for empty data
            if (totalIncome.compareTo(BigDecimal.ZERO) == 0 && totalExpense.compareTo(BigDecimal.ZERO) == 0) {
                noDataText.setVisible(true);
                pieChart.setVisible(false);
                return;
            }

            pieChart.setVisible(true);
            pieChart.getData().addAll(
                    new PieChart.Data("Income", totalIncome.doubleValue()),
                    new PieChart.Data("Expense", totalExpense.doubleValue())
            );

            Platform.runLater(() -> {
                for (PieChart.Data d : pieChart.getData()) {
                    if (d.getName().equals("Income"))
                        d.getNode().setStyle("-fx-pie-color: #33ba2f;");
                    else
                        d.getNode().setStyle("-fx-pie-color: #ba2f2f;");
                }
                pieChart.layout();
            });
        };

        // Smooth updates when switching months
        monthSelector.setOnAction(e -> {
            FadeTransition fadeOut = new FadeTransition(Duration.millis(150), chartContainer);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(ev -> {
                Platform.runLater(() -> {
                    updateChart.run();
                    FadeTransition fadeIn = new FadeTransition(Duration.millis(150), chartContainer);
                    fadeIn.setFromValue(0);
                    fadeIn.setToValue(1);
                    fadeIn.play();
                });
            });
            fadeOut.play();
        });

        // Initial load
        updateChart.run();

        chartBox.getChildren().addAll(monthSelector, title, chartContainer);
        getDialogPane().setContent(chartBox);
    }
}
