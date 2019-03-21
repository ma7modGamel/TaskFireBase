package com.mgh.taskfirebase.FragmentImage;


import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mgh.taskfirebase.Model.ImageModel;
import com.mgh.taskfirebase.R;
import com.mgh.taskfirebase.utils.AdapterRecycleImage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class showImages_Fragment extends Fragment {


    public showImages_Fragment() {
        // Required empty public constructor
    }


    private RecyclerView.LayoutManager manager;
    private RecyclerView recyclerViewImage;
    private Button btn_getImageByName, btn_getAllImage;
    private EditText et_nameImage;
    private ImageView iv_ImageFromDb;
    private DatabaseReference databaseReference;
    ArrayList<ImageModel> imageModelArrayList;
    ProgressDialog progress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_show_images, container, false);


        initialViews(view);
        setUpWidget();
        getDataFromDb();
        return view;
    }

    private void initialViews(View view) {
        recyclerViewImage = view.findViewById(R.id.recycleImage);
        btn_getAllImage = view.findViewById(R.id.btn_download_all_images);
        btn_getImageByName = view.findViewById(R.id.btn_getImageByName);
        et_nameImage = view.findViewById(R.id.et_get_nameImage);
        iv_ImageFromDb = view.findViewById(R.id.iv_getImageByName);
    }

    private void setUpWidget() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("names");
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewImage.setLayoutManager(manager);
        recyclerViewImage.setVisibility(View.INVISIBLE);
        iv_ImageFromDb.setVisibility(View.INVISIBLE);
    }
    //setUp ProgressDialog
    private void setUpProgres() {
        progress=new ProgressDialog(getContext());
        progress.setMessage("get image");
        progress.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
        progress.setProgress(0);
        progress.show();
    }

    private void getDataFromDb() {

        btn_getImageByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpProgres();
                recyclerViewImage.setVisibility(View.GONE);
                iv_ImageFromDb.setVisibility(View.VISIBLE);
                final String Name = et_nameImage.getText().toString().trim();
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        imageModelArrayList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ImageModel imageModel = snapshot.getValue(ImageModel.class);
                            if(imageModel.getNameImage().equals(Name)){
                                Picasso.with(getContext())
                                        .load(Uri.parse(imageModel.getUriImage()))
                                        .into(iv_ImageFromDb);
                                Toast.makeText(getContext(), "please woit to download image "+Name, Toast.LENGTH_SHORT).show();
                                progress.setProgress(100);
                                progress.hide();
                            }else {
                                progress.setProgress(100);
                                progress.hide();
                                Toast.makeText(getContext(), Name+" is not found .. ", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        btn_getAllImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpProgres();
                recyclerViewImage.setVisibility(View.VISIBLE);
                iv_ImageFromDb.setVisibility(View.GONE);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        imageModelArrayList = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            ImageModel imageModel = snapshot.getValue(ImageModel.class);
                            imageModelArrayList.add(imageModel);
                        }
                        recyclerViewImage.setAdapter(new AdapterRecycleImage(getContext(), imageModelArrayList));
                        Toast.makeText(getContext(), "Done .. ", Toast.LENGTH_SHORT).show();
                        progress.hide();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

    }


}
