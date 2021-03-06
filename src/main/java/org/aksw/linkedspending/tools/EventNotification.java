package org.aksw.linkedspending.tools;

import java.text.SimpleDateFormat;

/**
 * Class to support a common format of EventNotification usable by both Downloader and Converter
 * (and other modules as well) which can also be used to create some statistical information.
 */
public class EventNotification
{
	/* Feel free to add more constants if needed */
	/* Type constants */

	public enum EventType {
		FINISHED_SINGLE_DOWNLOAD, FINISHED_COMPLETED_OWNLOAD, FINISHED_CONVERTING_SINGLE, FINISHED_CONVERTING_COMPLETE, FILE_NOT_FOUND, UNSUPPORTED_FILE_TYPE,
		OUT_OF_MEMORY, STARTED_SINGLE_DOWNLOAD, STARTED_COMPLETE_DOWNLOAD, STARTED_CONVERTING_SINGLE, STARTED_CONVERTING_COMPLETE, FINISHED_DOWNLOADING_DATASET, DOWNLOAD_STOPPED,
		/** pause requested for downloader */
		DOWNLOAD_PAUSED, DOWNLOAD_RESUMED, TOO_MANY_ERRORS, RUN_TIME_ERROR, STOPPED_CONVERTER, PAUSED_CONVERTER, RESUMED_CONVERTER, IO_ERROR
	}

	/* source constants */
	public enum EventSource {
		CONVERTER, DOWNLOADER, DOWNLOAD_CALLABLE
	}

	private long		time;
	private EventType	type;
	private EventSource	source;
	private String		note;

	/**
	 * Indicates if the event was successful or not.
	 * Not to be used for all events (only makes sense with types such as finishedDownloading,...)
	 */
	private boolean		success;

	/**
	 * Creates new EventNotification.
	 *
	 * @param type
	 *            the type of Event to be created<br>
	 *            finishedDownloadingSingle = 0<br>
	 *            finishedDownloadingComplete = 1<br>
	 *            finishedConvertingSingle = 2<br>
	 *            fileNotFound = 4<br>
	 *            unsupportedFileType = 5<br>
	 *            outOfMemory = 6<br>
	 *            startedDownloadingSingle = 7<br>
	 *            startedDownloadingComplete = 8<br>
	 *            startedConvertingSingle = 9<br>
	 *            startedConvertingComplete = 10<br>
	 *            downloadStopped = 11<br>
	 *            downloadPaused = 12<br>
	 *            downloadResumed = 13
	 *            <p>
	 * @param source
	 *            by what softwaremodul the event is caused<br>
	 *            Converter = 0<br>
	 *            Downloader = 1
	 */
	public EventNotification(EventType type, EventSource source)
	{
		time = System.currentTimeMillis();
		this.type = type;
		this.source = source;
		note = null;
	}

	/**
	 * Creates new EventNotification.
	 *
	 * @param type
	 *            the type of Event to be created<br>
	 *            finishedDownloadingSingle = 0<br>
	 *            finishedDownloadingComplete = 1<br>
	 *            finishedConvertingSingle = 2<br>
	 *            fileNotFound = 4<br>
	 *            unsupportedFileType = 5<br>
	 *            outOfMemory = 6<br>
	 *            startedDownloadingSingle = 7<br>
	 *            startedDownloadingComplete = 8<br>
	 *            startedConvertingSingle = 9<br>
	 *            startedConvertingComplete = 10<br>
	 *            downloadStopped = 11<br>
	 *            downloadPaused = 12<br>
	 *            downloadResumed = 13
	 *            <p>
	 * @param source
	 *            by what softwaremodul the event is caused<br>
	 *            Converter = 0<br>
	 *            Downloader = 1
	 */
	public EventNotification(EventType type, EventSource source, boolean success)
	{
		time = System.currentTimeMillis();
		this.type = type;
		this.source = source;
		this.success = success;
		note = null;
	}

	/**
	 * Creates new EventNotification.
	 *
	 * @param type
	 *            the type of Event to be created<br>
	 *            finishedDownloadingSingle = 0<br>
	 *            finishedDownloadingComplete = 1<br>
	 *            finishedConvertingSingle = 2<br>
	 *            fileNotFound = 4<br>
	 *            unsupportedFileType = 5<br>
	 *            outOfMemory = 6<br>
	 *            startedDownloadingSingle = 7<br>
	 *            startedDownloadingComplete = 8<br>
	 *            startedConvertingSingle = 9<br>
	 *            startedConvertingComplete = 10<br>
	 *            downloadStopped = 11<br>
	 *            downloadPaused = 12<br>
	 *            downloadResumed = 13
	 *            <p>
	 * @param source
	 *            by what softwaremodul the event is caused<br>
	 *            Converter = 0<br>
	 *            Downloader = 1
	 * @param note
	 *            Adds a specified note to the notification (e.g. type=finishedDownloadingDataset,
	 *            note="berlin_de"
	 */
	public EventNotification(EventType type, EventSource source, String note)
	{
		time = System.currentTimeMillis();
		this.type = type;
		this.source = source;
		this.note = note;
	}

	/**
	 * Creates new EventNotification.
	 *
	 * @param type
	 *            the type of Event to be created<br>
	 *            finishedDownloadingSingle = 0<br>
	 *            finishedDownloadingComplete = 1<br>
	 *            finishedConvertingSingle = 2<br>
	 *            fileNotFound = 4<br>
	 *            unsupportedFileType = 5<br>
	 *            outOfMemory = 6<br>
	 *            startedDownloadingSingle = 7<br>
	 *            startedDownloadingComplete = 8<br>
	 *            startedConvertingSingle = 9<br>
	 *            startedConvertingComplete = 10<br>
	 *            downloadStopped = 11<br>
	 *            downloadPaused = 12<br>
	 *            downloadResumed = 13
	 *            <p>
	 * @param source
	 *            by what softwaremodul the event is caused<br>
	 *            Converter = 0<br>
	 *            Downloader = 1
	 * @param note
	 *            Adds a specified note to the notification (e.g. type=finishedDownloadingDataset,
	 *            note="berlin_de"
	 * @param success
	 *            Whether the event was successful or not.
	 */
	public EventNotification(EventType type, EventSource source, String note, boolean success)
	{
		time = System.currentTimeMillis();
		this.type = type;
		this.source = source;
		this.note = note;
		this.success = success;
	}

	public long getTime()
	{
		return time;
	}

	public EventType getType()
	{
		return type;
	}

	public EventSource getSource()
	{
		return source;
	}

	/**
	 * Returns a String of following format: "time source type note success" (for withTime = true)
	 * or "source type" (withTime = false)
	 */
	public String getEventCode(boolean withTime)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm.ss");
		String t = sdf.format(time);

		return (withTime ? t + " " : "") + source + " " + type + " " + (note == null ? "" : note + " ")
				+ (success ? "successful" : "unsuccessful");

		/*
		 * String s, t;
		 * if (withTime)
		 * {
		 * DateFormat dF = new SimpleDateFormat("HH:mm.ss");
		 * t = dF.format(time);
		 * s = t + " " + source + " " + type;
		 * }
		 * else s = source + " " + type;
		 * if(note != null) s += " " + note;
		 * if(success) s += " sucessful";
		 * else s += " unsuccessful";
		 * return s;
		 */
	}
}