package com.example.instagramclone;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class SignUp extends AppCompatActivity implements View.OnClickListener{

    private Button btnSubmit, btnGetAllData;
    private EditText editName, editPunchPower, editPunchSpeed, editKickPower, editKickSpeed;
    private TextView textGetData;
    private String allKickBoxers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        btnSubmit = findViewById(R.id.btnSubmit);
        editName = findViewById(R.id.editName);
        editPunchPower = findViewById(R.id.editPunchPower);
        editPunchSpeed = findViewById(R.id.editPunchSpeed);
        editKickPower = findViewById(R.id.editKickPower);
        editKickSpeed = findViewById(R.id.editKickSpeed);
        textGetData = findViewById(R.id.textGetData);
        btnGetAllData = findViewById(R.id.btnGetAllData);

        btnSubmit.setOnClickListener(SignUp.this);
        textGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("KickBoxer");
                parseQuery.getInBackground("BnRumXAjsZ", new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject object, ParseException e) {
                        if (object != null && e == null) {
                            textGetData.setText(object.get("Name") + "" + " - " + "Punch Power" +  object.get("punch_power"));
                        }
                    }
                });
            }
        });
//        ParseInstallation.getCurrentInstallation().saveInBackground();

        btnGetAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allKickBoxers = "" ;
                ParseQuery<ParseObject> queryAll = ParseQuery.getQuery("KickBoxer");
                queryAll.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            if (objects.size()>0) {
                                for (ParseObject kickBoxer : objects) {
                                    allKickBoxers = allKickBoxers + kickBoxer.get("Name") + "\n" ;
                                }
                                Toasty.success(SignUp.this, allKickBoxers, Toast.LENGTH_SHORT).show();
                            }else {
                                Toasty.error(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        });

    }

//    public void helloWorldTap(View view) {
//        ParseObject boxer = new ParseObject("Boxer");
//        boxer.put("punch_speed",200);
//        boxer.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Toast.makeText(SignUp.this,"Boxer Object is saved automatically", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//        final ParseObject kickBoxer = new ParseObject("KickBoxer");
//        kickBoxer.put("Name", "Hupta");
//        kickBoxer.put("punch_power", 1000);
//        kickBoxer.put("punch_speed", 2000);
//        kickBoxer.put("kick_power", 3000);
//        kickBoxer.put("kick_speed", 4000);
//        kickBoxer.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    Toast.makeText(SignUp.this, kickBoxer.get("Name") + " is saved automatically", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        try {
            final ParseObject kickBoxer = new ParseObject("KickBoxer");
            kickBoxer.put("Name", editName.getText().toString());
            kickBoxer.put("punch_power", Integer.parseInt(editPunchPower.getText().toString()));
            kickBoxer.put("punch_speed", Integer.parseInt(editPunchSpeed.getText().toString()));
            kickBoxer.put("kick_power", Integer.parseInt(editKickPower.getText().toString()));
            kickBoxer.put("kick_speed", Integer.parseInt(editKickSpeed.getText().toString()));
            kickBoxer.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toasty.success(SignUp.this, kickBoxer.get("Name") + " is saved to SERVER", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            Toasty.error(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
