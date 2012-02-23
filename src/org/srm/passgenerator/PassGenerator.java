package org.srm.passgenerator;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import android.text.ClipboardManager;
import android.util.Log;


public class PassGenerator extends Activity implements OnClickListener{
	
	private static final String TAG = "PassGenerator";
	
	private int optionLength;
	private boolean optionLetters;
	private boolean optionCapitalLetters;
	private boolean optionNumbers;
	private boolean optionSymbols;
	private boolean optionClipboard;	
	
	private String lastPassword = "";
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        View generate = findViewById(R.id.generate);
        generate.setOnClickListener(this);
        
        View clipboardCopy = findViewById(R.id.clipboard_copy);
    	clipboardCopy.setOnClickListener(this);
        
        //load preferences and set images
        loadPreferences();
        
    }
    
    
    @Override
    protected void onResume(){
    	super.onResume();
    	loadPreferences();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	super.onCreateOptionsMenu(menu);
    	
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	
    	return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	
    	switch(item.getItemId()){
    		
    		case R.id.settings:
    			startActivity(new Intent(this, Prefs.class));
    			return true;
    		
    		
    		case R.id.about:
    			startActivity(new Intent(this, About.class));
    			return true;
    	}
    	
    	return false;
    }
    
    
    @Override
    public void onClick(View v){
    	
    	switch (v.getId()){    		
    		case R.id.generate:
    			generate();
    		break;
    		
    		case R.id.clipboard_copy:
    			copyToClipboard();
    		break;
    	}
    }
    
    public void loadPreferences(){  	 
    	 
    	//load length and show to conf list
    	optionLength = Prefs.getLength(this);
    	Log.d(TAG, "Length: "+String.valueOf(optionLength));
    	
    	TextView txtLength = (TextView)findViewById(R.id.option_length);
    	txtLength.setText(String.valueOf(optionLength));
    	
    	
    	optionLetters = Prefs.isIncludeLetters(this);
    	Log.d(TAG, "Include Letters: " +  Boolean.toString(optionLetters));
    	
    	ImageView imageLetters = (ImageView)findViewById(R.id.option_letters);
        imageLetters.setImageResource((optionLetters)?R.drawable.on:R.drawable.off);         
    	         
    	if (optionLetters){
    		optionCapitalLetters = Prefs.isIncludeCapitalLetters(this);
    	}else optionCapitalLetters = false;
    	Log.d(TAG, "Include Capital Letters: " +  Boolean.toString(optionCapitalLetters));
    	 
    	ImageView imageCapitalLetters = (ImageView)findViewById(R.id.option_capital_letters);
    	imageCapitalLetters.setImageResource((optionCapitalLetters)?R.drawable.on:R.drawable.off);  
    	 
    	optionNumbers = Prefs.isIncludeNumbers(this);
    	Log.d(TAG, "Include Numbers: " +  Boolean.toString(optionNumbers));
    	 
    	ImageView imageNumbers = (ImageView)findViewById(R.id.option_numbers);
    	imageNumbers.setImageResource((optionNumbers)?R.drawable.on:R.drawable.off);  
    	 
    	optionSymbols = Prefs.isIncludeSymbols(this);
    	Log.d(TAG, "Include Symbols: " +  Boolean.toString(optionSymbols));
    	 
    	ImageView imageSymbols = (ImageView)findViewById(R.id.option_symbols);
    	imageSymbols.setImageResource((optionSymbols)?R.drawable.on:R.drawable.off);  
    	
    	optionClipboard = Prefs.isEnabledClipboard(this);
    	Log.d(TAG, "Enable clipboard: " +  Boolean.toString(optionClipboard));
    	
    	
    	//setup clipboard block
    	View clipboardBlock = findViewById(R.id.clipboardBlock);
        	
    	if (optionClipboard){    		 
    		clipboardBlock.setVisibility(LinearLayout.VISIBLE);   		 
    	}else{
    		clipboardBlock.setVisibility(LinearLayout.GONE);
    	}
    }
    
    public void generate(){
    	
    	
    	//Instance password class and setup it
    	Password pass = new Password();
    	
    	pass.setLength(optionLength);
    	pass.useLetters(optionLetters);
    	pass.useCapitalLetters(optionCapitalLetters);
    	pass.useNumbers(optionNumbers);
    	pass.useSymbols(optionSymbols);
    	
    	String generatedPass = pass.generate();
    	
    	TextView password = (TextView)findViewById(R.id.password);
    	password.setText(generatedPass);
    	
    	if (optionClipboard) lastPassword = generatedPass;
    }
    
    public void copyToClipboard(){
    	
    	if (lastPassword.equals("")){
    		Toast.makeText(this, R.string.clipboard_empty, Toast.LENGTH_SHORT).show();
    		return;
    	} 	
    	
    	ClipboardManager clip = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
    	clip.setText(lastPassword);    	
    	Toast.makeText(this, R.string.clipboard_copied, Toast.LENGTH_SHORT).show();    	
    }
}