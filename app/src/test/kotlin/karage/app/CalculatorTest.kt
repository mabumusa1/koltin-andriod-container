package karage.app

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CalculatorTest {
    private lateinit var calculator: Calculator

    @Before
    fun setup() {
        calculator = Calculator()
    }

    @Test
    fun testAddition() {
        assertEquals(4, calculator.add(2, 2))
        assertEquals(0, calculator.add(0, 0))
        assertEquals(-2, calculator.add(-1, -1))
        assertEquals(0, calculator.add(-5, 5))
    }

    @Test
    fun testSubtraction() {
        assertEquals(0, calculator.subtract(2, 2))
        assertEquals(5, calculator.subtract(10, 5))
        assertEquals(-5, calculator.subtract(5, 10))
    }

    @Test
    fun testMultiplication() {
        assertEquals(4, calculator.multiply(2, 2))
        assertEquals(0, calculator.multiply(0, 5))
        assertEquals(-10, calculator.multiply(2, -5))
    }

    @Test
    fun testDivision() {
        assertEquals(2, calculator.divide(4, 2))
        assertEquals(0, calculator.divide(0, 5))
        assertEquals(-2, calculator.divide(10, -5))
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDivisionByZero() {
        calculator.divide(10, 0)
    }
}
