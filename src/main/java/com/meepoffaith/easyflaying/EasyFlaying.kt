package com.meepoffaith.easyflaying

import com.meepoffaith.easyflaying.datagen.EasyFlayingDatagen
import com.meepoffaith.easyflaying.init.EasyFlayingActions
import com.meepoffaith.easyflaying.init.EasyFlayingRegistrar
import net.minecraft.resources.ResourceLocation
import net.neoforged.bus.api.IEventBus
import net.neoforged.fml.ModContainer
import net.neoforged.fml.ModList
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.registries.RegisterEvent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Mod(EasyFlaying.MODID)
class EasyFlaying(modBus: IEventBus, container: ModContainer){
    init{
        modBus.apply {
            addListener(EasyFlayingClient::init)
            addListener(EasyFlayingDatagen::init)
        }

        initRegistry(EasyFlayingActions)
    }

    fun <T : Any> initRegistry(registrar: EasyFlayingRegistrar<T>) {
        container.eventBus!!.addListener { event: RegisterEvent ->
            event.register(registrar.registryKey) { helper ->
                registrar.init(helper::register)
            }
        }
    }

    companion object{
        const val MODID = "easyflaying"

        @JvmField
        val LOGGER: Logger = LogManager.getLogger(MODID)

        @JvmStatic
        fun id(path: String): ResourceLocation = ResourceLocation.fromNamespaceAndPath(MODID, path)

        internal val container: ModContainer
            get() = ModList.get().getModContainerById(MODID).get()
    }
}
