package model;

public class Booking {

    private static int counter = 1;

    private int id;
    private Member member;
    private Lesson lesson;
    private String status;

    public Booking(Member member, Lesson lesson) {
        this.id = counter++;
        this.member = member;
        this.lesson = lesson;
        this.status = "booked";
    }

    public int getId() { return id; }
    public Member getMember() { return member; }
    public Lesson getLesson() { return lesson; }
    public String getStatus() { return status; }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
        this.status = "changed";
    }

    public void setStatus(String status) {
        this.status = status;
    }
}