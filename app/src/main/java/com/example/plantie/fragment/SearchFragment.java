package com.example.plantie.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.plantie.BuildConfig;
import com.example.plantie.R;
import com.example.plantie.activity.MainActivity;
import com.example.plantie.help.PackageManagerUtils;
import com.example.plantie.help.PermissionUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequest;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private Button btnSearchByName;
    private FloatingActionButton fabCamera, fabGallery;
    private ConstraintLayout clFromCloudVision;
    private ImageView imgCloud;
    private TextView txtFromCloud;

    private static String CLOUD_VISION_API_KEY = "";
    public static final String FILE_NAME = "temp.jpg";
    private static final String ANDROID_CERT_HEADER = "X-Android-Cert";
    private static final String ANDROID_PACKAGE_HEADER = "X-Android-Package";

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int GALLERY_PERMISSIONS_REQUEST = 0;
    private static final int GALLERY_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;

    String[] filterWords = {"flower", "plant", "yellow", "read", "white", "flora", "land plant", "flowering plant", 
            "daisy family", "petal", "field", "plant stem", "macro photography", "purple", "pink", "photography", 
            "close up", "blossom", "black and white", "green", "annual plant", "botany", "floristry", "biology", "leaf", 
            "branch", "tree", "shrub", "nature", "wildflower"};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        btnSearchByName = view.findViewById(R.id.btnSearch);
        fabCamera = view.findViewById(R.id.fabCamera);
        fabGallery = view.findViewById(R.id.fabGallery);
        imgCloud = view.findViewById(R.id.imgFromCamera);
        txtFromCloud = view.findViewById(R.id.txtCloudVisionResponse);

        btnSearchByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchByNameFragment searchByNameFragment = new SearchByNameFragment();
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragmentContainer, searchByNameFragment, searchByNameFragment.getTag()).commit();
            }
        });

        fabCamera.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                startCamera();
            }
        });

        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        return view;
    }
    
    private void startCamera() {
        if (PermissionUtils.requestPermission(getActivity(),
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //Uri photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".provider", getCameraFile());
            Uri photoUri = FileProvider.getUriForFile(getActivity().getApplicationContext(),
                    BuildConfig.APPLICATION_ID+".provider", getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    private File getCameraFile() {
        File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    private void openGallery() {
        if (PermissionUtils.requestPermission(getActivity(), GALLERY_PERMISSIONS_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE)){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select a photo"), GALLERY_IMAGE_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri photo = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName()+".provider", getCameraFile());
            imgCloud.setImageURI(photo);
            uploadImage(photo);
        } else if (requestCode == GALLERY_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            imgCloud.setImageURI(data.getData());
            uploadImage(data.getData());
        }
    }

    private void uploadImage(Uri photo) {
        if (photo != null) {
            try {
                Bitmap bitmap = scaleBitmapDown(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), photo), 1200);
                callCloudVision(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    private void callCloudVision(final Bitmap bitmap) {
        txtFromCloud.setText("Uploading image, please wait...");

        new AsyncTask<Object, Void, String>() {

            @Override
            protected String doInBackground(Object... objects) {
                HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                GsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                VisionRequestInitializer requestInitializer = new VisionRequestInitializer(CLOUD_VISION_API_KEY) {
                    @Override
                    protected void initializeVisionRequest(VisionRequest<?> request) throws IOException {
                        super.initializeVisionRequest(request);

                        String packageName = getActivity().getPackageName();
                        request.getRequestHeaders().set(ANDROID_PACKAGE_HEADER, packageName);

                        String sig = PackageManagerUtils.getSignature(getActivity().getPackageManager(), packageName);

                        request.getRequestHeaders().set(ANDROID_CERT_HEADER, sig);
                    }
                };
                Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                builder.setVisionRequestInitializer(requestInitializer);

                Vision vision = builder.build();

                BatchAnnotateImagesRequest batchAnnotateImagesRequest = new BatchAnnotateImagesRequest();
                batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                    Image base64EncodedImage = new Image();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();

                    base64EncodedImage.encodeContent(imageBytes);
                    annotateImageRequest.setImage(base64EncodedImage);

                    annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                        Feature labelDetection = new Feature();
                        labelDetection.setType("LABEL_DETECTION");
                        labelDetection.setMaxResults(15);
                        add(labelDetection);
                    }});
                    add(annotateImageRequest);
                }});

                Vision.Images.Annotate annotateRequest = null;
                try {
                    annotateRequest = vision.images().annotate(batchAnnotateImagesRequest);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                annotateRequest.setDisableGZipContent(true);
                Log.d(TAG, "created Cloud Vision request object, sending request");
                try {
                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }
            protected void  onPostExecute(String result) { txtFromCloud.setText(result);}
        }.execute();
    }

    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        int counter = 0;
        List<String> filterWordList = Arrays.asList(filterWords);

        String message = "It should be:\n\n";
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                String parsedWord = label.getDescription();
                //int setIndex = Collections.binarySearch(filterWordList, parsedWord);
                boolean bPass = filterWordList.contains(parsedWord);
                //Log.d("SRT", "setIndex =" + setIndex+", parsedWord = "+ parsedWord);
                if(bPass) continue;
                if(counter == 5) break;
                counter++;
                message += String.format(Locale.US, "%f%%: %s", label.getScore()*100, parsedWord);
                message += "\n";
            }
        } else {
            message += "nothing";
        }

        if (counter == 0){
            if (labels != null) {
                for (EntityAnnotation label : labels) {
                    message += String.format(Locale.US, "%f%%: %s", label.getScore()*100, label.getDescription());
                    message += "\n";
                }
            } else {
                message += "nothing";
            }
        }
        return message;
    }


}