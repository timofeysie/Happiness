package com.curchod.happiness;

import java.io.File;
import java.io.IOException;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        setup();
        getImages();
        loadImages();
    }
    
    private void loadImages()
    {
    	String method = "loadImages";
    	String positive_image = positive_images[current_round];
    	String negative_image = negative_images[current_round];
    	image1 = (ImageView)findViewById(R.id.imageView1);
    	image2 = (ImageView)findViewById(R.id.imageView2);
    	image1.setImageBitmap(BitmapFactory.decodeFile("images/positive/"+positive_image));
    	image2.setImageBitmap(BitmapFactory.decodeFile("images/negative/"+negative_image));
    	Log.i(DEBUG_TAG, method+" positive_image "+positive_image);
    	Log.i(DEBUG_TAG, method+" negative_image "+negative_image);
    	//File pos_file1 = new File(positive_image);
    	File pos_file2 = new File("images/positive/"+positive_image);
    	//File neg_file1 = new File(positive_image);
    	File neg_file2 = new File("images/negative/"+positive_image);
    	//Log.i(DEBUG_TAG, method+" posative_image "+pos_file1.exists());
    	Log.i(DEBUG_TAG, method+" posative_image "+pos_file2.exists());
    	//Log.i(DEBUG_TAG, method+" negative_image "+neg_file1.exists());
    	Log.i(DEBUG_TAG, method+" negative_image "+neg_file2.exists());
    	
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
