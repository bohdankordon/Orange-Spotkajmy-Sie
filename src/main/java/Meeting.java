import java.time.LocalTime;
import java.util.StringJoiner;

public class Meeting implements Comparable<Meeting> {

    private LocalTime startTime;
    private LocalTime endTime;

    public Meeting(LocalTime startTime, LocalTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public int compareTo(Meeting m) {
        if (m.startTime == null) {
            return 1;
        }
        int res = startTime.compareTo(m.startTime);
        return res == 0 ? endTime.compareTo(m.endTime) : res;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Meeting.class.getSimpleName() + "[", "]")
                .add("startTime=" + startTime)
                .add("endTime=" + endTime)
                .toString();
    }

}
