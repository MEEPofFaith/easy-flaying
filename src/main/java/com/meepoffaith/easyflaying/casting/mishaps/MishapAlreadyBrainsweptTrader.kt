package com.meepoffaith.easyflaying.casting.mishaps

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.pigment.FrozenPigment
import at.petrak.hexcasting.api.utils.TreeList
import at.petrak.hexcasting.mixin.accessor.AccessorLivingEntity
import de.maxhenkel.easyvillagers.blocks.tileentity.TraderTileentityBase
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Mob
import net.minecraft.world.item.DyeColor

class MishapAlreadyBrainsweptTrader(val trader: TraderTileentityBase, val mob: Mob) : Mishap() {
    override fun accentColor(env: CastingEnvironment, errorCtx: Context): FrozenPigment =
        dyeColor(DyeColor.GREEN)

    override fun execute(env: CastingEnvironment, errorCtx: Context, stack: TreeList<Iota>): TreeList<Iota> {
        trader.removeVillager()
        val sound = (mob as AccessorLivingEntity).`hex$getDeathSound`()
        if (sound != null)
            env.getWorld().playSound(null, trader.blockPos, sound, SoundSource.AMBIENT, 0.8f, 1f)
        return stack
    }

    override fun particleSpray(env: CastingEnvironment) =
        ParticleSpray.burst(trader.blockPos.center, 2.0)

    override fun errorMessage(env: CastingEnvironment, errorCtx: Context) =
        error("already_brainswept")

}
