package com.curchod.happiness;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity 
{
	
	private Button activity_button;
	private Button about_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		return true;
	}

}
