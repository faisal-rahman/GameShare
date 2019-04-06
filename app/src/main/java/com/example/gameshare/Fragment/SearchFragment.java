package com.example.gameshare.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.gameshare.Adapter.SearchAdapter;
import com.example.gameshare.Model.Post;
import com.example.gameshare.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private RecyclerView recyclerViewS;
    private SearchAdapter postAdapter;
    private List<Post> postList;

    EditText search;
    Spinner spinner;
    String text;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);




        recyclerViewS = view.findViewById(R.id.recyclerViewS);
        recyclerViewS.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerViewS.setLayoutManager(mLayoutManager);


        postList = new ArrayList<>();
        search = view.findViewById(R.id.editTextSearch);
        spinner = view.findViewById(R.id.spinLoc);
        postAdapter = new SearchAdapter(getContext(), postList);
        recyclerViewS.setAdapter(postAdapter);

        spinner.setOnItemSelectedListener(this);


        readPosts();

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //String loc = text+"_"+s;


                if(text.equals("Select Location"))
                {
                    searchPost(s.toString().toLowerCase());
                }
                else
                {
                    CharSequence loc =  text+"_"+s;
                    searchPostD(loc.toString());
                }




            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }


    private void searchPost (String s)
    {
        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("description")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Post post = snapshot.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchPostD (String s)
    {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
        Query query = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("des_loc")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren())
                {
                    Post post = snapshot.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readPosts(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(search.getText().toString().equals(""))
                {
                    postList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        Post post = snapshot.getValue(Post.class);
                        postList.add(post);
                    }
                    postAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text = parent.getItemAtPosition(position).toString();

        if(!text.equals("Select Location"))
        {
            searchPostD(text+"_");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
