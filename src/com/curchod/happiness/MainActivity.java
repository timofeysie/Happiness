package com.curchod.happiness;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.curchod.happiness.util.Constants;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends Activity 
{
	
	private static final String DEBUG_TAG = "MainActivity";
	private Button activity_button;
	private Button about_button;
	final Context context = this;
	private Editor shared_editor;
	private SharedPreferences shared_preferences;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String method = "onCreate";
		Log.i(DEBUG_TAG, method+": build 25");
		this.shared_preferences = context.getSharedPreferences(Constants.PREFERENCES, Activity.MODE_PRIVATE);
        this.shared_editor = shared_preferences.edit();
        String current_folder = shared_preferences.getString(Constants.FOLDER, "");
        if (current_folder == null)
        {
        	current_folder = Constants.DEFAULT;
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
		menu.add(0 , 2, 0, R.string.choose_happiness);
		return true;
	}
	
	/**
	 * Settings
	 * Create Happines which goes to the CreateActivity.
	 */
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
    	} else if (item.getItemId() == 2)
    	{
    		Log.i(DEBUG_TAG, method+": Choose your Happiness folder.");
    		promptForFolderName();
    	    return true;
    	} 
    	return super.onOptionsItemSelected(item);
    }
	
	/**
	 * Throw up an AlertDialog to get the name of the new Happiness folder to create.
	 * Then start up the CreateActivity and let the user go to work.
	 */
	private void promptForNewFolderName()
    {
    	final String method = "promptForNewFolderName";
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
        		Intent intent = new Intent(MainActivity.this, CreateActivity.class);
        		intent.putExtra("created_folder", new_folder);
        		startActivity(intent);
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
	
	/**
	 * Get a list of all the folders in the assets folder.
	 * Then set the chosen folder as the current one and set it in the shared preferences.
	 */
	private void promptForFolderName()
    {
    	final String method = "promptForFolderName";
    	dialog = new Dialog(this);
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	builder.setTitle("Select folder");
    	ListView modeList = new ListView(this);
    	//modeList.setBackgroundColor(getResources().getColor(com.curchod.wherewithal.R.color.white));
     	String [] folders = getUserCreatedFolders();
		final String [] static_folders = folders;
    	ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, folders);
    	modeList.setAdapter(modeAdapter);
    	builder.setView(modeList);
    	dialog = builder.create();
    	final MainActivity main_activity = this;
    	dialog.show();
    	modeList.setOnItemClickListener(new OnItemClickListener() 
		{
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	          {
	              String selected_folder = static_folders[position];
	              Log.i(DEBUG_TAG, method+" selected "+selected_folder);
	              shared_editor.putString(Constants.FOLDER, selected_folder);
	        	  shared_editor.commit();
	              main_activity.runOnUiThread(new Runnable() 
	              {
	                  @Override
	                  public void run() 
	                  {
	                	  dialog.dismiss();
	                  }
	              });
	          
	          }
	    });
    }
	
	private String [] getUserCreatedFolders()
	{
		File folders = context.getFilesDir();
		String [] user_folders = folders.list();
		String [] return_folders = new String [user_folders.length+1];
		return_folders[0] = Constants.DEFAULT;
		for (int i = 1; i < user_folders.length+1; i++)
			return_folders[i] = user_folders[i-1];
		return return_folders;
	}

}
