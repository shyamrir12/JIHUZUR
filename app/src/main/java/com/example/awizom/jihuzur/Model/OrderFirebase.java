package com.example.awizom.jihuzur.Model;

public class OrderFirebase {
    private  long id;
    private String StartTime;
    private String EndTime;

    public OrderFirebase() {
    }

    public OrderFirebase(long id, String startTime, String endTime) {
        this.id = id;
        StartTime = startTime;
        EndTime = endTime;
    }

    public long getId() {
        return id;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }
}
