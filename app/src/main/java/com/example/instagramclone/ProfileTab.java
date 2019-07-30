package com.example.instagramclone;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileTab extends Fragment {

    private EditText editProfileName, editProfileBio, editProfileProfession, editProfileHobbies, editProfileSport;
    private Button btnUpdateInfo;

    public ProfileTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        editProfileName = view.findViewById( R.id.editProfileName );
        editProfileBio = view.findViewById( R.id.editProfileBio );
        editProfileProfession = view.findViewById( R.id.editProfileProfession );
        editProfileSport = view.findViewById( R.id.editProfileSport );
        editProfileHobbies = view.findViewById( R.id.editProfileHobbies );
        btnUpdateInfo = view.findViewById( R.id.btnUpdateInfo );

        final ParseUser parseUser = ParseUser.getCurrentUser();

        if (parseUser.get( "ProfileName" ) == null) {
            editProfileName.setText("");
        }else { editProfileName.setText( parseUser.get( "ProfileName" ).toString());}
        if (parseUser.get( "ProfileBio" ) == null) {
            editProfileBio.setText("");
        }else { editProfileBio.setText( parseUser.get( "ProfileBio" ).toString());}
        if (parseUser.get( "ProfileProfession" ) == null) {
            editProfileProfession.setText("");
        }else { editProfileProfession.setText( parseUser.get( "ProfileProfession" ).toString());}
        if (parseUser.get( "ProfileSport" ) == null) {
            editProfileSport.setText("");
        }else { editProfileSport.setText( parseUser.get( "ProfileSport" ).toString());}
        if (parseUser.get( "ProfileHobbies" ) == null) {
            editProfileHobbies.setText("");
        }else { editProfileHobbies.setText( parseUser.get( "ProfileHobbies" ).toString());}


        btnUpdateInfo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseUser.put( "ProfileName", editProfileName.getText().toString() );
                parseUser.put( "ProfileBio", editProfileBio.getText().toString() );
                parseUser.put( "ProfileProfession", editProfileProfession.getText().toString() );
                parseUser.put( "ProfileSport", editProfileSport.getText().toString());
                parseUser.put( "ProfileHobbies", editProfileHobbies.getText().toString());

                parseUser.saveInBackground( new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) { Toasty.success( getContext(),"Info Updated", Toasty.LENGTH_SHORT ).show(); }
                        else { Toasty.error( getContext(),e.getMessage(), Toasty.LENGTH_SHORT ).show();}
                    }
                } );
            }
        } );

        return view;

    }

}
