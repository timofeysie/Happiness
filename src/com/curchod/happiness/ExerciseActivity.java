package com.curchod.happiness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.curchod.happiness.util.Constants;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * When the correct button is pressed,
 * hideNegateivePictureAndWait();
 * hideAllViews(true);
 * waitAndIncrementRound();
 * 
 * @author user
 *
 */
public class ExerciseActivity extends Activity 
{

    final Context context = this;
    private static final String DEBUG_TAG = "ExerciseActivity";
    String[] positive_images;
    String[] negative_images;
    private int current_round;

    private Button button1;
    private Button button2;
    
    ImageView image1;
    ImageView image2;
    
    private int positive;
    private int negative;
    
    private Editor shared_editor;
    private SharedPreferences shared_preferences;
    private String current_folder;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        String method = "onCreate";
        Log.i(DEBUG_TAG, method+": build 40g");
        setup();
        getCurrentFolder();
        getImages();
        chooseImageType();
        loadImages();
        setupButtons();
    }
    
    /**
     * Load the current folder from the shared preferences.
     */
    private void getCurrentFolder()
    {
        String method = "getCurrentFolder";
        this.shared_preferences = context.getSharedPreferences(Constants.PREFERENCES, Activity.MODE_PRIVATE);
        this.shared_editor = shared_preferences.edit();
        current_folder = shared_preferences.getString(Constants.FOLDER, "");
        Log.i(DEBUG_TAG, method+": current folder: "+current_folder);
        if (current_folder == null || current_folder == "")
        {
            current_folder = Constants.DEFAULT;
            Log.i(DEBUG_TAG, method+": first time? use and set default: "+current_folder);
            shared_preferences.edit().putString(Constants.FOLDER, Constants.DEFAULT).apply();
        }
        if (!current_folder.equals(Constants.DEFAULT))
        {
            // user created directory
            String current_path = context.getFilesDir().getAbsolutePath();
            current_folder = current_path+"/"+current_folder;
            Log.i(DEBUG_TAG, method+": add path to current folder: "+current_folder);
        } else
        {
            Log.i(DEBUG_TAG, method+": use default folder");
        }
    }
    
    /**
     * Hide the negative image and button and positive botton.
     * Slide the positive image into the center.
     * Then hide everything and wait for a few seconds.
     * Then start the next round.
     */
    private void nextRound()
    {
        hideNegateivePictureAndSlide();
        hideAllViews(true);
        waitAndIncrementRound();
    }
    
    /**
     * Hide the negative button and image and start the slide animation.
     * Down if the positive image is image1, up if not.
     */
    private void hideNegateivePictureAndSlide()
    {
        if (positive == 1)
        {
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            image2.setVisibility(View.GONE);
        } else
        {
            button2.setVisibility(View.GONE);
            button1.setVisibility(View.GONE);
            image1.setVisibility(View.GONE);
        }
        startSlide(); 
        Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() 
        { 
             public void run() 
             { 
                 waitAndWithBlankScrren();
             } 
        }, 2500); 
    }
    
    private void waitAndWithBlankScrren()
    {
        hideAllViews(true);
        Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() 
        { 
             public void run() 
             { 
                 waitAndIncrementRound();
             } 
        }, 800); 
    }
    
    private void waitAndIncrementRound()
    {
        Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() 
        { 
             public void run() 
             { 
                 hideAllViews(true);
                 current_round++;
                 if (current_round>=positive_images.length)
                 {
                     roundOver();
                 } else
                 {
                     chooseImageType();
                     loadImages();
                     setupButtons();
                     hideAllViews(false);
                     button1.setEnabled(true);
                     button2.setEnabled(true);
                 }
             } 
        }, 500); 
    }
    
    private void startSlide()
    {
        if (positive == 1)
        {
            Animation down =  AnimationUtils.loadAnimation(this, R.anim.slide_down);
            down.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation)
                {image1.setVisibility(View.GONE);}
                @Override
                public void onAnimationRepeat(Animation arg0) {}
                @Override
                public void onAnimationStart(Animation arg0) {}
            });
            image1.startAnimation(down);
        } else
        {
            Animation up =  AnimationUtils.loadAnimation(this, R.anim.slide_up);
            up.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationEnd(Animation animation)
                {image2.setVisibility(View.GONE);}
                @Override
                public void onAnimationRepeat(Animation arg0) {}
                @Override
                public void onAnimationStart(Animation arg0) {}
            });
            image2.startAnimation(up);
        }
    }
    
    /**
     * To hide the images and buttons pass true, otherwise false will show them.
     * @param hide
     */
    private void hideAllViews(boolean hide)
    {
        if (hide)
        {
            button1.setVisibility(View.GONE);
            button2.setVisibility(View.GONE);
            image1.setVisibility(View.GONE);
            image2.setVisibility(View.GONE);
        } else
        {
            button1.setVisibility(View.VISIBLE);
            button2.setVisibility(View.VISIBLE);
            image1.setVisibility(View.VISIBLE);
            image2.setVisibility(View.VISIBLE);
        }
    }
    
    private void roundOver()
    {
        String method = "roundOver";
        Log.i(DEBUG_TAG, method+" now what?");
    }
    
    /**
     * Set up the action listeners for the appropriate buttons.  If positive is 1, then
     * button one is positive and button 2 is negative.  The opposite for 2.
     */
    private void setupButtons()
    {
        final String method = "setupButtons";
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //disableButtons();
                if (positive == 1)
                {
                    Log.i(DEBUG_TAG, method+" button 1 is happy.");
                    Log.i(DEBUG_TAG, method+" button 2 is sad.");
                    hideNegateivePictureAndSlide();
                } else
                {
                    Log.i(DEBUG_TAG, method+" button 1 is sad.");
                    Log.i(DEBUG_TAG, method+" button 2 is happy.");
                    button1.setEnabled(false);
                }
            }
        });
        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //disableButtons();
                if (positive == 2)
                {
                    Log.i(DEBUG_TAG, method+" button 1 is sad.");
                    Log.i(DEBUG_TAG, method+" button 2 is happy.");
                    hideNegateivePictureAndSlide();
                } else
                {
                    Log.i(DEBUG_TAG, method+" button 1 is happy.");
                    Log.i(DEBUG_TAG, method+" button 2 is sad.");
                    button2.setEnabled(false);
                }
            }
        });
    }
    
    /**
     * Choose a random number between 1 and 2.
     * If the number is 1, then the first image and button will be used for the positive.
     * If it's two, then use the second button and image, and the other ones for the negative.
     */
    private void chooseImageType()
    {
        String method = "chooseImageType";
        int choose_one = (int)(1 + Math.random() * 2);
        Log.i(DEBUG_TAG, method+" choose_one "+choose_one);
        if (choose_one == 1)
        {
            positive = 1;
            negative = 2;
        } else
        {
            positive = 2;
            negative = 1;
        }
        Log.i(DEBUG_TAG, method+" positive "+positive+" negative "+negative);
    }
    
    /**
     * When we load the images, if positive is 1, then we put the positive images in image1_bitmap,
     * and the negative images in image2_bitmap.  We set the width and height at 225 and then
     * put the images in their respective layout images.
     */
    private void loadImages()
    {
        String method = "loadImages";
        String positive_image = positive_images[current_round];
        String negative_image = negative_images[current_round];
        Log.i(DEBUG_TAG, method+" positive_image "+positive_image);
        Log.i(DEBUG_TAG, method+" negative_image "+negative_image);
        Bitmap image1_bitmap = null;
        Bitmap image2_bitmap = null;
        if (positive == 1)
        {
            image1_bitmap = getBitmapFromAsset(current_folder+"/"+Constants.POSITIVE+"/"+positive_image);
            image2_bitmap = getBitmapFromAsset(current_folder+"/"+Constants.NEGATIVE+"/"+negative_image);
        } else
        {
            image2_bitmap = getBitmapFromAsset(current_folder+"/"+Constants.POSITIVE+"/"+positive_image);
            image1_bitmap = getBitmapFromAsset(current_folder+"/"+Constants.NEGATIVE+"/"+negative_image);
        }
        image1 = null;
        image2 = null; // reset the animation locations
        image1 = (ImageView)findViewById(R.id.imageView1);
        image2 = (ImageView)findViewById(R.id.imageView2);
        image1.setImageBitmap(image1_bitmap);
        image2.setImageBitmap(image2_bitmap);
        int width=225;
        int height=225;
        Bitmap resized_bitmap1 = Bitmap.createScaledBitmap(image1_bitmap, width, height, true);
        Bitmap resized_bitmap2 = Bitmap.createScaledBitmap(image2_bitmap, width, height, true);
        image1.setImageBitmap(resized_bitmap1);
        image2.setImageBitmap(resized_bitmap2);
    }
    
    private Bitmap getBitmapFromAsset(String file_name)
    {
        AssetManager assetManager = getAssets();
        InputStream istr = null;
        try 
        {
            istr = assetManager.open(file_name);
        } catch (IOException e) 
        {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
    
    private void setup()
    {
        current_round = 0;
    }
    
    public enum ImageType 
    {
        POSITIVE, NEGATIVE
    }

    /**
     * Load all the images from the positive and negative folders.
     */
    private void getImages()
    {
        String method = "getImages";
        if (current_folder.equals(Constants.DEFAULT))
        {
            try
            {
                positive_images = this.getResources().getAssets().list(current_folder+"/"+Constants.POSITIVE);
                negative_images = this.getResources().getAssets().list(current_folder+"/"+Constants.NEGATIVE);
                Log.i(DEBUG_TAG, method+" default positive_images path "+current_folder+"/"+Constants.POSITIVE);
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else
        {
            String path = context.getFilesDir().getAbsolutePath();
            Log.i(DEBUG_TAG, method+" path "+path);
            Log.i(DEBUG_TAG, method+" current folder "+current_folder);
            String path_to_positive_files = path+"/"+current_folder+"/"+Constants.POSITIVE;
            String path_to_negative_files = path+"/"+current_folder+"/"+Constants.NEGATIVE;
            File positive_files = new File(path_to_positive_files);
            File negative_files = new File(path_to_negative_files);
            Log.i(DEBUG_TAG, method+" path_to_positive_files "+path_to_positive_files);
            Log.i(DEBUG_TAG, method+" path_to_negative_files "+path_to_negative_files);
            if (positive_files.exists())
            {
                positive_images = positive_files.list();
                negative_images = negative_files.list();
            } else
            {
                Log.i(DEBUG_TAG, method+" no psoitive files folder");
            }
        }

        try {
            for (String file : positive_images) {
                Log.i(DEBUG_TAG, method + " positive_images " + file);
            }
            for (String file : negative_images) {
                Log.i(DEBUG_TAG, method + " negative_images " + file);
            }
        } catch (NullPointerException npe)
        {
            Log.i(DEBUG_TAG, npe.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exercise, menu);
        return true;
    }
    
}
