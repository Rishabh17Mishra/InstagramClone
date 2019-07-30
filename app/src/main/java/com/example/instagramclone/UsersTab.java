package com.example.instagramclone;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 */
public class UsersTab extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;

    public UsersTab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_users_tab, container, false);

        listView = view.findViewById( R.id.listView );
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(getContext(),android.R.layout.simple_list_item_1, arrayList);

        listView.setOnItemClickListener( UsersTab.this );
        listView.setOnItemLongClickListener( UsersTab.this );

        final TextView textLoading = view.findViewById( R.id.textLoading );

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereNotEqualTo( "username", ParseUser.getCurrentUser().getUsername() );
        parseQuery.findInBackground( new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    if (users.size() > 0) {
                        for (ParseUser user : users) {
                            arrayList.add( user.getUsername() );
                        }
                        listView.setAdapter( arrayAdapter );
                        textLoading.animate().alpha( 0 ).setDuration( 1500 );
                        listView.setVisibility( View.VISIBLE );
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent( getContext(), UsersPost.class );
        intent.putExtra( "username", arrayList.get(position));
        startActivity( intent );
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username", arrayList.get(position));
        parseQuery.getFirstInBackground( new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null && e == null) {
                    final PrettyDialog prettyDialog = new PrettyDialog( getContext() );
                    prettyDialog.setTitle( user.getUsername() + "'s Info" ).setMessage( user.get("ProfileBio" ) + "\n" +
                            user.get( "ProfileProfession" ) + "\n" + user.get( "ProfileHobbies" ) + "\n"
                    + user.get( "ProfileSport" ) ).setIcon( R.drawable.person ).addButton( "OK",
                            R.color.pdlg_color_white, R.color.pdlg_color_green, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    prettyDialog.dismiss();
                                }
                            } ).show();
                }
            }
        } );

        return true;
    }
}
