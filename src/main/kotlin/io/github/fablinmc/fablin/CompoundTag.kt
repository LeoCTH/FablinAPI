package io.github.fablinmc.fablin

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag

@DslMarker
annotation class CompoundTagDslMarker

@CompoundTagDslMarker
class CompoundTagScope(override val underlying: CompoundTag): ValueTagExt, StructureTagExt

operator fun CompoundTag.set(key: String, tag: Tag): Tag? = put(key, tag)

@Suppress("FunctionName")
fun CompoundTag(scopeInit: CompoundTagScope.() -> Unit = {}): CompoundTag {
    val tag = CompoundTag()
    val s = CompoundTagScope(tag)
    s.scopeInit()
    return tag
}

val tag = CompoundTag {
    "nested" to CompoundTag {
        "test" to 0
        "bytes" to byteArrayOf(0, 1, 3, 0x7F)
        "ints" to intArrayOf(1, 2, 3, 4)
        "woo" to listOf()
    }
}

