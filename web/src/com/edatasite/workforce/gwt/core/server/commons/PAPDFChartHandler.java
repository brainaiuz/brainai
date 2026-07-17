package com.edatasite.workforce.gwt.core.server.commons;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;

public class PAPDFChartHandler implements HttpRequestHandler {
    //column strings
    String self = "Self";
    String average = "Average";
    String initiator = "Initiator";
    String client = "Client";
    String manager = "Manager";
    String peer = "Peer";

    //row strings
    String rate0 = "Rate";
    String rate1 = "Rate1";
    String rate2 = "Rate2";
    String rate3 = "Rate3";
    String rate4 = "Rate4";
    String rate5 = "Rate5";

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public void handleRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String overRate = request.getParameter("overRate");
        String params = request.getParameter("params");
        String chartType = request.getParameter("chartType");
        String lastReviewOverRate = request.getParameter("lastRate");

        String fromRateScale = request.getParameter("fromRateScale");
        String toRateScale = request.getParameter("toRateScale");
        String stepSizeRate = request.getParameter("stepSizeRate");

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Float fromScaleR = fromRateScale != null ? Float.parseFloat(fromRateScale) : 0f;
        Float toScaleR = toRateScale != null ? Float.parseFloat(toRateScale) : 7f;
        Float stepSizeScaleR = Float.parseFloat(stepSizeRate);

        float upperBoundSize = toScaleR - fromScaleR;

        if (params != null) {//bu holatda empRating,averageRating,leaderRating larni tekshirib ko'rish kerak
            String cr = request.getParameter("cr");
            String mr = request.getParameter("mr");
            String pr = request.getParameter("pr");
            String av = request.getParameter("av");
            String[] paramArray = params.split("/");
            Float empRating, leaderRating, averageRating;

            if (paramArray[0] != null && !"-".equals(paramArray[0])) {
                empRating = Float.parseFloat(paramArray[0]);
                if (empRating != 0f) {
                    dataset.addValue(empRating, rate0, self);
                }
            }
            if (av != null && !"".equals(av)) {
                averageRating = Float.parseFloat(av);
                if (averageRating != 0f) {
                    dataset.addValue(averageRating, rate1, average);
                }
            }
            if (paramArray[1] != null && !"-".equals(paramArray[1])) {
                leaderRating = Float.parseFloat(paramArray[1]);
                if (leaderRating != 0f) {
                    dataset.addValue(leaderRating, rate2, initiator);
                }
            }

            putDatasetValues(rate3, client, cr, dataset);
            putDatasetValues(rate4, manager, mr, dataset);
            putDatasetValues(rate5, peer, pr, dataset);

            JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.HORIZONTAL,
                    false, true, false);
            chart.setBackgroundPaint(Color.white);

            chart.setBorderVisible(false);
            chart.setPadding(new RectangleInsets(0, 0, 0, 0));

            CategoryPlot plot = chart.getCategoryPlot();
            plot.setBackgroundPaint(Color.white);
            plot.setAxisOffset(new RectangleInsets(0, 0, 0, 0));
            plot.setInsets(new RectangleInsets(0, 0, 0, 0));
            final LayeredBarRenderer barRenderer = new LayeredBarRenderer();

            for (int i = 0; i < dataset.getColumnCount(); i++) {
                if (dataset.getColumnKey(i) == self) {
                    barRenderer.setSeriesPaint(i, Color.decode("#539DE1"));
                }
                if (dataset.getColumnKey(i) == average) {
                    barRenderer.setSeriesPaint(i, Color.decode("#07B414"));
                }
                if (dataset.getColumnKey(i) == initiator) {
                    barRenderer.setSeriesPaint(i, Color.decode("#FF8F20"));
                }
                if (dataset.getColumnKey(i) == client) {
                    barRenderer.setSeriesPaint(i, Color.decode("#C9A834"));
                }
                if (dataset.getColumnKey(i) == manager) {
                    barRenderer.setSeriesPaint(i, Color.decode("#17648C"));
                }
                if (dataset.getColumnKey(i) == peer) {
                    barRenderer.setSeriesPaint(i, Color.decode("#970000"));
                }
            }
            barRenderer.setSeriesBarWidth(0, 1.8);
            barRenderer.setSeriesBarWidth(1, 1.6);
            barRenderer.setSeriesBarWidth(2, 1.4);
            barRenderer.setSeriesBarWidth(3, 1.31);
            barRenderer.setSeriesBarWidth(4, 1.17);
            barRenderer.setSeriesBarWidth(5, 1.08);
            barRenderer.setDrawBarOutline(false);

            barRenderer.setBaseOutlinePaint(Color.black, true);


