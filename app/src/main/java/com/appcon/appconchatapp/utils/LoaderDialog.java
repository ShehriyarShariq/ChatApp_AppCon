package com.appcon.appconchatapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.appcon.appconchatapp.R;

public class LoaderDialog {

    /*
        MULTI-PURPOSE MESSAGE DIALOG BOX
    */

    Activity activity;
    Dialog dialog;
    String type; // Dialog type

    public LoaderDialog(Activity activity, String type){
        this.activity = activity;
        this.type = type;
    }

    public void showDialog(){
        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.loader_layout);

        TextView label = dialog.findViewById(R.id.label);
        TextView cancelButton = dialog.findViewById(R.id.cancel_loading);
        switch (type){
            case "InfoLoader": // Data Loader Dialog
                label.setText("Loading...");
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        activity.finish();
                    }
                });
                break;
            case "Process": // Data Processing Dialog
                label.setText("Processing...");
                cancelButton.setVisibility(View.GONE);
                break;
            case "Send": // Data Sending Dialog
                label.setText("Sending...");
                cancelButton.setVisibility(View.GONE);
                break;
        }

        dialog.show();
    }

    public void hideDialog(){
        dialog.dismiss();
    }

}
