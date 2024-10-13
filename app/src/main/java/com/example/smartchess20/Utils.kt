package com.example.smartchess20

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.animation.AlphaAnimation
import android.widget.TextView
import android.widget.Toast

class Utils {
    /* Copies text to a clipboard */
    fun copyToClipboard(context: Context, textToCopy: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Copied Text", textToCopy)
        clipboard.setPrimaryClip(clip)
    }

    /* makes a long Toast message */
    fun showLongToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    /* makes a short Toast message */
    fun showShortToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    /* makes textView blink */
    fun startBlinkingAnimation(textView: TextView) {
        val blink = AlphaAnimation(0.0f, 1.0f)
        blink.duration = 300
        blink.repeatMode = AlphaAnimation.REVERSE
        textView.startAnimation(blink)
    }
}