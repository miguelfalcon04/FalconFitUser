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
    val photo: PhotoWrapper?
)

data class PhotoWrapper(
    val data: List<PhotoRaw>? //Me devuelve la lista de fotos (small, thumbnail, hash)
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