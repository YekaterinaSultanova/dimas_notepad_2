package lv.ctco.notepad;

import java.time.LocalDate;
import java.time.LocalTime;

public class Alarm extends StickyNote implements Expirable {
    private LocalTime time;
    private LocalDate dismissedAt;

    @Override
    public boolean contains(String str) {
        return super.contains(str)
                || getFormattedTime().contains(str);
    }

    protected String getFormattedTime() {
        return time.format(Main.TIME_FORMATTER);
    }

    @Override
    public void askData() {
        this.time = Main.askTime("Enter time");
        super.askData();
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + getId() +
                ", date='" + getFormattedTime() + '\'' +
                ", text='" + getText() + '\'' +
                '}';
    }

    @Override
    public boolean isExpired() {
        LocalDate nowD = LocalDate.now(); // ctrl+alt+D -> 4tbi sokratitj
        if (dismissedAt != null && dismissedAt.isEqual(nowD)) { // objazateljno proveraem 4to eto ne null i proveraem segodnja li bil dismiss
            return false;
        }
        LocalTime now = LocalTime.now();
        return now.isAfter(time);
    }

    @Override
    public void dismiss() {
        dismissedAt = LocalDate.now();

    }
}
