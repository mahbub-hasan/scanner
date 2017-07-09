package com.muktolab.imagetopdf.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.muktolab.imagetopdf.R;
import com.muktolab.imagetopdf.activity.AddNewFileActivity;
import com.muktolab.imagetopdf.adapter.PDFContentAdapter;
import com.muktolab.imagetopdf.model.PDFAdapter;
import com.muktolab.imagetopdf.model.PDFAdapterRoot;
import com.muktolab.imagetopdf.model.PDFFileInfo;
import com.muktolab.imagetopdf.model.PdfImageFile;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private RecyclerView pdfContentView;
    private PDFContentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        //recycler View init
        pdfContentView = (RecyclerView) findViewById(R.id.pdfContentView);
        pdfContentView.setLayoutManager(new GridLayoutManager(this,3));
        pdfContentView.setHasFixedSize(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddNewFileActivity.class));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapterIntoRecyclerView();
    }

    private void setAdapterIntoRecyclerView() {
        PDFAdapterRoot root = new PDFAdapterRoot();

        Realm.init(this);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PDFFileInfo> pdfFileInfos = realm.where(PDFFileInfo.class).findAll();
        if(pdfFileInfos != null){
            for (PDFFileInfo fileInfo : pdfFileInfos) {
                PDFAdapter pdfAdapter = new PDFAdapter();
                pdfAdapter.pdfFileName = fileInfo.getPdfFileName();
                pdfAdapter.pdfFileCreateDate = fileInfo.getPdfCreateDate();

                RealmResults<PdfImageFile> pdfImageInfos = realm.where(PdfImageFile.class).equalTo("pdfFileName",fileInfo.getPdfFileName()).findAll();
                for (PdfImageFile imageFiles : pdfImageInfos) {
                    PdfImageFile imageFile = new PdfImageFile();
                    imageFile.setImagePath(imageFiles.getImagePath());
                    pdfAdapter.pdfImageFileArrayList.add(imageFile);
                }
                root.pdfAdaptersRoot.add(pdfAdapter);
            }
        }

        adapter = new PDFContentAdapter(this, root);
        pdfContentView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(getApplicationContext(), AddNewFileActivity.class));
                        }
                    });
                } else {
                    Toast.makeText(this,"Permission Deney.", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
