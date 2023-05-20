package com.example.biometricauthentication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;

public class FileSystemActivity extends AppCompatActivity {
    //source: https://stackoverflow.com/questions/17546101/get-real-path-for-uri-android
    public static String getPathFromUri(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    ListView listView;
    Button upload_button;
    Intent fileIntent;
    FileController fileController = new FileController();
    static String mFileNames[];
    static String mDescriptions[];
    static int mImages[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_system);
        try {
            fillViews();
        } catch (Exception e) {
            e.printStackTrace();
        }
        listView = findViewById(R.id.listView);
        upload_button = findViewById(R.id.upload_button);
        MyAdapter adapter = new MyAdapter(this, mFileNames, mDescriptions, mImages);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), FileItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", mFileNames[position]);
                bundle.putString("descr", mDescriptions[position]);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setType("*/*");
                startActivityForResult(fileIntent, 10);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            String path = getPathFromUri(this, data.getData());
            String path2 [] = path.split("/");
            String path3 = "";
            for(int i =0; i<path2.length-1;i++)
            {
                path3 += "/" + path2[i];
            }
            try {
                fileController.Upload(path3, path2[path2.length-1]);
                fileController.Show(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void fillViews() {
        this.mFileNames = fillFileNameView();
        this.mDescriptions = fillFileDescriptionView();
        this.mImages= fillImageView();
    }

    private int[] fillImageView() {
        int imgList[] = new int[FileController.files.size()];
        for(int i = 0; i < FileController.files.size(); i++) {
            imgList[i] = R.drawable.file_icon;
        }
        return imgList;
    }

    private String[] fillFileNameView() {
        String nameList[] = new String[FileController.files.size()];
        for(int i = 0; i < FileController.files.size(); i++) {
            nameList[i] = FileController.files.get(i).id;
        }
        return nameList;
    }

    private String[] fillFileDescriptionView() {
        String descriptionList[] = new String[FileController.files.size()];
        for(int i = 0; i < FileController.files.size(); i++) {
            descriptionList[i] = FileController.files.get(i).date + " " + FileController.files.get(i).size;
        }
        return descriptionList;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rFileNames[];
        String rDescriptions[];
        int rImages[];
        MyAdapter (Context c, String[] fileName,  String description[], int images[]) {
            super(c, R.layout.row, R.id.fileName, fileName);
            this.context = c;
            this.rFileNames = fileName;
            this.rDescriptions = description;
            this.rImages = images;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.fileName);
            TextView myDescription = row.findViewById(R.id.fileDetails);
            images.setImageResource(rImages[position]);
            myTitle.setText(rFileNames[position]);
            myDescription.setText(rDescriptions[position]);
            return row;
        }
    }
}