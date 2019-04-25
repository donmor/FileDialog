package com.github.donmor3000.filedialog.lib;

/**
 * The File type filter for FileDialog.
 */
@SuppressWarnings("WeakerAccess")
public class FileDialogFilter {
	/**
	 * The name of the filter.
	 */
	public String name;
	/**
	 * The extension names related.
	 */
	public String[] extensions;

	/**
	 * Instantiates a new File dialog filter.
	 *
	 * @param name       The name of the filter.
	 * @param extensions The extensions array. It should be formatted like { ".extension1", ".extension2", ... }. The extension can be like ".ext1.ext2". Use { "*" } for all types.
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
	public boolean meetExtensions(String filename) {
		if (extensions[0].equals("*")) return true;
		for (String e : extensions)
			if (filename.toLowerCase().endsWith(e)) return true;
		return false;
	}

	/**
	 * Format filename string.
	 *
	 * @param filename The original filename.
	 * @param index    The index in extensions. Accepts value that < 0 (will index from the end of extension). It will be reset to 0 if an invalid value passed in.
	 * @return         The new filename string.
	 */
	public String formatFilename(String filename, int index) {
		if (index < 0 && extensions.length - index >= 0) index = extensions.length - index;
		else if (index >= extensions.length || extensions.length - index < 0) index = 0;
		if (meetExtensions(filename)) return filename;
		else return filename + extensions[index];
	}
}
