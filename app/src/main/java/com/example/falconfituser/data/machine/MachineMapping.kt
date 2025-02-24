package com.example.falconfituser.data.machine

import com.example.falconfituser.data.api.machine.MachineRaw
import com.example.falconfituser.data.local.entities.MachineEntity

fun MachineRaw.toExternal(): Machine{
    return Machine(
        id = this.id.toString(),
        title = this.attributes.title,
        subtitle = this.attributes.subtitle,
        description = this.attributes.description,
        photo = this.attributes.photo?.data?.firstOrNull()?.attributes?.formats?.small?.url
            //this.attributes.photo.attributes.formats?.small!!.url
    )
}

fun MachineEntity.toExternal(): Machine{
    return Machine(
        id = this.id,
        title = this.title,
        subtitle = this.subtitle,
        description = this.description,
        photo = null // No quiero mostrar las fotos para que se vea cuando cambia a local
    )
}

fun Machine.toLocal(): MachineEntity{
    return MachineEntity(
        id = this.id,
        title = this.title,
        subtitle = this.subtitle,
        description = this.description,
        photo = this.photo
    )
}
fun List<MachineRaw>.toExternal():List<Machine> = map(MachineRaw::toExternal)