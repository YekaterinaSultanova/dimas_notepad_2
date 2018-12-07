package lv.ctco.notepad;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reminder extends Alarm implements Expirable {
    private LocalDate date;
    private boolean dismissed = false;

    @Override
    public void dismiss() {
        dismissed = true;
    }

    @Override
    public boolean contains(String str) {
        return super.contains(str)
                || getFormattedDate().contains(str);
    }

    @Override
    public void askData() {
        date = Main.askDate("Remainder date");
        super.askData();
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean isExpired() { // boolean - tip dannih, libo true libo false

        if (dismissed) {
            return false; //esli mi zadesmissili record, to vernjom false i ogda on nikogda boljwe ne vernjotsa
        }
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dt = LocalDateTime.of(getDate(), getTime());
        return now.isAfter(dt);
    }
    @Override
    public String toString() {
        return "Reminder{" +
                "id=" + getId() +
                ", date='" + getFormattedDate() + '\'' +
                ", time='" + getFormattedTime() + '\'' +
                ", text='" + getText() + '\'' +
                '}';
    }

    private String getFormattedDate() {
        return date.format(Main.DATE_FORMATTER);
    }
}
