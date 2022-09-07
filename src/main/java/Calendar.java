import java.util.HashSet;
import java.util.Set;

public class Calendar {

    private Set<Meeting> meetings = new HashSet<>();

    private void addMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

}
