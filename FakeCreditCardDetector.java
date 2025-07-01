import java.util.Scanner;

public class FakeCreditCardDetector {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Credit Card Number: ");
        String cardNumber = scanner.nextLine().replaceAll("\\s+", ""); // Remove spaces

        System.out.print("Enter Expiration Date (MM/YY): ");
        String expirationDate = scanner.nextLine();

        if (isValidCardNumber(cardNumber)) {
            System.out.println("The credit card number is valid.");
            String issuer = getCardIssuer(cardNumber);
            System.out.println("Issuer: " + issuer);

            if (isExpired(expirationDate)) {
                System.out.println("The card is expired.");
            } else {
                System.out.println("The card is not expired.");
            }
        } else {
            System.out.println("The credit card number is invalid (possibly fake).");
        }

        scanner.close();
    }

    // Luhn Algorithm to validate the credit card number
    public static boolean isValidCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 13 || cardNumber.length() > 19) {
            return false;
        }

        int sum = 0;
        boolean alternate = false;
        for (int i = cardNumber.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return (sum % 10 == 0);
    }

    // Determine the card issuer based on the card number prefix
    public static String getCardIssuer(String cardNumber) {
        if (cardNumber.startsWith("4")) {
            return "Visa";
        } else if (cardNumber.startsWith("5")) {
            return "MasterCard";
        } else if (cardNumber.startsWith("34") || cardNumber.startsWith("37")) {
            return "American Express";
        } else if (cardNumber.startsWith("6")) {
            return "Discover";
        } else {
            return "Unknown Issuer";
        }
    }

    // Check if the card is expired
    public static boolean isExpired(String expirationDate) {
        String[] parts = expirationDate.split("/");
        if (parts.length != 2) {
            return true; // Invalid format, assume expired
        }

        int month = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[1]) + 2000; // Convert YY to YYYY

        // Get current date
        java.time.YearMonth currentDate = java.time.YearMonth.now();
        java.time.YearMonth cardExpiry = java.time.YearMonth.of(year, month);

        return cardExpiry.isBefore(currentDate);
    }
}