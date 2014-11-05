package org.aksw.linkedspending;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import lombok.extern.java.Log;
import org.aksw.linkedspending.exception.DataSetDoesNotExistException;
import org.aksw.linkedspending.tools.PropertiesLoader;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

/** Represents information about source datasets from OpenSpending and offers utility methods to collect it from the OpenSpending JSON API.*/
@Log
public class OpenSpendingDatasetInfo
{
	public final String name;
	public final Instant created;
	public final Instant modified;

	private OpenSpendingDatasetInfo(String name, Instant created, Instant modified)
	{
		this.name = name;
		this.created = created;
		this.modified = modified;
	}

	private static final File DATASETS_CACHED	 = new File("cache/datasets.json");
	private static final ObjectMapper				m				= new ObjectMapper();
	//	private static final Set<String>	unfinishedDatasets	= new HashSet<>();
	//	private static final Set<String>	finishedDatasets	= new HashSet<>();
	private static final Set<String>	emptyDatasets	= Collections.synchronizedSet(new HashSet<String>());
	private static final File emptyDatasetFile	= new File("cache/emptydatasets.ser");

	static protected TreeMap<String,OpenSpendingDatasetInfo> datasetInfos = new TreeMap<>();

	/**
	 *
	 * loads the names of datasets(JSON-files) <br>
	 * if #datasetNames already exists, return them<br>
	 * if cache-file exists, load datasets from cache-file<br>
	 * if cache-file does not exist, load from openspending and write cache-file

	 * @return a set containing the names of all JSON-files
	 * @throws IOException
	 * - if one of many files can't be read from or written to
	 * @see JsonDownloader.getDatasetNamesFresh() */
	public static synchronized SortedMap<String,OpenSpendingDatasetInfo> getDatasetInfosCached()
	{
		return getDatasetInfos(false);
	}

	/** get fresh dataset names from openspending and update the cash.
	 * @see JsonDownloader.getDatasetNamesCached()*/
	public static synchronized SortedMap<String,OpenSpendingDatasetInfo> getDatasetInfosFresh()
	{
		return getDatasetInfos(true);
	}

	// todo does the cache file get updated once in a while? if not functionality is needed
	/** @param readCache read datasets from cache (may be outdated but faster) */
	private static synchronized SortedMap<String,OpenSpendingDatasetInfo> getDatasetInfos(boolean readCache)
	{
		try
		{
			JsonNode datasets = null;

			if(readCache)
			{
				if (!datasetInfos.isEmpty()) return datasetInfos;
				if(DATASETS_CACHED.exists()) {	datasets = m.readTree(DATASETS_CACHED);}
			} else
			{
				datasetInfos.clear();
			}
			// either caching didn't work or it is disabled
			if(datasets==null)
			{
				datasets = m.readTree(PropertiesLoader.urlDatasets);
				m.writeTree(new JsonFactory().createGenerator(DATASETS_CACHED, JsonEncoding.UTF8), datasets);
			}

			ArrayNode datasetArray = (ArrayNode) datasets.get("datasets");
			log.info(datasetArray.size() + " datasets available. " + emptyDatasets.size() + " marked as empty, "
					+ (datasetArray.size() - emptyDatasets.size()) + " remaining.");
			for (int i = 0; i < datasetArray.size(); i++)
			{
				JsonNode datasetJson = datasetArray.get(i);
				String name  = datasetJson.get("name").textValue();
				datasetInfos.put(name,new OpenSpendingDatasetInfo(
						name,
						Instant.parse(datasetJson.get("timestamps").get("created").asText()+'Z'),
						Instant.parse(datasetJson.get("timestamps").get("last_modified").asText()+'Z')));
			}
			return datasetInfos;
		}
		catch(IOException e) {throw new RuntimeException(e);}
	}

	/** fresh dataset information from OpenSpending about a single dataset	 */
	public static OpenSpendingDatasetInfo osDatasetInfo(String datasetName) throws DataSetDoesNotExistException
	{
		try
		{
			JsonNode datasetJson = m.readTree(PropertiesLoader.prefixOpenSpending);
			return new OpenSpendingDatasetInfo(datasetName,
					Instant.parse(datasetJson.get("timestamps").get("created").asText()+'Z'),
					Instant.parse(datasetJson.get("timestamps").get("last_modified").asText()+'Z'));
		}
		catch (IOException e)
		{
			if(e.getMessage().contains("HTTP response code 404")) {throw new DataSetDoesNotExistException(datasetName);}
			throw new RuntimeException("Error getting OpenSpending dataset info for dataset '"+datasetName+"'",e);
		}
	}


}