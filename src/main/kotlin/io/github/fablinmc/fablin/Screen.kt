package io.github.fablinmc.fablin

import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.screen.PropertyDelegate
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.slot.Slot
import net.minecraft.world.GameMode

const val SLOT_DIMENSIONS = 18

fun addSlots2D(
    inv: Inventory,
    offsetX: Int, offsetY: Int,
    rows: Int, columns: Int,
    startIndex: Int = 0,
    spacingX: Int = SLOT_DIMENSIONS,
    spacingY: Int = SLOT_DIMENSIONS,
    addSlot: (Slot) -> Slot,
) {
    var curX = offsetX
    var curY = offsetY
    var index = startIndex
    for (i in 0 until rows) {
        for (j in 0 until columns) {
            addSlot(Slot(inv, index, curX, offsetY))
            ++index
            curX += spacingX
        }
        curX = offsetX // reset the current x pos and start again
        curY += spacingY
    }
}

fun addSlots1D(
    inv: Inventory,
    offsetX: Int, offsetY: Int,
    columns: Int,
    startIndex: Int = 0,
    spacingX: Int = SLOT_DIMENSIONS,
    addSlot: (Slot) -> Slot,
) {
    var curX = offsetX
    var curY = offsetY
    var index = startIndex
    for (i in 0 until columns) {
        addSlot(Slot(inv, index, curX, offsetY))
        ++index
        curX += spacingX
    }
}

object PlayerInventorySlots {
    fun addMainSlots(
        inv: PlayerInventory,
        offsetX: Int, offsetY: Int,
        spacingX: Int = SLOT_DIMENSIONS,
        spacingY: Int = SLOT_DIMENSIONS,
        addSlot: (Slot) -> Slot,
    ) {
        addSlots2D(
            inv,
            offsetX, offsetY,
            rows = 3, columns = 9,
            startIndex = 9,
            spacingX, spacingY,
            addSlot
        )
    }

    fun addHotbarSlots(
        inv: PlayerInventory,
        offsetX: Int, offsetY: Int,
        spacingX: Int = SLOT_DIMENSIONS,
        addSlot: (Slot) -> Slot,
    ) {
        addSlots1D(
            inv,
            offsetX, offsetY,
            columns = 3,
            startIndex = 0,
            spacingX,
            addSlot
        )
    }
}

inline fun <reified T: Enum<T>> toEnum(): (Int) -> T? = { enumValues<T>()[it] }


operator fun <T> PropertyDelegate.get(index: Int, parser: (Int) -> T?): T? = parser(this[index])

operator fun <T> PropertyDelegate.set(index: Int, value: T?, parser: (T?) -> Int) {
    this[index] = parser(value)
}

inline fun <reified T: Enum<T>> PropertyDelegate.getEnum(index: Int) = get(index, toEnum<T>())
inline fun <reified T: Enum<T>> PropertyDelegate.getEnum(index: Int, default: T) = getEnum(index) ?: default


class TestScreenHandler(
    playerInventory: PlayerInventory,
    private val propertyDelegate: PropertyDelegate,
    syncId: Int)

    : ScreenHandler(null, syncId)
{

    val gameMode: GameMode
        @Environment(EnvType.CLIENT) get() = propertyDelegate.getEnum(0, GameMode.NOT_SET)

    init {
        addProperties(propertyDelegate)
        PlayerInventorySlots.addHotbarSlots(playerInventory, 0, 0, addSlot = this::addSlot)
        PlayerInventorySlots.addMainSlots(playerInventory, 0, 0, addSlot = this::addSlot)


    }

    override fun canUse(player: PlayerEntity?) = true

}