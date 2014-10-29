package org.aksw.linkedspending.downloader;

import java.io.IOException;
import org.aksw.linkedspending.exception.MissingDataException;
import org.aksw.linkedspending.job.Job;
import org.aksw.linkedspending.job.Job.DataSetDoesNotExistException;
import org.junit.Test;

public class DownloadCallableTest
{

	@Test public void testCall() throws IOException, InterruptedException, MissingDataException, DataSetDoesNotExistException
	{
		new DownloadCallable("2013", Job.forDataset("2013")).call();
		new DownloadCallable("2012_tax", Job.forDataset("2012_tax")).call();
	}

}
