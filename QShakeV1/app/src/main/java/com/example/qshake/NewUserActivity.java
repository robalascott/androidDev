package com.example.qshake;
/**Robert Scott
 * Lab4
 * QShake
 * NewUserActivity
 * User Data Class
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewUserActivity extends Activity  {
    private ColorDrawable colorDrawable;
    private Button exitBtn;
    private ImageView img;
    private EditText eText;
    private static final int REQUEST_CODE = 99;
    private Players p1;
    private InputStream open;
    private AssetManager manager;
    private Bitmap bitmap;
    private String imageName;
    private final String imageString = "user_profile.bmp";
    private List<Players> player = new ArrayList<Players>();
    private SharedPreference prefs;
    private Uri outURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        open = null;
        manager = getAssets();
        bitmap = null;
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        prefs = new SharedPreference();
        player.addAll(prefs.collectPlayers(getApplicationContext()));
        imageName = prefs.getImageType(getApplicationContext());
        menuInit();
        ui();
        pic(imageName);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_user, menu);
        menu.hasVisibleItems();
        return super.onCreateOptionsMenu(menu);
    }
    private void ui() {
        exitBtn = (Button) findViewById(R.id.exitBT);
        img= (ImageView) findViewById(R.id.imageView);;
        eText =(EditText) findViewById(R.id.userT);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.takephoto:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,REQUEST_CODE);
                return true;
            case R.id.userdefault:
                pic(imageString);
            return true;
        }

        return false;
    }
    /*Places Image */
    private void pic(String str){
        try {
            if(str.compareTo(imageString)==0){
                open = manager.open(imageString);
                bitmap = BitmapFactory.decodeStream(open);
                imageName=imageString;
            }else{
                bitmap = BitmapFactory.decodeFile(getOutputMediaFileType(str));
            }

            int size = returnSize();
            bitmap = getResizedBitmap(bitmap,size,size);
            img.setImageBitmap(bitmap);
        }catch (IOException e){
           ErrorMessage("Failed to load Image");
        }
    }
    /*Messages*/
    public void ErrorMessage(String error) {
        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        imageName = prefs.getImageType(getApplicationContext());
        player.addAll(prefs.collectPlayers(getApplicationContext()));
    }

    @Override
    protected void onPause() {
        super.onPause();
        prefs.setImageType(getApplicationContext(),imageName);
        prefs.savePlayers(getApplicationContext(), player);
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void newuser(View v) {
        switch (v.getId()) {
            case R.id.exitBT:
                String x = eText.getText().toString();
                if(x.isEmpty()){
                    ErrorMessage("Need a Name!");
                }else{
                    Players p1 = new Players();
                    p1.setName(x);
                    p1.setPicID(imageName);
                    p1.setTurn(0);
                    player.add(p1);
                    prefs.savePlayers(getApplicationContext(), player);
                    imageName = imageString;
                    finish();
                }
                break;
            default:break;
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int size = returnSize();
        InputStream stream = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            try {
                stream = getContentResolver().openInputStream(data.getData());
                Bitmap bitmap1 = BitmapFactory.decodeStream(stream);
                bitmap1 =getResizedBitmap(bitmap1,size,size);
                if(getScreenOrientation(this)==90){
                    bitmap1 = rotateImage(bitmap1, -90);
                }
                storeImage(bitmap1);
                /*Place Image in shared prefs and set Image*/
                prefs.setImageType(getApplicationContext(),imageName);
                img.setImageBitmap(bitmap1);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    public void menuInit() {
        colorDrawable = new ColorDrawable(Color.parseColor("#267058"));
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(colorDrawable);
        actionBar.setTitle("Input New User");
        getActionBar().setDisplayShowHomeEnabled(false);
    }
    /*http://stackoverflow.com/questions/14066038/why-image-captured-using-camera-intent-gets-rotated-on-some-devices-in-android*/
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap retVal;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        retVal = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        return retVal;
    }
    //http://stackoverflow.com/questions/10380989/how-do-i-get-the-current-orientation-activityinfo-screen-orientation-of-an-a
    public static int getScreenOrientation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int orientation = activity.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_270) {
                return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            } else {
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
            }
        }
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            if (rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_90) {
                return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            } else {
                return ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
            }
        }
        return ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
    }
    /*Resizes and Scales Bitmap image to screen Size*/
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;

    }
    /* http://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels*/
    /*Set Bitmap image to screen Size*/
    public int returnSize() {
        int measuredWidth = 0;
        int measuredHeight = 0;
        WindowManager w = getWindowManager();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            w.getDefaultDisplay().getSize(size);
            measuredWidth = size.x;
            measuredHeight = size.y;
        } else {
            Display d = w.getDefaultDisplay();
            measuredWidth = d.getWidth();
            measuredHeight = d.getHeight();
        }
        int bestSize = Math.max(measuredHeight, measuredWidth);
        if (bestSize <= 1280 &&  bestSize > 960 ) {
            return 300;
        }else if (bestSize <= 960 &&  bestSize > 640 ) {
            return 200;
        } else if (bestSize <= 640 && bestSize >= 470 ){
            return 150 ;
        }else if (bestSize <= 470 &&bestSize >= 320){
            return 120;
        }
        return 112;
    }
    /*Saving method for Image FILE*/
    private void storeImage(Bitmap image) {
        String TAG = "AddUser";
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }
    /*Returns File */
    private File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");
        /* Create the storage directory if it does not exist*/
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        /*Create a media file name*/
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName="MI_"+ timeStamp +".jpg";
        imageName= mImageName;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }
    /*String for path*/
    private String getOutputMediaFileType(String str){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String temp = Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files"+File.separator + str;
        return temp;
    }
}
