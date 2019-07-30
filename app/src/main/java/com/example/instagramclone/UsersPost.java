package com.example.instagramclone;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class UsersPost extends AppCompatActivity {

    private LinearLayout linearLayoutUsersPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_users_post );

        linearLayoutUsersPost = findViewById( R.id.linearLayoutUsersPost );

        Intent receivedIntentObject = getIntent();
        final String receivedUserName = receivedIntentObject.getStringExtra( "username" );
        Toasty.success( this, receivedUserName, Toasty.LENGTH_SHORT ).show();

        setTitle( receivedUserName + "'s Posts" );

        ParseQuery<ParseObject> parseQuery = new ParseQuery<>( "Photo" );
        parseQuery.whereEqualTo( "username", receivedUserName );
        parseQuery.orderByDescending( "createdAt" );

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        parseQuery.findInBackground( new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects.size() > 0 && e == null) {
                    for (ParseObject post : objects) {
                        final TextView postDescription = new TextView(UsersPost.this);
                        postDescription.setText(post.get("image_description") + "");
                        ParseFile postPicture = (ParseFile) post.get("pic");
                        postPicture.getDataInBackground( new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                if (data != null && e == null) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    ImageView postImageView = new ImageView(UsersPost.this);
                                    LinearLayout.LayoutParams imageView_params =
                                            new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                                    imageView_params.setMargins(5, 5, 5, 5);
                                    postImageView.setLayoutParams(imageView_params);
                                    postImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                                    postImageView.setImageBitmap(bitmap);

                                    LinearLayout.LayoutParams des_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    des_params.setMargins(5, 5, 5, 15);
                                    postDescription.setLayoutParams(des_params);
                                    postDescription.setGravity( Gravity.CENTER);
                                    postDescription.setBackgroundColor( Color.RED);
                                    postDescription.setTextColor(Color.WHITE);
                                    postDescription.setTextSize(30f);

                                    linearLayoutUsersPost.addView( postImageView );
                                    linearLayoutUsersPost.addView( postDescription );
                                }
                            }
                        } );
                    }
                }else {
                    Toasty.info( UsersPost.this, receivedUserName + " doesn't have any posts", Toasty.LENGTH_SHORT ).show();
                    finish();
                }
                dialog.dismiss();
            }
        } );

    }
}
