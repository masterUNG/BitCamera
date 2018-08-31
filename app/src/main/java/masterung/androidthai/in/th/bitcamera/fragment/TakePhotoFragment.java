package masterung.androidthai.in.th.bitcamera.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Random;

import id.zelory.compressor.Compressor;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import masterung.androidthai.in.th.bitcamera.R;
import masterung.androidthai.in.th.bitcamera.utility.MyConstant;

public class TakePhotoFragment extends Fragment {

    private String resultQRString;
    private ImageView camaraCImageView, cameraDImageView;
    private Uri cameraCUri, cameraDUri;
    private File cameraFile, cameraCFile, cameraDFile;
    private File resizeCameraCFile;

    private String dirString, bitCFileString, bitDFileString;
    private String desinationPath;
    private boolean cameraCABoolean = false, cameraDABoolean = false;

    public static TakePhotoFragment takePhotoInstance(String resultString) {
        TakePhotoFragment takePhotoFragment = new TakePhotoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Result", resultString);
        takePhotoFragment.setArguments(bundle);
        return takePhotoFragment;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Show View
        showView();

//        Create File
        createFile();

//        Cancel Controller
        cancelController();

//        CameraC Controller
        cameraCController();

//        CameraD Controller
        cameraDController();

//        Save Controller
        saveController();


    }   // Main Method

    private void saveController() {
        Button button = getView().findViewById(R.id.btnSave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cameraCABoolean || cameraDABoolean) {
                    uploadPhotoToServer();
                } else {
                    Toast.makeText(getActivity(), "Please Take Photo", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void uploadPhotoToServer() {

        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy
                .Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);

        FTPClient ftpClient = new FTPClient();
        MyConstant myConstant = new MyConstant();
        String tag = "31AugV3";



//        For C
        if (cameraCABoolean) {

            try {

                ftpClient.connect(myConstant.getHostString(), myConstant.getPortAnInt());
                ftpClient.login(myConstant.getUserString(), myConstant.getPasswordString());
                ftpClient.setType(FTPClient.TYPE_BINARY);
                ftpClient.changeDirectory("MasterTest");
                ftpClient.upload(resizeCameraCFile, new MyCheckUploadListener());


            } catch (Exception e) {
                Log.d(tag, "e upload ==> " + e.toString());
                try {

                } catch (Exception e1) {
                    Log.d(tag, "e1 ==> " + e1.toString());
                }
            }



        }   // if

//        For D

    }   // upload

    public class MyCheckUploadListener implements FTPDataTransferListener{


        @Override
        public void started() {
            Toast.makeText(getActivity(), "Start Upload", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void transferred(int i) {
            Toast.makeText(getActivity(), "Transfer Upload", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void completed() {
            Toast.makeText(getActivity(), "Completed Upload", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void aborted() {

        }

        @Override
        public void failed() {
            Toast.makeText(getActivity(), "Failed Upload", Toast.LENGTH_SHORT).show();
        }
    }   // MyCheck Class





    public String getDirString() {
        return dirString;
    }

    private void createFile() {

        desinationPath = Environment.getExternalStorageDirectory() + "/" + dirString;

        cameraFile = new File(desinationPath);
        if (!cameraFile.exists()) {
            cameraFile.mkdirs();
        }
    }

    private void cameraDController() {
        cameraDImageView = getView().findViewById(R.id.imvCameraD);
        cameraDImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cameraDFile = new File(cameraFile, bitDFileString + "D" + ".jpg");

                cameraDUri = Uri.fromFile(cameraDFile);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraDUri);
                startActivityForResult(intent, 2);


            }   // onClick
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_OK) {

            switch (requestCode) {
                case 1:
                    cameraCABoolean = true;
                    break;
                case 2:
                    cameraDABoolean = true;
                    break;
            }

            showPhoto(requestCode);

        } else {
            Toast.makeText(getActivity(), "Please Take Photo", Toast.LENGTH_SHORT).show();
        }


    }   // onActivityResult

    private void showPhoto(int requestCode) {

        try {

            switch (requestCode) {
                case 1:
                    Bitmap rowBitmap = BitmapFactory.decodeStream(getActivity()
                            .getContentResolver().openInputStream(cameraCUri));

                    Bitmap resizeCBitmap = Bitmap.createScaledBitmap(rowBitmap, 800, 480, false);

                    camaraCImageView.setImageBitmap(resizeCBitmap);
                    break;
                case 2:
                    Bitmap rowBitmap1 = BitmapFactory.decodeStream(getActivity()
                            .getContentResolver().openInputStream(cameraDUri));

                    Bitmap resizeDBitmap = Bitmap.createScaledBitmap(rowBitmap1, 800, 480, false);

                    cameraDImageView.setImageBitmap(resizeDBitmap);
                    break;
            }


        } catch (Exception e) {
            Log.d("31AugV1", "e showPhoto ==> " + e.toString());
        }


    }   //  showPhoto


    private void cameraCController() {

        camaraCImageView = getView().findViewById(R.id.imvCameraC);
        camaraCImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cameraCFile = new File(cameraFile, bitCFileString + "C" + ".jpg");




                try {

                    resizeCameraCFile = new Compressor(getActivity())
                            .setMaxWidth(640)
                            .setMaxHeight(480)
                            .setQuality(100)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath())
                            .compressToFile(cameraCFile);

                    Log.d("31AugV2", "resizeCameraCFile Path ==> " + resizeCameraCFile.getPath());


                } catch (Exception e) {
                    Log.d("31AugV2", "e resize C ==> " + e.toString());

                    try {

                    } catch (Exception e1) {
                        Log.d("31AugV3", "e1 --> " + e1.toString());
                    }


                }

                cameraCUri = Uri.fromFile(cameraCFile);

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraCUri);
                startActivityForResult(intent, 1);

            }   // onClick
        });

    }   // cameraCController

