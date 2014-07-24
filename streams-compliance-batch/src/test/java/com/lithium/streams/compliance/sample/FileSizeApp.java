package com.lithium.streams.compliance.sample;

import java.io.File;

import org.apache.log4j.Logger;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class FileSizeApp {
	private static final Logger LOG = Logger.getLogger(FileSizeApp.class);

	private final EventBus eventBus = new EventBus("FileSizeEventBus");

	private long filesPending;
	private long totalSize;

	private long start = System.nanoTime();

	private void process(File file) {
		eventBus.register(this);

		eventBus.post(new ProcessFileEvent(file));
	}

	@Subscribe
	public void processFile(ProcessFileEvent e) {
		filesPending++;
		eventBus.post(new CalculateSizeEvent(e.getFile()));

		if (LOG.isDebugEnabled()) {
			LOG.debug(filesPending + ": " + e.getFile().getAbsolutePath());
		}
	}

	@Subscribe
	public void calculateSize(CalculateSizeEvent e) {
		long size = 0;
		File file = e.getFile();

		if (file.isFile()) {
			size = file.length();
		} else {
			File[] children = file.listFiles();

			if (children != null) {
				for (File child : children)
					if (child.isFile()) {
						size += child.length();
					} else {
						eventBus.post(new ProcessFileEvent(child));
					}
			}
		}

		eventBus.post(new FileSizeEvent(size));
	}

	@Subscribe
	public void fileSize(FileSizeEvent e) {
		totalSize += e.getSize();
		filesPending--;

		LOG.info(filesPending + ": " + e.getSize() + ", " + totalSize);

		if (filesPending == 0) {
			System.out.println("Total size: " + totalSize);
			System.out.println("Time taken (s): " + (System.nanoTime() - start) / 1.0e9);

			System.exit(0);
		}
	}

	private static class ProcessFileEvent {
		private final File file;

		public ProcessFileEvent(File file) {
			this.file = file;
		}

		public File getFile() {
			return file;
		}
	}

	private static class CalculateSizeEvent {
		private final File file;

		public CalculateSizeEvent(File file) {
			this.file = file;
		}

		public File getFile() {
			return file;
		}
	}

	private static class FileSizeEvent {
		private final long size;

		public FileSizeEvent(long size) {
			this.size = size;
		}

		public long getSize() {
			return size;
		}
	}

	public static void main(String[] args) {
		final String fileName = args[0];
		final FileSizeApp app = new FileSizeApp();

		System.out.println("Calculating file size for: " + fileName);
		app.process(new File(fileName));
	}
}