package com.example.biometricauthentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class FileItemActivity extends AppCompatActivity {

    ImageView imageView;
    TextView fileName, description;
    Button download_button, delete_button;
    FileController fileController = new FileController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_item);
        imageView = findViewById(R.id.item_imageView);
        fileName = findViewById(R.id.titleText);
        description = findViewById(R.id.item_description);
        download_button = findViewById(R.id.download_button);
        delete_button = findViewById(R.id.delete_button);
        Intent intent = getIntent();
        int pic = R.drawable.file_icon;
        String bundledName = intent.getStringExtra("name");
        String bundledDescr[] = intent.getStringExtra("descr").split(" ");
        imageView.setImageResource(pic);
        fileName.setText(bundledName);
        description.setText(bundledDescr[0] + "\n" + bundledDescr[1] + bundledDescr[2]);
        download_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("download");
                try {
                    fileController.Download(FileItemActivity.this, bundledName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fileController.Delete(FileItemActivity.this, bundledName);
                    fileController.Show(FileItemActivity.this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}