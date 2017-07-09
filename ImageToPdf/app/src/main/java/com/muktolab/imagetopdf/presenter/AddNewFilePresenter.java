package com.muktolab.imagetopdf.presenter;

import android.app.Activity;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.muktolab.imagetopdf.R;
import com.muktolab.imagetopdf.activity.interfaces.AddNewFileUiInterface;
import com.muktolab.imagetopdf.model.PDFFileInfo;
import com.muktolab.imagetopdf.utils.CommonTask;

import io.realm.RealmResults;


/**
 * Created by mahbubhasan on 6/17/17.
 */

public class AddNewFilePresenter implements AddNewFileInterface, SurfaceHolder.Callback {
    private AddNewFileUiInterface addNewFileView;
    private Activity activity;
    private SurfaceView surfaceView;
    private FloatingActionButton capture;
    private TextInputLayout textInputLayout;
    private SurfaceHolder surfaceHolder;
    private android.hardware.Camera camera;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    public AddNewFilePresenter(AddNewFileUiInterface addNewFileUiInterface, Activity _activity){
        addNewFileView = addNewFileUiInterface;
        activity = _activity;
    }

    @Override
    public void initialization() {
        //surfaceView = (SurfaceView) activity.findViewById(R.id.surfaceViewCamera);
        capture = (FloatingActionButton) activity.findViewById(R.id.captureImage);

        //get surface holder
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //create surface holder callback
        surfaceHolder.addCallback(this);

        //capture button press
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camera.autoFocus(autoFocusCallback);
            }
        });
    }


    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean b, Camera camera) {
            camera.takePicture(null, null, pictureCallback);
        }
    };

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            savePictureIntoSDCard(bytes, camera);
        }
    };



    @Override
    public void createPDFPath() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_pdf_create,null);
        textInputLayout = view.findViewById(R.id.tilPDFName);
        builder.setView(view);
        builder.setTitle("PDF Name");
        builder.setCancelable(false);
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialogInterface, int i) {
                if(CommonTask.saveNewPDFFileNameIntoDatabaseWith(textInputLayout.getEditText().getText().toString().trim(),activity)){
                    dialogInterface.dismiss();
                }else{
                    dialogInterface.dismiss();
                    new AlertDialog.Builder(activity)
                            .setTitle("PDF Name Error")
                            .setMessage("Name is already taken. Please try again with different name.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int i) {
                                    dialog.dismiss();
                                    createPDFPath();
                                }
                            })
                            .create().show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                activity.onBackPressed();
            }
        });
        builder.create().show();
    }

    private void savePictureIntoSDCard(byte[] bytes, Camera camera) {
        try {
            CommonTask.savePictureIntoSDCard(bytes,camera,textInputLayout.getEditText().getText().toString().trim(), activity);
            //release camera for next picture
            refreshCamera();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            camera = android.hardware.Camera.open();
            // orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            Log.d("TAG", "Window Display Rotation: " + rotation);
            camera.setDisplayOrientation(ORIENTATIONS.get(rotation));
            Log.d("TAG", "Set Camera Display Orientation: " + ORIENTATIONS.get(rotation));
            //set camera width and height
            android.hardware.Camera.Parameters parameters = camera.getParameters();

            parameters.setPreviewSize(surfaceView.getWidth(), surfaceView.getHeight());
            parameters.setJpegQuality(100);
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);

            camera.setParameters(parameters);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        addNewFileView.navigateToRefreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    @Override
    public void refreshCamera() {
        try {
            if(surfaceHolder.getSurface() == null)
                return;
            camera.stopPreview();
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void makePDF() {
        CommonTask.makePDFFile(textInputLayout.getEditText().getText().toString().trim(), activity);
    }

    @Override
    public void checkCreatePDFValidation() {
        try {
            addNewFileView.makePDF(CommonTask.getPDFInfoUsing(textInputLayout.getEditText().getText().toString().trim(), activity));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void backPressYesOrNot() {
        try {
            addNewFileView.navigateToBackPress(CommonTask.isBackPressHappening(activity));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
