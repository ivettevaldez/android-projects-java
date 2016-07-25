package com.silvia_valdez.multibleconnectionapp.ble;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;

/**
 * Custom Handler implementation to process Message and Runnable
 * of the BLE Callbacks on multiple devices.
 * Created by silvia.valdez@hunabsys.com on 7/20/16.
 */
public class MultiBLEHandler extends Handler {

    private static final String TAG = MultiBLEHandler.class.getSimpleName();

    private ProgressDialog mProgressDialog;
    private boolean mStopCapture;

    /**
     * Public constructor of Multi BLE Handler.
     *
     * @param dialog the dialog.
     */
    public MultiBLEHandler(ProgressDialog dialog) {
        this.mProgressDialog = dialog;
        this.mStopCapture = false;
    }

    @Override
    public void handleMessage(Message msg) {
        int subject = msg.what;

        if (subject == IMessageType.STOP_CAPTURE && !mStopCapture) {
            mStopCapture = true;
        } else if (subject == IMessageType.RESUME_CAPTURE) {
            mStopCapture = false;
        }

        if (!mStopCapture) {
            switch (msg.what) {
                case IMessageType.PROGRESS:
                    mProgressDialog.setMessage((String) msg.obj);
                    if (!mProgressDialog.isShowing()) {
                        mProgressDialog.show();
                    }
                    break;

                case IMessageType.DISMISS:
                    mProgressDialog.hide();
                    break;
            }
        }
    }

}
