package com.muktolab.imagetopdf.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.muktolab.imagetopdf.R;
import com.muktolab.imagetopdf.activity.interfaces.AddNewFileUiInterface;
import com.muktolab.imagetopdf.model.PDFFileInfo;
import com.muktolab.imagetopdf.model.PdfImageFile;
import com.muktolab.imagetopdf.presenter.AddNewFileInterface;
import com.muktolab.imagetopdf.presenter.AddNewFilePresenter;
import com.muktolab.imagetopdf.utils.CommonTask;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddNewFileActivity extends AppCompatActivity implements AddNewFileUiInterface {

    private AddNewFileInterface presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_file);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //presenter = new AddNewFilePresenter(this, this);
        //presenter.initialization();
        //presenter.createPDFPath();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_new_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_create_pdf){
            presenter.checkCreatePDFValidation();
        }else if(item.getItemId() == android.R.id.home){
            presenter.backPressYesOrNot();
        }
        return true;
    }

    @Override
    public void navigateToRefreshCamera() {
        presenter.refreshCamera();
    }

    @Override
    public void makePDF(boolean check) {
        if(check){
            presenter.makePDF();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Make PDF")
                    .setMessage("Please take at list one picture.")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }

    }

    @Override
    public void navigateToBackPress(boolean okSure) {
        if(okSure){
            dismissActivity();
        }else{
            new AlertDialog.Builder(this)
                    .setTitle("Cancel Event")
                    .setMessage("Discard create pdf?")
                    .setCancelable(false)
                    .setPositiveButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            CommonTask.showToast(AddNewFileActivity.this, "Press Create PDF for make a pdf file", Toast.LENGTH_SHORT);
                        }
                    })
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            dismissActivity();
                        }
                    })
                    .create()
                    .show();
        }
    }

    private void dismissActivity(){
        finish();
    }

    @Override
    public void onBackPressed() {
        presenter.backPressYesOrNot();
    }
}
