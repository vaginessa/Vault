package com.arsvechkarev.vault.features.common

import android.graphics.drawable.Drawable
import com.arsvechkarev.vault.R
import com.arsvechkarev.vault.viewdsl.ContextHolder
import com.arsvechkarev.vault.viewdsl.retrieveDrawable

private const val AMAZON = "amazon"
private const val GOOGLE = "google"
private const val FACEBOOK_MESSENGER = "facebook messenger"
private const val FACEBOOK = "facebook"
private const val MICROSOFT = "microsoft"
private const val NETFLIX = "netflix"
private const val INSTAGRAM = "instagram"
private const val SNAPCHAT = "snapchat"
private const val TELEGRAM = "telegram"
private const val TWITTER = "twitter"
private const val SPOTIFY = "spotify"
private const val VK = "vk"
private const val WHATSAPP = "whatsapp"
private const val GITHUB = "github"

fun getIconForServiceName(inputText: String): Drawable? {
  val text = inputText.trim()
  if (text.isBlank()) return null
  val drawable: (Int) -> Drawable = { ContextHolder.context.retrieveDrawable(it) }
  return when {
    text has AMAZON -> drawable(R.drawable.icon_amazon)
    text has GOOGLE -> drawable(R.drawable.icon_google)
    text has FACEBOOK_MESSENGER -> drawable(R.drawable.icon_facebook_messenger)
    text has FACEBOOK -> drawable(R.drawable.icon_facebook)
    text has MICROSOFT -> drawable(R.drawable.icon_microsoft)
    text has NETFLIX -> drawable(R.drawable.icon_netfilx)
    text has INSTAGRAM -> drawable(R.drawable.icon_instagram)
    text has SNAPCHAT -> drawable(R.drawable.icon_snapchat)
    text has TELEGRAM -> drawable(R.drawable.icon_telegram)
    text has TWITTER -> drawable(R.drawable.icon_twitter)
    text has SPOTIFY -> drawable(R.drawable.icon_spotify)
    text has VK -> drawable(R.drawable.icon_vk)
    text has WHATSAPP -> drawable(R.drawable.icon_whatsapp)
    text has GITHUB -> drawable(R.drawable.icon_github)
    else -> null
  }
}

private infix fun String.has(text: String): Boolean {
  return this.contains(text, ignoreCase = true)
}