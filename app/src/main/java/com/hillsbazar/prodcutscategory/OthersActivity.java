package com.hillsbazar.prodcutscategory;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hillsbazar.Cart;
import com.hillsbazar.IndividualProduct;
import com.hillsbazar.NotificationActivity;
import com.hillsbazar.R;
import com.hillsbazar.models.GenericProductModel;
import com.hillsbazar.networksync.CheckInternetConnection;
import com.squareup.picasso.Picasso;

public class OthersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private LottieAnimationView tv_no_item;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mDatabaseReference = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();

        //Initializing our Recyclerview
        mRecyclerView = findViewById(R.id.my_recycler_view);
        tv_no_item = findViewById(R.id.tv_no_cards);


        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
        final FirebaseRecyclerAdapter<GenericProductModel, FruitsActivity.MovieViewHolder> adapter = new FirebaseRecyclerAdapter<GenericProductModel, FruitsActivity.MovieViewHolder>(
                GenericProductModel.class,
                R.layout.cards_cardview_layout,
                FruitsActivity.MovieViewHolder.class,
                //referencing the node where we want the database to store the data from our Object
                mDatabaseReference.child("Products").child("OthersActivity").getRef()
        ) {
            @Override
            protected void populateViewHolder(final FruitsActivity.MovieViewHolder viewHolder, final GenericProductModel model, final int position) {
                if (tv_no_item.getVisibility() == View.VISIBLE) {
                    tv_no_item.setVisibility(View.GONE);
                }
                viewHolder.cardname.setText(model.getCardname());
                viewHolder.cardprice.setText("$ " + model.getCardprice());
                Picasso.with(OthersActivity.this).load(model.getCardimage()).into(viewHolder.cardimage);

                viewHolder.mView.setOnClickListener(v -> {
                    Intent intent = new Intent(OthersActivity.this, IndividualProduct.class);
                    intent.putExtra("product", getItem(position));
                    startActivity(intent);
                });
            }
        };


        mRecyclerView.setAdapter(adapter);

    }

    public void viewCart(View view) {
        startActivity(new Intent(OthersActivity.this, Cart.class));
        finish();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        TextView cardname;
        ImageView cardimage;
        TextView cardprice;

        View mView;

        public MovieViewHolder(View v) {
            super(v);
            mView = v;
            cardname = v.findViewById(R.id.cardcategory);
            cardimage = v.findViewById(R.id.cardimage);
            cardprice = v.findViewById(R.id.cardprice);
        }
    }

    public void Notifications(View view) {
        startActivity(new Intent(OthersActivity.this, NotificationActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //check Internet Connection
        new CheckInternetConnection(this).checkConnection();
    }
}