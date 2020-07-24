package com.fbu.thefoodienetwork.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ActivityEditProfileBinding;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.enums.EPickType;
import com.vansuita.pickimage.listeners.IPickResult;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class EditProfileActivity extends AppCompatActivity implements IPickResult {
    private final static String TAG = "EditProfileActivity";
    private ActivityEditProfileBinding binding;
    private ParseUser currentUser = ParseUser.getCurrentUser();
    private ImageView profileImageView;
    private Button imageButton;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        profileImageView = binding.profileImageView;
        imageButton = binding.imageButton;
        saveButton = binding.saveButton;

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(EditProfileActivity.this);
            }
        });
        setContentView(view);
    }

    @Override
    public void onPickResult(PickResult imageResult) {
        if (imageResult.getError() == null) {

            profileImageView.setImageBitmap(imageResult.getBitmap());

            uploadImage(imageResult, imageResult.getPickType());

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, imageResult.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage(final PickResult imageResult, final EPickType pickType) {
        saveButton.setVisibility(View.VISIBLE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pickType == EPickType.CAMERA){
                    saveImageFromCamera(new File(imageResult.getPath()));
                    return;
                }
                if(pickType == EPickType.GALLERY){
                    saveImageFromGallery(imageResult.getBitmap());
                }
            }
        });
    }

    private void saveImageFromGallery(Bitmap photoBitmap) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        final byte[] image = stream.toByteArray();
        final ParseFile file = new ParseFile("Profile.png", image);

        file.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {

                currentUser.put("profileImage", file);

                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        //if success
                        if (e == null) {
                            Toast.makeText(EditProfileActivity.this, getString(R.string.imageSaveSuccess), Toast.LENGTH_LONG).show();
                            return;
                        }
                        //if fail
                        Toast.makeText(EditProfileActivity.this, getString(R.string.imageSaveFail), Toast.LENGTH_LONG).show();
                    }
                });
            }

        });

    }

    private void saveImageFromCamera(File photoFile) {

        final ParseFile parseProfileFile = new ParseFile(photoFile);

        parseProfileFile.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {

                currentUser.put("profileImage", parseProfileFile);

                currentUser.saveInBackground(new SaveCallback() {

                    @Override
                    public void done(ParseException e) {
                        //if success
                        if (e == null) {
                            Toast.makeText(EditProfileActivity.this, getString(R.string.imageSaveSuccess), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //If fail
                        Toast.makeText(EditProfileActivity.this, getString(R.string.imageSaveFail), Toast.LENGTH_SHORT).show();
                    }

                });

            }

        });

    }
}