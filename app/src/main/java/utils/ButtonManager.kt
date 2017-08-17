package utils


import android.support.v7.app.AppCompatActivity
import android.widget.Button

import com.example.hyunjin.activities.MainActivity

/**
 * Created by HyunJin on 7/28/2017.
 */

class ButtonManager : AppCompatActivity() {
    fun toggleButton(button: Button, bool: Boolean?) {
        if (bool!!) {
            button.isEnabled = bool
        } else {
            button.isEnabled = bool
        }
    }
}
