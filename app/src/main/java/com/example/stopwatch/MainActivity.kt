package com.example.stopwatch

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Chronometer
import android.widget.NumberPicker
import android.widget.Toast
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var isrunning = false
    private var minutes: String? = "00:00:00"
    private var seconds: String? = "00:00:00"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        var laplist = mutableListOf<String>()
        var arrayadapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, laplist)
        binding.listview.adapter = arrayadapter

        //set timer
        binding.timerImg.setOnClickListener {
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.dialogue)
            dialog.show()
            var timertime_mins = dialog.findViewById<NumberPicker>(R.id.numberpicker_minutes)
            var timertime_secs = dialog.findViewById<NumberPicker>(R.id.numberpicker_seconds)

            timertime_mins.minValue = 0
            timertime_mins.maxValue = 10

            timertime_secs.minValue = 0
            timertime_secs.maxValue = 60

            dialog.findViewById<Button>(R.id.confirm_btn).setOnClickListener {
                binding.timerMins.text = timertime_mins.value.toString() + " Mins"
                binding.timerSecs.text = timertime_secs.value.toString() + " secs"
                minutes = timertime_mins.value.toString()
                seconds = timertime_secs.value.toString()
                dialog.dismiss()
            }
        }

        binding.runBtn.setOnClickListener {
            if (!isrunning) {
                isrunning = true
                binding.runBtn.text = "Stop"
                binding.chronometer.base = SystemClock.elapsedRealtime()
                binding.chronometer.start()
                if (!minutes.equals("00:00:00")) {
                    var total_time = minutes!!.toInt() * 60 * 1000L + seconds!!.toInt() * 1000L
                    binding.chronometer.base = SystemClock.elapsedRealtime() + total_time
                    binding.chronometer.start()

                    binding.chronometer.onChronometerTickListener =
                        Chronometer.OnChronometerTickListener {
                            var elpased_time =
                                SystemClock.elapsedRealtime() - binding.chronometer.base
//                                Toast.makeText(this,"time starts base = ${elpased_time}",Toast.LENGTH_SHORT).show()
                            if (elpased_time >= 0) {
                                binding.chronometer.stop()
                                isrunning = false
                                binding.runBtn.text = "Run"
                            }
                        }
                }
            } else {
                binding.chronometer.stop()
                isrunning = false
                binding.runBtn.text = "Run"
            }

        }


        //lap btn
        binding.lapBtn.setOnClickListener {
            laplist.add(binding.chronometer.text.toString())
            arrayadapter.notifyDataSetChanged()
        }
    }
}


