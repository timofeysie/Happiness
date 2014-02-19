package com.curchod.happiness;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ExerciseActivity extends Activity 
{

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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        String method = "onCreate";
        Log.i(DEBUG_TAG, method+" build 22c");
        setup();
        getImages();
        chooseImageType();
        loadImages();
        setupButtons();
    }
    
    private void nextRound()
    {
    	hideView(true);
    	Handler handler = new Handler(); 
        handler.postDelayed(new Runnable() 
        { 
             public void run() 
             { 
            	 current_round++;
            	 if (current_round>positive_images.length)
            	 {
            		 roundOver();
            	 }
                 chooseImageType();
                 loadImages();
                 setupButtons();
                 hideView(false);
             } 
        }, 5000); 
    }
    
    private void hideView(boolean hide)
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
                	nextRound();
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
                	nextRound();
                } else
                {
                	Log.i(DEBUG_TAG, method+" button 1 is happy.");
                	Log.i(DEBUG_TAG, method+" button 2 is sad.");
                	button2.setEnabled(false);
                }
            }
        });
    }
    
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
    		image1_bitmap = getBitmapFromAsset("positive/"+positive_image);
    		image2_bitmap = getBitmapFromAsset("negative/"+negative_image);
    	} else
    	{
    		image2_bitmap = getBitmapFromAsset("positive/"+positive_image);
    		image1_bitmap = getBitmapFromAsset("negative/"+negative_image);
    	}
    	image1 = (ImageView)findViewById(R.id.imageView1);
    	image2 = (ImageView)findViewById(R.id.imageView2);
    	image1.setImageBitmap(image1_bitmap);
    	image2.setImageBitmap(image2_bitmap);
    	int width=100;
    	int height=100;
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


    private void getImages()
	{
		String method = "getImages";
		try
		{
			positive_images = this.getResources().getAssets().list("positive");
			negative_images = this.getResources().getAssets().list("negative");
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (String file:positive_images)
		{
			Log.i(DEBUG_TAG, method+" positive_images "+file);
		}
		for (String file:negative_images)
		{
			Log.i(DEBUG_TAG, method+" negative_images "+file);
		}
	}
	

    private void showImages()
	{
		//LinearLayOut Setup
        LinearLayout linearLayout= new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));

        //ImageView Setup
        ImageView imageView = new ImageView(this);
        //setting image resource
        //imageView.setImageResource(R.drawable.play);
        //setting image position
        imageView.setLayoutParams(new LayoutParams(
        		LayoutParams.MATCH_PARENT,
        		LayoutParams.WRAP_CONTENT));

        //adding view to layout
        linearLayout.addView(imageView);
        //make visible to program
        setContentView(linearLayout);
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.exercise, menu);
        return true;
    }
    
}
