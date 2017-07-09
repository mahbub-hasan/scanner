package com.muktolab.imagetopdf.presenter;

/**
 * Created by mahbubhasan on 6/17/17.
 */

public interface AddNewFileInterface {
    void initialization();

    void createPDFPath();

    void refreshCamera();

    void makePDF();

    void checkCreatePDFValidation();

    void backPressYesOrNot();
}
