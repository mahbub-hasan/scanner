package com.muktolab.imagetopdf.utils;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Environment;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.muktolab.imagetopdf.model.PDFFileInfo;
import com.muktolab.imagetopdf.model.PdfImageFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by mahbubhasan on 6/17/17.
 */

public class CommonTask {
    public static void makePDFFile(String pdfFileName, Activity activity){
        try {
            // make document for pdf
            Document document = new Document();
            //make file path
            File pdfFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"ImageToPdf");
            if(!pdfFile.exists() && !pdfFile.mkdirs()){
                return;
            }
            String pdf = pdfFileName+".pdf";
            FileOutputStream fileOutputStream = new FileOutputStream(new File(pdfFile.getAbsoluteFile()+"/"+pdf));
            // create instance of PDFWriter
            PdfWriter.getInstance(document,fileOutputStream);
            // open document for write
            document.open();
            // after document open get actual image from sd card using realm database;
            Realm.init(activity);
            Realm realm = Realm.getDefaultInstance();
            RealmResults<PdfImageFile> results = realm.where(PdfImageFile.class)
                    .equalTo("pdfFileName",pdfFileName)
                    .findAll();

            if(results != null){
                for (PdfImageFile images:
                        results) {
                    Image img = Image.getInstance(images.getImagePath());
                    document.setPageSize(PageSize.A4);
                    document.newPage();
                    img.setAbsolutePosition(0, 0);
                    img.scaleAbsolute(document.getPageSize().getWidth(), document.getPageSize().getHeight());
                    document.add(img);
                }
            }

            //update isPDFDone field from PDFFileInfo table
            PDFFileInfo pdfFileInfo = new PDFFileInfo();
            pdfFileInfo.setPdfFileName(pdfFileName);
            pdfFileInfo.setPdfCreateDate(new Date().getTime());
            pdfFileInfo.setPDFDone(true);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(pdfFileInfo);
            realm.commitTransaction();

            document.close();
            activity.onBackPressed();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void savePictureIntoSDCard(byte[] bytes, Camera camera, String pdfFileName, Activity activity){
        try {
            FileOutputStream fileOutputStream;

            File imagePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(imagePath, "ImageToPdf");
            if(!file.exists() && !file.mkdirs()){
                return;
            }
            File pdfFilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM+"/ImageToPdf");
            File pdfFile = new File(pdfFilePath,pdfFileName);
            if(!pdfFile.exists() && !pdfFile.mkdirs()){
                return;
            }
            String imageName = "ImageToPDF_"+new Date().getTime()+".jpg";
            String fullPathName = pdfFile.getAbsolutePath()+"/"+imageName;

            fileOutputStream = new FileOutputStream(new File(fullPathName));
            fileOutputStream.write(bytes);
            fileOutputStream.close();

            // before camera release
            savePicPathIntoDatabase(fullPathName,activity,pdfFileName);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private static void savePicPathIntoDatabase(String fullPathName, Activity activity, String pdfFileName) {
        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        PdfImageFile imageFile = realm.createObject(PdfImageFile.class);
        imageFile.setPdfFileName(pdfFileName);
        imageFile.setImagePath(fullPathName);
        realm.commitTransaction();
    }

    public static boolean saveNewPDFFileNameIntoDatabaseWith(String fileName, Activity activity) {
        try {
            Realm.init(activity);
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            PDFFileInfo pdfFileInfo = new PDFFileInfo();
            pdfFileInfo.setPdfFileName(fileName);
            pdfFileInfo.setPdfCreateDate(new Date().getTime());
            pdfFileInfo.setPDFDone(false);
            realm.copyToRealmOrUpdate(pdfFileInfo);
            realm.commitTransaction();
            return Boolean.TRUE;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return Boolean.FALSE;
    }

    public static boolean getPDFInfoUsing(String pdfFileName, Activity activity){
        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PDFFileInfo> fileInfos = realm.where(PDFFileInfo.class).equalTo("pdfFileName",pdfFileName).findAll();
        if(fileInfos != null && !fileInfos.get(0).isPDFDone()){
            RealmResults<PdfImageFile> imageCount = realm.where(PdfImageFile.class).equalTo("pdfFileName",pdfFileName).findAll();
            if(imageCount != null && imageCount.size()>0){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static void showToast(Activity activity, String message, int time){
        Toast.makeText(activity, message==null?"":message,time).show();
    }

    public static boolean isBackPressHappening(Activity activity){
        Realm.init(activity);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<PDFFileInfo> pdfFileInfos = realm.where(PDFFileInfo.class).equalTo("isPDFDone",false).findAll();
        if(pdfFileInfos != null && pdfFileInfos.size()>0){
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
