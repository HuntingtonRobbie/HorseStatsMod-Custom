package dev.gallon.domain;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ModConfigTest {
    @Test
    void declaresNoStaticFieldSoConfigGuisDoNotTryToWriteIt() {
        for (Field field : ModConfig.class.getDeclaredFields()) {
            assertFalse(Modifier.isStatic(field.getModifiers()), field.getName() + " must not be static");
        }
    }

    @Test
    void resetsEveryNullConfigEntryToItsDefaultValue() throws IllegalAccessException {
        ModConfig config = new ModConfig();

        for (Field field : ModConfig.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !field.getType().isPrimitive()) {
                field.setAccessible(true);
                field.set(config, null);
            }
        }

        config.resetInvalidValues();

        for (Field field : ModConfig.class.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !field.getType().isPrimitive()) {
                field.setAccessible(true);
                assertNotNull(field.get(config), field.getName() + " was not reset");
            }
        }

        assertTrue(config.getDisplayStatsInInventory());
        assertEquals(InteractionKind.RIGHT_CLICK, config.getDisplayStatsOnInteraction());
        assertTrue(config.getColoredStats());
        assertEquals(DisplayMinMax.DISABLED, config.getDisplayMinMax());
        assertFalse(config.getDisplayStatsInPercentage());
        assertEquals(GroupedKind.INDIVIDUAL, config.getGroupedStats());
        assertTrue(config.getIncludeAttributeModifiers());
        assertEquals(AboveHeadKind.DISABLED, config.getDisplayStatsAboveHead());
        assertTrue(config.getExtendOverlayDuration());
        assertEquals(10, config.getOverlayDurationSeconds());
    }

    @Test
    void clampsTheOverlayDurationToOneToThreeHundredSeconds() {
        ModConfig config = new ModConfig();

        config.setOverlayDurationSeconds(0);
        assertEquals(1, config.getOverlayDurationSeconds());

        config.setOverlayDurationSeconds(1000);
        assertEquals(300, config.getOverlayDurationSeconds());
    }

    @Test
    void gettersRemainSafeBeforePostLoadValidationRuns() throws ReflectiveOperationException {
        ModConfig config = new ModConfig();
        Field groupedStats = ModConfig.class.getDeclaredField("groupedStats");
        groupedStats.setAccessible(true);
        groupedStats.set(config, null);

        assertEquals(GroupedKind.INDIVIDUAL, config.getGroupedStats());
    }
}
