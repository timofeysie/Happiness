package com.curchod.happiness;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.curchod.happiness.util.Constants;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CreateActivity extends Activity 
{

	private static final String DEBUG_TAG = "CreateActivity";
	final Context context = this;
	
       private Uri mImageCaptureUri;
       private ImageView mImageView;
       private AlertDialog dialog;
       private static final int PICK_FROM_CAMERA = 1;
       private static final int CROP_FROM_CAMERA = 2;
       private static final int PICK_FROM_FILE = 3;
       private String new_folder;
       private String type;

       @Override
       protected void onCreate(Bundle savedInstanceState) 
       {
              super.onCreate(savedInstanceState);
              setContentView(R.layout.activity_create);
              final String method = "onCreate";
              Log.i(DEBUG_TAG, method+": build 5");
              getNewFolderFromIntent();
              captureImageInitialization();
              Button button1 = (Button)findViewById(R.id.SelectImageBtn1);
              mImageView = (ImageView)findViewById(R.id.ProfilePicIV);
              button1.setOnClickListener(new View.OnClickListener() 
              {
                     @Override
                     public void onClick(View v) 
                     {
                    	 type = Constants.POSITIVE;
                    	 Log.i(DEBUG_TAG, method+": positive");
                         dialog.show();
                     }
              });
              Button button2 = (Button)findViewById(R.id.SelectImageBtn2);
              mImageView = (ImageView)findViewById(R.id.ProfilePicIV);
              button2.setOnClickListener(new View.OnClickListener() 
              {
                     @Override
                     public void onClick(View v) 
                     {
                    	 type = Constants.NEGATIVE;
                    	 Log.i(DEBUG_TAG, method+": negative");
                         dialog.show();
                     }
              });

       }
       
       private void getNewFolderFromIntent()
       {
    	   Intent sender = getIntent();
    	   new_folder = sender.getExtras().getString("created_folder");
       }

       private void captureImageInitialization() 
       {
    	   final String method = "captureImageInitialization";
              /**
               * a selector dialog to display two image source options, from camera
               * ¡®Take from camera¡¯ and from existing files ¡®Select from gallery¡¯
               */
              final String[] items = new String[] { "Take from camera",
                           "Select from gallery" };
              ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                           android.R.layout.select_dialog_item, items);
              AlertDialog.Builder builder = new AlertDialog.Builder(this);
              builder.setTitle("Select Image");
              builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int item) 
                     { // pick from camera
                           if (item == 0) 
                           {
                                  /**
                                   * To take a photo from camera, pass intent action
                                   * ¡®MediaStore.ACTION_IMAGE_CAPTURE¡® to open the camera app.
                                   */
                                  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                  /**
                                   * Also specify the Uri to save the image on specified path
                                   * and file name. Note that this Uri variable also used by
                                   * gallery app to hold the selected image path.
                                   */
                                  String path_to_image = new_folder+"/"+type+"/"
                                		  +String.valueOf(System.currentTimeMillis())
                                          + ".jpg";
                                  mImageCaptureUri = Uri.fromFile(new File(context.getFilesDir(), 
                                		  path_to_image));
                                  intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                                mImageCaptureUri);
                                  Log.i(DEBUG_TAG, method+": path "+context.getFilesDir()+" + "
                                		  +path_to_image);
                                  try {
                                         intent.putExtra("return-data", true);
                                         startActivityForResult(intent, PICK_FROM_CAMERA);
                                  } catch (ActivityNotFoundException e) {
                                         e.printStackTrace();
                                  }
                           } else 
                           {
                                  // pick from file
                                  /**
                                   * To select an image from existing files, use
                                   * Intent.createChooser to open image chooser. Android will
                                   * automatically display a list of supported applications,
                                   * such as image gallery or file manager.
                                   */
                                  Intent intent = new Intent();
                                  intent.setType("image/*");
                                   intent.setAction(Intent.ACTION_GET_CONTENT);
                                  startActivityForResult(Intent.createChooser(intent,
                                                "Complete action using"), PICK_FROM_FILE);
                           }
                     }
              });
              dialog = builder.create();
       }
       
       /** Method to save bitmap
        * 
        * @param path
        * @param bitmap
        * @return
        */
       public boolean saveBitmapToFile(String path, Bitmap bitmap) 
       {
    	   File file = new File(path);
    	   boolean res = false;
    	   if (!file.exists()) 
    	   {
    		   try 
    		   {
    			   FileOutputStream fos = new FileOutputStream(file);
    			   res = bitmap.compress(CompressFormat.JPEG, 100, fos);
    			   fos.close();
    		   } catch (Exception e) { }
    	   }
       return res;
       }

       public class CropOptionAdapter extends ArrayAdapter<CropOption> 
       {
              private ArrayList<CropOption> mOptions;
              private LayoutInflater mInflater;
              public CropOptionAdapter(Context context, ArrayList<CropOption> options) 
              {
                     super(context, R.layout.crop_selector, options);
                     mOptions = options;
                     mInflater = LayoutInflater.from(context);
              }

              @Override
              public View getView(int position, View convertView, ViewGroup group) {
                     if (convertView == null)
                           convertView = mInflater.inflate(R.layout.crop_selector, null);
                     CropOption item = mOptions.get(position);
                     if (item != null) {
                           ((ImageView) convertView.findViewById(R.id.iv_icon))
                                         .setImageDrawable(item.icon);
                           ((TextView) convertView.findViewById(R.id.tv_name))
                                         .setText(item.title);
                           return convertView;
                     }
                     return null;
              }
       }

       public class CropOption 
       {
              public CharSequence title;
              public Drawable icon;
              public Intent appIntent;
       }

       @Override
       protected void onActivityResult(int requestCode, int resultCode, Intent data) 
       {
    	   String method = "onActivityResult";
              if (resultCode != RESULT_OK)
                     return;
              switch (requestCode) {
              case PICK_FROM_CAMERA:
                     /** After taking a picture, do the crop*/
                     doCrop();
                     break;
              case PICK_FROM_FILE:
                     /** After selecting image from files, save the selected path */
                     mImageCaptureUri = data.getData();
                     doCrop();
                     break;
              case CROP_FROM_CAMERA:
                     Bundle extras = data.getExtras();
                     /** After cropping the image, get the bitmap of the cropped image and
                      * display it on imageview.*/
                     if (extras != null) 
                     {
                           Bitmap photo = extras.getParcelable("data");
                           mImageView.setImageBitmap(photo);
                           String path_to_image = context.getFilesDir()+"/"
                        		   +new_folder+"/"+type+"/"
                         		  +String.valueOf(System.currentTimeMillis())
                                  +".jpg";
                           saveBitmapToFile(path_to_image, photo);
                           Log.i(DEBUG_TAG, method+": save file "+path_to_image);
                     }
                     File f = new File(mImageCaptureUri.getPath());
                     Log.i(DEBUG_TAG, method+": delete file "+f.getAbsolutePath());
                     /**
                      * Delete the temporary image
                      */
                     if (f.exists())
                           f.delete();
                     break;
              }
       }

       private void doCrop() 
       {
              final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
              /**
               * Open image crop app by starting an intent
               * ¡®com.android.camera.action.CROP¡®.
               */
              Intent intent = new Intent("com.android.camera.action.CROP");
              intent.setType("image/*");
              /**
               * Check if there is image cropper app installed.
               */
              List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                           intent, 0);
              int size = list.size();
              /**
               * If there is no image cropper app, display warning message
               */
              if (size == 0) 
              {

                     Toast.makeText(this, "Can not find image crop app",
                                  Toast.LENGTH_SHORT).show();
                     return;
              } else 
              {
                     /**
                      * Specify the image path, crop dimension and scale
                      */
                     intent.setData(mImageCaptureUri);
                     intent.putExtra("outputX", 200);
                     intent.putExtra("outputY", 200);
                     intent.putExtra("aspectX", 1);
                     intent.putExtra("aspectY", 1);
                     intent.putExtra("scale", true);
                     intent.putExtra("return-data", true);
                     /**
                      * There is posibility when more than one image cropper app exist,
                      * so we have to check for it first. If there is only one app, open
                      * then app.
                      */
                     if (size == 1) 
                     {
                           Intent i = new Intent(intent);
                           ResolveInfo res = list.get(0);
                           i.setComponent(new ComponentName(res.activityInfo.packageName,
                                         res.activityInfo.name));
                           startActivityForResult(i, CROP_FROM_CAMERA);
                     } else 
                     {
                           /**
                            * If there are several app exist, create a custom chooser to
                            * let user selects the app.
                            */
                           for (ResolveInfo res : list) {
                                  final CropOption co = new CropOption();

                                  co.title = getPackageManager().getApplicationLabel(
                                                res.activityInfo.applicationInfo);
                                  co.icon = getPackageManager().getApplicationIcon(
                                                res.activityInfo.applicationInfo);
                                  co.appIntent = new Intent(intent);
                                  co.appIntent
                                                .setComponent(new ComponentName(
                                                              res.activityInfo.packageName,
                                                              res.activityInfo.name));
                                  cropOptions.add(co);
                           }
                           CropOptionAdapter adapter = new CropOptionAdapter(
                                         getApplicationContext(), cropOptions);
                           AlertDialog.Builder builder = new AlertDialog.Builder(this);
                           builder.setTitle("Choose Crop App");
                           builder.setAdapter(adapter,
                                         new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int item) {
                                                       startActivityForResult(
                                                                     cropOptions.get(item).appIntent,
                                                                     CROP_FROM_CAMERA);
                                                }
                                         });
                           builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                  @Override
                                  public void onCancel(DialogInterface dialog) {

                                         if (mImageCaptureUri != null) {
                                                getContentResolver().delete(mImageCaptureUri, null,
                                                              null);
                                                mImageCaptureUri = null;
                                         }
                                  }
                           });
                           AlertDialog alert = builder.create();
                           alert.show();
                     }
              }
       }

}
