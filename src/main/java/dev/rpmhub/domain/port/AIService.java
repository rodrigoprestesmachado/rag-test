/**
 * This file contains confidential and proprietary information.
 * Unauthorized copying, distribution, or use of this file or its contents is
 * strictly prohibited.
 *
 * 2025 Rodrigo Prestes Machado. All rights reserved.
 */

package dev.rpmhub.domain.port;

import dev.rpmhub.domain.model.AIRequest;
import io.smallrye.mutiny.Multi;

public interface AIService {

    /**
     * Generates a response based on the provided AI request.
     *
     * @param request the AI request
     * @return a Multi emitting the generated response
     */
    Multi<String> generateResponse(AIRequest request);

    /**
     * Generates a contextual response based on the provided AI request.
     *
     * @param request the AI request containing the context
     * @return a Multi emitting the generated contextual response
     */
    Multi<String> generateContextualResponse(AIRequest request);
}
