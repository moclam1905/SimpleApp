package com.nguyenmoclam.simpleapp.database.entiry.mapper

import com.nguyenmoclam.simpleapp.database.entiry.ItemEntity
import com.nguyenmoclam.simpleapp.database.utils.generateInitials
import com.nguyenmoclam.simpleapp.database.utils.getColorFromInitials
import com.nguyenmoclam.simpleapp_model.Item

object ItemInfoEntityMapper : EntityMapper<Item, ItemEntity> {

    override fun asEntity(domain: Item): ItemEntity {
        val initials = generateInitials(domain.title)
        val (backgroundColor, textColor) = getColorFromInitials(initials)
        return ItemEntity(
            page = domain.page,
            index = domain.index,
            title = domain.title,
            description = domain.description,
            date = domain.date,
            initials = initials,
            backgroundColor = backgroundColor,
            textColor = textColor
        )

    }

    override fun asDomain(entity: ItemEntity): Item {
        return Item(
            page = entity.page,
            index = entity.index,
            title = entity.title,
            description = entity.description,
            date = entity.date,
            initials = entity.initials,
            backgroundColor = entity.backgroundColor,
            textColor = entity.textColor
        )
    }
}

fun Item.asEntity(): ItemEntity {
    return ItemInfoEntityMapper.asEntity(this)
}

fun ItemEntity.asDomain(): Item {
    return ItemInfoEntityMapper.asDomain(this)
}