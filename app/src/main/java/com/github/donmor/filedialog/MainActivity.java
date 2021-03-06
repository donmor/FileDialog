package com.github.donmor.filedialog;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.donmor.filedialog.lib.FileDialog;
import com.github.donmor.filedialog.lib.FileDialogFilter;

import java.io.File;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
		setContentView(R.layout.activity_main);

		checkPermission();

		Button btn1 = findViewById(R.id.button);
		Button btn2 = findViewById(R.id.button2);
		Button btn3 = findViewById(R.id.button3);
		Button btn4 = findViewById(R.id.button4);

		final FileDialogFilter[] fileDialogFilters = new FileDialogFilter[]{new FileDialogFilter(".html;.htm", new String[]{".html", ".htm"}), FileDialog.ALL};

		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FileDialog.fileOpen(MainActivity.this, fileDialogFilters, new FileDialog.OnFileTouchedListener() {
					@Override
					public void onFileTouched(File[] files) {
						for (File f : files)
							Toast.makeText(MainActivity.this, f.getAbsolutePath(), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCanceled() {
						Toast.makeText(MainActivity.this, "CANCELLED", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FileDialog.fileOpenMultiple(MainActivity.this, fileDialogFilters, new FileDialog.OnFileTouchedListener() {
					@Override
					public void onFileTouched(File[] files) {
						for (File f : files)
							Toast.makeText(MainActivity.this, f.getAbsolutePath(), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCanceled() {
						Toast.makeText(MainActivity.this, "CANCELLED", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		btn3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FileDialog.fileSelectDirectory(MainActivity.this, new FileDialog.OnFileTouchedListener() {
					@Override
					public void onFileTouched(File[] files) {
						for (File f : files)
							Toast.makeText(MainActivity.this, f.getAbsolutePath(), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCanceled() {
						Toast.makeText(MainActivity.this, "CANCELLED", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		btn4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FileDialog.fileSave(MainActivity.this, fileDialogFilters, new FileDialog.OnFileTouchedListener() {
					@Override
					public void onFileTouched(File[] files) {
						for (File f : files)
							Toast.makeText(MainActivity.this, f.getAbsolutePath(), Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onCanceled() {
						Toast.makeText(MainActivity.this, "CANCELLED", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	@TargetApi(23)
	private void checkPermission() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
		}
	}

}
