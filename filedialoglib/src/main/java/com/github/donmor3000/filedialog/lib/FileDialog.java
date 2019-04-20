package com.github.donmor3000.filedialog.lib;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.donmor3000.filedialog.lib.utils.FileDialogAdapter;
import com.github.donmor3000.filedialog.lib.utils.MimeTypeUtil;

import java.io.File;
import java.lang.reflect.Method;

/**
 * The class FileDialog provides a set of methods to open dialogs for file-operating. Make sure that WRITE_EXTERNAL_STORAGE permission of the app have been set to GRANTED if API level is higher than 22, and "android.defaultConfig.vectorDrawables.useSupportLibrary = true" should be added into build.gradle of the app if API level is lower than 21.
 */
public abstract class FileDialog {

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
	 * @param mimes    The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, String[] mimes, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 0, mimes, 1, false, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent   The parent Context.
	 * @param mimes    The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, String[] mimes, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 1, mimes, 1, false, false, listener);
	}

	/**
	 * Open a dialog to select a directory.
	 *
	 * @param parent   The parent Context.
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileSelectDirectory(final Context parent, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 2, null, 1, false, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent   The parent Context.
	 * @param mimes    The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, String[] mimes, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 3, mimes, 1, false, false, listener);
	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent     The parent Context.
	 * @param mimes      The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, String[] mimes, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 0, mimes, 1, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent     The parent Context.
	 * @param mimes      The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, String[] mimes, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 1, mimes, 1, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a directory.
	 *
	 * @param parent     The parent Context.
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileSelectDirectory(final Context parent, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 2, null, 1, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent     The parent Context.
	 * @param mimes      The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, String[] mimes, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 3, mimes, 1, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent   The parent Context.
	 * @param mimes    The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det      The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, String[] mimes, int det, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 0, mimes, det, false, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent   The parent Context.
	 * @param mimes    The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det      The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, String[] mimes, int det, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 1, mimes, det, false, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent   The parent Context.
	 * @param mimes    The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det      The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param listener The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, String[] mimes, int det, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 3, mimes, det, false, false, listener);
	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent     The parent Context.
	 * @param mimes      The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det        The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, String[] mimes, int det, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 0, mimes, det, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent     The parent Context.
	 * @param mimes      The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det        The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, String[] mimes, int det, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 1, mimes, det, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent     The parent Context.
	 * @param mimes      The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det        The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param showHidden This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener   The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, String[] mimes, int det, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, Environment.getExternalStorageDirectory(), 3, mimes, det, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, File startDirectory, String[] mimes, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 0, mimes, 1, false, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, File startDirectory, String[] mimes, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 1, mimes, 1, false, false, listener);
	}

	/**
	 * Open a dialog to select a directory.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSelectDirectory(final Context parent, File startDirectory, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 2, null, 1, false, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, File startDirectory, String[] mimes, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 3, mimes, 1, false, false, listener);
	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, File startDirectory, String[] mimes, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 0, mimes, 1, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, File startDirectory, String[] mimes, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 1, mimes, 1, showHidden, false, listener);
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
		fileDialog(parent, startDirectory, 2, null, 1, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, File startDirectory, String[] mimes, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 3, mimes, 1, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det            The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, File startDirectory, String[] mimes, int det, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 0, mimes, det, false, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det            The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, File startDirectory, String[] mimes, int det, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 1, mimes, det, false, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det            The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, File startDirectory, String[] mimes, int det, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 3, mimes, det, false, false, listener);
	}

	/**
	 * Open a dialog to select a single file to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det            The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpen(final Context parent, File startDirectory, String[] mimes, int det, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 0, mimes, det, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select multiple files to be opened.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det            The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileOpenMultiple(final Context parent, File startDirectory, String[] mimes, int det, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 1, mimes, det, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select a file path to be saved.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det            The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	public static void fileSave(final Context parent, File startDirectory, String[] mimes, int det, boolean showHidden, final OnFileTouchedListener listener) {
		fileDialog(parent, startDirectory, 3, mimes, det, showHidden, false, listener);
	}

	/**
	 * Open a dialog to select folder or files. All parameters are required using this method.
	 *
	 * @param parent         The parent Context.
	 * @param startDirectory The directory the dialog will start with. if invalid, the dialog will start with the default SD card directory.
	 * @param mode           This parameter decides the type of the dialog. 0 for fileOpen, 1 for fileOpenMultiple, 2 for fileSelectDirectory, and 3 for fileSave.
	 * @param mimes          The MIME types strings. Each String should be formatted like "type/subtype". Use "&#42;/&#42;" for all types. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in. If one of the strings matches none of common MIME types, it will be ignored, and "&#42;/&#42;" will be used if no valid string passed in.
	 * @param det            The detail level of mime-type filter. 0 for original MIME types strings, 1 for all of the extensions matches the MIME types, and 2 for both(not recommend because the filter spinner could be too long).
	 * @param showHidden     This parameter decides whether hidden(starts with '.') files could be shown or be created.
	 * @param ignoreReadOnly This parameter decides whether read-only status of a device will be ignored, for example, most of systems prevents third part apps from writing to external SD cards.
	 * @param listener       The call back that will be run when the dialog is closed.
	 */
	@SuppressWarnings("WeakerAccess")
	public static void fileDialog(final Context parent, File startDirectory, final int mode, String[] mimes, int det, final boolean showHidden, boolean ignoreReadOnly, final OnFileTouchedListener listener) {
		final View view = LayoutInflater.from(parent).inflate(R.layout.file_dialog, null);
		final String[] mimeTypes = MimeTypeUtil.trimMime(mimes);
		final EditText fName = view.findViewById(R.id.save_f_name);
		if (mode < 3) fName.setVisibility(View.GONE);
		final TextView lblPath = view.findViewById(R.id.lblPath);
		lblPath.setText(startDirectory.getAbsolutePath());
		Spinner spnExt = view.findViewById(R.id.spnExt);
		if (mode == 2) spnExt.setVisibility(View.GONE);
		else {
			ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(view.getContext(), R.layout.ext_slot, MimeTypeUtil.getDescriptions(mimeTypes, det));
			spinnerAdapter.setDropDownViewResource(R.layout.ext_slot);
			spnExt.setAdapter(spinnerAdapter);
			if (mimeTypes.length == 1) spnExt.setEnabled(false);
		}
		final RecyclerView dir = view.findViewById(R.id.diFileList);
		dir.setLayoutManager(new LinearLayoutManager(view.getContext()));
		final FileDialogAdapter dirAdapter = new FileDialogAdapter(view.getContext(), mimeTypes, startDirectory, mode == 1, mode == 2, showHidden, ignoreReadOnly);
		dir.setAdapter(dirAdapter);
		final Button btnBack = view.findViewById(R.id.btnBack);
		btnBack.setEnabled(dirAdapter.getDevices().length > 1);
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
						if (editText.getText().toString().startsWith(".")) {
							try {
								if (showHidden) {
									okx.setEnabled(false);
									Toast.makeText(view1.getContext(), R.string.cannot_create_hidden_files, Toast.LENGTH_SHORT).show();
								} else if (editText.getText().toString().substring(1).length() == 0)
									okx.setEnabled(false);
								else okx.setEnabled(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (editText.getText().toString().startsWith("+") || editText.getText().toString().startsWith("-")) {
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
					dirAdapter.setMimeIndex(position);
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
		ok.setEnabled(mode == 2);
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
					fn = MimeTypeUtil.formatFilename(fn, mimeTypes[dirAdapter.getMimeIndex()], -1);
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
						if (fName.getText().toString().startsWith(".")) {
							if (showHidden || fName.getText().toString().substring(1).length() == 0)
								ok.setEnabled(false);
							else ok.setEnabled(true);
						} else if (fName.getText().toString().startsWith("+") || fName.getText().toString().startsWith("-") || illegalFilename(fName.getText().toString())) {
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
					lblPath.setText("");
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
						if (fName.getText().toString().startsWith(".")) {
							if (showHidden || fName.getText().toString().substring(1).length() == 0)
								ok.setEnabled(false);
							else ok.setEnabled(true);
						} else if (fName.getText().toString().startsWith("+") || fName.getText().toString().startsWith("-") || illegalFilename(fName.getText().toString())) {
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
				if (fName.getText().toString().startsWith(".")) {
					if (showHidden) {
						ok.setEnabled(false);
						Toast.makeText(view.getContext(), R.string.cannot_create_hidden_files, Toast.LENGTH_SHORT).show();
					} else if (fName.getText().toString().substring(1).length() == 0)
						ok.setEnabled(false);
					else ok.setEnabled(true);
				} else if (fName.getText().toString().startsWith("+") || fName.getText().toString().startsWith("-")) {
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
			Method method = StorageManager.class.getDeclaredMethod("getVolumePaths");
			method.setAccessible(true);
			Object result = method.invoke(storageManager);
			if (result instanceof String[]) {
				String[] pathArray = (String[]) result;
				StatFs statFs;
				File[] files = new File[pathArray.length];
				int i = 0;
				for (String path : pathArray) {
					File file = new File(path);
					System.out.println(file.getAbsolutePath());
					if (!TextUtils.isEmpty(path) && file.exists()) {
						statFs = new StatFs(path);
						long v;
						if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2)
							v = statFs.getBlockCount() * statFs.getBlockSize();
						else v = statFs.getBlockCountLong() * statFs.getBlockSizeLong();
						if (v != 0 && (ignoreReadOnly || file.canWrite())) {
							System.out.println(file.canWrite());
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