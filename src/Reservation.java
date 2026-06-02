public class Reservation implements Displayable {
    private static int idCounter = 1;
    private String id;
    private Student student;
    private Equipment equipment;
    private int days;
    private ReservationStatus status;
    private double finalCost;

    public Reservation(Student student, Equipment equipment, int days, DiscountPolicy discountPolicy) {
        this.id = String.format("R%03d", idCounter++);
        this.student = student;
        this.equipment = equipment;
        this.days = days;
        this.status = ReservationStatus.ACTIVE; //new reservations are active by default
        this.finalCost = calculateTotalCost(discountPolicy);
    }

    public double calculateTotalCost(DiscountPolicy discountPolicy) {
        double priceBeforeDiscount = equipment.calculateDailyPrice() * days;

        return discountPolicy.applyDiscount(student, priceBeforeDiscount);
    }

    //getters and setters
    public String getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public int getDays() {
        return days;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public double getFinalCost() {
        return finalCost;
    }

    @Override
    public String getDisplayText() {
        return String.format("Res ID: %s | Student: %s (%s) | Equipment: %s | Days: %d | Total: %.2f PLN | Status: %s", id, student.getFullName(), student.getId(), equipment.getName(), days, finalCost, status);
    }
}