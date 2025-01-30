package com.example.falconfituser.data.api.exercise

class ExerciseRaw (
    val id: Int,
    val attributes: ExerciseRawAttributesMedia
)

class UserIdRaw(
    val id: Int
)

data class ExerciseRawAttributes(
    val title: String,
    val subtitle: String,
    val description: String,
    val userId: UserIdRaw,
)

data class ExerciseRawAttributesMedia(
    val title: String,
    val subtitle: String,
    val description: String,
    val userId: UserIdRaw,
    val photo: PhotoWrapper?
)

data class PhotoWrapper(
    val data: List<PhotoRaw>? //Me devuelve la lista de fotos (small, thumbnail, hash)
)

data class ExerciseCreateData(
    val data: ExerciseRawAttributes
)

data class Media(
    val documentId: String,
    val formats: MediaFormats
)

data class MediaFormats(
    val small: ImageAttributes,
    val thumbnail: ImageAttributes,
)

data class ImageAttributes(
    val url: String
)

data class CreatedMediaItemResponse(
    val id:Int,
    val documentId: String,
    val name:String,
    val formats: MediaFormats
)

data class PhotoRaw(
    val id: Int,
    val attributes: PhotoRawAttributes
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