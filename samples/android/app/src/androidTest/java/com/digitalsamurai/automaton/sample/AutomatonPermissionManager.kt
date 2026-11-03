package com.digitalsamurai.automaton.sample

import android.content.pm.PackageManager
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import com.kaspersky.kaspresso.testcases.core.testcontext.BaseTestContext
import kotlin.Boolean
import kotlin.String

/**
 * WARN! AUTO-GENERATED
 * Generate with love by Automaton
 * Datetime: 1793742286016
 */
public class AutomatonPermissionManager {
  /**
   * Применяет требуемое состояние разрешения, если оно отличается от текущего.
   *
   * @param testContext контекст before-секции, предоставляющий device и adbServer.
   * @param permission системное имя разрешения.
   * @param isGranted требуемое состояние разрешения.
   * @return Unit после применения состояния или без действия, если состояние уже совпадает.
   */
  public fun applyPermission(
    testContext: BaseTestContext,
    permission: String,
    isGranted: Boolean,
  ) {
    if (isGranted == isPermissionGranted(testContext, permission)) {
      return
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
      if (isGranted) {
        InstrumentationRegistry.getInstrumentation().uiAutomation.grantRuntimePermission(testContext.device.targetContext.packageName, permission)
      } else {
        InstrumentationRegistry.getInstrumentation().uiAutomation.revokeRuntimePermission(testContext.device.targetContext.packageName, permission)
      }
    } else {
      val operation = if (isGranted) "grant" else "revoke"
      testContext.adbServer.performShell("pm " + operation + " " + testContext.device.targetContext.packageName + " " + permission)
    }
  }

  /**
   * Проверяет текущее состояние разрешения приложения.
   *
   * @param testContext контекст before-секции с доступом к тестируемому приложению.
   * @param permission системное имя разрешения.
   * @return true, если разрешение уже выдано приложению.
   */
  private fun isPermissionGranted(testContext: BaseTestContext, permission: String): Boolean = testContext.device.targetContext.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
}
