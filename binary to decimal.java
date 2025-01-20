import java.util.Scanner;

public class BinaryToDecimalConverter {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter a binary number: ");
        String binaryString = scanner.nextLine();
        
        try {
            int decimalValue = convertBinaryToDecimal(binaryString);
            System.out.println("The decimal value of binary " + binaryString + " is: " + decimalValue);
        } catch (NumberFormatException e) {
            System.out.println("Invalid binary number. Please enter a valid binary string.");
        } finally {
            scanner.close();
        }
    }

    public static int convertBinaryToDecimal(String binaryString) {
        
        if (!binaryString.matches("[01]+")) {
            throw new NumberFormatException("Invalid binary number");
        }

        int decimalValue = 0;
        int length = binaryString.length();

        for (int i = 0; i < length; i++) {
            
            char bit = binaryString.charAt(length - 1 - i);
            
            int bitValue = Character.getNumericValue(bit);
            
            decimalValue += bitValue * Math.pow(2, i);
        }

        return decimalValue;
    }
}
