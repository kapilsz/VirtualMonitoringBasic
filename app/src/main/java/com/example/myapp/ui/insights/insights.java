package com.example.myapp.ui.insights;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.icu.util.ULocale;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class insights extends Fragment {

//Screnshot to pdf
    Display mDisplay;
    String imagesUri;
    String path,path_pdf;
    Bitmap bitmap;
    int totalHeight;
    int totalWidth;
    float id_insights;
    public static final int READ_PHONE = 110;
    String file_name = "Insights_";
    File myPath;
    TextView link_insights;
    ConstraintLayout z;
    View u;

    EditText txt_from;
    Calendar from_calender;

    EditText txt_to;
    Calendar to_calendar;

    private PieChart pieChart1;
    private HorizontalBarChart mHorizontalBarChart1;

    private InsightsViewModel mViewModel;

    public static insights newInstance() {
        return new insights();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insights_fragment, container, false);

//Insights Link Hyperlink & save pdf
        id_insights = System.currentTimeMillis();

        link_insights = view.findViewById(R.id.insights_link);
        link_insights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveImageToGallery();

                //link_insights.setVisibility(View.GONE);
                //takeScreenShot();
                //link_insights.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "Insights has been saved, file://Internal Storage/ScreenShot/Insights_.pdf" , Toast.LENGTH_SHORT ).show();
                //gotoUrl("https://www.google.com");
            }
        });

//From date picker
        txt_from = view.findViewById(R.id.date_from);
        Calendar from_calender = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener from_date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                from_calender.set(Calendar.YEAR, i);
                from_calender.set(Calendar.MONTH, i1);
                from_calender.set(Calendar.DAY_OF_MONTH, i2);

                updateCalender();
            }

            private void updateCalender() {
                String formate1 = "dd MMMM yyyy";
                SimpleDateFormat sdf1 = new SimpleDateFormat(formate1);

                txt_from.setText(sdf1.format(from_calender.getTime()));
            }
        };
        txt_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), from_date, from_calender.get(Calendar.YEAR), from_calender.get(Calendar.MONTH),
                        from_calender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


//TO date Picker
        txt_to = view.findViewById(R.id.date_to);
        Calendar to_calender = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener to_date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                to_calender.set(Calendar.YEAR, i);
                to_calender.set(Calendar.MONTH, i1);
                to_calender.set(Calendar.DAY_OF_MONTH, i2);

                updateCalender();
            }

            private void updateCalender() {
                String formate = "dd MMMM yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(formate);

                txt_to.setText(sdf.format(to_calender.getTime()));

            }
        };

        txt_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getActivity(), to_date, to_calender.get(Calendar.YEAR), to_calender.get(Calendar.MONTH),
                        to_calender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



// Bar Chart

        mHorizontalBarChart1 = (HorizontalBarChart) view.findViewById(R.id.bargraph_insights);

        //Set related properties
        //  mHorizontalBarChart.setOnChartValueSelectedListener(onContextItemSelected());
        mHorizontalBarChart1.setDrawBarShadow(false);
        mHorizontalBarChart1.setDrawValueAboveBar(true);
        mHorizontalBarChart1.getDescription().setEnabled(false);
        mHorizontalBarChart1.setMaxVisibleValueCount(60);
        mHorizontalBarChart1.setPinchZoom(false);
        mHorizontalBarChart1.setDrawGridBackground(false);

        //y axis labels
        String[] values = new String[]{"Fault 1", "Fault 2", "Fault 3", "Fault 4", "other Faults"};

        //x-axis
        XAxis xl = mHorizontalBarChart1.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        //y-axis
        YAxis yl = mHorizontalBarChart1.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f);
        yl.setDrawLabels(false);

        //y-axis
        YAxis yr = mHorizontalBarChart1.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f);

        //Set up data
        setData(5, 1000);
        mHorizontalBarChart1.setFitBars(true);
        mHorizontalBarChart1.animateY(2500);

        Legend l = mHorizontalBarChart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

// pie Chart ???
        pieChart1 = view.findViewById(R.id.piechart_insights);
        setupPieChart();
        loadPieChartData();

        return view;
    }

    private void gotoUrl(String s) {
        Uri url = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, url));

    }                   //google.com

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InsightsViewModel.class);
        // TODO: Use the ViewModel
    }

