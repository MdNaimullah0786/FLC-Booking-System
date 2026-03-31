package service;

import model.*;
import java.util.*;

public class BookingService {

    private List<Lesson> lessons = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    public void addLesson(Lesson l) {
        lessons.add(l);
    }

    public List<Lesson> getAllLessons() {
        return lessons;
    }

    public List<Lesson> getLessonsByDay(String day) {
        List<Lesson> result = new ArrayList<>();
        for (Lesson l : lessons) {
            if (l.getDay().equalsIgnoreCase(day)) result.add(l);
        }
        return result;
    }

    public List<Lesson> getLessonsByType(String type) {
        List<Lesson> result = new ArrayList<>();
        for (Lesson l : lessons) {
            if (l.getExerciseType().equalsIgnoreCase(type)) result.add(l);
        }
        return result;
    }

    private boolean hasTimeConflict(Member m, Lesson newLesson) {
        for (Booking b : bookings) {
            if (b.getMember().equals(m)
                    && b.getLesson().getDay().equalsIgnoreCase(newLesson.getDay())
                    && b.getLesson().getTime().equalsIgnoreCase(newLesson.getTime())
                    && !b.getStatus().equals("cancelled")) {
                return true;
            }
        }
        return false;
    }

    private boolean isDuplicate(Member m, Lesson l) {
        for (Booking b : bookings) {
            if (b.getMember().equals(m)
                    && b.getLesson().equals(l)
                    && !b.getStatus().equals("cancelled")) {
                return true;
            }
        }
        return false;
    }

    public Booking bookLesson(Member m, Lesson l) {

        if (isDuplicate(m, l) || hasTimeConflict(m, l))
            return null;

        if (l.addMember(m)) {
            Booking b = new Booking(m, l);
            bookings.add(b);
            return b;
        }
        return null;
    }

    public boolean changeBooking(int id, Lesson newLesson) {

        for (Booking b : bookings) {

            if (b.getId() == id) {

                if (b.getStatus().equals("attended")) return false;

                if (hasTimeConflict(b.getMember(), newLesson)) return false;

                if (newLesson.addMember(b.getMember())) {
                    b.getLesson().removeMember(b.getMember());
                    b.setLesson(newLesson);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean cancelBooking(int id) {

        for (Booking b : bookings) {

            if (b.getId() == id) {

                if (b.getStatus().equals("attended")) return false;

                b.getLesson().removeMember(b.getMember());
                b.setStatus("cancelled");
                return true;
            }
        }
        return false;
    }

    public void attendLesson(int id, int rating, String review) {

        for (Booking b : bookings) {

            if (b.getId() == id) {

                b.getLesson().addRating(rating);
                b.getLesson().addReview(review);
                b.setStatus("attended");
            }
        }
    }

    // ---------------- MONTHLY REPORT ----------------
    public void generateMonthlyReport(int month) {

        System.out.println("\n--- Monthly Lesson Report ---");

        for (Lesson l : lessons) {

            if (l.getMonth() == month) {

                int attended = 0;

                for (Booking b : bookings) {
                    if (b.getLesson().equals(l)
                            && b.getStatus().equals("attended")) {
                        attended++;
                    }
                }

                System.out.println(l.getExerciseType() + " | "
                        + l.getDay() + " | " + l.getTime()
                        + " | Attended: " + attended
                        + " | Avg Rating: " + l.getAverageRating());
            }
        }
    }

    // ---------------- CHAMPION REPORT ----------------
    public void generateMonthlyChampion(int month) {

        System.out.println("\n--- Monthly Champion Report ---");

        Map<String, Double> map = new HashMap<>();

        for (Lesson l : lessons) {

            if (l.getMonth() == month) {

                int attended = 0;

                for (Booking b : bookings) {
                    if (b.getLesson().equals(l)
                            && b.getStatus().equals("attended")) {
                        attended++;
                    }
                }

                double income = attended * l.getPrice();

                map.put(l.getExerciseType(),
                        map.getOrDefault(l.getExerciseType(), 0.0) + income);
            }
        }

        String top = "";
        double max = 0;

        for (String k : map.keySet()) {

            double val = map.get(k);
            System.out.println(k + " Income: £" + val);

            if (val > max) {
                max = val;
                top = k;
            }
        }

        System.out.println("Champion Exercise: " + top);
    }
}