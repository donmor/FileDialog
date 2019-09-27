# FileDialog

The class FileDialog provides a set of methods to open dialogs for file-operating. It contains a set of static methods opening AlertDialogs to choose files or a directory, and a callback providing a java.io.File array contains files or directory chosen for further usages.

### Demo

The library is published with a demo app. Here is a part of the MainActivity.java: 

```java
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        // Request for permission to write to external storage.
		checkPermission();

		Button btn1 = findViewById(R.id.button);
		Button btn2 = findViewById(R.id.button2);
		Button btn3 = findViewById(R.id.button3);
		Button btn4 = findViewById(R.id.button4);
		
        // Create a FileDialogFilter array containing two FDFs, one is constructed with HTML file type and its extensions, and another is constructed to accept all file types (Can be replaced with constant FileDialog.ALL).
		final FileDialogFilter[] fileDialogFilters = new FileDialogFilter[]{new FileDialogFilter(".html;.htm", new String[]{".html", ".htm"}),new FileDialogFilter("*", new String[]{"*"})};

		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			    // Example of fileOpen, the fileDialogFilters is passed in.
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
			    // Example of fileOpenMultiple, the fileDialogFilters is passed in.
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
			    // Example of fileSelectDirectory.
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
			    // Example of fileSave, the fileDialogFilters is passed in.
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
```

### Usage

First of all, add dependency to the build.gradle: 

```groovy
compile 'com.github.donmor:filedialog:1.0' // For gradle plugin <3.0.0
implementation 'com.github.donmor:filedialog:1.0' // For gradle plugin >=3.0.0
```

Then make sure that WRITE_EXTERNAL_STORAGE permission of the app have been set to GRANTED if API level is higher than 22, and "android.defaultConfig.vectorDrawables.useSupportLibrary = true" should be added into build.gradle of the app if API level is lower than 21.

Now methods fileOpen, fileOpenMultiple, fileSelectDirectory and fileSave are ready to use. Note that these methods actually calls method fileDialog, if you want to control all params of the dialogs, for example, forcing the dialog to load volumes that is not writable, you should use fileDialog instead. Go to /doc for more details.














