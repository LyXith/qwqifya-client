package qwq.qwqifya.qwqifyaClient.mixins;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MinecartItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static qwq.qwqifya.qwqifyaClient.utils.CheckData.checkEntityData;

@Mixin(MinecartItem.class)
public class UseCommandBlockMinecartListener {
    @Inject(method = "useOn",at = @At("HEAD"),cancellable = true)
    public void useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        Player player = useOnContext.getPlayer();
        ItemStack item = useOnContext.getItemInHand();
        if(!checkEntityData(item,player)) {
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }
}
