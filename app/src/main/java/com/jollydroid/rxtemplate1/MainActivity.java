package com.jollydroid.rxtemplate1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements IMainView {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textdate)
    TextView textdate;
    @BindView(R.id.inputtext)
    EditText inputtext;
    @BindView(R.id.inputdate)
    EditText inputdate;
    @BindView(R.id.savebutton)
    Button savebutton;
    @BindView(R.id.listbutton)
    Button listbutton;
    private IInputPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        presenter = new InputPresenter(this);

    }

    @OnClick(R.id.savebutton)
    public void onClickSave(View view) {
        presenter.saveData();
        Log.d("something","Нажата кнопка сохранить");
    }

    @OnClick(R.id.listbutton)
    public void onClickList(View view) {
        startActivity(new Intent(MainActivity.this,ListActivity.class));
    }

    @Override
    public String getInputText() {
        return inputtext.getText().toString();
    }

    @Override
    public String getInputData() {
        return inputdate.getText().toString();
    }
}
