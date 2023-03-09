package cu.slam.wrep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class AcercaDActivity extends Activity{
	TextView textView;	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_acercad);	
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//to = MailTo.parse("aayes89@gmail.com");
		
		textView = (TextView) findViewById(R.id.textView2);
		textView.setText("\n\n\nWRep 1.0 es la primera aplicación que permite "+
		"reparar la WI-FI de cualquier smartphone SAMSUNG "+
				"al simular con seguridad la localización geográfica del smartphone.\n\n\n"+
		"Desarrollada por Slam. (2016-2017)\n"+
				"Información de contacto: aayes89@gmail.com");
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ayuda, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				setResult(RESULT_CANCELED);
				this.finish();
				return true;
			case R.id.back:
				startActivity(new Intent(this,MainActivity.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
