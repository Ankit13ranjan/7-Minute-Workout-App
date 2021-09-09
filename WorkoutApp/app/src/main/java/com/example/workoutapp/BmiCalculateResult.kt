package com.example.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_bmi_calculate_result.*
import kotlinx.android.synthetic.main.activity_bmi_calculate_result.toolbar_BMI_Activity
import kotlinx.android.synthetic.main.activity_bmiactivity.*

class BmiCalculateResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi_calculate_result)

        setSupportActionBar(toolbar_BMI_Activity)

        val actionbar = supportActionBar

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title = "CALCULATE BMI"
        }

        toolbar_BMI_Activity.setNavigationOnClickListener {
            onBackPressed()
        }

    }
}