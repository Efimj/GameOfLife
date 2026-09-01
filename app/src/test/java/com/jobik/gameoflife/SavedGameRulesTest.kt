package com.jobik.gameoflife

import com.jobik.gameoflife.gameOfLife.GameOfLife.Companion.GameOfLifeStepSettings
import com.jobik.gameoflife.gameOfLife.GameSettings
import com.jobik.gameoflife.screens.game.GameRuleSet
import com.jobik.gameoflife.screens.game.SavedGameRules
import com.jobik.gameoflife.screens.game.ruleSetId
import com.jobik.gameoflife.screens.game.toRuleNotation
import com.jobik.gameoflife.util.settings.SettingsState
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SavedGameRulesTest {
    @Test
    fun savedRulesSurviveSerialization() {
        val savedRules = SavedGameRules(
            id = "rule-id",
            name = "High Life",
            rules = GameOfLifeStepSettings(
                neighborsForReviving = setOf(3, 6),
                neighborsForAlive = setOf(2, 3),
            ),
        )

        val restored = Json.decodeFromString<SavedGameRules>(
            Json.encodeToString(savedRules)
        )

        assertEquals(savedRules, restored)
        assertEquals("B36/S23", restored.rules.toRuleNotation())
    }

    @Test
    fun oldSettingsWithoutSavedRulesRemainCompatible() {
        val restored = Json.decodeFromString<SettingsState>("{}")

        assertTrue(restored.savedGameRules.isEmpty())
    }

    @Test
    fun equalRulesKeepDifferentSavedIdentities() {
        val rules = GameOfLifeStepSettings(
            neighborsForReviving = setOf(3),
            neighborsForAlive = setOf(2, 3),
        )
        val first = SavedGameRules(id = "first", name = "First", rules = rules)
        val second = SavedGameRules(id = "second", name = "Second", rules = rules)

        assertNotEquals(first.id, second.id)
        assertNotEquals(first, second)
    }

    @Test
    fun standardRuleSetIdsAreUnique() {
        val ids = GameRuleSet.map { it.ruleSetId() }

        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun selectedRuleSetIdSurvivesSerialization() {
        val settings = GameSettings(selectedRuleSetId = "saved-rule-id")

        val restored = Json.decodeFromString<GameSettings>(Json.encodeToString(settings))

        assertEquals("saved-rule-id", restored.selectedRuleSetId)
    }
}
