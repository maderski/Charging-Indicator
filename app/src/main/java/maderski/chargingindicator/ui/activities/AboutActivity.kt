package maderski.chargingindicator.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import maderski.chargingindicator.R
import java.util.*

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        supportActionBar?.let {
            Objects.requireNonNull(it).setDisplayHomeAsUpEnabled(true)
        }
        setVersionText()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    //Smooth fade transition from about activity back to the main activity
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(0, R.animator.fadeout)
    }

    //Displays version number
    private fun setVersionText() {
        try {
            val versionTV: TextView = findViewById(R.id.versionTxt)
            val pkgInfo = packageManager.getPackageInfo(packageName, 0)
            val versionInfo = "v" + pkgInfo.versionName
            versionTV.text = versionInfo
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    companion object {
        private val TAG = AboutActivity::class.java.name
    }
}
