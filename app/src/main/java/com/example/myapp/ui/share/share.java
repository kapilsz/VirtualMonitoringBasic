package com.example.myapp.ui.share;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapp.MainActivity;
import com.example.myapp.R;
import com.example.myapp.databinding.LogoutFragmentBinding;
import com.example.myapp.databinding.ShareFragmentBinding;
import com.example.myapp.ui.logout.LogoutViewModel;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;

public class share extends Fragment {

    private ShareFragmentBinding binding;

    Button btnshare;
    Display mDisplay;
    String imagesUri;
    String path,path_pdf;
    Bitmap bitmap;
    int totalHeight;
    int totalWidth;
    public static final int READ_PHONE = 110;
    String file_name = "Insights_";
    File myPath;
    TextView link_insights;
    ConstraintLayout z;
    View u;

    public static share newInstance() {
        return new share();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ShareViewModel logoutViewModel =
                new ViewModelProvider(this).get (ShareViewModel.class);
        binding = ShareFragmentBinding.inflate (inflater, container, false);
        View root = binding.getRoot ();

        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Thank You For Using And Sharing Virtual Monitoring";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));

/*
        btnshare = view.findViewById(R.id.btn_print);

        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveImageToGallery();
                //btnshare.setVisibility(View.GONE);
                //takeScreenShot();
                //btnshare.setVisibility(View.VISIBLE);
               // Toast.makeText(getActivity(), "Insights has been saved, file://Internal Storage/ScreenShot/Insights_.pdf" , Toast.LENGTH_SHORT ).show();
            }
        });*/

        return root;
    }
/*    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth){

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

        View u = getView().findViewById(R.id.ticket);

        NestedScrollView z = getActivity().findViewById(R.id.ticket);
        totalHeight = z.getChildAt(0).getHeight();
        totalWidth = z.getChildAt(0).getWidth();



        bitmap = getBitmapFromView(u, totalHeight, totalWidth);




        OutputStream fos;
        try {
            if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
                ContentResolver resolver = getActivity().getContentResolver();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "Insights_" + ".jpg");
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
    }

    private void takeScreenShot(){

        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Insight records/");

        if(!folder.exists()){
            boolean success = folder.mkdir();
        }

        path = folder.getAbsolutePath();
        path = path + "/" + file_name + System.currentTimeMillis() + ".pdf";

        View u = getActivity().findViewById(R.id.ticket);

        NestedScrollView z = getActivity().findViewById(R.id.ticket);
        totalHeight = z.getChildAt(0).getHeight();
        totalWidth = z.getChildAt(0).getWidth();

        String extr = Environment.getExternalStorageDirectory() + "/Insight records/";
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

        if (myPath.exists())
            myPath.delete();

        //openPdf(path);

    }*/

/*    private void openPdf(String path) {
        File file = new File(path);
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open FIle");
        try{
            startActivity(intent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(this, "No Apps to read PDF FIle", Toast.LENGTH_SHORT).show();
        }
    }*/


}