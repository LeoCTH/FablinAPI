package io.github.fablinmc.fablin

import net.minecraft.nbt.*

// to not clog up the main classes :yeef:

// me: mom can we have partial classes
// kotlin: we already have partial classes at home
// partial classes at home:

internal interface TagExt {
    val underlying: CompoundTag

    infix fun String.to(tag: Tag) {
        underlying[this] = tag
    }
}

internal interface ValueTagExt: TagExt {
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
}

internal interface StructureTagExt: TagExt {
    // cant use String#to(Tag) because `*ArrayTag`s are also `List`s
    infix fun String.to(arr: ByteArray) { underlying[this] = ByteArrayTag(arr) }
    infix fun String.to(arr: IntArray)  { underlying[this] = IntArrayTag(arr) }
    infix fun String.to(arr: LongArray) { underlying[this] = LongArrayTag(arr) }

    infix fun String.to(list: List<Tag>) {
        val listTag = ListTag()
        listTag.addAll(list)
        underlying[this] = listTag
    }
}
