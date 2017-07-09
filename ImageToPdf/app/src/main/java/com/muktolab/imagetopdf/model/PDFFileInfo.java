package com.muktolab.imagetopdf.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mahbubhasan on 6/16/17.
 */

public class PDFFileInfo extends RealmObject {
    @PrimaryKey
    private String pdfFileName;
    private long pdfCreateDate;
    private boolean isPDFDone;

    public String getPdfFileName() {
        return pdfFileName;
    }

    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }

    public long getPdfCreateDate() {
        return pdfCreateDate;
    }

    public void setPdfCreateDate(long pdfCreateDate) {
        this.pdfCreateDate = pdfCreateDate;
    }

    public boolean isPDFDone() {
        return isPDFDone;
    }

    public void setPDFDone(boolean PDFDone) {
        isPDFDone = PDFDone;
    }

}
