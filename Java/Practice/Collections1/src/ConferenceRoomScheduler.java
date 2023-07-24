import java.util.Date;
import java.util.TreeMap;

public class ConferenceRoomScheduler {
    TreeMap<Meeting, Integer> meetings = new TreeMap<>((a, b) -> b.start.compareTo(a.start));

    class Meeting {
        Date start;
        Date end;
        public Meeting(Date start, Date end) {
            this.start = start;
            this.end = end;
        }
    }

    public int reserveRoom(long startEpochSeconds, long endEpochSeconds) {
        Date start = new Date(startEpochSeconds * 1000);
        Date end = new Date(endEpochSeconds * 1000);
        int startDay = start.getDay();
        int endDay = end.getDay();
        int startHour = start.getHours();
        int endHour = end.getHours();
        int startMinutes = start.getMinutes();
        int endMinutes = end.getMinutes();

        if (endDay - startDay > 7) return -2;

        if (startHour == endHour && endMinutes - startMinutes < 5) return -2;

        if (startMinutes % 5 != 0 || endMinutes % 5 != 0) return -2;

        Meeting newMeeting = new Meeting(start, end);

        int conf = 0;
        for (Meeting meeting : meetings.keySet()) {
            if (meeting.end.compareTo(newMeeting.start) > 0 && newMeeting.end.compareTo(meeting.start) > 0) {
                conf = conf + meetings.get(meeting);
            }
        }

        if (conf >= 8) return -1;

        meetings.put(newMeeting, meetings.getOrDefault(newMeeting, 0) + 1);

        return conf + 1;
    }

    public static void main(String[] args) {
        System.out.println("Hi");
        ConferenceRoomScheduler conferenceRoomScheduler = new ConferenceRoomScheduler();
        int room = conferenceRoomScheduler.reserveRoom(1, 2);
        System.out.println(room);
    }

}