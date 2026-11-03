package com.digitalsamurai.automaton.sample

import android.content.pm.PackageManager
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import com.kaspersky.kaspresso.device.Device
import com.kaspersky.kaspresso.device.server.AdbServer
import kotlin.Boolean
import kotlin.String

/**
 * WARN! AUTO-GENERATED
 * Generate with love by Automaton
 * Datetime: 1793723105726
 */
public class AutomatonPermissionManager {
  /**
   * Применяет требуемое состояние разрешения, если оно отличается от текущего.
   *
   * @param device устройство и контекст тестируемого приложения.
   * @param adbServer ADB server для устройств с API 26–27.
   * @param permission системное имя разрешения.
   * @param isGranted требуемое состояние разрешения.
   * @return Unit после применения состояния или без действия, если состояние уже совпадает.
   */
  public fun applyPermission(
    device: Device,
    adbServer: AdbServer,
    permission: String,
    isGranted: Boolean,
  ) {
    if (isGranted == isPermissionGranted(device, permission)) {
      return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      if (isGranted) {
        InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(device.targetContext.packageName, permission)
      } else {
        InstrumentationRegistry.getInstrumentation().uiAutomation.revokeRuntimePermission(device.targetContext.packageName, permission)
      }
    } else {
      val operation = if (isGranted) "grant" else "revoke"
      adbServer.performShell("pm " + operation + " " + device.targetContext.packageName + " " + permission)
    }
  }

  /**
   * Проверяет текущее состояние разрешения приложения.
   *
   * @param device устройство и контекст тестируемого приложения.
   * @param permission системное имя разрешения.
   * @return true, если разрешение уже выдано приложению.
   */
  private fun isPermissionGranted(device: Device, permission: String): Boolean = device.targetContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}
