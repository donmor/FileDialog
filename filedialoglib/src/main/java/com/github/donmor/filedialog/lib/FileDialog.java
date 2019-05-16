/*
 * com.github.donmor.filedialog.lib.FileDialog <= [P|FileDialog]
 * Last modified: 15:32:28 2019/05/10
 * Copyright (c) 2019 donmor
 */

package com.github.donmor.filedialog.lib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.github.donmor.filedialog.lib.utils.FileDialogAdapter;
//import com.github.donmor.filedialog.lib.utils.MimeTypeUtil;

import java.io.File;
import java.lang.reflect.Method;

/**
 * The class FileDialog provides a set of methods to open dialogs for file-operating. It contains a set of static methods opening AlertDialogs to choose files or a directory, and a callback providing a java.io.File array contains files or directory chosen for further usages. Make sure that WRITE_EXTERNAL_STORAGE permission of the app have been set to GRANTED if API level is higher than 22, and "android.defaultConfig.vectorDrawables.useSupportLibrary = true" should be added into build.gradle of the app if API level is lower than 21.
 */
@SuppressWarnings("WeakerAccess")
public abstract class FileDialog {

	/**
	 * The constant MIME_ALL(&#42;/&#42;).
	 */
	public static final String MIME_ALL = "*/*";

	private static final String METHOD_GET_VOLUME_PATHS = "getVolumePaths", STR_EMPTY = "";

	/**
	 * The call back that will be run when the dialog is closed.
	 */
	public interface OnFileTouchedListener {

		/**
		 * Invoked if one or more file is to be opened/saved.
		 *
		 * @param files the files to open/save. It will contain more than one File when mode is 2(open multiple files), otherwise it will contain only one File.
		 */
		void onFileTouched(File[] files);

