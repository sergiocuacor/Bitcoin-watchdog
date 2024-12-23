package com.bitcoinwatchdog.metrics.model;

import java.util.Objects;

public class PuellMultiple extends BaseMetric{

	private String puellMultiple;

	public String getPuellMultiple() {
		return puellMultiple;
	}

	public void setPuellMultiple(String puellMultiple) {
		this.puellMultiple = puellMultiple;
	}

	@Override
	public int hashCode() {
		
		return Objects.hash(super.hashCode(), puellMultiple);
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) return true;
        if (!(obj instanceof PuellMultiple)) return false;
        if (!super.equals(obj)) return false;
        PuellMultiple that = (PuellMultiple) obj;
        return Objects.equals(puellMultiple, that.puellMultiple);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "PuellMultiple{" +
        "date='" + getDate() + '\'' +
        ", unixTs='" + getUnixTs() + '\'' +
        ", puellMultiple='" + puellMultiple + '\'' +
        '}';
	}

	 
}
