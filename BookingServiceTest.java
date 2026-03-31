package test;

import org.junit.jupiter.api.*;
import model.*;
import service.*;

import static org.junit.jupiter.api.Assertions.*;

public class BookingServiceTest {

    private BookingService service;
    private Lesson lesson1, lesson2;
    private Member member;

    // ---------------- SETUP ----------------
    @BeforeEach
    void setUp() {
        service = new BookingService();

        lesson1 = new Lesson("Yoga", "Saturday", "Morning", 10, 5);
        lesson2 = new Lesson("Zumba", "Saturday", "Afternoon", 12, 5);

        service.addLesson(lesson1);
        service.addLesson(lesson2);

        member = new Member(1, "TestUser");
    }

    // ✅ 1. Booking (BookingService)
    @Test
    @DisplayName("Booking should succeed when slots available")
    void testBookingSuccess() {

        Booking b = service.bookLesson(member, lesson1);

        assertNotNull(b);
        assertEquals(1, lesson1.getMemberCount());
    }

    // ✅ 2. Duplicate prevention (BookingService logic)
    @Test
    @DisplayName("Duplicate booking should not be allowed")
    void testDuplicateBooking() {

        service.bookLesson(member, lesson1);
        Booking second = service.bookLesson(member, lesson1);

        assertNull(second);
    }

    // ✅ 3. Capacity constraint (Lesson class logic)
    @Test
    @DisplayName("Lesson should not exceed capacity of 4")
    void testCapacityLimit() {

        for (int i = 1; i <= 5; i++) {
            service.bookLesson(new Member(i, "M" + i), lesson1);
        }

        assertEquals(4, lesson1.getMemberCount());
    }

    // ✅ 4. Change booking (Booking + Service)
    @Test
    @DisplayName("Booking should be changed successfully")
    void testChangeBooking() {

        Booking b = service.bookLesson(member, lesson1);

        boolean result = service.changeBooking(b.getId(), lesson2);

        assertTrue(result);
        assertEquals(0, lesson1.getMemberCount());
        assertEquals(1, lesson2.getMemberCount());
    }

    // ✅ 5. Attend + Rating (Lesson logic)
    @Test
    @DisplayName("Attending lesson should update average rating")
    void testAttendAndRating() {

        Booking b = service.bookLesson(member, lesson1);

        service.attendLesson(b.getId(), 5, "Excellent");
        service.attendLesson(b.getId(), 3, "Okay");

        assertEquals(4.0, lesson1.getAverageRating());
    }
}