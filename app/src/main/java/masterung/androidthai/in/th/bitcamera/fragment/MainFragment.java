package masterung.androidthai.in.th.bitcamera.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import masterung.androidthai.in.th.bitcamera.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainFragment extends Fragment{

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Create QRscanner
        ZXingScannerView zXingScannerView = getView().findViewById(R.id.zxingMain);
        zXingScannerView.startCamera();

    }   // Main Method

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }
}   //Main Class
