package com.muktolab.imagetopdf.model;

import io.realm.RealmObject;

/**
 * Created by mahbubhasan on 6/16/17.
 */

public class PdfImageFile extends RealmObject {
    private String pdfFileName;
    private String imagePath;

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
