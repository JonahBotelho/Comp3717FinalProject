package com.bcit.final_project.data

fun Recipe.asFavourite(
    savedAtEpochMillis: Long = System.currentTimeMillis()
): Recipe {
    require(id.isNotBlank()) {
        "Favourite recipes must have a non-blank id"
    }

    return copy(savedAtTime = savedAtEpochMillis)
}
