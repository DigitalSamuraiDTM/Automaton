package com.digitalsamurai.automaton.sample

import androidx.appcompat.app.AppCompatDelegate
import kotlin.Int

/**
 * WARN! AUTO-GENERATED
 * Generate with love by Automaton
 * Datetime: 04.11.2026 03:03:59
 */
public class AutomatonThemeManager {
  private var originalNightMode: Int? = null

  /**
   * Сохраняет исходный night mode и применяет тему конкретного теста к приложению.
   *
   * @param theme тема, заданная в семантической модели теста.
   * @return Unit после сохранения исходного режима и применения заданной темы приложения.
   */
  public fun applyTheme(theme: AutomatonTheme) {
    if (originalNightMode == null) {
      originalNightMode = AppCompatDelegate.getDefaultNightMode()
    }
    val nightMode = when (theme) {
      AutomatonTheme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
      AutomatonTheme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
      AutomatonTheme.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
    AppCompatDelegate.setDefaultNightMode(nightMode)
  }

  /**
   * Восстанавливает исходный night mode приложения и очищает сохранённое значение.
   *
   * @return Unit после восстановления режима или без действия, если исходный режим не сохранён.
   */
  public fun restoreTheme() {
    val nightMode = originalNightMode ?: return
    AppCompatDelegate.setDefaultNightMode(nightMode)
    originalNightMode = null
  }
}
