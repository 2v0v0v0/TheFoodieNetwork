package com.fbu.thefoodienetwork.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fbu.thefoodienetwork.R;
import com.fbu.thefoodienetwork.databinding.ActivityProfileBinding;
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

//TODO change LoginActivity goMainActivity when finish
public class ProfileActivity extends AppCompatActivity implements IPickResult {
    private final static String TAG = "ProfileActivity";
    private ActivityProfileBinding binding;
    private ParseUser currentUser = ParseUser.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(ProfileActivity.this);
            }
        });
    }

    @Override
    public void onPickResult(PickResult imageResult) {
        if (imageResult.getError() == null) {

            binding.image.setImageBitmap(imageResult.getBitmap());

            uploadImage(imageResult, imageResult.getPickType());

        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, imageResult.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void uploadImage(final PickResult imageResult, final EPickType pickType) {
        binding.uploadButton.setVisibility(View.VISIBLE);
        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(ProfileActivity.this, getString(R.string.imageSaveSuccess), Toast.LENGTH_LONG).show();
                            return;
                        }
                        //if fail
                        Toast.makeText(ProfileActivity.this, getString(R.string.imageSaveFail), Toast.LENGTH_LONG).show();
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
                            Toast.makeText(ProfileActivity.this, getString(R.string.imageSaveSuccess), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //If fail
                        Toast.makeText(ProfileActivity.this, getString(R.string.imageSaveFail), Toast.LENGTH_SHORT).show();
                    }

                });

            }

        });

    }
}