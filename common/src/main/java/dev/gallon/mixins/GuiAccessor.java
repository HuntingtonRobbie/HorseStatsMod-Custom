package dev.gallon.mixins;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Gui.class)
public interface GuiAccessor {
    // Ticks left before the overlay (action bar) message disappears. Vanilla sets it to 60 (3 seconds).
    @Accessor("overlayMessageTime")
    void setOverlayMessageTime(int overlayMessageTime);
}
