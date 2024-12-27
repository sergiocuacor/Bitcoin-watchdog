package com.bitcoinwatchdog.metrics.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;


public abstract class BaseMetric {

	@JsonProperty("d")
	private String date;
	private String unixTs;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUnixTs() {
		return unixTs;
	}

	public void setUnixTs(String unixTs) {
		this.unixTs = unixTs;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (!(obj instanceof BaseMetric))
			return false;

		BaseMetric that = (BaseMetric) obj;
		return Objects.equals(date, that.date) && Objects.equals(unixTs, that.unixTs);

	}

	@Override
	public String toString() {

		return "BaseMetric{" + "date='" + date + '\'' + ", unixTs='" + unixTs + '\'' + '}';
	}

}
