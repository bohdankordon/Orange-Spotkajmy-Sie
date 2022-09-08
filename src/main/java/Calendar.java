import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Calendar {

    private LocalTime startWorkingHours;
    private LocalTime endWorkingHours;

    private List<Meeting> meetings = new ArrayList<>();

    public Calendar() {

    }

    public Calendar(LocalTime startWorkingHours, LocalTime endWorkingHours, List<Meeting> meetings) {
        this.startWorkingHours = startWorkingHours;
        this.endWorkingHours = endWorkingHours;
        this.meetings = meetings;
        sortMeetings();
    }

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
        sortMeetings();
    }

    private void sortMeetings() {
        Collections.sort(meetings);
    }

    public static List<Meeting> findAvailableMeetingTime(Calendar cal1, Calendar cal2, String duration) {
        // merge 2 calendars
        LocalTime maxStartWorkingHours = cal1.startWorkingHours.compareTo(cal2.startWorkingHours) >= 0 ? cal1.startWorkingHours : cal2.startWorkingHours;
        LocalTime minEndWorkingHours = cal1.endWorkingHours.compareTo(cal2.endWorkingHours) < 0 ? cal1.endWorkingHours : cal2.endWorkingHours;
        Calendar merged = new Calendar(maxStartWorkingHours, minEndWorkingHours, Stream.concat(cal1.meetings.stream(), cal2.meetings.stream()).collect(Collectors.toList()));

        // remove meetings that is not in the working hours
        merged.meetings.removeIf(m -> m.getStartTime().compareTo(merged.startWorkingHours) < 0 || m.getEndTime().compareTo(merged.endWorkingHours) > 0);

        // combine overlapping meeting hours
        for (int i = 0, len = merged.meetings.size(); i < len; i++) {
            Meeting m1 = merged.meetings.get(i);
            for (int j = i + 1; j < len; j++) {
                Meeting m2 = merged.meetings.get(j);
                if (m1.getEndTime().compareTo(m2.getStartTime()) <= 0) {
                    break;
                }
                m1.setEndTime(m2.getEndTime());
                merged.meetings.remove(m2);
                len -= 1;
            }
        }

        // find available time slots
        List<Meeting> available = new ArrayList<>();
        if (merged.startWorkingHours.compareTo(merged.meetings.get(0).getStartTime()) < 0) {
            available.add(new Meeting(merged.startWorkingHours, merged.meetings.get(0).getStartTime()));
        }
        for (int i = 0, len = merged.meetings.size() - 1; i < len; i++) {
            LocalTime endTime = merged.meetings.get(i).getEndTime();
            LocalTime startTime = merged.meetings.get(i + 1).getStartTime();
            if (endTime.compareTo(startTime) < 0) {
                available.add(new Meeting(endTime, startTime));
            }
        }
        if (merged.meetings.get(merged.meetings.size() - 1).getEndTime().compareTo(merged.endWorkingHours) < 0) {
            available.add(new Meeting(merged.meetings.get(merged.meetings.size() - 1).getEndTime(), merged.endWorkingHours));
        }
        return available;
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
