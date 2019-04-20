package com.github.donmor3000.filedialog.lib.utils;

public abstract class MimeTypeUtil {
	public static String getMIMEType(String filePath) {
		String type = "*/*";
		int dotIndex = filePath.lastIndexOf('.');
		if (dotIndex < 0) {
			return type;
		}
		String end = filePath.substring(dotIndex).toLowerCase();
		for (String[] mime : MIME_MapTable)
			if (end.equals(mime[1])) type = mime[0];
		return type;
	}

	public static String[] trimMime(String[] mimes) {
		if (mimes == null) return new String[]{"*/*"};
		String[] u = new String[MIME_MapTable.length];
		int i = 0;
		for (String p : mimes) {
			if (p != null) {
				int x = p.indexOf('/') + 1;
				if (x < p.length() && p.charAt(x) == '*') {
					for (String[] e : MIME_MapTable) {
						if (e[0].startsWith(p.substring(0, x))) {
							u[i] = p;
							i++;
							break;
						}
					}
				} else {
					for (String[] e : MIME_MapTable) {
						if (e[0].equals(p)) {
							u[i] = p;
							i++;
							break;
						}
					}

				}
			}
		}
		if (i == 0) return new String[]{"*/*"};
		String[] v = new String[i];
		System.arraycopy(u, 0, v, 0, i);
		return v;
	}

	@SuppressWarnings("unused")
	private static String[] trimMime(String mime) {
		return trimMime(new String[]{mime});
	}

	private static String[] getExtensions(String mime) {
		String[] m = new String[MIME_MapTable.length];
		int k = 0;
		if (mime != null && !mime.equals("")) {
			int w = mime.indexOf('/') + 1;
			if (mime.charAt(w) == '*')
				for (String[] i : MIME_MapTable) {
					if (i[0].startsWith(mime.substring(0, w))) {
						m[k] = i[1];
						k++;
					}
				}
			else
				for (String[] i : MIME_MapTable) {
					if (i[0].equals(mime)) {
						m[k] = i[1];
						k++;
					}
				}
		}
		if (k == 0) return new String[]{".*"};
		String[] x = new String[k];
		System.arraycopy(m, 0, x, 0, k);
		return x;
	}

	public static String[] getDescriptions(String[] mime, int det) {
		if (det == 0) return mime;
		String[] m = new String[mime.length];
		int k = 0;
		for (String mim : mime) {
			if (mim != null && !mim.equals("")) {
				String[] r = getExtensions(mim);
				StringBuilder t = new StringBuilder();
				for (String i : r) {
					t.append(i).append(';');
				}
				String v = t.toString();
				if (v.length() > 0 && det < 2)
					v = v.substring(0, v.length() - 1);
				else if (v.length() > 0)
					v = v.substring(0, v.length() - 1) + "(" + mim + ")";
				if (v.length() > 0) {
					m[k] = v;
					k++;
				}
			}
		}
		if (k == 0 && det > 1) return new String[]{".*(*/*)"};
		else if (k == 0) return new String[]{".*"};
		String[] x = new String[k];
		System.arraycopy(m, 0, x, 0, k);
		return x;
	}

	public static boolean meetsMimeTypes(String fn, String mime) {
		for (String s : getExtensions(mime))
			if (s.equals(".*") || fn.toLowerCase().endsWith(s)) return true;
		return false;
	}

	public static String formatFilename(String fn, String mimes, int index) {
		if (mimes.charAt(mimes.indexOf('/') + 1) == '*') return fn;
		String[] ext = getExtensions(mimes);
		for (String ex : ext)
			if (fn.endsWith(ex)) return fn;
		if (ext.length > 0) {
			if (index == -1)
				return fn + ext[ext.length - 1];
			else if (index >= 0 && index < ext.length)
				return fn + ext[index];
			else
				return fn + ext[0];
		}
		return fn;
	}

	public static void traceAll() {
		for (String[] i : MIME_MapTable) {
			System.out.println(i[0]);
			System.out.println(i[1]);
		}
	}

