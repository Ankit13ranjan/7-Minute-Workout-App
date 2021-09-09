package com.example.workoutapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_bmiactivity.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    val METRIC_UNITS_VIEW = "METRIC_UNITS_VIEW"
    val US_UNITS_VIEW = "US_UNIT_VIEW"

    var currentVisibleView:String = METRIC_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        setSupportActionBar(toolbar_BMI_Activity)

        val actionbar = supportActionBar

        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
            actionbar.title = "CALCULATE BMI"
        }

        toolbar_BMI_Activity.setNavigationOnClickListener {
            onBackPressed()
        }


        btncalculateunits.setOnClickListener {

            if(currentVisibleView==METRIC_UNITS_VIEW){
                if (validateMetricUnits()) {


                    val heightValue: Float = etmetricunitheight.text.toString().toFloat() / 100


                    val weightValue: Float = etMetricunitWeight.text.toString().toFloat()


                    val bmi = weightValue / (heightValue * heightValue)

                    displayBMIResult(bmi)
                } else {
                    Toast.makeText(this@BMIActivity, "Please enter valid values.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            else{
                if(validateUsUnits()){
                    val usUnitWeight = etUsUnitWeight.text.toString().toFloat()
                    val usUnitHeightFeet = etUsUnitHeightFeet.text.toString()
                    val usUnitHeightInch = etUsUnitHeightInch.text.toString()

                    val heightvalue = usUnitHeightInch.toFloat() + usUnitHeightFeet.toFloat() * 12
                    val bmi = 703*(usUnitWeight/(heightvalue * heightvalue))
                    displayBMIResult(bmi)
                }
                else{
                    Toast.makeText(this@BMIActivity,"Please enter valid values,",Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }


        makevisiblemetricunitsview()
        rgunit.setOnCheckedChangeListener { group, checkedId ->
            if(checkedId == R.id.rbMetricUnits){
                makevisiblemetricunitsview()
            }
            else{
                makevisibleusunitsview()
            }
        }

    }


    private fun makevisiblemetricunitsview(){
        currentVisibleView = METRIC_UNITS_VIEW

        llMetricUnitsView.visibility = View.VISIBLE


        etMetricunitWeight.text!!.clear()
        etmetricunitheight.text!!.clear()



        tilUsUnitWeight.visibility = View.GONE
        llUsUnitsHeight.visibility = View.GONE



        llDisplayBMIResults.visibility = View.INVISIBLE
    }
    private fun makevisibleusunitsview(){
        currentVisibleView = US_UNITS_VIEW

        tilUsUnitWeight.visibility = View.VISIBLE
        llUsUnitsHeight.visibility = View.VISIBLE

        etUsUnitHeightFeet.text!!.clear()
        etUsUnitHeightInch.text!!.clear()
        etUsUnitWeight.text!!.clear()

        llMetricUnitsView.visibility = View.GONE

        llDisplayBMIResults.visibility = View.INVISIBLE
    }


    private fun validateMetricUnits(): Boolean {
        var isValid = true

        if (etMetricunitWeight.text.toString().isEmpty()) {
            isValid = false
        } else if (etmetricunitheight.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }


    private fun validateUsUnits(): Boolean {
        var isValid = true

        if (etUsUnitWeight.text.toString().isEmpty()) {
            isValid = false
        }
        else if (etUsUnitHeightInch.text.toString().isEmpty()) {
            isValid = false
        }
        else if(etUsUnitHeightFeet.text.toString().isEmpty()){
            isValid = false
        }

        return isValid
    }

    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops!You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }



        llDisplayBMIResults.visibility=View.VISIBLE
//        tvyourbmi.visibility = View.VISIBLE
//        tvbmivalue.visibility = View.VISIBLE
//        tvbmitype.visibility = View.VISIBLE
//        tvbmidescription.visibility = View.VISIBLE


        val bmiValue = BigDecimal(bmi.toDouble()).setScale(2, RoundingMode.HALF_EVEN).toString()

        tvbmivalue.text = bmiValue
        tvbmitype.text = bmiLabel
        tvbmidescription.text = bmiDescription
    }

}