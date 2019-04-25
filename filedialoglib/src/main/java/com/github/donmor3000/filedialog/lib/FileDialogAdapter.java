package com.github.donmor3000.filedialog.lib;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

//import com.github.donmor3000.filedialog.lib.utils.MimeTypeUtil;

import java.io.File;
import java.io.FileFilter;

class FileDialogAdapter extends RecyclerView.Adapter<FileDialogAdapter.FileViewHolder> {

	private File currentDir;
	private final File rootDir;
	private boolean[] selected;
	private File[] files, dirs, devices;
	private boolean enRoot;
	private final boolean multiSelect, dirOnly, showHidden, ignoreReadOnly;
	private int filterIndex;
	private final Context context;
	final String[] mimeTypes;
	final FileDialogFilter[] filters;
	private final LayoutInflater inflater;

	//	FileDialogAdapter(Context context, String[][] filters, int filterIndex, String[] mimeTypes, File dir, boolean multiSelect, boolean dirOnly, boolean showHidden, boolean ignoreReadOnly) {
	FileDialogAdapter(Context context, FileDialogFilter[] filters, int filterIndex, String[] mimeTypes, File dir, boolean multiSelect, boolean dirOnly, boolean showHidden, boolean ignoreReadOnly) {
		this.context = context;
		this.multiSelect = multiSelect;
		this.dirOnly = dirOnly;
		this.showHidden = showHidden;
		this.ignoreReadOnly = ignoreReadOnly;
		this.mimeTypes = mimeTypes;
		this.filters = filters;
		this.filterIndex = filterIndex;
		inflater = LayoutInflater.from(context);
		try {
			currentDir = dir;
			if (!currentDir.isDirectory()) throw new Exception();
		} catch (Exception e) {
			currentDir = Environment.getExternalStorageDirectory();
		}
		System.out.println(currentDir.getAbsolutePath());
		rootDir = Environment.getExternalStorageDirectory();
		enRoot = false;
		dirs = sortFile(getDirs());
		if (dirOnly) files = new File[0];
		else files = sortFile(getFiles());
		if (files != null) selected = new boolean[files.length];
		devices = FileDialog.getStorage(context.getApplicationContext(), ignoreReadOnly);
	}

	class FileViewHolder extends RecyclerView.ViewHolder {
		private final Button cbD, cbF, cbR;
		private final CheckBox cbMF;

		FileViewHolder(View itemView) {
			super(itemView);
			cbD = itemView.findViewById(R.id.c_buttonD);
			cbF = itemView.findViewById(R.id.c_buttonF);
			cbMF = itemView.findViewById(R.id.c_buttonMF);
			cbR = itemView.findViewById(R.id.c_buttonR);
		}
	}

