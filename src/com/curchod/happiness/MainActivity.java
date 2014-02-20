package com.curchod.happiness;

import java.io.File;

import com.curchod.happiness.util.Constants;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity 
{
	
	private static final String DEBUG_TAG = "MainActivity";
	private Button activity_button;
	private Button about_button;
	final Context context = this;
	private Editor shared_editor;
	private SharedPreferences shared_preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String method = "onCreate";
		Log.i(DEBUG_TAG, method+": build 22");
		this.shared_preferences = context.getSharedPreferences(Constants.PREFERENCES, Activity.MODE_PRIVATE);
        this.shared_editor = shared_preferences.edit();
        String current_folder = shared_preferences.getString(Constants.FOLDER, "");
        if (current_folder == null)
        {
        	current_folder = "";
        }
        Log.i(DEBUG_TAG, method+": current folder: "+current_folder);
		activity_button = (Button) findViewById(R.id.exercise_button);
        activity_button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	//disableButtons();
                startActivity(new Intent(MainActivity.this,
                    ExerciseActivity.class));
            }
        });
        
        about_button = (Button) findViewById(R.id.about_button);
        about_button.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            	//disableButtons();
                startActivity(new Intent(MainActivity.this,
                    AboutActivity.class));
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		menu.add(0 , 1, 0, R.string.create_happiness);
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) 
    {
    	String method = "onOptionsItemSelected";
    	String selected = item.toString();
    	Log.i(DEBUG_TAG, method+": selected "+selected);
    	getIntent();
    	if (item.getItemId() == 1)
    	{
    		Log.i(DEBUG_TAG, method+": Create a new Happiness folder.");
    		promptForNewFolderName();
    	    return true;
    	} 
    	return super.onOptionsItemSelected(item);
    }
	
	private void promptForNewFolderName()
    {
    	final String method = "promptForIPAndSave";
    	 AlertDialog.Builder alert = new AlertDialog.Builder(context);
         alert.setTitle(R.string.creat_happiness_input_title);
         alert.setMessage(R.string.enter_new_folder_name); 
         final EditText input = new EditText(context);
         alert.setView(input);
         alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() 
         {
        	 public void onClick(DialogInterface dialog, int whichButton) 
        	 {
        		String new_folder = input.getEditableText().toString();
        		File new_file = new File(context.getFilesDir(),new_folder);
        		File positive_file = new File(context.getFilesDir(),new_folder+"/"+Constants.POSITIVE);
        		File negative_file = new File(context.getFilesDir(),new_folder+"/"+Constants.NEGATIVE);
        	    boolean file = new_file.mkdirs();
        	    boolean pos = positive_file.mkdirs();
        	    boolean neg = negative_file.mkdirs();
        		Log.i(DEBUG_TAG, method+" saved new "+new_folder+" ok? "+file+" + "+pos+" - "+neg);
        	 }
         }); 
         alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() 
         {
           public void onClick(DialogInterface dialog, int whichButton) 
           {
               dialog.cancel();
           }
         });
         AlertDialog alertDialog = alert.create();
         alertDialog.show();
    }

}
