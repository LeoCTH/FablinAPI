package io.github.fablinmc.fablin

import net.minecraft.nbt.*



// to not clog up the main classes :yeef:

// me: mom can we have partial classes
// kotlin: we already have partial classes at home
// partial classes at home:

@NbtDslMarker
internal interface TagExt {

}

@NbtDslMarker
internal interface ValueTagExt: TagExt {

}

@NbtDslMarker
internal interface StructureTagExt: TagExt {



}
