package org.speer.assessment.configs;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.PostgreSQLDialect;

public class CustomPostgreSQLDialect extends PostgreSQLDialect {
    public CustomPostgreSQLDialect() {
        super();
    }

    /**
     * To define function "tsvector_match" for text indexing filter
     *
     * @param functionContributions
     */
    @Override
    public void initializeFunctionRegistry(FunctionContributions functionContributions) {
        super.initializeFunctionRegistry(functionContributions);
        var functionRegistry = functionContributions.getFunctionRegistry();
        functionRegistry.registerPattern(
                "tsvector_match",
                "(?1 @@ ?2)"
        );
    }
}
