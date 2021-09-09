package com.example.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation_bar.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity() , TextToSpeech.OnInitListener {

    private var restTimer : CountDownTimer ?= null
    private var restProgress = 0
    private var restTimerDuration:Long=1

    private var exerciseTimer : CountDownTimer ?= null
    private var exerciseProgress = 0

    private var exerciseList : ArrayList<ExerciseModel>?=null
    private var currentexercisepossition=-1
    private var player:MediaPlayer?=null


    private var tts:TextToSpeech?=null

    private var exerciseAdapter : ExerciseStatusAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)


        setSupportActionBar(toolbar_exercise_activity)
        val actionbar = supportActionBar
        if(actionbar!=null)
        {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }

        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }


        tts = TextToSpeech(this,this)



        exerciseList=Constants.defaultExerciseList()

        setupRestView()

        setUpExerciseStatusRecyclerView()
    }

    public override fun onDestroy() {

        if(restTimer!=null)
        {
            restTimer!!.cancel()
            restProgress=0
        }

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if(tts!=null){
            tts!!.stop()
            tts!!.shutdown()
        }

        if(player!=null){
            player!!.stop()
        }

        super.onDestroy()

    }


    private fun setupRestView() {

        try {
            player = MediaPlayer.create(applicationContext,R.raw.press_start)
            player!!.isLooping=false
            player!!.start()
        }
        catch (e: Exception){
            e.printStackTrace()
        }


        llRestView.visibility=View.VISIBLE
        llExerciseView.visibility=View.GONE
        restProgress=0
        if(restTimer!=null)
        {
            restTimer!!.cancel()
            restProgress=0
        }
        tv_upcoming_exercise_name.text = exerciseList!![currentexercisepossition+1].getName()
        setRestProgressBar()
    }



    private fun setRestProgressBar()
    {
        progressBar.progress = restProgress
        object : CountDownTimer(1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress=10-restProgress
                tvTimer.text = (10-restProgress).toString()

            }



            override fun onFinish() {

                currentexercisepossition++

                exerciseList!![currentexercisepossition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }
        }.start()
    }
    private fun setupExerciseView(){
        llRestView.visibility= View.GONE
        llExerciseView.visibility=View.VISIBLE
        if(exerciseTimer!=null)
        {
            exerciseTimer!!.cancel()
            exerciseProgress=0
        }


        speakOut(exerciseList!![currentexercisepossition].getName())

        ivImage.setImageResource(exerciseList!![currentexercisepossition].getImage())
        tvExerciseName.text = exerciseList!![currentexercisepossition].getName()

        setExerciseProgressBar()

    }

    private fun setExerciseProgressBar()
    {
        progressBar.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(1000,1000){
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBar.progress=30-exerciseProgress
                tvExerciseTimer.text = (30-exerciseProgress).toString()

            }

            override fun onFinish() {
                if(currentexercisepossition < 11)
                {
                    exerciseList!![currentexercisepossition].setIsSelected(false)
                    exerciseList!![currentexercisepossition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
                    setupRestView()
                }
                else
                {
                    finish()
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                }
            }
        }.start()
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS)
        {
            val result = tts!!.setLanguage(Locale.ENGLISH)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            {
                Log.e("TTS","The Language Specified Is Not Supported!")
            }
        }
        else
        {
            Log.e("TTS","Initialization Failed!")
        }

    }

    private fun speakOut(text : String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }

    private fun setUpExerciseStatusRecyclerView(){
        rvExerciseStatus.layoutManager=LinearLayoutManager(this,
            LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!,this)
        rvExerciseStatus.adapter=exerciseAdapter

    }


    private fun customDialogForBackButton(){
        val customDialogback = Dialog(this)

        customDialogback.setContentView(R.layout.dialog_custom_back_confirmation_bar)

        customDialogback.btnyes.setOnClickListener{
            finish()
            customDialogback.dismiss()
        }

        customDialogback.btnno.setOnClickListener {
            customDialogback.dismiss()
        }


        customDialogback.show()


    }

}

