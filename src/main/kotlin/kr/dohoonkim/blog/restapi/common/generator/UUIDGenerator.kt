package kr.dohoonkim.blog.restapi.common.generator

import com.github.f4b6a3.uuid.UuidCreator
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import org.hibernate.id.uuid.UuidValueGenerator
import java.util.UUID

class UUIDGenerator : UuidValueGenerator {

    override fun generateUuid(p0: SharedSessionContractImplementor?): UUID {
        return UuidCreator.getTimeOrderedEpoch()
    }
}