	private static final String[][] MIME_MapTable =
			{
					{"application/cu-seeme", ".csm"},
					{"application/cu-seeme", ".cu"},
					{"application/dsptype", ".tsp"},
					{"application/excel", ".xls"},
					{"application/futuresplash", ".spl"},
					{"application/mac-binhex40", ".hqx"},
					{"application/msword", ".doc"},
					{"application/msword", ".dot"},
					{"application/octet-stream", ".bin"},
					{"application/oda", ".oda"},
					{"application/pdf", ".pdf"},
					{"application/pgp-signature", ".pgp"},
					{"application/postscript", ".ai"},
					{"application/postscript", ".eps"},
					{"application/postscript", ".ps"},
					{"application/powerpoint", ".ppt"},
					{"application/rtf", ".rtf"},
					{"application/wordperfect5.1", ".wp5"},
					{"application/x-123", ".wk"},
					{"application/x-bcpio", ".bcpio"},
					{"application/x-chess-pgn", ".pgn"},
					{"application/x-cpio", ".cpio"},
					{"application/x-debian-package", ".deb"},
					{"application/x-director", ".dcr"},
					{"application/x-director", ".dir"},
					{"application/x-director", ".dxr"},
					{"application/x-dvi", ".dvi"},
					{"application/x-font", ".gsf"},
					{"application/x-font", ".pcf"},
					{"application/x-font", ".pcf.Z"},
					{"application/x-font", ".pfa"},
					{"application/x-font", ".pfb"},
					{"application/x-gtar", ".gtar"},
					{"application/x-gtar", ".tgz"},
					{"application/x-hdf", ".hdf"},
					{"application/x-httpd-php", ".php"},
					{"application/x-httpd-php", ".pht"},
					{"application/x-httpd-php", ".phtml"},
					{"application/x-httpd-php3", ".php3"},
					{"application/x-httpd-php3-preprocessed", ".php3p"},
					{"application/x-httpd-php3-source", ".phps"},
					{"application/x-java", ".class"},
					{"application/x-latex", ".latex"},
					{"application/x-maker", ".book"},
					{"application/x-maker", ".fb"},
					{"application/x-maker", ".fbdoc"},
					{"application/x-maker", ".fm"},
					{"application/x-maker", ".frame"},
					{"application/x-maker", ".frm"},
					{"application/x-maker", ".maker"},
					{"application/x-mif", ".mif"},
					{"application/x-msdos-program", ".bat"},
					{"application/x-msdos-program", ".com"},
					{"application/x-msdos-program", ".dll"},
					{"application/x-msdos-program", ".exe"},
					{"application/x-netcdf", ".cdf"},
					{"application/x-netcdf", ".nc"},
					{"application/x-ns-proxy-autoconfig", ".pac"},
					{"application/x-object", ".o"},
					{"application/x-perl", ".pl"},
					{"application/x-perl", ".pm"},
					{"application/x-shar", ".shar"},
					{"application/x-shockwave-flash", ".swf"},
					{"application/x-shockwave-flash", ".swfl"},
					{"application/x-stuffit", ".sit"},
					{"application/x-sv4cpio", ".sv4cpio"},
					{"application/x-sv4crc", ".sv4crc"},
					{"application/x-tar", ".tar"},
					{"application/x-tex-gf", ".gf"},
					{"application/x-texinfo", ".texi"},
					{"application/x-texinfo", ".texinfo"},
					{"application/x-tex-pk", ".pk"},
					{"application/x-tex-pk", ".PK"},
					{"application/x-trash", ".%"},
					{"application/x-trash", ".~"},
					{"application/x-trash", ".bak"},
					{"application/x-trash", ".old"},
					{"application/x-trash", ".sik"},
					{"application/x-troff", ".roff"},
					{"application/x-troff", ".t"},
					{"application/x-troff", ".tr"},
					{"application/x-troff-man", ".man"},
					{"application/x-troff-me", ".me"},
					{"application/x-troff-ms", ".ms"},
					{"application/x-ustar", ".ustar"},
					{"application/x-wais-source", ".src"},
					{"application/x-wingz", ".wz"},
					{"application/zip", ".zip"},
					{"audio/basic", ".au"},
					{"audio/basic", ".snd"},
					{"audio/midi", ".mid"},
					{"audio/midi", ".midi"},
					{"audio/mpeg", ".mp2"},
					{"audio/mpeg", ".mp3"},
					{"audio/mpeg", ".mpega"},
					{"audio/mpeg", ".mpga"},
					{"audio/mpegurl", ".m3u"},
					{"audio/x-aiff", ".aif"},
					{"audio/x-aiff", ".aifc"},
					{"audio/x-aiff", ".aiff"},
					{"audio/x-gsm", ".gsm"},
					{"audio/x-pn-realaudio", ".ra"},
					{"audio/x-pn-realaudio", ".ram"},
					{"audio/x-pn-realaudio", ".rm"},
					{"audio/x-pn-realaudio-plugin", ".rpm"},
					{"audio/x-wav", ".wav"},
					{"image/gif", ".gif"},
					{"image/ief", ".ief"},
					{"image/jpeg", ".jpe"},
					{"image/jpeg", ".jpeg"},
					{"image/jpeg", ".jpg"},
					{"image/png", ".png"},
					{"image/tiff", ".tif"},
					{"image/tiff", ".tiff"},
					{"image/x-cmu-raster", ".ras"},
					{"image/x-ms-bmp", ".bmp"},
					{"image/x-portable-anymap", ".pnm"},
					{"image/x-portable-bitmap", ".pbm"},
					{"image/x-portable-graymap", ".pgm"},
					{"image/x-portable-pixmap", ".ppm"},
					{"image/x-rgb", ".rgb"},
					{"image/x-xbitmap", ".xbm"},
					{"image/x-xpixmap", ".xpm"},
					{"image/x-xwindowdump", ".xwd"},
					{"text/comma-separated-values", ".csv"},
					{"text/html", ".htm"},
					{"text/html", ".html"},
					{"text/mathml", ".mml"},
					{"text/plain", ".txt"},
					{"text/richtext", ".rtx"},
					{"text/tab-separated-values", ".tsv"},
					{"text/x-c++hdr", ".h++"},
					{"text/x-c++hdr", ".hh"},
					{"text/x-c++hdr", ".hpp"},
					{"text/x-c++hdr", ".hxx"},
					{"text/x-c++src", ".c++"},
					{"text/x-c++src", ".cc"},
					{"text/x-c++src", ".cpp"},
					{"text/x-c++src", ".cxx"},
					{"text/x-chdr", ".h"},
					{"text/x-csh", ".csh"},
					{"text/x-csrc", ".c"},
					{"text/x-java", ".java"},
					{"text/x-moc", ".moc"},
					{"text/x-pascal", ".p"},
					{"text/x-pascal", ".pas"},
					{"text/x-setext", ".etx"},
					{"text/x-sh", ".sh"},
					{"text/x-tcl", ".tcl"},
					{"text/x-tcl", ".tk"},
					{"text/x-tex", ".cls"},
					{"text/x-tex", ".ltx"},
					{"text/x-tex", ".sty"},
					{"text/x-tex", ".tex"},
					{"text/x-vcalendar", ".vcs"},
					{"text/x-vcard", ".vcf"},
					{"video/dl", ".dl"},
					{"video/fli", ".fli"},
					{"video/gl", ".gl"},
					{"video/mpeg", ".mpe"},
					{"video/mpeg", ".mpeg"},
					{"video/mpeg", ".mpg"},
					{"video/quicktime", ".mov"},
					{"video/quicktime", ".qt"},
					{"video/x-ms-asf", ".asf"},
					{"video/x-ms-asf", ".asx"},
					{"video/x-msvideo", ".avi"},
					{"video/x-sgi-movie", ".movie"},
					{"x-world/x-vrml", ".vrm"},
					{"x-world/x-vrml", ".vrml"},
					{"x-world/x-vrml", ".wrl"},
					{"*/*", ".*"}
			};
}
