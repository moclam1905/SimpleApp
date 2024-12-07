

package com.nguyenmoclam.simpleapp.database.entiry.mapper

interface EntityMapper<Domain, Entity> {

  fun asEntity(domain: Domain): Entity

  fun asDomain(entity: Entity): Domain
}