	@Override
	@NonNull
	public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		return new FileViewHolder(inflater.inflate(R.layout.file_slot, parent, false));
	}

	@Override
	public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
		final int pos = position;
		if (enRoot) {
			holder.cbR.setText(devices[pos].getAbsolutePath());
			holder.cbR.setVisibility(View.VISIBLE);
		} else {
			if (pos < dirs.length) {
				{
					holder.cbD.setText(dirs[pos].getName());
					holder.cbD.setVisibility(View.VISIBLE);
				}
			} else if (pos < dirs.length + files.length) {
				if (multiSelect) {
					holder.cbMF.setText(files[pos - dirs.length].getName());
					holder.cbMF.setChecked(false);
					holder.cbMF.setVisibility(View.VISIBLE);
				} else {
					holder.cbF.setText(files[pos - dirs.length].getName());
					holder.cbF.setVisibility(View.VISIBLE);
				}
			}
		}
		View.OnClickListener ocl = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mItemClickListener.onItemClick(pos);
			}
		};
		CompoundButton.OnCheckedChangeListener orl = new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				selected[pos - dirs.length] = isChecked;
				mItemClickListener.onItemClick(pos);
			}
		};
		holder.cbD.setOnClickListener(ocl);
		holder.cbF.setOnClickListener(ocl);
		holder.cbMF.setOnCheckedChangeListener(orl);
		holder.cbR.setOnClickListener(ocl);
	}

	@Override
	public int getItemCount() {
		if (enRoot) return devices.length;
		else {
			int d = 0, f = 0;
			if (dirs != null) d = dirs.length;
			if (files != null) f = files.length;
			return d + f;
		}
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	private ItemClickListener mItemClickListener;

	public interface ItemClickListener {
		void onItemClick(int position);
	}

	public void setOnItemClickListener(ItemClickListener itemClickListener) {
		this.mItemClickListener = itemClickListener;

	}

	private File[] getDirs() {
		return currentDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return !(pathname.isHidden() && showHidden) && pathname.isDirectory();
			}
		});
	}

	private File[] getFiles() {
		return currentDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (mimeTypes != null)
					return !(pathname.isHidden() && showHidden) && pathname.isFile() && MimeTypeUtil.meetsMimeTypes(pathname.getName(), mimeTypes[filterIndex]);
				else {
					return !(pathname.isHidden() && showHidden) && pathname.isFile() && filters[filterIndex].meetExtensions(pathname.getName());
				}
			}
		});
	}

	private File[] sortFile(File[] src) {
		if (src == null || src.length == 0) return src;
		for (int i = 0; i < src.length; i++) {
			for (int j = 0; j < src.length - i - 1; j++) {
				if (chrSort(src[j + 1].getName(), src[j].getName())) {
					File temp = src[j + 1];
					src[j + 1] = src[j];
					src[j] = temp;
				}
			}
		}
		return src;
	}

	private Boolean chrSort(String s1, String s2) {
		char[] sa1 = s1.toCharArray();
		char[] sa2 = s2.toCharArray();
		for (int i = 0; i < Math.min(sa1.length, sa2.length); i++) {
			if (sa1[i] < sa2[i]) return true;
			else if (sa1[i] > sa2[i]) return false;
		}
		return false;
	}

	public void setFilterIndex(int index) {
		filterIndex = index;
	}

	public int getFilterIndex() {
		return filterIndex;
	}

	public File getFile(int position) {
		if (enRoot) return devices[position];
		else {
			if (position < dirs.length) {
				return dirs[position];
			} else {
				return files[position - dirs.length];
			}
		}
	}

	public File[] getSelectedFiles() {
		if (enRoot) return new File[0];
		else {
			File[] files1 = new File[files.length];
			int i = 0;
			for (int j = 0; j < selected.length; j++)
				if (selected[j]) {
					files1[i] = files[j];
					i++;
				}
			File[] files2 = new File[i];
			System.arraycopy(files1, 0, files2, 0, i);
			return files2;
		}
	}

	public File[] getDevices() {
		return devices;
	}

	public File getCurrentDir() {
		return currentDir;
	}

	public File getRootDir() {
		return rootDir;
	}

	public File getParentDir() {
		for (File dev : devices) {
			if (dev.equals(currentDir)) {
				return null;
			}
		}
		return currentDir.getParentFile();
	}

	public void setDir(File dir) {
		try {
			currentDir = dir;
			if (!currentDir.isDirectory()) throw new Exception();
		} catch (Exception e) {
			currentDir = rootDir;
		}
		dirs = sortFile(getDirs());
		if (dirOnly) files = new File[0];
		else files = sortFile(getFiles());
		if (files != null) selected = new boolean[files.length];
		enRoot = false;
	}

	public void setRoot() {
		devices = FileDialog.getStorage(context.getApplicationContext(), ignoreReadOnly);
		selected = new boolean[0];
		enRoot = true;
	}

	public void reload() {
		if (!enRoot) {
			dirs = sortFile(getDirs());
			if (dirOnly) files = new File[0];
			else files = sortFile(getFiles());
			if (files != null) selected = new boolean[files.length];
		}else{
			devices = FileDialog.getStorage(context.getApplicationContext(), ignoreReadOnly);
			selected = new boolean[0];		}
	}
}
