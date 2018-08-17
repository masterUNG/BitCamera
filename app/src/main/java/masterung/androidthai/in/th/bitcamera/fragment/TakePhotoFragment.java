package masterung.androidthai.in.th.bitcamera.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import masterung.androidthai.in.th.bitcamera.R;

public class TakePhotoFragment extends Fragment{

    private String resultQRString;

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

//        Cancel Controller
        cancelController();


    }   // Main Method

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
        TextView textView = getView().findViewById(R.id.txtResult);
        textView.setText(resultQRString);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_photo, container, false);
        return view;
    }
}