		/**
		 * Invoked if the dialog is cancelled.
		 */
		void onCanceled();

	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent   The parent Context.
	 * @param filters  This parameter provides a set of filters.
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, FileDialogFilter[] filters, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), null, 0, filters, 0, null, 0, false, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent   The parent Context.
	 * @param filters  This parameter provides a set of filters.
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, FileDialogFilter[] filters, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), null, 1, filters, 0, null, 0, false, false, listener);
	}

	/**
	 * Open a dialog to select a directory.
	 *
	 * @param parent   The parent Context.
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileSelectDirectory(final Context parent, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), null, 2, null, 0, null, 0, false, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent   The parent Context.
	 * @param filters  This parameter provides a set of filters.
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, FileDialogFilter[] filters, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), null, 3, filters, 0, null, 0, false, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent   The parent Context.
	 * @param filename The default filename.
	 * @param filters  This parameter provides a set of filters.
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, String filename, FileDialogFilter[] filters, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), filename, 3, filters, 0, null, 0, false, false, listener);
	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent     The parent Context.
	 * @param filters    This parameter provides a set of filters.
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, FileDialogFilter[] filters, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), null, 0, filters, 0, null, 0, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent     The parent Context.
	 * @param filters    This parameter provides a set of filters.
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, FileDialogFilter[] filters, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), null, 1, filters, 0, null, 0, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a directory.
	 *
	 * @param parent     The parent Context.
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileSelectDirectory(final Context parent, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), null, 2, null, 0, null, 0, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent     The parent Context.
	 * @param filters    This parameter provides a set of filters.
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, FileDialogFilter[] filters, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), null, 3, filters, 0, null, 0, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent     The parent Context.
	 * @param filename   The default filename.
	 * @param filters    This parameter provides a set of filters.
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, String filename, FileDialogFilter[] filters, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), filename, 3, filters, 0, null, 0, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param filters        This parameter provides a set of filters.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, File startDirectory, FileDialogFilter[] filters, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, null, 0, filters, 0, null, 0, false, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param filters        This parameter provides a set of filters.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, File startDirectory, FileDialogFilter[] filters, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, null, 1, filters, 0, null, 0, false, false, listener);
	}

	/**
	 * Open a dialog to select a directory.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSelectDirectory(final Context parent, File startDirectory, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, null, 2, null, 0, null, 0, false, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param filters        This parameter provides a set of filters.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, File startDirectory, FileDialogFilter[] filters, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, null, 3, filters, 0, null, 0, false, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param filename       The default filename.
	 * @param filters        This parameter provides a set of filters.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, File startDirectory, String filename, FileDialogFilter[] filters, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, filename, 3, filters, 0, null, 0, false, false, listener);
	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param filters        This parameter provides a set of filters.
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, File startDirectory, FileDialogFilter[] filters, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, null, 0, filters, 0, null, 0, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param filters        This parameter provides a set of filters.
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, File startDirectory, FileDialogFilter[] filters, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, null, 1, filters, 0, null, 0, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a directory.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSelectDirectory(final Context parent, File startDirectory, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, null, 2, null, 0, null, 0, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param filters        This parameter provides a set of filters.
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, File startDirectory, FileDialogFilter[] filters, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, null, 3, filters, 0, null, 0, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param filename       The default filename.
	 * @param filters        This parameter provides a set of filters.
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, File startDirectory, String filename, FileDialogFilter[] filters, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, filename, 3, filters, 0, null, 0, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select folder or files. All parameters are required using this method.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. If invalid, the dialog will start with the default SD card directory.
	 * @param filename       The default filename for fileSave mode. It will be ignored in other modes.
	 * @param mode           This parameter decides the type of the dialog. 0 for fileOpen, 1 for fileOpenMultiple, 2 for fileSelectDirectory, and 3 for fileSave.
	 * @param filterIndex    This parameter provides the default position of filter spinner. Usually its value is 0, and it will be reset if an invalid value was passed.
	 * @param filters        This parameter provides a set of filters.
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param ignoreReadOnly This parameter decides whether read-only status of a device will be ignored, for example, most of systems prevents third part apps from writing to external SD cards.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileDialog(final Context parent, File startDirectory, String filename, final int mode, int filterIndex, FileDialogFilter[] filters, final boolean showHidden, boolean ignoreReadOnly, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, filename, mode, filters, filterIndex, null, 0, showHidden, ignoreReadOnly, listener);
	}

	/**
	 * Open a dialog to select folder or files. All parameters are required using this method.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. If invalid, the dialog will start with the default SD card directory.
	 * @param filename       The default filename for fileSave mode. It will be ignored in other modes.
	 * @param mode           This parameter decides the type of the dialog. 0 for fileOpen, 1 for fileOpenMultiple, 2 for fileSelectDirectory, and 3 for fileSave.
	 * @param filterIndex    This parameter provides the default position of filter spinner. Usually its value is 0, and it will be reset if an invalid value was passed.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. The string array will be firstly trimmed, if one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det            The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param ignoreReadOnly This parameter decides whether read-only status of a device will be ignored, for example, most of systems prevents third part apps from writing to external SD cards.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileDialog(final Context parent, File startDirectory, String filename, final int mode, int filterIndex, String[] mimes, int det, final boolean showHidden, boolean ignoreReadOnly, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, filename, mode, null, filterIndex, mimes, det, showHidden, ignoreReadOnly, listener);
	}

	private static void fileDialog(final Context parent, File startDirectory, String filename, final int mode, FileDialogFilter[] filters, int filterIndex, String[] mimes, int det, final boolean showHidden, boolean ignoreReadOnly, final OnFileTouchedListener listener) {
		final View view = LayoutInflater.from(parent).inflate(R.layout.file_dialog, null);
		String[] mimeTypes = null;
		final MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		if (mimes != null) {
			boolean vAll = false;
			String[] vMime = new String[mimes.length];
			int i = 0;
			for (String v : mimes) {
				if (v.equals(MIME_ALL)) vAll = true;
				else if (mimeTypeMap.hasMimeType(v)) {
					vMime[i] = v;
					i++;
				}
			}
			if (vAll || i == 0) {
				vMime[i] = MIME_ALL;
				i++;
			}
			String[] xm = new String[i];
			System.arraycopy(vMime, 0, xm, 0, i);
			mimeTypes = xm;
		}
		final EditText fName = view.findViewById(R.id.save_f_name);
		if (mode < 3) fName.setVisibility(View.GONE);
		else if (filename != null && filename.length() > 0) fName.setText(filename);
		final TextView lblPath = view.findViewById(R.id.lblPath);
		try {
			if (!startDirectory.isDirectory() || !startDirectory.canWrite()) throw new Exception();
		} catch (Exception e) {
			startDirectory = Environment.getExternalStorageDirectory();
		}
		lblPath.setText(startDirectory.getAbsolutePath());
		Spinner spnExt = view.findViewById(R.id.spnExt);
		if (mode == 2) spnExt.setVisibility(View.GONE);
		else {
			ArrayAdapter<String> spinnerAdapter;
			if (mimeTypes != null) {
				switch (det) {
					case 0:
						spinnerAdapter = new ArrayAdapter<>(view.getContext(), R.layout.ext_slot, mimeTypes);
						break;
					case 1:
						String[] des = new String[mimeTypes.length];
						for (int i = 0; i < mimeTypes.length; i++) {
							if (mimeTypes[i].equals(MIME_ALL))
								des[i] = '.' + mimeTypeMap.getExtensionFromMimeType(mimeTypes[i]);
						}
						spinnerAdapter = new ArrayAdapter<>(view.getContext(), R.layout.ext_slot, des);
						break;
					case 2:
						String[] desc = new String[mimeTypes.length];
						for (int i = 0; i < mimeTypes.length; i++) {
							if (mimeTypes[i].equals(MIME_ALL))
								desc[i] = desc[i] + '(' + '.' + mimeTypeMap.getExtensionFromMimeType(mimeTypes[i]) + ')';
						}
						spinnerAdapter = new ArrayAdapter<>(view.getContext(), R.layout.ext_slot, desc);
						break;
					default:
						spinnerAdapter = new ArrayAdapter<>(view.getContext(), R.layout.ext_slot, mimeTypes);
						break;
				}

				spinnerAdapter.setDropDownViewResource(R.layout.ext_slot);
				spnExt.setAdapter(spinnerAdapter);
				if (filterIndex > 0 && filterIndex < mimeTypes.length)
					spnExt.setSelection(filterIndex);
				if (mimeTypes.length < 2) spnExt.setEnabled(false);
			} else if (filters != null) {
				String[] fil = new String[filters.length];
				for (int i = 0; i < filters.length; i++)
					fil[i] = filters[i].name;
				spinnerAdapter = new ArrayAdapter<>(view.getContext(), R.layout.ext_slot, fil);
				spinnerAdapter.setDropDownViewResource(R.layout.ext_slot);
				spnExt.setAdapter(spinnerAdapter);
				if (filterIndex > 0 && filterIndex < filters.length)
					spnExt.setSelection(filterIndex);
				if (filters.length < 2) spnExt.setEnabled(false);
			}
		}
		final RecyclerView dir = view.findViewById(R.id.diFileList);
		dir.setLayoutManager(new LinearLayoutManager(view.getContext()));
		final FileDialogAdapter dirAdapter = new FileDialogAdapter(view.getContext(), filters, spnExt.getSelectedItemPosition(), mimeTypes, mimeTypeMap, startDirectory, mode == 1, mode == 2, showHidden, ignoreReadOnly);
		dir.setAdapter(dirAdapter);
		final Button btnBack = view.findViewById(R.id.btnBack);
		btnBack.setEnabled(dirAdapter.getDevices().length > 1 || !startDirectory.equals(Environment.getExternalStorageDirectory()));
		ImageButton btnNewFolder = view.findViewById(R.id.btnNewFolder);
		if (mode < 2) btnNewFolder.setVisibility(View.GONE);
		else btnNewFolder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final View view1 = LayoutInflater.from(view.getContext()).inflate(R.layout.fn_slot, null);
				final EditText editText = view1.findViewById(R.id.eFn);
				final AlertDialog newFolderDialog = new AlertDialog.Builder(view.getContext())
						.setTitle(R.string.new_folder)
						.setView(view1)
						.setNegativeButton(android.R.string.cancel, null)
						.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								File f = new File(dirAdapter.getCurrentDir().getAbsolutePath() + '/' + editText.getText().toString());
								if (f.exists())
									Toast.makeText(view.getContext(), R.string.folder_already_exist, Toast.LENGTH_SHORT).show();
								else {
									boolean d = f.mkdir();
									if (d && f.exists()) {
										dirAdapter.setDir(f);
										dir.setAdapter(dirAdapter);
										lblPath.setText(f.getAbsolutePath());
										btnBack.setEnabled(true);
									}
								}
							}
						})
						.show();
				final Button okx = newFolderDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				okx.setEnabled(false);
				editText.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {

					}

					@Override
					public void afterTextChanged(Editable s) {
						if (editText.getText().toString().indexOf('.') == 0) {
							try {
								if (!showHidden) {
									okx.setEnabled(false);
									Toast.makeText(view1.getContext(), R.string.cannot_create_hidden_files, Toast.LENGTH_SHORT).show();
								} else if (editText.getText().toString().substring(1).length() == 0)
									okx.setEnabled(false);
								else okx.setEnabled(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (editText.getText().toString().indexOf('+') == 0 || editText.getText().toString().indexOf('-') == 0) {
							okx.setEnabled(false);
							Toast.makeText(view1.getContext(), R.string.filename_cannot_begin_with, Toast.LENGTH_SHORT).show();
						} else if (illegalFilename(editText.getText().toString())) {
							okx.setEnabled(false);
							Toast.makeText(view1.getContext(), R.string.filename_cannot_contains, Toast.LENGTH_SHORT).show();
						} else if (editText.getText().toString().length() > 0) okx.setEnabled(true);
						else okx.setEnabled(false);
					}
				});
				editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
						if (okx.isEnabled())
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
								okx.performClick();
							else
								okx.callOnClick();
						return true;
					}
				});
			}
		});
		if (mode != 2) {
			spnExt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					dirAdapter.setFilterIndex(position);
					dirAdapter.reload();
					dir.setAdapter(dirAdapter);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {

				}
			});
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(parent).setView(view);
		if (mode == 0) builder.setTitle(R.string.open);
		else {
			if (mode == 1) builder.setTitle(R.string.open);
			else if (mode == 2) builder.setTitle(R.string.select_dir);
			else if (mode == 3) builder.setTitle(R.string.save_as);
			builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
					.setPositiveButton(android.R.string.ok, null);
		}
		final AlertDialog fileDialog = builder.create();
		fileDialog.setCanceledOnTouchOutside(false);
		fileDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				listener.onCanceled();
			}
		});
		fileDialog.show();
		final Button ok = fileDialog.getButton(AlertDialog.BUTTON_POSITIVE);
		if (mode == 1) ok.setEnabled(false);
		else if (mode == 2) ok.setEnabled(true);
		else if (mode == 3) {
			if (fName.getText().toString().indexOf('.') == 0) {
				if (!showHidden || fName.getText().toString().substring(1).length() == 0)
					ok.setEnabled(false);
				else ok.setEnabled(true);
			} else if (fName.getText().toString().indexOf('+') == 0 || fName.getText().toString().indexOf('-') == 0 || illegalFilename(fName.getText().toString())) {
				ok.setEnabled(false);
			} else if (fName.getText().toString().length() > 0)
				ok.setEnabled(true);
			else ok.setEnabled(false);
		}
		ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mode == 1) {
					File[] files = dirAdapter.getSelectedFiles();
					if (files.length > 0) {
						listener.onFileTouched(files);
						fileDialog.dismiss();
					}
				} else if (mode == 2) {
					listener.onFileTouched(new File[]{dirAdapter.getCurrentDir()});
					fileDialog.dismiss();
				} else if (mode == 3) {
					String fn = fName.getText().toString();
					String mmx = dirAdapter.mimeTypes != null
							?
							dirAdapter.mimeTypes[dirAdapter.getFilterIndex()]
							:
							null;
					FileDialogFilter ffx = dirAdapter.filters != null
							?
							dirAdapter.filters[dirAdapter.getFilterIndex()]
							:
							null;
					fn = mmx != null
							?
							(mmx.equals(MIME_ALL) || mmx.equals(mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(fn)))
									?
									fn
									:
									fn + '.' + mimeTypeMap.getExtensionFromMimeType(mmx))
							:
							ffx != null
									?
									ffx.meetExtensions(fn)
											?
											fn
											:
											fn + ffx.extensions[0]
									:
									fn;
					String fPath = dirAdapter.getCurrentDir().getAbsolutePath() + '/' + fn;
					final File of = new File(fPath);
					if (of.exists()) {
						new AlertDialog.Builder(view.getContext())
								.setMessage(R.string.file_already_exist)
								.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										listener.onFileTouched(new File[]{of});
										fileDialog.dismiss();
									}
								})
								.setNegativeButton(android.R.string.cancel, null)
								.show();
					} else {
						listener.onFileTouched(new File[]{of});
						fileDialog.dismiss();
					}
				}
			}
		});
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				File f = dirAdapter.getParentDir();
				if (f != null && f.exists() && f.isDirectory()) {
					dirAdapter.setDir(f);
					dir.setAdapter(dirAdapter);
					lblPath.setText(f.getAbsolutePath());
					btnBack.setEnabled(dirAdapter.getDevices().length > 1 || !dirAdapter.getRootDir().getAbsolutePath().equals(f.getAbsolutePath()));
					if (mode == 1) ok.setEnabled(false);
					else if (mode == 2) ok.setEnabled(true);
					else if (mode == 3) {
						if (fName.getText().toString().indexOf('.') == 0) {
							if (!showHidden || fName.getText().toString().substring(1).length() == 0)
								ok.setEnabled(false);
							else ok.setEnabled(true);
						} else if (fName.getText().toString().indexOf('+') == 0 || fName.getText().toString().indexOf('-') == 0 || illegalFilename(fName.getText().toString())) {
							ok.setEnabled(false);
						} else if (fName.getText().toString().length() > 0)
							ok.setEnabled(true);
						else ok.setEnabled(false);
					}
					if (fName.getText().toString().length() > 0 && !illegalFilename(fName.getText().toString()))
						ok.setEnabled(true);
				} else {
					dirAdapter.setRoot();
					dir.setAdapter(dirAdapter);
					lblPath.setText(STR_EMPTY);
					btnBack.setEnabled(false);
					ok.setEnabled(false);
				}
			}
		});
		dirAdapter.setOnItemClickListener(new FileDialogAdapter.ItemClickListener() {
			@Override
			public void onItemClick(int position) {
				File f = dirAdapter.getFile(position);
				if (f.isDirectory()) {
					dirAdapter.setDir(f);
					dir.setAdapter(dirAdapter);
					lblPath.setText(f.getAbsolutePath());
					btnBack.setEnabled(dirAdapter.getDevices().length > 1 || !dirAdapter.getRootDir().getAbsolutePath().equals(f.getAbsolutePath()));
					if (mode == 1) ok.setEnabled(false);
					else if (mode == 2) ok.setEnabled(true);
					else if (mode == 3) {
						if (fName.getText().toString().indexOf('.') == 0) {
							if (!showHidden || fName.getText().toString().substring(1).length() == 0)
								ok.setEnabled(false);
							else ok.setEnabled(true);
						} else if (fName.getText().toString().indexOf('+') == 0 || fName.getText().toString().indexOf('-') == 0 || illegalFilename(fName.getText().toString())) {
							ok.setEnabled(false);
						} else if (fName.getText().toString().length() > 0)
							ok.setEnabled(true);
						else ok.setEnabled(false);
					}
				} else if (f.isFile()) {
					if (mode == 0) {
						listener.onFileTouched(new File[]{f});
						fileDialog.dismiss();
					} else if (mode == 1) {
						ok.setEnabled(dirAdapter.getSelectedFiles().length > 0);
					} else if (mode == 3) {
						String fn = f.getName();
						if (fn.equals(fName.getText().toString()))
							if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
								ok.performClick();
							else
								ok.callOnClick();
						else
							fName.setText(fn);
					}
				}
			}
		});
		fName.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (fName.getText().toString().indexOf('.') == 0) {
					if (!showHidden) {
						ok.setEnabled(false);
						Toast.makeText(view.getContext(), R.string.cannot_create_hidden_files, Toast.LENGTH_SHORT).show();
					} else if (fName.getText().toString().substring(1).length() == 0)
						ok.setEnabled(false);
					else ok.setEnabled(true);
				} else if (fName.getText().toString().indexOf('+') == 0 || fName.getText().toString().indexOf('-') == 0) {
					ok.setEnabled(false);
					Toast.makeText(view.getContext(), R.string.filename_cannot_begin_with, Toast.LENGTH_SHORT).show();
				} else if (illegalFilename(fName.getText().toString())) {
					ok.setEnabled(false);
					Toast.makeText(view.getContext(), R.string.filename_cannot_contains, Toast.LENGTH_SHORT).show();
				} else if (fName.getText().toString().length() > 0) ok.setEnabled(true);
				else ok.setEnabled(false);
			}
		});
		fName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (ok.isEnabled())
					if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
						ok.performClick();
					else
						ok.callOnClick();
				return true;
			}
		});
	}


	/**
	 * Return an array contains file paths of all storage devices mounted.
	 *
	 * @param context        The parent Context.
	 * @param ignoreReadOnly This parameter decides whether read-only status of a device will be ignored, for example, most of systems prevents third part apps from writing to external SD cards.
	 * @return the file [ ]
	 */
	public static File[] getStorage(Context context, boolean ignoreReadOnly) {
		StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
		try {
			Method method = StorageManager.class.getDeclaredMethod(METHOD_GET_VOLUME_PATHS);
			method.setAccessible(true);
			Object result = method.invoke(storageManager);
			if (result instanceof String[]) {
				String[] pathArray = (String[]) result;
				StatFs statFs;
				File[] files = new File[pathArray.length];
				int i = 0;
				for (String path : pathArray) {
					File file = new File(path);
					if (!TextUtils.isEmpty(path) && file.exists()) {
						statFs = new StatFs(path);
						long v;
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
							v = statFs.getBlockCount() * statFs.getBlockSize();
						else v = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
						if (v != 0 && (ignoreReadOnly || file.canWrite())) {
							files[i] = file;
							i++;
						}
					}
				}
				File[] files1 = new File[i];
				System.arraycopy(files, 0, files1, 0, i);
				return files1;
			}
		} catch (Exception e) {
			e.printStackTrace();
			File externalFolder = Environment.getExternalStorageDirectory();
			if (externalFolder != null) {
				return new File[]{externalFolder};
			}
		}
		return null;
	}

	/**
	 * This FileDialogFilter accepts all kinds of files.
	 */
	public static final FileDialogFilter ALL = new FileDialogFilter("*", new String[]{"*"});

	private static boolean illegalFilename(CharSequence e) {
		String v = e.toString();
		for (int i = 0; i < 32; i++) if (v.indexOf(i) >= 0) return true;
		return v.indexOf('"') >= 0
				|| v.indexOf('*') >= 0
				|| v.indexOf('/') >= 0
				|| v.indexOf(':') >= 0
				|| v.indexOf('<') >= 0
				|| v.indexOf('>') >= 0
				|| v.indexOf('?') >= 0
				|| v.indexOf('\\') >= 0
				|| v.indexOf('|') >= 0
				|| v.indexOf(127) >= 0;
	}
}