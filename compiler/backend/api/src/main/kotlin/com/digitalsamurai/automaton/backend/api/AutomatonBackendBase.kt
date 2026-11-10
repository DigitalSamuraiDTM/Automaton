package com.digitalsamurai.automaton.backend.api

import com.digitalsamurai.automaton.integrator.api.AutomatonIntegrator
import com.digitalsamurai.automaton.logger.api.AutomatonLogger
import com.digitalsamurai.automaton.semantic.CanonicalSemanticModel
import com.digitalsamurai.automaton.semantic.SemanticModel
import com.digitalsamurai.automaton.utils.fingerprint

public class AutomatonBackendBase(
    private val generator: AutomatonGenerator,
    private val integrator: AutomatonIntegrator,
): AutomatonBackend {

    override suspend fun makeOutput(semanticModel: SemanticModel, logger: AutomatonLogger?): BackendResult {
        // TODO ключевая проблема, что команду о перемещении автотеста в другое место мы отдаем также при генерации автотеста. Из-за этого порождается проблема:
        //  Для переноса теста в другое место требуется повторно получить полную информацию о семантической модели, хотя до факта это всего лишь перемещение из
        //  одной папки в другую папку. Мне кажется, что нужно разделение: генерация теста, перемещение теста, удаление теста и обновление теста
        //  разделение нужно на уровне верхнего апи автоматона. Возможно апи фронтенда надо расширить и сделать в нем функции generate, move, delete
        //  Возможно подумать над какими-то еще способами. Сейчас кажется, что история с расширенным апи Frontend не кажется дурным, так как через AutomatonRequest
        //  можно обеспечивать управление
        val modelFingerprint = CanonicalSemanticModel.fromSemanticModel(semanticModel).fingerprint()
        logger?.debug("Semantic model fingerprint: $modelFingerprint")
        val generatorFingerprint = generator.fingerprint()
        logger?.debug("Generator fingerprint: $generatorFingerprint")
        val generatedData = generator.generate(semanticModel)

        val integratedResult = integrator.integrate(generatedData)

        return BackendResult(
            requestId = semanticModel.requestId,
            metadata = semanticModel.test.metadata.id,
        )
    }
}