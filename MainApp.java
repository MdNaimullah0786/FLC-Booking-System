package main;

import model.*;
import service.*;

import java.util.*;

public class MainApp {

    private static BookingService service = new BookingService();
    private static List<Member> members = new ArrayList<>();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        setupData();

        while (true) {

            System.out.println("\n===== FLC BOOKING SYSTEM =====");
            System.out.println("1. View Timetable by Day");
            System.out.println("2. View Timetable by Exercise");
            System.out.println("3. Book Lesson");
            System.out.println("4. Change Booking");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Attend Lesson");
            System.out.println("7. Monthly Lesson Report");
            System.out.println("8. Monthly Champion Report");
            System.out.println("9. Exit");

            System.out.print("Enter your choice: ");
            int choice = getIntInput();

            switch (choice) {

                case 1:
                    viewByDay();
                    break;

                case 2:
                    viewByExercise();
                    break;

                case 3:
                    bookLesson();
                    break;

                case 4:
                    changeBooking();
                    break;

                case 5:
                    cancelBooking();
                    break;

                case 6:
                    attendLesson();
                    break;

                case 7:
                    generateMonthlyReport();
                    break;

                case 8:
                    generateChampionReport();
                    break;

                case 9:
                    System.out.println("Exiting system...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ---------------- SAFE INPUT ----------------
    private static int getIntInput() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.print("Invalid input! Enter a number: ");
            }
        }
    }

    // ---------------- SETUP ----------------
    private static void setupData() {

        String[] days = {"Saturday", "Sunday"};
        String[] times = {"Morning", "Afternoon", "Evening"};
        String[] types = {"Yoga", "Zumba", "Aquacise", "BoxFit"};

        Map<String, Double> prices = new HashMap<>();
        prices.put("Yoga", 10.0);
        prices.put("Zumba", 12.0);
        prices.put("Aquacise", 14.0);
        prices.put("BoxFit", 15.0);

        int month = 5;

        for (int week = 1; week <= 8; week++) {

            if (week == 5) month = 6;

            for (String day : days) {
                for (int i = 0; i < 3; i++) {

                    String type = types[(week + i) % types.length];

                    service.addLesson(new Lesson(
                            type, day, times[i], prices.get(type), month
                    ));
                }
            }
        }

        members.add(new Member(1, "Ayan"));
        members.add(new Member(2, "Rahul"));
        members.add(new Member(3, "Priya"));
    }

    // ---------------- VIEW BY DAY ----------------
    private static void viewByDay() {

        System.out.print("Enter day (Saturday/Sunday): ");
        String day = sc.nextLine();

        List<Lesson> list = service.getLessonsByDay(day);

        if (list.isEmpty()) {
            System.out.println("No lessons found!");
            return;
        }

        System.out.println("\n--- Timetable ---");
        int i = 1;
        for (Lesson l : list) {
            System.out.println(i++ + ". " + l.getExerciseType()
                    + " | " + l.getTime()
                    + " | £" + l.getPrice()
                    + " | Slots: " + l.getMemberCount() + "/4");
        }
    }

    // ---------------- VIEW BY EXERCISE ----------------
    private static void viewByExercise() {

        System.out.print("Enter exercise type: ");
        String type = sc.nextLine();

        List<Lesson> list = service.getLessonsByType(type);

        if (list.isEmpty()) {
            System.out.println("No lessons found!");
            return;
        }

        System.out.println("\n--- Timetable ---");
        int i = 1;
        for (Lesson l : list) {
            System.out.println(i++ + ". " + l.getDay()
                    + " | " + l.getTime()
                    + " | £" + l.getPrice());
        }
    }

    // ---------------- BOOK ----------------
    private static void bookLesson() {

        System.out.print("Enter Member ID: ");
        Member m = findMember(getIntInput());

        if (m == null) {
            System.out.println("Member not found!");
            return;
        }

        List<Lesson> list = service.getAllLessons();

        System.out.println("\n--- Available Lessons ---");
        for (int i = 0; i < list.size(); i++) {
            Lesson l = list.get(i);
            System.out.println((i + 1) + ". " + l.getExerciseType()
                    + " | " + l.getDay()
                    + " | " + l.getTime());
        }

        System.out.print("Select lesson number: ");
        int choice = getIntInput();

        if (choice < 1 || choice > list.size()) {
            System.out.println("Invalid selection!");
            return;
        }

        Booking b = service.bookLesson(m, list.get(choice - 1));

        if (b != null) {
            System.out.println("Booking successful! ID: " + b.getId());
        } else {
            System.out.println("Booking failed (duplicate / full / time conflict)");
        }
    }

    // ---------------- CHANGE ----------------
    private static void changeBooking() {

        System.out.print("Enter Booking ID: ");
        int id = getIntInput();

        List<Lesson> list = service.getAllLessons();

        System.out.println("\n--- Select New Lesson ---");
        for (int i = 0; i < list.size(); i++) {
            Lesson l = list.get(i);
            System.out.println((i + 1) + ". " + l.getExerciseType()
                    + " | " + l.getDay()
                    + " | " + l.getTime());
        }

        System.out.print("Enter choice: ");
        int choice = getIntInput();

        if (choice < 1 || choice > list.size()) {
            System.out.println("Invalid selection!");
            return;
        }

        boolean result = service.changeBooking(id, list.get(choice - 1));

        System.out.println(result ? "Booking changed successfully" : "Change failed");
    }

    // ---------------- CANCEL ----------------
    private static void cancelBooking() {

        System.out.print("Enter Booking ID: ");
        int id = getIntInput();

        boolean result = service.cancelBooking(id);

        System.out.println(result ? "Booking cancelled" : "Cancellation failed");
    }

    // ---------------- ATTEND ----------------
    private static void attendLesson() {

        System.out.print("Enter Booking ID: ");
        int id = getIntInput();

        System.out.print("Enter rating (1-5): ");
        int rating = getIntInput();

        System.out.print("Enter review: ");
        String review = sc.nextLine();

        service.attendLesson(id, rating, review);

        System.out.println("Lesson attended successfully!");
    }

    // ---------------- REPORTS ----------------
    private static void generateMonthlyReport() {

        System.out.print("Enter month (5 or 6): ");
        int month = getIntInput();

        service.generateMonthlyReport(month);
    }

    private static void generateChampionReport() {

        System.out.print("Enter month (5 or 6): ");
        int month = getIntInput();

        service.generateMonthlyChampion(month);
    }

    // ---------------- HELPER ----------------
    private static Member findMember(int id) {
        for (Member m : members) {
            if (m.getId() == id) return m;
        }
        return null;
    }
}