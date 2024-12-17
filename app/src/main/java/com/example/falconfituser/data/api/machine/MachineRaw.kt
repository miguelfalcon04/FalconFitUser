package com.example.falconfituser.data.api.machine

class MachineRaw (
    val id: Int,
    val attributes: MachineRawAttributes
)

data class PhotoRaw(
    val id: Int,
    val attributes: PhotoRawAttributes
)

data class MachineRawAttributes(
    val title: String,
    val subtitle: String,
    val description: String,
    val photo: PhotoRawAttributes
)

data class PhotoRawAttributes(
    val name: String,
    val formats: FormatPhoto?
)

data class FormatPhoto(
    val small: PhotoDetail
)

data class PhotoDetail(
    val url: String
)