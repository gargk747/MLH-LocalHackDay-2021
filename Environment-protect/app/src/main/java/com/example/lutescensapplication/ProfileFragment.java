package com.example.lutescensapplication;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;


public class ProfileFragment extends Fragment {

    //Firebase variables needed to get user info and display them on user profile
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private Button btn_open, btn_completed;

    //Storage
    StorageReference storageReference;

    //The path where the images of users' profile and cover picture will be stored
    String storagePath = "Users_Profile_Cover_Images/";

    //The views from the .xml file
    ImageView avatarProfile, coverProfile;
    TextView nicknameProfile, emailProfile;
    FloatingActionButton fab;

    //Progress dialog
    ProgressDialog pd;

    //User permissions for camera and storage access
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    //Arrays of permission to be requested
    String cameraPermissions[];
    String storagePermissions[];

    //URI of choose image
    Uri image_uri;

    //Needed to check the profile or cover image
    String profileOrCoverPhoto;

    public ProfileFragment() {
        //Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        //Init Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getInstance().getReference("Users");

        //Firebase storage reference
        storageReference = getInstance().getReference();

        //Init permissions arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Init views
        avatarProfile = view.findViewById(R.id.avatarProfile);
        coverProfile = view.findViewById(R.id.coverProfile);
        emailProfile = view.findViewById(R.id.emailProfile);
        nicknameProfile = view.findViewById(R.id.nicknameProfile);
        btn_open = view.findViewById(R.id.btn_open);
        btn_completed = view.findViewById(R.id.btn_completed);
        fab = view.findViewById(R.id.fab);

        //Init progress dialog
        pd = new ProgressDialog(getActivity());

        /*The app needs to retrieve info of currently signed is user and it can get it using user's e-mail or uid. Here is done with e-mail.
            By using orderByChild query, I will show the detail from a node whose key (named email) has value equal to the current signed in email.
            It will search all the nodes and where the key matches, it will get its detail.*/
        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Checking until the required data is found and took
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String nickname = "" + ds.child("nickname").getValue();
                    String email = "" + ds.child("email").getValue();
                    String image = "" + ds.child("image").getValue();
                    String cover = "" + ds.child("cover").getValue();

                    //Set the data
                    nicknameProfile.setText(nickname);
                    emailProfile.setText(email);

                    try {
                        //If the app has received the profile image of the user, then it will set it.
                        Picasso.get().load(image).into(avatarProfile);
                    } catch (Exception e) {
                        //If there is any exception on trying to get the image, then the app will set a default one.
                        Picasso.get().load(R.drawable.ic_profile_image).into(avatarProfile);
                    }

                    try {
                        //If the app has received the cover image of the user, then it will set it.
                        Picasso.get().load(cover).into(coverProfile);
                    } catch (Exception e) {
                        //If there is any exception on trying to get the image, then the app will set a default one.
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Click listener for Floating TaskMenu.java Button (FAB) in profile activity.
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), OpenTasks.class));
            }
        });
        btn_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CompletedTasks.class));
            }
        });


        return view;
    }

    //This method checks if permissions for storage is enabled (true) or not enabled (false)
    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    //Request storage permission at runtime
    private void requestStoragePermission() {
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);
    }

    //This method checks if permissions for camera is enabled (true) or not enabled (false)
    private boolean checkCameraPermission() {
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result1 && result2;
    }

    //Request camera permission at runtime.
    private void requestCameraPermission() {
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //This dialog contains the options: edit profile picture, edit cover picture, edit nickname.
    private void showEditProfileDialog() {

        //What options the dialog will show.
        String options[] = {"Edit profile picture", "Edit cover picture", "Edit username"};

        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Set title
        builder.setTitle("What would you like to change?");

        //Set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //To handle the clicks on the dialog items.
                //In order: profile, cover, nickname. The the Dialog methods are called.
                if (which == 0) {
                    pd.setMessage("Updating your profile picture..");
                    profileOrCoverPhoto = "image"; //Changing profile picture. Make sure to assign same value.
                    showImagePictureDialog();
                } else if (which == 1) {
                    pd.setMessage("Updating your cover picture..");
                    profileOrCoverPhoto = "cover"; //Changing cover picture. Make sure to assign same value.
                    showImagePictureDialog();
                } else if (which == 2) {
                    pd.setMessage("Updating your nickname..");
                    showNicknameUpdateDialog("nickname");
                }
            }
        });

        //Create and show the dialog.
        builder.create().show();

    }

    /*This dialog is showed when updating the nickname. The method's parameter "key" is set instead
     of nickname because if other features are needed in profile, the method can be developed
     for them as well.*/
    private void showNicknameUpdateDialog(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update " + key);

        //Set the layout of dialog
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10, 10, 10, 10);

        //Add edit text field
        final EditText editText = new EditText(getActivity());
        editText.setHint("Enter " + key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //Add a button in the dialog to update
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Input text from edit text
                String value = editText.getText().toString().trim();

                //Check if the user has entered something or not
                if (!TextUtils.isEmpty(value)) {
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Updated. Dismiss progress.
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Failed. Dismiss progress, then get and show an error message.
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getActivity(), "Please enter your" + key, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Add a button in the dialog to cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        //Create and show the dialog
        builder.create().show();


    }

    //This dialog shows the two options (camera and gallery) to pick the pictures from.
    private void showImagePictureDialog() {

        //What options the dialog will show
        String options[] = {"Camera", "Gallery"};

        //Alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Set title
        builder.setTitle("Pick image from..");

        //Set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //To handle the clicks on the dialog items.
                //In order: profile picture, cover picture.
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });

        //Create and show the dialog
        builder.create().show();

    }

    //This method is called when the user press Allow or Deny during the permission request.
    //Here the application handles the permissions cases: allowed and denied.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                // Picking image from camera. It checks first if the camera and storage permission is allowed or not.
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        //If permission is enabled
                        pickFromCamera();
                    } else {
                        //If permission is not enabled
                        Toast.makeText(getActivity(), "Enable camera and storage permission to user app features", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                // Picking image from gallery. It checks first if storage permission is allowed or not.
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        //If permission is enabled
                        pickFromGallery();
                    } else {
                        //If permission is not enabled
                        Toast.makeText(getActivity(), "Enable storage permission to user app features", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    //This method will be called after picking images from Camera or Gallery.
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                //If the image is picked from gallery, get the uri of the image and upload profile cover photo.
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                //If the image is picked from camera, get the uri of the image
                uploadProfileCoverPhoto(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //Instead of creating two different functions for profile and cover photo, here I created only one
    //which will handle both the actions.
    private void uploadProfileCoverPhoto(Uri uri) {

        //Show progress
        pd.show();

        //Path and name of images that will be stored in Firebase.
        String filePathAndName = storagePath + "" + profileOrCoverPhoto + "_" + user.getUid();

        StorageReference storageReference2 = storageReference.child(filePathAndName);
        storageReference2.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //Once image is uploaded to storage, it gets the uri and store it in user's database.
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful()) ;
                        Uri downloadUri = uriTask.getResult();

                        //Check if image is uploaded, if is not and if url is received
                        if (uriTask.isSuccessful()) {
                            //If image is uploaded
                            //Add - Update url in user's database
                            HashMap<String, Object> results = new HashMap<>();

                            /*The first parameter is profileOrCoverPhoto, that has value "image" or "cover",
                            which are keys in user's database where the image's url will be stored.
                            The second parameter contains the url of the image stored in Firebase and this url
                            will be saved as value against the keys "image" or "cover.
                             */
                            results.put(profileOrCoverPhoto, downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            //Url in user database is added successfully
                                            //Dismiss the progress bar
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Image updated!", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //Error while adding url in user database
                                            //Dismiss the progress bar
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), "Error while updating image.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            //Error
                            pd.dismiss();
                            Toast.makeText(getActivity(), "An error has occurred.", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        //If there was an error, the application will show an error message with a dismiss progress dialog.
                        pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //This method coordinates the user action of picking a picture from the camera.
    private void pickFromCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        //Insert image uri
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //Intent to start the device camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    //This method coordinates the user action of picking a picture from the device gallery.
    private void pickFromGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

    }
}
