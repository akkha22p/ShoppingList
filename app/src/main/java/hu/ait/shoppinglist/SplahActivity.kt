package hu.ait.shoppinglist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast


class SplahActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splah)

        val background = object : Thread() {
            override fun run() {
                try {
                    // Thread will sleep for 5 seconds
                    Thread.sleep(5000)

                    // After 5 seconds redirect to another intent
                    val myIntent = Intent(baseContext, MainActivity::class.java)
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
