package com.example.instagramclone;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class SharePicsTab extends Fragment implements View.OnClickListener{

    private ImageView imageView;
    private EditText textImgName;
    private Button btnShareImage;
    Bitmap receivedImageBitmap;

    public SharePicsTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_share_pics_tab, container, false);

        imageView = view.findViewById(R.id.imageView);
        textImgName = view.findViewById( R.id.textImgName );
        btnShareImage = view.findViewById( R.id.btnShareImage );

        imageView.setOnClickListener(SharePicsTab.this);
        btnShareImage.setOnClickListener(SharePicsTab.this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageView :
                //Below Code checks for permission from user
                if (Build.VERSION.SDK_INT >= 23 && ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE},1000);
                }else {
                    getChosenImage();
                }
                break;
            case R.id.btnShareImage :
                if (receivedImageBitmap != null) {
                    if (textImgName.getText().toString().equals( "" )) {
                        Toasty.error( getContext(),"Error: You must specify a Description", Toasty.LENGTH_SHORT ).show();
                    }else {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedImageBitmap.compress(Bitmap.CompressFormat.PNG,100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile( "pic.png",bytes );
                        ParseObject parseObject = new ParseObject( "Photo" );
                        parseObject.put( "pic", parseFile );
                        parseObject.put( "image_description", textImgName.getText().toString() );
                        parseObject.put( "username", ParseUser.getCurrentUser().getUsername() );
                        final ProgressDialog dialog = new ProgressDialog( getContext() );
                        dialog.setMessage( "Loading..." );
                        dialog.show();

                        parseObject.saveInBackground( new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toasty.success( getContext(),"Successfuly Uploaded!!!", Toasty.LENGTH_SHORT ).show();
                                } else { Toasty.error( getContext(),"Unknown Error: " + e.getMessage(), Toasty.LENGTH_SHORT ).show(); }
                                dialog.dismiss();
                            }
                        } );
                    }
                }else { Toasty.error( getContext(),"Error: You must select a pic", Toasty.LENGTH_SHORT ).show(); }
                break;
        }
    }

    private void getChosenImage() {
        Toasty.success(getContext(),"Now we can access the images", Toasty.LENGTH_SHORT).show();

        Intent intent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
        startActivityForResult( intent, 2000 );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == 1000) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) { getChosenImage(); }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == 2000) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query( selectedImage, filePathColumn,null,null,null );
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex( filePathColumn[0] );
                    String picPath = cursor.getString( columnIndex );
                    cursor.close();
                    receivedImageBitmap = BitmapFactory.decodeFile( picPath );
                    imageView.setImageBitmap( receivedImageBitmap );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
