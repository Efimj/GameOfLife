package com.jobik.gameoflife.util.settings

import android.content.Context
import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettings
import com.jobik.gameoflife.screens.game.SavedGameRules
import java.util.UUID

object SavedGameRulesManager {
    fun save(
        context: Context,
        name: String,
        rules: GameOfLifeStepSettings,
    ): SavedGameRules {
        val savedRules = SavedGameRules(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            rules = rules,
        )
        SettingsManager.update(
            context = context,
            settings = SettingsManager.settings.copy(
                savedGameRules = SettingsManager.settings.savedGameRules + savedRules
            )
        )
        return savedRules
    }

    fun delete(context: Context, id: String) {
        SettingsManager.update(
            context = context,
            settings = SettingsManager.settings.copy(
                savedGameRules = SettingsManager.settings.savedGameRules.filterNot { it.id == id }
            )
        )
    }
}
