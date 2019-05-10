/*
 * com.github.donmor.filedialog.lib.FileDialogFilter <= [P|FileDialog]
 * Last modified: 15:32:28 2019/05/10
 * Copyright (c) 2019 donmor
 */

package com.github.donmor.filedialog.lib;

/**
 * The File type filter for FileDialog.
 */
@SuppressWarnings("WeakerAccess")
public class FileDialogFilter {
	/**
	 * The name of the filter.
	 */
	public final String name;
	/**
	 * The extension names related.
	 */
	public final String[] extensions;

	private static final String ALL = "*";

	/**
	 * Instantiates a new File dialog filter.
	 *
	 * @param name       The name of the filter.
	 * @param extensions The extensions array. It should be formatted like { ".extension1", ".extension2", ... }. The extension can be like ".ext1.ext2". Use { "*" } for all types. The first extension in array will be used as default to create new files.
	 */
	public FileDialogFilter(String name, String[] extensions) {
		this.name = name;
		this.extensions = extensions;
	}

	/**
	 * Check whether a file matches extensions.
	 *
	 * @param filename The filename to check.
	 * @return         The value will be true if the extension of the file equals one of extensions, or false if the extension of the file equals nothing.
	 */
	public boolean meetExtensions(String filename) throws ArrayIndexOutOfBoundsException {
		if (extensions[0].equals(ALL)) return true;
		for (String e : extensions)
			if (filename.toLowerCase().endsWith(e)) return true;
		return false;
	}
}
