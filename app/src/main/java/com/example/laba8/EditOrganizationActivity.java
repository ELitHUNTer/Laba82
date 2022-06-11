package com.example.laba8;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import Network.NetworkManager;
import Organizations.OrganizationType;

public class EditOrganizationActivity extends AppCompatActivity {

    private EditText ID, NAME, FULL_NAME, X, Y, ANNUAL_TURNOVER, STREET, ZIP_CODE, LOCATION_X, LOCATION_Y, LOCATION_NAME;
    private TextView OWNER;
    private Spinner ORGANIZATION_TYPE;
    private Button submitChangesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_organization);

        ID = findViewById(R.id.ID_ET_editOrganizationActivity);
        NAME = findViewById(R.id.NAME_ET_editOrganizationActivity);
        FULL_NAME = findViewById(R.id.FULL_NAME_ET_editOrganizationActivity);
        X = findViewById(R.id.X_ET_editOrganizationActivity);
        Y = findViewById(R.id.Y_ET_editOrganizationActivity);
        ANNUAL_TURNOVER = findViewById(R.id.ANNUAL_TURNOVER_ET_editOrganizationActivity);
        ORGANIZATION_TYPE = findViewById(R.id.ORGANIZATION_TYPE_ET_editOrganizationActivity);
        STREET = findViewById(R.id.STREET_ET_editOrganizationActivity);
        ZIP_CODE = findViewById(R.id.ZIP_CODE_ET_editOrganizationActivity);
        LOCATION_X = findViewById(R.id.LOCATION_X_ET_editOrganizationActivity);
        LOCATION_Y = findViewById(R.id.LOCATION_Y_ET_editOrganizationActivity);
        LOCATION_NAME = findViewById(R.id.LOCATION_NAME_ET_editOrganizationActivity);
        OWNER = findViewById(R.id.OWNER_ET_editOrganizationActivity);
        submitChangesButton = findViewById(R.id.submitButton_editOrganizationActivity);

        String[] s = { "COMMERCIAL", "TRUST", "PRIVATE_LIMITED_COMPANY", "OPEN_JOINT_STOCK_COMPANY", "NONE" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, s);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ORGANIZATION_TYPE.setAdapter(adapter);

        ID.setText(getIntent().getStringExtra("ID"));
        NAME.setText(getIntent().getStringExtra("NAME"));
        FULL_NAME.setText(getIntent().getStringExtra("FULL_NAME"));
        X.setText(getIntent().getStringExtra("X"));
        Y.setText(getIntent().getStringExtra("Y"));
        ANNUAL_TURNOVER.setText(getIntent().getStringExtra("ANNUAL_TURNOVER"));
        ORGANIZATION_TYPE.setSelection(OrganizationType.getPosition(getIntent().getStringExtra("ORGANIZATION_TYPE")));
        STREET.setText(getIntent().getStringExtra("STREET"));
        ZIP_CODE.setText(getIntent().getStringExtra("ZIP_CODE"));
        LOCATION_X.setText(getIntent().getStringExtra("LOCATION_X"));
        LOCATION_Y.setText(getIntent().getStringExtra("LOCATION_Y"));
        LOCATION_NAME.setText(getIntent().getStringExtra("LOCATION_NAME"));
        OWNER.setText(getIntent().getStringExtra("OWNER"));

        submitChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (!NetworkManager.getInstance().getLogin().equals(getIntent().getStringExtra("OWNER")))
                            return;
                        NetworkManager.getInstance().executeCommand("update " + ID.getText().toString() + " "
                                + NAME.getText().toString() + " "
                                + FULL_NAME.getText().toString() + " "
                                + X.getText().toString() + " "
                                + Y.getText().toString() + " "
                                + ANNUAL_TURNOVER.getText().toString() + " "
                                + OrganizationType.getValue((int) ORGANIZATION_TYPE.getSelectedItemId()) + " "
                                + STREET.getText().toString() + " "
                                + ZIP_CODE.getText().toString() + " "
                                + LOCATION_X.getText().toString() + " "
                                + LOCATION_Y.getText().toString() + " "
                                + LOCATION_NAME.getText().toString() + " "
                                + OWNER.getText().toString() + " ");
                        finish();
                    }
                }).start();
            }
        });

        if (!NetworkManager.getInstance().getLogin().equals(getIntent().getStringExtra("OWNER"))) {
            // Установите заголовок
            Dialog dialog = new Dialog(EditOrganizationActivity.this);
            // Передайте ссылку на разметку
            dialog.setContentView(R.layout.dialog_view);
            // Найдите элемент TextView внутри вашей разметки
            // и установите ему соответствующий текст
            TextView text = (TextView) dialog.findViewById(R.id.dialogTextView);
            text.setText("Вы не можете редактировать организацию, т.к. не являетесь ее создателем");
            dialog.show();
        }
    }
}