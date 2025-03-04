package org.thoughtcrime.securesms.util

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

object SystemWindowInsetsSetter {
  /**
   * Updates the view whenever a layout occurs to properly set the system bar insets. setPadding is safe here because it will only trigger an extra layout
   * call IF the values actually changed.
   */
  fun attach(view: View, lifecycleOwner: LifecycleOwner, @WindowInsetsCompat.Type.InsetsType insetType: Int = WindowInsetsCompat.Type.systemBars()) {
    val listener = view.doOnEachLayout {
      val insets = ViewCompat.getRootWindowInsets(view)?.getInsets(insetType)

      if (insets != null) {
        view.setPadding(
          insets.left,
          insets.top,
          insets.right,
          insets.bottom
        )
      } else {
        view.setPadding(
          0,
          ViewUtil.getStatusBarHeight(view),
          0,
          ViewUtil.getNavigationBarHeight(view)
        )
      }
    }

    val lifecycleObserver = object : DefaultLifecycleObserver {
      override fun onDestroy(owner: LifecycleOwner) {
        view.removeOnLayoutChangeListener(listener)
      }
    }

    lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
  }
}
