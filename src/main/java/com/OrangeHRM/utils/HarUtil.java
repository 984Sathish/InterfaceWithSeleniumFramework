package com.OrangeHRM.utils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import com.OrangeHRM.data.HarSummary;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.core.har.HarLog;

public class HarUtil {

	/**
	 * Takes a Har object and calculated the total size of all files in it, the
	 * amount of http calls made, and total time to make all the calls.
	 * 
	 * @param har the Har file to be summarized
	 * @return HarSummary
	 */
	public static HarSummary generateHarSummary(Har har) {
		HarLog log = har.getLog();
		List<HarEntry> harEntries = log.getEntries();
		Long totalSize = 0L;
		int callCount = 0;
		long totalTime = 0;
		for (HarEntry entry : harEntries) {
			callCount++;
			// System.out.println( "entry:" + entry.getRequest().getUrl() );
			totalSize += entry.getResponse().getBodySize();
			totalTime += entry.getTime(TimeUnit.MILLISECONDS);
		}
		return new HarSummary((double) totalSize / 1024, callCount, totalTime);
	}
}
