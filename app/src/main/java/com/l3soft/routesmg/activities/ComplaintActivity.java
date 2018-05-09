package com.l3soft.routesmg.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.l3soft.routesmg.MainActivity;
import com.l3soft.routesmg.R;
import com.l3soft.routesmg.api.Api;
import com.l3soft.routesmg.entity.Commentary;
import com.l3soft.routesmg.entity.CustomCommentary;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplaintActivity extends AppCompatActivity {

    public static final int GALLERY_INTENT = 10;
    private EditText title;
    private EditText description;
    private Button button;
    private String busID;
    private ImageButton imageButton;

    private StorageReference mStorage;
    private Uri uri ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        initView();

        mStorage = FirebaseStorage.getInstance().getReference();

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO save to save data
                receiveParameter();


                if(!isEmpty()){
                    postCommentary();
                    startActivity(new Intent(ComplaintActivity.this, MainActivity.class));
                   Toast.makeText(getApplicationContext(),"Ruta denunciada", Toast.LENGTH_LONG).show();
                }
            }

        });


    }

        public void loadImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Elija la aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            uri = data.getData();
            Glide.with(this.getApplicationContext()).load(uri.toString())
                    .apply(new RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.ic_default_image_complaint))
                    .into(imageButton);

            StorageReference filePath = mStorage.child("images").child(uri.getLastPathSegment());

            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    uri = taskSnapshot.getDownloadUrl();


                }
            });
        }
    }

    public void initView (){
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        button = findViewById(R.id.create);
        imageButton = findViewById(R.id.add_image);
    }

    public void receiveParameter(){
        Bundle parameter = this.getIntent().getExtras();
        if(parameter != null) {
            busID = getIntent().getExtras().getString("busID");
            System.out.println("busID: "+busID);
        }
    }

    private boolean isEmpty(){

        boolean empty = false;

         if(description.getText().toString().isEmpty() && title.getText().toString().isEmpty()){
            empty = true;
            Toast.makeText(getApplicationContext(),"Complete todos los campos", Toast.LENGTH_LONG).show();
        }else if(title.getText().toString().isEmpty()){
            empty = true;
            Toast.makeText(getApplicationContext(),"Título es requerido", Toast.LENGTH_LONG).show();
        }else if(description.getText().toString().isEmpty()) {
             empty = true;
             Toast.makeText(getApplicationContext(), "Descripción es requerida", Toast.LENGTH_LONG).show();
         }

        return empty;
    }

    private void postCommentary(){

        CustomCommentary customCommentary= new CustomCommentary();

        customCommentary.setDescription(description.getText().toString());
        customCommentary.setTitle(title.getText().toString());
        customCommentary.setUrlImage(uri.toString());
        customCommentary.setBusID(busID);
        
        try {
            Call<Commentary> call = Api.instance().postCommentary(customCommentary);
            call.enqueue(new Callback<Commentary>() {
                @Override
                public void onResponse(Call<Commentary> call, Response<Commentary> response) {
                    if(response.body() != null){
                        System.out.println("Éxito al denunciar");

                    }else{
                        System.out.println("ERROR al Denunciar: "+response.code());

                    }
                }

                @Override
                public void onFailure(Call<Commentary> call, Throwable t) {
                    System.out.println("ERROR al Denunciar2: "+t.getMessage());
                }
            });
        }catch (Exception i) {
            System.out.println("Error al: "+i);
        }
    }



}