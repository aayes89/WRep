package cu.slam.wrep;

import java.io.File;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import cu.slam.wrep.R;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static String rutaDB = "/data/data/com.android.providers.settings/databases/";
	private static String dbName = "settings.db";
	ImageView status;
	ImageView std;
	ImageButton rep;
	File fDB;
	SQLiteDatabase sdb;
	Cursor cursor;
	ContentValues values;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ObtenerDBXroot();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				setResult(RESULT_CANCELED);
				this.finish();
				return true;
			case R.id.item1://acerca D
				startActivity(new Intent(this, AcercaDActivity.class));
			break;
			case R.id.action_settings://ayuda				
				startActivity(new Intent(this,AyudaActivity.class));
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void ObtenerDBNroot() {
		try {
			File dbF = new File(rutaDB, dbName);
			if (dbF.exists()) {
				FileInputStream fis = new FileInputStream(dbF);
				FileOutputStream fos = new FileOutputStream(
						Environment.getExternalStorageDirectory());
				byte[] buffer = new byte[1024];
				int length;
				while ((length = fis.read(buffer)) > 0) {
					fos.write(buffer, 0, length);
				}
				fos.flush();
				fos.close();
				fis.close();
			}else
				Toast.makeText(getApplicationContext(), "BD no encontrada", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void RestaurarDBNroot(File orig, File dest) {
		try {
			FileInputStream fis = new FileInputStream(orig);
			FileOutputStream fos = new FileOutputStream(dest.getAbsolutePath());
			byte[] buffer = new byte[1024];
			int length;
			while ((length = fis.read(buffer)) > 0) {
				fos.write(buffer, 0, length);
			}
			fos.flush();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ObtenerDBXroot() {
		try {
			Runtime.getRuntime().exec(
					"su busybox cp " + (rutaDB + dbName) + " "
							+ Environment.getExternalStorageDirectory());
		} catch (Exception e) {
			Log.d("ERR: ", e.getMessage());
		}
	}

	public void RestaurarDBXroot(File src, File dest) {
		try {
			Runtime cmd = Runtime.getRuntime();
			// dest.delete();
			Process p = cmd.exec("su busybox rm " + dest.getAbsolutePath());
			p.waitFor();
			p = cmd.exec("su busybox cp -f " + src.getAbsolutePath() + " "
					+ dest.getAbsolutePath());
			p.waitFor();
			src.delete();
		} catch (Exception e) {
			Log.d("ERR: ", e.getMessage());
		}
	}

	public void CHGIconsSiRep() {
		rep = (ImageButton) findViewById(R.id.imageButton2);
		rep.setVisibility(ImageButton.VISIBLE);
		std = (ImageView) findViewById(R.id.estadoS);
		std.setVisibility(ImageView.VISIBLE);
		std.setImageResource(R.drawable.ic_nocompletado);
		status = (ImageView) findViewById(R.id.imgSennal);
		status.setImageResource(R.drawable.ic_sinsennal);
	}

	public void CHGIconsNoRep() {
		std = (ImageView) findViewById(R.id.estadoS);
		std.setVisibility(ImageView.VISIBLE);
		std.setImageResource(R.drawable.ic_completado);
		status = (ImageView) findViewById(R.id.imgSennal);
		status.setImageResource(R.drawable.ic_sennal);
		rep = (ImageButton) findViewById(R.id.imageButton2);
		rep.setVisibility(ImageButton.INVISIBLE);
	}

	public void BuscarProblema(View v) {
		if (NecesitaReparar() == true) {
			CHGIconsSiRep();
		} else {
			CHGIconsNoRep();
		}
	}

	/*
	 * cursor = sdb.rawQuery(
	 * "SELECT * FROM global WHERE name='wifi_country_code'", null);
	 * cursor.moveToFirst(); values = new ContentValues(3);
	 * values.put(cursor.getColumnName(0), cursor.getString(0));
	 * values.put(cursor.getColumnName(1), cursor.getString(1));
	 * values.put(cursor.getColumnName(2), cursor.getString(2)); valor =
	 * cursor.getString(2);
	 */

	public boolean NecesitaReparar() {
		String valor = "";
		try {
			fDB = new File(Environment.getExternalStorageDirectory(), dbName);
			if (fDB.exists()) {
				sdb = SQLiteDatabase.openDatabase(fDB.getAbsolutePath(), null,
						SQLiteDatabase.OPEN_READWRITE);

				cursor = sdb.rawQuery("SELECT value FROM global WHERE name='wifi_country_code'",null);

				cursor.moveToFirst();
				valor = cursor.getString(0);				
				cursor.close();
				sdb.close();
				Toast.makeText(getApplicationContext(), valor,
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return valor.equalsIgnoreCase("cu");
		}
	}

	public void RepararWifi(View v) {
		try {
			if (fDB.exists()) {
				sdb = SQLiteDatabase.openDatabase(fDB.getAbsolutePath(), null,
						SQLiteDatabase.OPEN_READWRITE);

				sdb.execSQL("UPDATE global SET value='US' WHERE name='wifi_country_code'");

				cursor = sdb.rawQuery("SELECT value FROM global WHERE name='wifi_country_code'",null);
				cursor.moveToFirst();

				Toast.makeText(getApplicationContext(), cursor.getString(0),
						Toast.LENGTH_SHORT).show();
			
			 if (cursor == null) {
				 CHGIconsNoRep();
				}
			 else {
				 CHGIconsSiRep();
				 //RestaurarDBNroot(fDB, new File(new File(rutaDB), dbName));
				 RestaurarDBXroot(fDB, new File(new File(rutaDB), dbName));
				 }
			 }
			cursor.close();
			sdb.close();
			if(fDB.exists())
				fDB.delete(); 			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
