package com.example.projectgabi.charts;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.example.projectgabi.classes.Budget;
import com.example.projectgabi.classes.BudgetItem;
import com.example.projectgabi.classes.Category;
import com.example.projectgabi.classes.Transaction;
import com.example.projectgabi.controllers.BudgetController;
import com.example.projectgabi.controllers.TransactionController;
import com.example.projectgabi.interfaces.BudgetCallBack;
import com.example.projectgabi.interfaces.TransactionCallback;
import com.example.projectgabi.utils.DateConverter;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class BudgetCombinedChart {

    private Context context;
    private final static int QUARTERS = 4; // number of quarters in wich the charts are divided

    public void generateChart(Budget budget, ArrayList<Category> categories, CombinedChart combinedChart) {

        List<BarEntry> barEntries = this.makeBarData(budget, categories);

        BarDataSet barDataSet = this.makeBarDataSet(barEntries, "Bar combinedData");

        this.makeCombinedGraph(budget, barDataSet, categories , combinedChart);



    }

    public List<BarEntry> makeBarData(Budget budget, ArrayList<Category> categories) {

        List<BarEntry> barEntries = new ArrayList<>();

        Date startDate = budget.getStartDate();
        Date endDate = budget.getEndDate();
        // interval type [i1, i2)

        List<Date> quarters = this.divideInterval(startDate, endDate, QUARTERS);

        Log.d("BudgetCC" , quarters.size() + quarters.toString());

        for (int i = 0 ; i < quarters.size() -1 ; i++){
            barEntries.add(new BarEntry(i,
                    this.getCategoryValueByDate(categories, quarters.get(i), quarters.get(i+1))));
        }

        return barEntries ;
    }



    private float getCategoryValueByDate(ArrayList<Category> categories, Date interval1, Date interval2) {

        // for each cateogry, go throught every Transaction list and if the Transaction date is between interval1 and interval2 add its value to quarterSum
        float quarterSum = 0;
        for(Category category : categories){
            for(Transaction transaction : category.getTransactions()){
                if(transaction.getDate().after(interval1 )
                        && transaction.getDate().before(interval2)
                        || transaction.getDate().equals(interval1)){
                    quarterSum += transaction.getValue();
                }
            }
        }
        return quarterSum;
    }

    public BarDataSet makeBarDataSet(List<BarEntry> barEntries, String label) {
        BarDataSet barDataSet = new BarDataSet(barEntries,label);
        barDataSet.setColor(Color.BLUE);
        barDataSet.setValueTextSize(12f);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        int[] barColors = new int[]{
                Color.parseColor("#FF5722"),
                Color.parseColor("#FFC107"),
                Color.parseColor("#4CAF50"),
                Color.parseColor("#ADD8E6") };
        barDataSet.setColors(barColors);
        return barDataSet;

    }

    public String getPreviousMonth(Budget budget, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -1);
        Date previousMonth = budget.getStartDate();
        previousMonth.setMonth(previousMonth.getMonth() - 1);

        return DateConverter.fromDate(calendar.getTime());
    }
    public void makeCombinedGraph(Budget budget, BarDataSet barDataSet, ArrayList<Category> categories,
                                  CombinedChart combinedChart) {

        List<Entry > lineEntries = new ArrayList<>();
        String startDate ;
        String endDate ;
        endDate = DateConverter.fromDate(budget.getStartDate());
        startDate = this.getPreviousMonth(budget, budget.getStartDate());


        BudgetController budgetController = new BudgetController();
        budgetController.getBudgetBundle(context, startDate, endDate);

        this.setUpCombinedChart(lineEntries, barDataSet, combinedChart, budget);

        budgetController.setBudgetCallBack(new BudgetCallBack() {
            @Override
            public void onReceivedBudget(Budget previousBudget, ArrayList<BudgetItem> budgetItems, ArrayList<Category> categories) {


                   Log.d("BudgetCC ", "budget: " +  previousBudget.toString());

                   TransactionController transactionController = new TransactionController();
                   transactionController.getTransactionsFromDB(context);

                   transactionController.setTransactionCallback(new TransactionCallback() {
                       @Override
                       public void onReceivedTransaction(HashMap<String, List<Transaction>> transactionHashMap, HashMap<String, List<Transaction>> transactionMapByParentCategory, ArrayList<PieEntry> categories) {
                       }

                       @Override
                       public void getTransactions(ArrayList<Transaction> transactions) {

                           previousBudget.setBudgetItems(budgetItems);

                           // add transactions to categories
                           for (Category category : categories) {
                               for (Transaction transaction : transactions) {
                                   if (category.getCategoryName().equals(transaction.getCategory())) {
                                       category.getTransactions().add(transaction);
                                       category.setCategoryAmount(category.getCategoryAmount() + transaction.getValue());
                                   }
                               }
                           }

                           // set-up line chart values
                           List<Entry>lineEntriesPreviousBudget =  BudgetCombinedChart.this.setLineChartValues(previousBudget, categories);

                           BudgetCombinedChart.this.setUpCombinedChart(lineEntriesPreviousBudget, barDataSet, combinedChart, budget);
                       }
                   });

                }

        });

        Log.d("BudgetCC", "lineEntries: " + lineEntries.toString());




        combinedChart.invalidate();
    }

    private void setUpCombinedChart(List<Entry> lineEntries, BarDataSet barDataSet, CombinedChart combinedChart, Budget budget) {

        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Expenses by time");
        lineDataSet.setColor(Color.RED);
        lineDataSet.setValueTextSize(12f);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineDataSet.setColor(Color.parseColor("#2196F3"));

        CombinedData combinedData = BudgetCombinedChart.this.makeCombinedData(barDataSet, lineDataSet);

        BudgetCombinedChart.this.initializeCombinedDataChart(combinedChart, combinedData, budget);

        combinedChart.invalidate();
        combinedData.notifyDataChanged();

    }

    private List<Entry> setLineChartValues(Budget previousBudget, ArrayList<Category> categories) {

        List<Entry> lineEntries = new ArrayList<>();

        List<Date> quarters = this.divideInterval(previousBudget.getStartDate(), previousBudget.getEndDate(), QUARTERS);

        for (int i = 0 ; i < quarters.size() -1 ; i++){
            lineEntries.add(new Entry(i,
                    this.getCategoryValueByDate(categories, quarters.get(i), quarters.get(i+1))));
        }

        return  lineEntries ;
    }

    public CombinedData makeCombinedData(BarDataSet barDataSet, LineDataSet lineEntries) {

        CombinedData data = new CombinedData();
        data.setData(new BarData(barDataSet));
        data.setData(new LineData(lineEntries));

        return data;
    }

    public void initializeCombinedDataChart(CombinedChart combinedChart, CombinedData combinedData, Budget budget ) {

        // Set up X-axis labels
        //  String[] labels = new String[]{"Q1", "Q2", "Q3", "Q4"};
        String []labels = new String[4] ;
        List<Date> intervals =  this.divideInterval(budget.getStartDate(), budget.getEndDate(), QUARTERS);

        for (int i = 0 ; i<  intervals.size() -1; i++){

            Log.d("BudgetCC" , DateConverter.fromDate(intervals.get(i)));
            labels[i] = "Q" + (1+i) + ": "+ "\n"+ intervals.get(i).getDate() + " - " + intervals.get(i).getMonth();
        }

        // Set up X-axis
        XAxis xAxis = combinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        // Set up Y-axis
        YAxis leftAxis = combinedChart.getAxisLeft();
        leftAxis.setTextSize(12f);
        leftAxis.setTextColor(Color.BLACK);

        YAxis rightAxis = combinedChart.getAxisRight();
        rightAxis.setEnabled(false);

        // Set up chart
        combinedChart.setData(combinedData);
        combinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE});
        combinedChart.setPinchZoom(true);
        combinedChart.setDoubleTapToZoomEnabled(false);
        combinedChart.setDrawGridBackground(false);

        // Set up chart description and legend
        Description description = new Description();
//        description.setText("Combined Chart");
//        combinedChart.setDescription(description);


        combinedChart.setDrawGridBackground(true);
        combinedChart.setGridBackgroundColor(Color.WHITE);
        combinedChart.setBorderColor(Color.LTGRAY);
        combinedChart.setBorderWidth(1f);


        combinedChart.setExtraBottomOffset(16f);
        combinedChart.setExtraTopOffset(16f);
        combinedChart.setExtraRightOffset(32f);
        combinedChart.setExtraLeftOffset(32f);

    }

    public static List<Date> divideInterval(Date startDate, Date endDate, int numIntervals) {
        List<Date> intervals = new ArrayList<>();

        // Calculate the interval duration
        long intervalDuration = (endDate.getTime() - startDate.getTime()) / numIntervals;

        // Create a calendar instance and set it to the start date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        // Divide the interval into equal parts
        for (int i = 0; i < numIntervals; i++) {
            intervals.add(calendar.getTime());

            // Increment the calendar by the interval duration
            calendar.setTimeInMillis(calendar.getTimeInMillis() + intervalDuration);
        }

        // Add the end date as the last interval
        intervals.add(endDate);

        return intervals;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


}