            plot.setRenderer(barRenderer);

            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);

            plot.getRangeAxis().setUpperBound(/*7*/upperBoundSize);
            plot.getRangeAxis().setVisible(false);

            plot.getDomainAxis().setLowerMargin(0.00001);
            plot.getDomainAxis().setUpperMargin(0.00001);

            response.setContentType("image/jpeg");

            chart.setPadding(RectangleInsets.ZERO_INSETS);
            plot.setOutlineVisible(false);

            int width = 0, height = 0;
            int count = dataset.getColumnCount();
            switch (count) {
                case 2 -> {
                    barRenderer.setSeriesBarWidth(0, 2.25);
                    barRenderer.setSeriesBarWidth(1, 1.35);
                    width = 339;
                    height = 43;
                }
                case 3 -> {
                    barRenderer.setSeriesBarWidth(0, 2.3);
                    barRenderer.setSeriesBarWidth(1, 1.4);
                    barRenderer.setSeriesBarWidth(2, 1.37);
                    width = 595;
                    height = 120;
                }
                case 4 -> {
                    barRenderer.setSeriesBarWidth(0, 2.1);
                    barRenderer.setSeriesBarWidth(2, 1.3);
                    width = 800;
                    height = 221;

                }
                case 5 -> {
                    barRenderer.setSeriesBarWidth(0, 1.86);
                    barRenderer.setSeriesBarWidth(3, 1.21);
                    width = 1300;
                    height = 455;

                }
                case 6 -> {
                    width = 1800;
                    height = 750;

                }
            }

            ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, width, height);
        } else if (overRate != null) {

            // Barrenderer colours
            String defaults = "#AE0000";
            String skillChart = "#315266";
            String lastReview = "#06824E";
            String single = "#539DE1";

            Float overallRate = Float.parseFloat(overRate) - fromScaleR;

            // put dataset values this and last reviews
            if (lastReviewOverRate != null) {
                Float lastOverallRate = Float.parseFloat(lastReviewOverRate);
                dataset.addValue(overallRate, "overallRate", "this");
                dataset.addValue(lastOverallRate, "lastOverallRate", "last");
            } else {
                dataset.addValue(overallRate, "overallRate", "");
            }

            JFreeChart chart = ChartFactory.createBarChart("", "", "", dataset, PlotOrientation.HORIZONTAL,
                    false, false, false);
            chart.setBackgroundPaint(Color.white);
            chart.setBorderVisible(false);
            CategoryPlot plot = chart.getCategoryPlot();

            plot.setBackgroundPaint(Color.white);
            plot.getDomainAxis().setLowerMargin(0.0001);
            plot.getDomainAxis().setUpperMargin(0.0001);

            final LayeredBarRenderer barRenderer = new LayeredBarRenderer();

            //Setting colour
            if (chartType != null && chartType.equals("skill")) {
                barRenderer.setSeriesPaint(0, Color.decode(skillChart));
            } else if (chartType != null && chartType.equals("last")) {
                barRenderer.setSeriesPaint(0, Color.decode(lastReview));
            } else if (chartType != null && chartType.equals("single")) {
                barRenderer.setSeriesPaint(0, Color.decode(single));
            } else {
                barRenderer.setSeriesPaint(0, Color.decode(defaults));
            }

            chart.setPadding(RectangleInsets.ZERO_INSETS);
            plot.setOutlineVisible(false);

            plot.getDomainAxis().setVisible(false);
            plot.setRangeGridlinesVisible(false);
            plot.setDomainGridlinesVisible(false);
            plot.getRangeAxis().setUpperBound(/*7*/upperBoundSize);
            plot.getRangeAxis().setVisible(false);
            plot.setRenderer(barRenderer);
            response.setContentType("image/png");

            // Getting different chart

            // if chart type is skill
            if (chartType != null && chartType.equals("skill")) {
                ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 1800, 135);
            }
            //if last review overall rate available
            if (lastReviewOverRate != null) {
                barRenderer.setSeriesPaint(1, Color.decode(lastReview));
                barRenderer.setSeriesBarWidth(0, 2.2);
                barRenderer.setSeriesBarWidth(1, 1.3);
                ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 2100, 165);
            }
            // default : this review overall
            else {
                ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, 2100, 125);
            }

        }
    }

    private void putDatasetValues(String rowkey, String columnKey, String rateValues, DefaultCategoryDataset dataset) {
        float averageRate = 0;
        int j = 0;
        if (rateValues != null && !rateValues.equals("")) {
            String[] rates = rateValues.split("/");
            if (rates != null && rates.length > 0) {
                for (String rate : rates) {
                    averageRate += Integer.parseInt(rate);
                    j++;
                }
                putDatasetValues(averageRate, j, rowkey, columnKey, dataset);
            }
        }
    }

    private void putDatasetValues(float averageRate, int count, String rowkey, String columnKey, DefaultCategoryDataset dataset) {
        if (averageRate != 0f) {
            if (count > 1) {
                averageRate /= count;
                dataset.addValue(averageRate, rowkey, columnKey);
            } else {
                dataset.addValue(averageRate, rowkey, columnKey);
            }
        }
    }


}
