package com.digitalsamurai.automaton.grabber.storage.saas.mongo

import java.time.Instant

internal data class SaaSMongoNodeMetadata(
    /** Команда-владелец конкретного узла; размещена на каждом уровне для независимого владения в будущем. */
    val teamId: String,

    /** Источник управления узлом: вручную или конкретной интеграцией. */
    val management: SaaSManagementMeta,

    /** Пользователь или интеграция, создавшая узел. */
    val createdBy: SaaSActorMeta,

    /** UTC-время создания узла; не меняется после создания. */
    val createdAt: Instant,

    /** Пользователь или интеграция, выполнившая последнее изменение узла. */
    val updatedBy: SaaSActorMeta,

    /** UTC-время последнего изменения узла; предназначено для аудита. */
    val updatedAt: Instant,

    /** Номер ревизии узла для проверки конкурентных изменений через CAS. */
    val version: Long,

    /** Ревизия внешнего источника, из которой получен узел; null при ручном управлении. */
    val sourceRevision: String?,
)