package karage.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import karage.app.databinding.ActivityMainBinding

/**
 * Main activity for the Karage calculator application.
 *
 * This activity provides a simple calculator interface where users can perform
 * basic arithmetic operations on two numbers.
 *
 * @since 1.0.0
 */
class MainActivity : AppCompatActivity() {
    /**
     * View binding for the activity layout.
     */
    private lateinit var binding: ActivityMainBinding

    /**
     * Calculator instance for performing arithmetic operations.
     */
    private val calculator = Calculator()

    /**
     * Called when the activity is first created.
     *
     * Initializes the activity, sets up view binding, and configures calculator buttons.
     *
     * @param savedInstanceState Bundle containing the activity's previously saved state, or null
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCalculatorButtons()
    }

    /**
     * Sets up click listeners for all calculator operation buttons.
     *
     * Each button is configured to perform its respective arithmetic operation
     * when clicked by the user.
     */
    private fun setupCalculatorButtons() {
        binding.buttonAdd.setOnClickListener {
            performCalculation { num1, num2 -> calculator.add(num1, num2) }
        }

        binding.buttonSubtract.setOnClickListener {
            performCalculation { num1, num2 -> calculator.subtract(num1, num2) }
        }

        binding.buttonMultiply.setOnClickListener {
            performCalculation { num1, num2 -> calculator.multiply(num1, num2) }
        }

        binding.buttonDivide.setOnClickListener {
            performCalculation { num1, num2 -> calculator.divide(num1, num2) }
        }
    }

    /**
     * Performs a calculation using the provided operation function.
     *
     * Extracts numbers from the input fields, applies the given operation,
     * and displays the result. Handles input validation and error cases.
     *
     * @param operation Lambda function that takes two integers and returns a result
     */
    private fun performCalculation(operation: (Int, Int) -> Int) {
        try {
            val num1 = binding.inputNum1.text.toString().toInt()
            val num2 = binding.inputNum2.text.toString().toInt()
            val result = operation(num1, num2)
            binding.resultText.text = "Result: $result"
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }
}
