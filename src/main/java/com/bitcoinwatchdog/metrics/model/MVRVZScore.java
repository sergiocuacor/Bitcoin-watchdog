package com.bitcoinwatchdog.metrics.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MVRVZScore extends BaseMetric {

	@JsonProperty("mvrvZscore")
	private String mvrvz;

	public String getMvrvz() {
		return mvrvz;
	}

	public void setMvrvz(String mvrvz) {
		this.mvrvz = mvrvz;
	}

	@Override
	public int hashCode() {

		return Objects.hash(super.hashCode(), mvrvz);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof MVRVZScore))
			return false;
		if (!super.equals(obj))
			return false;
		MVRVZScore that = (MVRVZScore) obj;
		return Objects.equals(mvrvz, that.mvrvz);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "MVRV-Z Score{" + "date='" + getDate() + '\'' + ", unixTs='" + getUnixTs() + '\'' + ", MVRV-Z='" + mvrvz
				+ '\'' + '}';
	}

}
