package com.github.donmor3000.filedialog;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.donmor3000.filedialog.lib.FileDialog;
import com.github.donmor3000.filedialog.lib.FileDialogFilter;

import java.io.File;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		checkPermission();

		Button btn1 = findViewById(R.id.button);
		Button btn2 = findViewById(R.id.button2);
		Button btn3 = findViewById(R.id.button3);
		Button btn4 = findViewById(R.id.button4);

		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FileDialog.fileOpen(MainActivity.this, new FileDialogFilter[]{new FileDialogFilter(".html;.htm", new String[]{".html", ".htm"}),new FileDialogFilter("*", new String[]{"*"})}, true, new FileDialog.OnFileTouchedListener() {
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
				FileDialog.fileOpenMultiple(MainActivity.this, new FileDialogFilter[]{new FileDialogFilter(".html;.htm", new String[]{".html", ".htm"}),new FileDialogFilter("*", new String[]{"*"})}, true, new FileDialog.OnFileTouchedListener() {
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
				FileDialog.fileSelectDirectory(MainActivity.this, true, new FileDialog.OnFileTouchedListener() {
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
				FileDialog.fileSave(MainActivity.this, new FileDialogFilter[]{new FileDialogFilter(".html;.htm", new String[]{".html", ".htm"}),new FileDialogFilter("*", new String[]{"*"})}, true, new FileDialog.OnFileTouchedListener() {
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
