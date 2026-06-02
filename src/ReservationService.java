import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private final List<Student> students = new ArrayList<>();
    private final List<Equipment> equipmentList = new ArrayList<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final DiscountPolicy discountPolicy;

    public ReservationService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    //methods
    public void addStudent(Student s) {
        students.add(s);
    }

    public void addEquipment(Equipment e) {
        equipmentList.add(e);
    }

    //getters
    public List<Student> getStudents() {
        return students;
    }

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    //creation of a reservation
    public Reservation createReservation(String studentId, String equipmentId, int days) throws Exception {
        Student student = students.stream().filter(s -> s.getId().equals(studentId)).findFirst().orElse(null);

        if (student == null) {
            throw new Exception("Error: Student with ID " + studentId + " does not exist.");
        }

        Equipment eq = equipmentList.stream().filter(e -> e.getId().equals(equipmentId)).findFirst().orElse(null);

        if (eq == null) {
            throw new Exception("Error: Equipment with ID " + equipmentId + " does not exist.");
        }

        if (!eq.isAvailable()) {
            throw new Exception("Error: Equipment " + equipmentId + " is not available.");
        }

        if (days < 1 || days > 14) {
            throw new Exception("Error: Rental days must be between 1 and 14.");
        }

        //update equipment status
        eq.setAvailable(false);
        Reservation res = new Reservation(student, eq, days, discountPolicy);
        reservations.add(res);
        return res;
    }

    //return of equipment
    public void returnEquipment(String reservationId) throws Exception {
        Reservation res = reservations.stream().filter(r -> r.getId().equals(reservationId)).findFirst().orElse(null);

        if (res == null) {
            throw new Exception("Error: Reservation " + reservationId + " not found.");
        }

        if (res.getStatus() != ReservationStatus.ACTIVE) {
            throw new Exception("Error: This reservation is already processed or cancelled.");
        }

        res.setStatus(ReservationStatus.RETURNED);
        res.getEquipment().setAvailable(true);

        //1 point for each full 10 PLN spent
        int pointsGained = (int) (res.getFinalCost() / 10);
        res.getStudent().addLoyaltyPoints(pointsGained);

        System.out.printf("Equipment returned. The student received %d loyalty points.%n", pointsGained);
    }

    //reports
    public void printReport() {
        System.out.println("\n=================================");
        System.out.println("         SYSTEM REPORT           ");
        System.out.println("=================================");

        //active
        System.out.println("Active Reservations:");
        reservations.stream().filter(r -> r.getStatus() == ReservationStatus.ACTIVE).forEach(r -> System.out.println("  " + r.getDisplayText()));

        //completed
        System.out.println("\nCompleted Reservations:");
        reservations.stream().filter(r -> r.getStatus() == ReservationStatus.RETURNED).forEach(r -> System.out.println("  " + r.getDisplayText()));

        //revenue
        double totalRevenue = reservations.stream().filter(r -> r.getStatus() == ReservationStatus.RETURNED).mapToDouble(Reservation::getFinalCost).sum();
        System.out.printf("%nTotal Revenue from Completed Rentals: %.2f PLN%n", totalRevenue);

        //top student
        Student topStudent = students.stream().max((s1, s2) -> Integer.compare(s1.getLoyaltyPoints(), s2.getLoyaltyPoints())).orElse(null);

        if (topStudent != null) {
            System.out.printf("Student with highest loyalty points: %s (%d points)%n",
                    topStudent.getFullName(), topStudent.getLoyaltyPoints());
        }

        System.out.println("=================================\n");
    }
}