    private void cancelController() {
        Button button = getView().findViewById(R.id.btnCancel);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            }
        });
    }

    private void showView() {
        resultQRString = getArguments().getString("Result", "Non");

//        รับค่า String ที่ Decode แล้ว
        String QRcode_Convert = "";
        String Tag = "29AugV1", DateTimeIn = "เข้า : ";
        String DateIn = "", TimeIn = "";

//        เช็คข้อมูลตัวแรกเท่ากับเครื่องหมาย | หรือไม่
        if (resultQRString.charAt(0) == '|') {

            int check_FontThai = 0;
//            วน loop เพื่อทำการ Decode Qrcode
            for (int i = 0; i < resultQRString.length(); ++i) {

                if (resultQRString.charAt(i) == '!') {
                    i++;
                    char CharDecimal = resultQRString.charAt(i);
                    int ValueASCII = (int) CharDecimal;

                    char char_decode = (char) (ValueASCII + 3536);
                    if (ValueASCII == '}') {
                        QRcode_Convert += "ะ";
                    } else {
                        QRcode_Convert += char_decode;
                    }
                } else {
                    //กรณี QRcode ที่เข้ามาเป็นเครื่องหมาย | ให้คืนค่าว่างกลับไป
                    if (resultQRString.charAt(i) == '|') {
                        QRcode_Convert += "";
                    } else {
                        char CharDecimal = resultQRString.charAt(i);
                        int ValueASCII = (int) CharDecimal;
                        //เช็คว่า เป็นตัวอักษรไทยหรือไม่
                        if (ValueASCII <= 0)
                            check_FontThai = 1;

                        if (check_FontThai == 1) {
                            QRcode_Convert += resultQRString.charAt(i);
                        } else {
                            char char_decode = (char) (158 - ValueASCII);
                            if (ValueASCII == '>') {
                                //char_decode
                                QRcode_Convert += ">";
                            } else if (ValueASCII == ' ') {
                                QRcode_Convert += " ";
                            } else {

                                QRcode_Convert += char_decode;
                            }
                        }
                    }
                }

            }
        }


        if (QRcode_Convert.split("\\$", -1).length - 1 == 10) {

            String[] data = QRcode_Convert.split("\\$");

            TextView textView = getView().findViewById(R.id.txtResult);
            //textView.setText(data[0] + ", " + data[2]);
            String styledText = "<font color='blue'>" + data[0] + "</font>" + ", " + data[2];
            textView.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);

            DateIn = data[8].substring(4, 6) + "/" + data[8].substring(2, 4);
            TimeIn = data[9].substring(0, 2) + ":" + data[9].substring(2, 4);
            DateTimeIn += DateIn + " " + TimeIn;

            TextView textView2 = getView().findViewById(R.id.txtResult2);
            textView2.setText(DateTimeIn);

            dirString = data[8] + data[10];
            Log.d("31AugV1", "dirString ==> " + dirString);

            bitCFileString = data[9] + data[10];
            Log.d("31AugV1", "bitCFileString ==> " + bitCFileString);

            bitDFileString = data[9] + data[10];

            Log.d(Tag, "Debug QRcode ---->" + QRcode_Convert);
            Log.d(Tag, "Debug[0]---->" + data[0]);
            Log.d(Tag, "Debug[1]---->" + data[1]);
            Log.d(Tag, "Debug[2]---->" + data[2]);
            Log.d(Tag, "Debug[3]---->" + data[3]);
            Log.d(Tag, "Debug[4]---->" + data[4]);
            Log.d(Tag, "Debug[5]---->" + data[5]);
            Log.d(Tag, "Debug[6]---->" + data[6]);
            Log.d(Tag, "Debug[7]---->" + data[7]);
            Log.d(Tag, "Debug[8]---->" + data[8]);
            Log.d(Tag, "Debug[9]---->" + data[9]);
            Log.d(Tag, "Debug[10]---->" + data[10]);

        } else {
            Log.d(Tag, "Debug---->" + "Error!!!");
            TextView textView = getView().findViewById(R.id.txtResult);
            textView.setText(resultQRString);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_photo, container, false);
        return view;
    }
}
