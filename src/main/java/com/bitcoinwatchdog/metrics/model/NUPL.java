
package com.bitcoinwatchdog.metrics.model;

import java.util.Objects;

public class NUPL extends BaseMetric {
    private String nupl;

    public String getNupl() {
        return nupl;
    }

    public void setNupl(String nupl) {
        this.nupl = nupl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NUPL)) return false;
        if (!super.equals(o)) return false;
        NUPL that = (NUPL) o;
        return Objects.equals(nupl, that.nupl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), nupl);
    }

    @Override
    public String toString() {
        return "NUPL{" +
                "date='" + getDate() + '\'' +
                ", unixTs='" + getUnixTs() + '\'' +
                ", nupl='" + nupl + '\'' +
                '}';
    }
}