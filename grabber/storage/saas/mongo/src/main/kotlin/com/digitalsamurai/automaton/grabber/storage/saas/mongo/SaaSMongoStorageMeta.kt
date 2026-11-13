package com.digitalsamurai.automaton.grabber.storage.saas.mongo

import com.digitalsamurai.automaton.grabber.storage.api.AutomatonGrabberStorageMeta
import java.time.Instant

public data class SaaSMongoStorageMeta(
    /** Команда, в области которой выполняется операция. */
    val teamId: String,

    /** Пользователь или интеграция, выполняющая операцию. */
    val actor: SaaSActorMeta,

    /** Ревизия источника данных; отсутствует у ручной операции. */
    val sourceRevision: String? = null,

) : AutomatonGrabberStorageMeta

public sealed interface SaaSActorMeta {

    /** Стабильный идентификатор исполнителя. */
    val id: String

    data class User(
        /** Идентификатор пользователя системы. */
        override val id: String,
    ) : SaaSActorMeta

    data class Integration(
        /** Идентификатор зарегистрированной интеграции. */
        override val id: String,
    ) : SaaSActorMeta
}

internal sealed interface SaaSManagementMeta {

    data object Manual : SaaSManagementMeta

    data class Integration(
        /** Идентификатор интеграции, которой принадлежит управление узлом. */
        val integrationId: String,
    ) : SaaSManagementMeta
}


