package karage.app

/**
 * A calculator utility class that provides basic arithmetic operations.
 * 
 * This class encapsulates fundamental mathematical operations including
 * addition, subtraction, multiplication, and division with proper error handling.
 * 
 * @since 1.0.0
 */
class Calculator {
    /**
     * Adds two integers together.
     * 
     * @param a The first number to add
     * @param b The second number to add
     * @return The sum of a and b
     */
    fun add(
        a: Int,
        b: Int,
    ): Int {
        return a + b
    }

    /**
     * Subtracts the second integer from the first.
     * 
     * @param a The number to subtract from
     * @param b The number to subtract
     * @return The difference of a minus b
     */
    fun subtract(
        a: Int,
        b: Int,
    ): Int {
        return a - b
    }

    /**
     * Multiplies two integers together.
     * 
     * @param a The first number to multiply
     * @param b The second number to multiply
     * @return The product of a and b
     */
    fun multiply(
        a: Int,
        b: Int,
    ): Int {
        return a * b
    }

    /**
     * Divides the first integer by the second.
     * 
     * @param a The dividend (number to be divided)
     * @param b The divisor (number to divide by)
     * @return The quotient of a divided by b
     * @throws IllegalArgumentException when attempting to divide by zero
     */
    fun divide(
        a: Int,
        b: Int,
    ): Int {
        if (b == 0) {
            throw IllegalArgumentException("Cannot divide by zero")
        }
        return a / b
    }
}
