package hu.ait.shoppinglist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_splah.*


class SplahActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splah)

        var demoAnim = AnimationUtils.loadAnimation(
            this@SplahActivity, R.anim.welcome
        )


        demoAnim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {

            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })

        TodoTitle.startAnimation(demoAnim)
        welcomeText.startAnimation(demoAnim)


        val background = object : Thread() {
            override fun run() {
                try {
                    // Thread will sleep for 5 seconds
                    Thread.sleep(5000)

                    // After 5 seconds redirect to another intent
                    val myIntent = Intent(baseContext, ScrollingActivity::class.java)
                    startActivity(myIntent)

                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@SplahActivity, "Going to Main Activity", Toast.LENGTH_LONG).show()
                }

            }
        }

        background.start()
    }
}
