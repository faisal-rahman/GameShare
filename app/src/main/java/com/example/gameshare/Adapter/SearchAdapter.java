package com.example.gameshare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gameshare.Model.Post;
import com.example.gameshare.Model.User;
import com.example.gameshare.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    public Context mContext;
    public List<Post> mPosts;
    public List<Post> mPostsFull;

    private FirebaseUser firebaseUser;

    public SearchAdapter(Context mContext, List<Post> mPosts)
    {
        this.mContext=mContext;
        this.mPosts= mPosts;
        mPostsFull= new ArrayList<>(mPosts);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_products, viewGroup,false);
        SearchAdapter.ViewHolder holder = new SearchAdapter.ViewHolder(view);


        return holder;
        //return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final Post post = mPosts.get(i);

        Glide.with(mContext).load(post.getPostimage()).into(viewHolder.post_image);

        if (post.getDescription().equals("")){
            viewHolder.description.setVisibility(View.GONE);
        } else {
            viewHolder.description.setVisibility(View.VISIBLE);
            viewHolder.description.setText(post.getDescription());
        }
        viewHolder.price.setVisibility(View.VISIBLE);
        viewHolder.price.setText("TK "+ post.getPrice());

        viewHolder.location.setVisibility(View.VISIBLE);
        viewHolder.location.setText(post.getLocation());

        publisherInfo(viewHolder.image_profile,viewHolder.username,viewHolder.publisher,post.getPublisher());
    }



    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    /*@Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Post> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(mPostsFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Post item : mPostsFull)
                {
                    if(item.getDescription().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mPosts.clear();
            mPosts.addAll((List)results.values);
            notifyDataSetChanged();
        }
    } ;
*/

    public class ViewHolder extends RecyclerView.ViewHolder{



        public ImageView image_profile, post_image, like, comment, more;
        public TextView username, search, publisher, description, location, price;

        public ViewHolder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            post_image = itemView.findViewById(R.id.imageView);
            price = itemView.findViewById(R.id.textViewPrice);
           description = itemView.findViewById(R.id.textViewTitle);
            location = itemView.findViewById(R.id.textViewShortDesc);
            search = itemView.findViewById(R.id.editTextSearch);

        }
    }

    private void publisherInfo(final ImageView image_profile, final TextView username, final TextView publisher, final String userid){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                //Glide.with(mContext).load(user.getImageurl()).into(image_profile);
                //username.setText(user.getUsername());
                //publisher.setText(user.getUsername());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
