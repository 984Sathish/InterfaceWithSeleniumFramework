package com.OrangeHRM.data;

/**
 * HarSummary consists of retrieving total PayLoadsize,call count, total Load
 * time
 */
public class HarSummary {
	private double totalPayloadSize;
	private int callCount;
	private long totalLoadTime;

	public HarSummary(double totalPayloadSize, int callCount, long totalLoadTime) {
		this.totalPayloadSize = totalPayloadSize;
		this.callCount = callCount;
		this.totalLoadTime = totalLoadTime;
	}

	public double getTotalPayloadSize() {
		return totalPayloadSize;
	}

	public long getTotalLoadTime() {
		return totalLoadTime;
	}

	public int getCallCount() {
		return callCount;
	}
}
