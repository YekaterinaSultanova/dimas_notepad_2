package lv.ctco.notepad;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import static java.time.format.DateTimeFormatter.ofPattern;

public class Main {
    public static final String DATE_PATTERN = "uuuu-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = ofPattern(DATE_PATTERN);
    public static final String TIME_PATTERN = "HH:mm";
    public static final DateTimeFormatter TIME_FORMATTER = ofPattern(TIME_PATTERN);

    static Scanner scanner = new Scanner(System.in);
    static List<Record> records = new ArrayList<>();

    public static void main(String[] args) {
        for (; ;) {
            System.out.print("cmd: ");
            String cmd = scanner.next();
            switch (cmd) {
                case "dismiss":
                    dismiss();
                    break;
                case "expired":
                    listExpired();
                    break;
                case "search":
                    search();
                    break;
                case "ca":
                case "createAlarm":
                    createRecord(new Alarm());
                    break;
                case "cr":
                case "createReminder":
                    createRecord(new Reminder());
                    break;
                case "cp":
                case "createPerson":
                    createRecord(new Person());
                    break;
                case "cn":
                case "createNote":
                    createRecord(new StickyNote());
                    break;
                case "help":
                    showHelp();
                    break;
                case "delete":
                    deleteRecordById();
                case "list":
                    showList();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Wrong command. Try 'help'");
            }
        }
    }

    private static void dismiss() {
        int id = askInt("Enter ID of record to dismiss");
        Optional<Expirable> first = records.stream() //optional - eto kak kontejner u kotorogo mozhem sprositj pro najdennuju expired
                .filter(r -> r.getId() == id) // nahodim pervuju zapisj s toj id kotoruju hotim iskatj
                .filter(r -> r instanceof Expirable) //proverajem javlaetsa li zapisj expireble
                .map(r -> (Expirable) r) //convertirujem v expireble
                .findFirst();
        first.ifPresent(e -> e.dismiss()); // esli estj u optional, to vipolnitj sledujuweje dejstvije
    }

    private static void listExpired() {
        records.stream()
                .filter(r -> r instanceof Expirable)
                .map(r -> (Expirable)r)
                .filter(Expirable::isExpired)
                .forEach(System.out::println);

//        for (Record r : records) {           // stream
//            if (r instanceof Expirable) {    // 1st filter
//                Expirable e = (Expirable) r; // map
//                if (e.isExpired()) {         // 2nd filter
//                    System.out.println(e);   // forEach
//                }
//            }
//        }
    }

    private static void search() {
        String ss = askString("What do you want to find?");

        records.stream()
                .filter(r -> r.contains(ss))
                .forEach(System.out::println);

    }

    private static void createRecord(Record record) {
        record.askData();
        records.add(record);
        System.out.println(record);
    }

    private static void deleteRecordById() {
        int id = askInt("ID to delete");
        for (int i = 0; i < records.size(); i++) {
            Record r = records.get(i);
            if (r.getId() == id) {
                records.remove(i);
                break;
            }
        }
        showList();
    }

    private static void showList() {
        records.forEach(System.out::println);
    }

    private static void showHelp() { }

    public static String askString(String msg) {
        for (;;) {
            System.out.print(msg + ": ");
            String val = scanner.next();
            if (!val.startsWith("\"")) {
                return val;
            }
            List<String> words = new ArrayList<>();
            words.add(val);
            while (!val.endsWith("\"")) {
                val = scanner.next();
                words.add(val);
            }
            String result = String.join(" ", words);
            result = result.substring(1, result.length() - 1);
            if (result.length() < 1) {
                System.out.println("at least one character, please");
                continue;
            }
            return result;
        }
    }

    public static int askInt(String msg) {
        System.out.print(msg + ": ");
        return scanner.nextInt();
    }

    public static String askPhone(String msg) {
        for (; ; ) {
            String result = askString(msg);
            boolean hasWrongChars = result.codePoints()
                    .anyMatch(c -> !(Character.isDigit(c) || Character.isSpaceChar(c) || c == '-' || c == '+'));
            if (hasWrongChars) {
                System.out.println("Only numbers, spaces dashes and pluses are allowed");
                continue;
            }

            long digitCount = result.codePoints()
                    .filter(Character::isDigit)
                    .count();
            if (digitCount < 5) {
                System.out.println("Should be 5 or more digits");
                continue;
            }

            return result;
        }
    }

    public static LocalDate askDate(String msg) {
        String strDate = askString(msg);
        return LocalDate.parse(strDate, DATE_FORMATTER);
    }

    public static LocalTime askTime(String msg) {
        String strTime = askString(msg + "(" + TIME_PATTERN + ")");
        return LocalTime.parse(strTime, TIME_FORMATTER);
    }
}

