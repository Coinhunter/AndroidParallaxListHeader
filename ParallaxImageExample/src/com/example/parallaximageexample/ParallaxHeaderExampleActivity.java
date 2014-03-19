package com.example.parallaximageexample;

import com.springworks.parallaxview.ParallaxImage;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.app.ListActivity;

public class ParallaxHeaderExampleActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setListActivityAdapter();
	}

	private void setListActivityAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getListObjects());
        addListHeader();
        setListAdapter(adapter);
	}

	private void addListHeader() {
		ListView list = (ListView) findViewById(android.R.id.list);

		// Add the header to the list. 
		// Note: This has to be done before the listadapter is created.
		View header = View.inflate(this, R.layout.header, null);	
		list.addHeaderView(header);
		
		ImageView headerImage = (ImageView) findViewById(R.id.header_image);
		headerImage.setImageResource(R.drawable.banner);
		ParallaxImage headerController = new ParallaxImage(headerImage, list, this, ParallaxImage.Modes.FOLD);
	}
	
	// Just get a list of small solar system
	// bodies to have something to fill out the
	// list enough that it will actually scroll.
	private String[] getListObjects() {
		return new String [] {
	    		"Ceres",
	    		"Pluto",
	    		"Haumea",
	    		"Makemake",
	    		"Eris",
	    		"Sedna",
	    		"Charon",
	    		"Varuna",
	    		"Orcus",
	    		"Quaoar",
	    		"Ganymede",
	    		"Titan",
	    		"Callisto",
	    		"Io",
	    		"Europa",
	    		"Triton",
	    		"Moon",
	    		"Venus"
		};
	}

}
