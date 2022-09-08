import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.util.*;

public class Calendar {

    private LocalTime startWorkingHours;
    private LocalTime endWorkingHours;

    private Set<Meeting> meetings = new HashSet<>();

    @JsonProperty("working_hours")
    public void setWorkingHours(Map<String, String> workingHours) {
        this.startWorkingHours = LocalTime.parse(workingHours.get("start"));
        this.endWorkingHours = LocalTime.parse(workingHours.get("end"));
    }

    @JsonProperty("planned_meeting")
    public void setPlannedMeetings(List<Map<String, String>> meetings) {
        for (Map<String, String> meeting : meetings) {
            this.meetings.add(new Meeting(LocalTime.parse(meeting.get("start")),
                    LocalTime.parse(meeting.get("end"))));
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Calendar.class.getSimpleName() + "[", "]")
                .add("startWorkingHours=" + startWorkingHours)
                .add("endWorkingHours=" + endWorkingHours)
                .add("meetings=" + meetings)
                .toString();
    }

}
