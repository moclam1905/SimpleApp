package com.nguyenmoclam.simpleapp.database.entiry.mapper

import com.nguyenmoclam.simpleapp.database.entiry.ItemEntity
import com.nguyenmoclam.simpleapp.database.utils.generateInitials
import com.nguyenmoclam.simpleapp.database.utils.getColorFromInitials
import com.nguyenmoclam.simpleapp_model.Item

object ItemEntityMapper : EntityMapper<List<Item>, List<ItemEntity>> {

    override fun asEntity(domain: List<Item>): List<ItemEntity> {
        return domain.map { item ->
            val initials = generateInitials(item.title)
            val (backgroundColor, textColor) = getColorFromInitials(initials)
            ItemEntity(
                page = item.page,
                index = item.index,
                title = item.title,
                description = item.description,
                date = item.date,
                initials = initials,
                backgroundColor = backgroundColor,
                textColor = textColor
            )
        }
    }

    override fun asDomain(entity: List<ItemEntity>): List<Item> {
        return entity.map { itemEntity ->
            Item(
                page = itemEntity.page,
                index = itemEntity.index,
                title = itemEntity.title,
                description = itemEntity.description,
                date = itemEntity.date,
                initials = itemEntity.initials,
                backgroundColor = itemEntity.backgroundColor,
                textColor = itemEntity.textColor
            )
        }
    }
}

fun List<Item>.asEntity(): List<ItemEntity> {
    return ItemEntityMapper.asEntity(this)
}

fun List<ItemEntity>?.asDomain(): List<Item> {
    return ItemEntityMapper.asDomain(this.orEmpty())
}