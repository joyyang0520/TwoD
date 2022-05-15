package tw.edu.pu.csim.cyyang.twod

import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.coroutines.*
import tw.edu.pu.csim.cyyang.twod.databinding.ActivityMainBinding

lateinit var job: Job

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var secondsLeft:Int = 1000  //倒數

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txv.text = secondsLeft.toString()
        binding.btnStart.isEnabled = true
        binding.btnStop.isEnabled = false

        binding.btnStart.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                //binding.txv.text = "開始倒數計時"
                binding.mysv.Score = 0
                job = GlobalScope.launch(Dispatchers.Main) {
                    while(secondsLeft > 0) {
                        secondsLeft--
                        binding.txv.text = secondsLeft.toString()
                        binding.btnStart.isEnabled = false
                        binding.btnStop.isEnabled = true
                        delay(25)

                        var canvas: Canvas = binding.mysv.surfaceHolder.lockCanvas()
                            binding.mysv.drawSomething(canvas)
                        binding.mysv.surfaceHolder.unlockCanvasAndPost(canvas)
                    }
                    secondsLeft = 1000
                    binding.btnStart.isEnabled = true
                    binding.btnStop.isEnabled = false
                }
            }
        })

        binding.btnStop.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                //binding.txv.text = "結束倒數計時"
                job.cancel()
                binding.btnStart.isEnabled = true
                binding.btnStop.isEnabled = false
            }
        })

    }


    override fun onPause() {
        super.onPause()
        job.cancel()
    }

    override fun onResume() {
        super.onResume()
        if (binding.btnStart.isEnabled == false){
            job = GlobalScope.launch(Dispatchers.Main) {
                while(secondsLeft > 0) {
                    secondsLeft--
                    binding.txv.text = secondsLeft.toString()
                    delay(25)
                }
                secondsLeft = 1000
                binding.btnStart.isEnabled = true
                binding.btnStop.isEnabled = false
            }
        }
    }

}
