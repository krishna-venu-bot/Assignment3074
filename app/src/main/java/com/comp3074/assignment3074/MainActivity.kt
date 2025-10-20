package com.comp3074.assignment3074

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var etHours: EditText
    private lateinit var etRate: EditText
    private lateinit var etTaxRate: EditText
    private lateinit var tvPay: TextView
    private lateinit var tvOvertimePay: TextView
    private lateinit var tvTotalPay: TextView
    private lateinit var tvTax: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Toolbar for menu (needed for the ⋮ About entry)
        setSupportActionBar(findViewById(R.id.topAppBar))

        // Keep your insets handling
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1) Wire up views (IDs must match XML)
        etHours = findViewById(R.id.etHours)
        etRate = findViewById(R.id.etRate)
        etTaxRate = findViewById(R.id.etTaxRate)
        tvPay = findViewById(R.id.tvPay)
        tvOvertimePay = findViewById(R.id.tvOvertimePay)
        tvTotalPay = findViewById(R.id.tvTotalPay)
        tvTax = findViewById(R.id.tvTax)

        // 2) Attach click listener to the Calculate button
        findViewById<Button>(R.id.btnCalculate).setOnClickListener {
            calculateAndShow()
        }
    }

    // Overflow menu (About)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                startActivity(Intent(this, AboutActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun calculateAndShow() {
        val hours = etHours.text.toString().trim().toDoubleOrNull()
        val rate = etRate.text.toString().trim().toDoubleOrNull()
        val taxRate = etTaxRate.text.toString().trim().toDoubleOrNull()

        if (hours == null || rate == null || taxRate == null) {
            Toast.makeText(this, "Please enter all numbers", Toast.LENGTH_SHORT).show()
            return
        }
        if (hours < 0 || rate < 0 || taxRate < 0) {
            Toast.makeText(this, "Values must be non-negative", Toast.LENGTH_SHORT).show()
            return
        }

        val pay: Double
        val overtimePay: Double
        if (hours <= 40.0) {
            pay = hours * rate
            overtimePay = 0.0
        } else {
            pay = 40.0 * rate
            overtimePay = (hours - 40.0) * rate * 1.5
        }
        val totalPay = pay + overtimePay
        val tax = pay * taxRate   // tax is on base pay only

        tvPay.text = "Pay: %.2f".format(pay)
        tvOvertimePay.text = "Overtime Pay: %.2f".format(overtimePay)
        tvTotalPay.text = "Total Pay: %.2f".format(totalPay)
        tvTax.text = "Tax: %.2f".format(tax)
    }
}
