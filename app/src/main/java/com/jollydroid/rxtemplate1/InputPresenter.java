package com.jollydroid.rxtemplate1;

import android.content.Context;
import android.widget.Toast;

import java.util.zip.InflaterInputStream;

/**
 * Created by User on 24.07.2016.
 */
public class InputPresenter implements IInputPresenter {

    private IMainView view;
    private Context applicationContext;

    public InputPresenter(IMainView view) {
        this.view = view;
    }


    @Override
    public void saveData() {
        String text = view.getInputText();
        String data = view.getInputData();
    }

}
