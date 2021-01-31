package io.github.fablinmc.fablin

import net.minecraft.nbt.*
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag as VListTag


@NbtDslMarker
class CompoundTagScope(internal val underlying: CompoundTag): StructureTagExt {
    infix fun String.to(tag: Tag) {
        underlying[this] = tag
    }
    infix fun String.to(value: Byte)    { this to ByteTag.of(value)     }
    infix fun String.to(value: Short)   { this to ShortTag.of(value)    }
    infix fun String.to(value: Int)     { this to IntTag.of(value)      }
    infix fun String.to(value: Long)    { this to LongTag.of(value)     }
    infix fun String.to(value: Float)   { this to FloatTag.of(value)    }
    infix fun String.to(value: Double)  { this to DoubleTag.of(value)   }
    infix fun String.to(value: String)  { this to StringTag.of(value)   }

    infix fun String.to(value: Boolean) {
        this to if (value) ByteTag.ONE else ByteTag.ZERO
    }

    // cant use String#to(Tag) because `*ArrayTag`s are also `List`s
    infix fun String.to(arr: ByteArray) { underlying[this] = ByteArrayTag(arr) }
    infix fun String.to(arr: IntArray)  { underlying[this] = IntArrayTag(arr) }
    infix fun String.to(arr: LongArray) { underlying[this] = LongArrayTag(arr) }

    infix fun String.to(list: VListTag) {
        underlying[this] = list
    }
    infix fun String.to(list: List<Tag>) {
        val listTag = net.minecraft.nbt.ListTag()
        listTag.addAll(list)
        underlying[this] = listTag
    }

    infix fun String.toCompound(scopeInit: CompoundTagScope.() -> Unit) { this to CompoundTag(scopeInit) }
}

operator fun CompoundTag.set(key: String, tag: Tag): Tag? = put(key, tag)
operator fun CompoundTagScope.set(key: String, tag: Tag): Tag? = underlying.put(key, tag)

@Suppress("FunctionName")
@NbtDslMarker
fun CompoundTag(scopeInit: CompoundTagScope.() -> Unit = {}): CompoundTag {
    val tag = CompoundTag()
    val s = CompoundTagScope(tag)
    s.scopeInit()
    return tag
}

val tag = CompoundTag {
    "nested" toCompound {
        "test" to 0
        "bytes" to byteArrayOf(0, 1, 3, 0x7F)
        "ints" to intArrayOf(1, 2, 3, 4)
        "woow" to ListTag(
            CompoundTag {

            }
        )
    }
}