/// Bar Chart Set Data
    private void setData(int count, float range) {
        float barWidth = 9f;
        float spaceForBar = 10f;

        ArrayList <BarEntry> yVals1 = new ArrayList <BarEntry>();

        yVals1.add(new BarEntry(0,97));
        yVals1.add(new BarEntry(10,111));
        yVals1.add(new BarEntry(20,296));
        yVals1.add(new BarEntry(30,630));
        yVals1.add(new BarEntry(40,751));


        /*for (int i = 0; i < count; i++) {                             ///Random data for bar graph
            float val = (float) (Math.random() * range);
            yVals1.add(new BarEntry(i * spaceForBar, val));
        }*/

        BarDataSet set1;
        if (mHorizontalBarChart1.getData() != null &&
                mHorizontalBarChart1.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mHorizontalBarChart1.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mHorizontalBarChart1.getData().notifyDataChanged();
            mHorizontalBarChart1.notifyDataSetChanged();
        }
        else {
            ArrayList<Integer> colors = new ArrayList<>();
            for (int color: ColorTemplate.MATERIAL_COLORS) {
                colors.add(color);
            }

            for (int color: ColorTemplate.VORDIPLOM_COLORS) {
                colors.add(color);
            }

            set1 = new BarDataSet(yVals1, "DataSet 1");
            set1.setColors(colors);

            /*set1.setColors(Color.parseColor("#F78B5D"),                                           //color set 2
                    Color.parseColor("#FCB232"), Color.parseColor("#FDD930"),
                    Color.parseColor("#ADD137"), Color.parseColor("#A0C25A"));*/

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(barWidth);
            mHorizontalBarChart1.setData(data);
        }
    }

//Pie Chart ???
    private void setupPieChart() {
        pieChart1.setDrawHoleEnabled(true);
        pieChart1.setUsePercentValues(true);
        pieChart1.setEntryLabelTextSize(12);
        pieChart1.setEntryLabelColor(Color.BLACK);
        pieChart1.setCenterText("Faults");
        pieChart1.setCenterTextSize(22);
        pieChart1.getDescription().setEnabled(false);

        Legend l = pieChart1.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.10f, "Other Faults"));
        entries.add(new PieEntry(0.08f, "precision degradation"));
        entries.add(new PieEntry(0.35f, "Outliers"));
        entries.add(new PieEntry(0.15f, "Drifting"));
        entries.add(new PieEntry(0.32f, "Bias"));


        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Faults Category");
        dataSet.setColors(colors);


        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart1.setData(data);
        pieChart1.invalidate();

        pieChart1.animateY(1400);
    }

// take screenshot and save to pdf
    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth){

    Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(returnedBitmap);
    Drawable bgDrawable = view.getBackground();

    if(bgDrawable != null){
        bgDrawable.draw(canvas);
    }else{
        canvas.drawColor(Color.WHITE);
    }

    view.draw(canvas);
    return returnedBitmap;
}

    private  void saveImageToGallery(){

        View u = getView().findViewById(R.id.insights_view);

        ConstraintLayout z = getActivity().findViewById(R.id.insights_view);
        totalHeight = z.getHeight();
        totalWidth = z.getWidth();

        bitmap = getBitmapFromView(u, totalHeight, totalWidth);

        File folder = new File(Environment.DIRECTORY_PICTURES + File.separator + "Insights Data");
        path = folder.getAbsolutePath();

        path = path + "/" + file_name + id_insights + ".pdf";
        String fileName = file_name + ".jpeg";
        myPath = new File(folder, fileName);

        OutputStream fos;
        try {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
                ContentResolver resolver = getActivity().getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, file_name + id_insights + ".jpg");
                contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + "Insights Data");
                Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

                fos = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                bitmap.compress(Bitmap.CompressFormat.JPEG ,100,fos);

                Objects.requireNonNull(fos);

                Toast.makeText(getActivity(),"Insights Saved", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            Toast.makeText(getActivity(),"Insights Not Saved" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
       // createPdf();
    }

    private void takeScreenShot(){

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Insights Record/");

        if(!folder.exists()){
            boolean success = folder.mkdir();
        }

        path = folder.getAbsolutePath();
        path = path + "/" + file_name + System.currentTimeMillis() + ".pdf";
        path_pdf = path;

        View u = getActivity().getWindow().getDecorView().getRootView();
        ConstraintLayout z = getActivity().findViewById(R.id.insights_view);
        totalHeight = z.getChildAt(0).getHeight();
        totalWidth = z.getChildAt(0).getWidth();

        String extr = Environment.getExternalStorageDirectory() + "/Insights Record/";
        File file = new File(extr);
        if(!file.exists())
            file.mkdir();
        String fileName = file_name + ".jpg";
        myPath = new File(extr, fileName);
        imagesUri = myPath.getPath();
        bitmap = getBitmapFromView(u, totalHeight, totalWidth);

        try{
            FileOutputStream fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        createPdf();
    }

    private void createPdf() {

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(bitmap.getWidth(), bitmap.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);

        Bitmap bitmap = Bitmap.createScaledBitmap(this.bitmap, this.bitmap.getWidth(), this.bitmap.getHeight(), true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        document.finishPage(page);
        File filePath = new File(path);
        try{

            document.writeTo(new FileOutputStream(filePath));
        }catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something Wrong: "+e.toString(), Toast.LENGTH_SHORT).show();
        }

        document.close();

      //  if (myPath.exists())
      //      myPath.delete();
    }
}