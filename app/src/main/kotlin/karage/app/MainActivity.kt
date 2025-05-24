package karage.app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import karage.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val calculator = Calculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupCalculatorButtons()
    }

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
