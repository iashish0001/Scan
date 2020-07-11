package com.example.scan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton cameraButton;
    Uri uri;
    String pictureFilePath;
    Intent intent;
    long timestamp;
    File file;
    Bitmap bitmap;
    BreakIterator btn_convert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        String[] qwe = {"ahuiosh", "jahdushn", "kjahsfuiha","ahuiosh", "jahdushn", "kjahsfuiha","ahuiosh", "jahdushn", "kjahsfuiha","ahuiosh", "jahdushn", "kjahsfuiha"};
        recyclerView.setAdapter(new RecyclerAdapter(qwe));

        cameraButton = (FloatingActionButton) findViewById(R.id.cameraButton);

        String path = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        ArrayList<File> pdfs = new ArrayList<>();
       // Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("asdfFiles", "FileName:" + files[i].getName());
            String extension = files[i].getAbsolutePath().substring(files[i].getAbsolutePath().lastIndexOf(".")+1);
            System.out.println("asdf"+extension);
            if(extension.equals("pdf")){
                pdfs.add(files[i]);
            }
        }

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.CAMERA)+
                        ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) +
                        ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.CAMERA) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE) ||
                            ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Permissions");
                        builder.setMessage("Allow to access Camera and Storage. ");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                                        Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},1
                                );
                            }
                        });
                        builder.setNegativeButton("Cancel",null);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},1
                        );
                    }
                } else{
                    Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                    try {
                         intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        timestamp = System.currentTimeMillis();
                        file = new File(getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath()+File.separator+timestamp +".jpg");
                        pictureFilePath = file.getAbsolutePath();
                        uri = Uri.fromFile(file);
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);

                        } else {
                            Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider" , file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        }
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null){
                            startActivityForResult(intent,0);


                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_1, menu);

        return super.onCreateOptionsMenu(menu);

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
      if (item.getItemId() == R.id.gallary) {
        try {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) +
                    ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_GRANTED) {
                openGallary();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
        return super.onOptionsItemSelected(item);

    }

    private void openGallary(){
        Intent gallary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallary,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            File imgFile = new File(pictureFilePath);
            if (imgFile.exists()){
                uri = Uri.fromFile(imgFile);
                timestamp = System.currentTimeMillis();
                file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + File.separator + timestamp +".jpg");
                    createPdf();

                    Intent cropView = new Intent(this,CropView.class);
                    startActivity(cropView);

            }
        }
    }

    public void createPdf() {
        Document document = new Document();

        String directoryPath = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(directoryPath + "/"+System.currentTimeMillis()+"example.pdf")); //  Change pdf's name.

            document.open();

            Image image = null;  // Change image's name and extension.
            image = Image.getInstance(pictureFilePath);
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

            document.add(image);
        } catch (BadElementException e) {
            Log.e("asdf 1", e.getLocalizedMessage());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("asdf 2", e.getLocalizedMessage());
            e.printStackTrace();
        } catch (DocumentException e) {
            Log.e("asdf 3", e.getLocalizedMessage());
            e.printStackTrace();
        }


        document.close();
    }




}
