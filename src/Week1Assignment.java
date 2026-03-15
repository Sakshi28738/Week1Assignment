import java.util.*;

public class Week1Assignment {

    static class ParkingSpot {
        String licensePlate;
        long entryTime;
        boolean occupied;

        ParkingSpot() {
            licensePlate = null;
            entryTime = 0;
            occupied = false;
        }
    }

    static final int TOTAL_SPOTS = 500;
    static ParkingSpot[] parkingLot = new ParkingSpot[TOTAL_SPOTS];

    static int totalProbes = 0;
    static int vehiclesParked = 0;

    static {
        for (int i = 0; i < TOTAL_SPOTS; i++) {
            parkingLot[i] = new ParkingSpot();
        }
    }


    public static int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % TOTAL_SPOTS;
    }


    public static void parkVehicle(String licensePlate) {

        int index = hash(licensePlate);
        int probes = 0;

        while (parkingLot[index].occupied) {
            index = (index + 1) % TOTAL_SPOTS;
            probes++;
        }

        parkingLot[index].licensePlate = licensePlate;
        parkingLot[index].entryTime = System.currentTimeMillis();
        parkingLot[index].occupied = true;

        vehiclesParked++;
        totalProbes += probes;

        System.out.println("parkVehicle(\"" + licensePlate + "\") → Assigned spot #" +
                index + " (" + probes + " probes)");
    }


    public static void exitVehicle(String licensePlate) {

        int index = hash(licensePlate);

        while (parkingLot[index].occupied) {

            if (parkingLot[index].licensePlate.equals(licensePlate)) {

                long durationMillis =
                        System.currentTimeMillis() - parkingLot[index].entryTime;

                double hours = durationMillis / (1000.0 * 60 * 60);

                double fee = hours * 5.5;

                parkingLot[index].occupied = false;
                parkingLot[index].licensePlate = null;

                vehiclesParked--;

                System.out.println("exitVehicle(\"" + licensePlate + "\") → Spot #" +
                        index + " freed, Duration: " +
                        String.format("%.2f", hours) +
                        "h, Fee: $" + String.format("%.2f", fee));

                return;
            }

            index = (index + 1) % TOTAL_SPOTS;
        }

        System.out.println("Vehicle not found.");
    }


    public static int findNearestSpot() {

        for (int i = 0; i < TOTAL_SPOTS; i++) {
            if (!parkingLot[i].occupied) {
                return i;
            }
        }

        return -1;
    }


    public static void getStatistics() {

        double occupancy = ((double) vehiclesParked / TOTAL_SPOTS) * 100;

        double avgProbes = vehiclesParked == 0 ?
                0 : (double) totalProbes / vehiclesParked;

        System.out.println("Occupancy: " +
                String.format("%.2f", occupancy) + "%");

        System.out.println("Avg Probes: " +
                String.format("%.2f", avgProbes));
    }

    public static void main(String[] args) {

        parkVehicle("ABC-1234");
        parkVehicle("ABC-1235");
        parkVehicle("XYZ-9999");

        exitVehicle("ABC-1234");

        System.out.println("Nearest available spot: " + findNearestSpot());

        getStatistics();
    }
}