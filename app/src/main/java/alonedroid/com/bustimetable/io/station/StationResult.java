package alonedroid.com.bustimetable.io.station;

import lombok.Getter;

public class StationResult {

    @Getter
    private boolean arrival;
    @Getter
    private String candidateKey;
    @Getter
    private String candidateVal;
    @Getter
    private String station;

    public StationResult setArrival(boolean arrival) {
        this.arrival = arrival;
        return this;
    }

    public StationResult setCandidateKey(String candidateKey) {
        this.candidateKey = candidateKey;
        return this;
    }

    public StationResult setCandidateVal(String candidateVal) {
        this.candidateVal = candidateVal;
        return this;
    }

    public StationResult setStation(String station) {
        this.station = station;
        return this;
    }
}
