package model;

import java.util.*;

public class Lesson {

    private String exerciseType;
    private String day;
    private String time;
    private double price;
    private int month;

    private List<Member> members = new ArrayList<>();
    private List<Integer> ratings = new ArrayList<>();
    private List<String> reviews = new ArrayList<>();

    public Lesson(String exerciseType, String day, String time, double price, int month) {
        this.exerciseType = exerciseType;
        this.day = day;
        this.time = time;
        this.price = price;
        this.month = month;
    }

    public boolean addMember(Member m) {
        if (members.size() < 4 && !members.contains(m)) {
            members.add(m);
            return true;
        }
        return false;
    }

    public void removeMember(Member m) {
        members.remove(m);
    }

    public void addRating(int rating) {
        ratings.add(rating);
    }

    public void addReview(String review) {
        reviews.add(review);
    }

    public double getAverageRating() {
        if (ratings.isEmpty()) return 0;
        return ratings.stream().mapToInt(i -> i).average().orElse(0);
    }

    public int getMemberCount() {
        return members.size();
    }

    public String getExerciseType() { return exerciseType; }
    public String getDay() { return day; }
    public String getTime() { return time; }
    public double getPrice() { return price; }
    public int getMonth() { return month; }
}