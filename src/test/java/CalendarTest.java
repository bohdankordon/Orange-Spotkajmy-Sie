import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;

class CalendarTest {

    @Test
    void createCalendar() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Calendar cal1 = mapper.readValue(new File("src/data/calendar1.json"), Calendar.class);
        Calendar cal2 = mapper.readValue(new File("src/data/calendar2.json"), Calendar.class);

        assertEquals("Calendar[startWorkingHours=09:00, endWorkingHours=19:55, meetings=[Meeting[startTime=09:00, endTime=10:30], Meeting[startTime=12:00, endTime=13:00], Meeting[startTime=16:00, endTime=18:00]]]",
                cal1.toString());
        assertEquals("Calendar[startWorkingHours=10:00, endWorkingHours=18:30, meetings=[Meeting[startTime=10:00, endTime=11:30], Meeting[startTime=12:30, endTime=14:30], Meeting[startTime=14:30, endTime=15:00], Meeting[startTime=16:00, endTime=17:00]]]",
                cal2.toString());
    }

    @Test
    void findAvailableMeetingTime() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Calendar cal1 = mapper.readValue(new File("src/data/calendar1.json"), Calendar.class);
        Calendar cal2 = mapper.readValue(new File("src/data/calendar2.json"), Calendar.class);

        assertEquals("[Meeting[startTime=11:30, endTime=12:00], Meeting[startTime=15:00, endTime=16:00], Meeting[startTime=18:00, endTime=18:30]]",
                Calendar.findAvailableMeetingTime(cal1, cal2, "00:30").toString());
    }

}