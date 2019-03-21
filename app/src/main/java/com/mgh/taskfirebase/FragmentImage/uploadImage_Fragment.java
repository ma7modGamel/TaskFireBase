package com.mgh.taskfirebase.FragmentImage;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mgh.taskfirebase.Model.ImageModel;
import com.mgh.taskfirebase.R;

import java.io.IOException;

import static android.support.constraint.Constraints.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class uploadImage_Fragment extends Fragment {


    public uploadImage_Fragment() {
        // Required empty public constructor
    }

    private ProgressDialog progress;
    private ImageModel imageModel;
    private Uri selectedImage;
    final int GALLERY_REQUEST = 100;
    private Button btn_uploadImage;
    private ImageView iv_imageUplade;
    private EditText et_nameImage;
    private DatabaseReference mDatabase;
    private String nameImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_image, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();


        initialViews(view);


        iv_imageUplade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");

                startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
            }
        });
        btn_uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpProgres();
                UploagData();
            }
        });
        return view;
    }

    //setUp ProgressDialog
    private void setUpProgres() {
        progress=new ProgressDialog(getContext());
        progress.setMessage("uploading  image");
        progress.setProgressStyle(ProgressDialog.BUTTON_NEGATIVE);
        progress.setProgress(0);
        progress.show();
    }


    private void initialViews(View view) {
        iv_imageUplade = view.findViewById(R.id.id_UploadImg);
        btn_uploadImage = view.findViewById(R.id.btn_upload);
        et_nameImage = view.findViewById(R.id.et_nameImage);
    }

    //get Img from gallery

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 100:
                    selectedImage = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                        iv_imageUplade.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        Log.i(TAG, "Some exception " + e);
                    }
                    break;
            }
    }

    /************************* Firebase-firestorage********************/

    private void UploagData() {
        if (iv_imageUplade.getDrawable() != null) {

            BitmapDrawable drawable = (BitmapDrawable) iv_imageUplade.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            Log.e(TAG, "addtoDataBase: " + bitmap.toString());
            if (TextUtils.isEmpty(et_nameImage.getText())) {
                Toast.makeText(getContext(), "please enter image name ..!", Toast.LENGTH_SHORT).show();
            } else {

                 nameImg= et_nameImage.getText().toString().trim();
                String key = mDatabase.child("Images").push().getKey();
                imageModel = new ImageModel(nameImg, key, selectedImage + "");
                uploadToFireStorageDB(selectedImage, key);
            }
        }else {
            Toast.makeText(getContext(), "please select image ..!", Toast.LENGTH_SHORT).show();
            progress.hide();
        }
    }


    private void uploadToFireStorageDB(Uri uriProfileImage, String key) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        if (uriProfileImage == null) {
            Toast.makeText(getContext(), "no photo selected .. ", Toast.LENGTH_SHORT).show();
            progress.hide();
        } else {
            final StorageReference profileimageRef = storageRef.child(key + ".jpg");
            UploadTask uploadTask = profileimageRef.putFile(uriProfileImage);
            Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return profileimageRef.getDownloadUrl();
                }

            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {
                        profileimageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                progress.setMessage("done upload");
                                progress.setProgress(100);
                                saveImagePathToDatabase(uri.toString());
                            }
                        });
                    }
                }
            });
        }
    }

    private void saveImagePathToDatabase(String link) {

        Toast.makeText(getContext(), " Done .. ", Toast.LENGTH_SHORT).show();
        iv_imageUplade.setImageResource(0);
        et_nameImage.setText("");
        progress.hide();
        imageModel.setUriImage(link);
        mDatabase.push().setValue(imageModel);
        mDatabase.child("names").child(nameImg).setValue(new ImageModel(nameImg,link));
    }
}
