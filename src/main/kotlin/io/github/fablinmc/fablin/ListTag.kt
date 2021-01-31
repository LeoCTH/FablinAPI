package io.github.fablinmc.fablin

import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag

@NbtDslMarker
class ListTagScope<T: Tag>(internal val underlying: ListTag) {
    operator fun T.unaryPlus() {
        underlying += this
    }
}

@Suppress("FunctionName")
@NbtDslMarker
fun <T: Tag> ListTag(vararg values: T): ListTag {
    val tag = ListTag()
    tag.addAll(values)
    return tag
}

