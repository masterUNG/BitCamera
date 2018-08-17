package masterung.androidthai.in.th.bitcamera.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.Result;

import masterung.androidthai.in.th.bitcamera.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainFragment extends Fragment implements ZXingScannerView.ResultHandler{

    private ZXingScannerView zXingScannerView;
    private String resultString;
    private String tag = "17AugV1";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        zXingScannerView = new ZXingScannerView(getActivity());
        return zXingScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {

        resultString = result.getText().toString().trim();
        Log.d(tag, "Result ==> " + resultString);
        if (!resultString.isEmpty()) {
//            Replace Fragment
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.contentFragmentMain, TakePhotoFragment.takePhotoInstance(resultString))
                    .addToBackStack(null)
                    .commit();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                zXingScannerView.resumeCameraPreview(MainFragment.this);
            }
        }, 2000);


    }
}   //Main Class