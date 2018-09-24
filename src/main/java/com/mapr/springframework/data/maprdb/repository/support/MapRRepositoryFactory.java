package com.mapr.springframework.data.maprdb.repository.support;

import com.mapr.springframework.data.maprdb.core.MapROperations;
import com.mapr.springframework.data.maprdb.repository.MapRPersistentEntityInformation;
import com.mapr.springframework.data.maprdb.repository.query.ConditionBasedMapRQuery;
import com.mapr.springframework.data.maprdb.repository.query.MapRQueryMethod;
import com.mapr.springframework.data.maprdb.repository.query.StringBasedMapRQuery;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.EntityInformation;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.query.EvaluationContextProvider;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;

import java.lang.reflect.Method;
import java.util.Optional;

public class MapRRepositoryFactory extends RepositoryFactorySupport {

    private final MapROperations mapROperations;

    public MapRRepositoryFactory(final MapROperations mapROperations) {
        this.mapROperations = mapROperations;
    }

    @Override
    public <T, ID> EntityInformation<T, ID> getEntityInformation(Class<T> domainClass) {
        return new MapRPersistentEntityInformation<>(
                null);
    }

    @Override
    protected Object getTargetRepository(final RepositoryInformation metadata) {
        return new SimpleMapRRepository<>(mapROperations, metadata.getDomainType());
    }

    @Override
    protected Class<?> getRepositoryBaseClass(final RepositoryMetadata metadata) {
        return SimpleMapRRepository.class;
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(
            final QueryLookupStrategy.Key key,
            final EvaluationContextProvider evaluationContextProvider) {

        QueryLookupStrategy strategy = null;
        switch (key) {
            case CREATE_IF_NOT_FOUND:
                strategy = new DefaultMapRQueryLookupStrategy(mapROperations);
                break;
            case CREATE:
                break;
            case USE_DECLARED_QUERY:
                break;
        }
        return Optional.ofNullable(strategy);
    }

    static class DefaultMapRQueryLookupStrategy implements QueryLookupStrategy {

        private final MapROperations operations;

        public DefaultMapRQueryLookupStrategy(final MapROperations operations) {
            this.operations = operations;
        }

        @Override
        public RepositoryQuery resolveQuery(
                final Method method,
                final RepositoryMetadata metadata,
                final ProjectionFactory factory,
                final NamedQueries namedQueries) {

            final MapRQueryMethod queryMethod = new MapRQueryMethod(method, metadata, factory);

            if (queryMethod.hasAnnotatedQuery()) {
                return new StringBasedMapRQuery(queryMethod, metadata.getDomainType(), operations);
            } else {
                return new ConditionBasedMapRQuery(queryMethod, metadata.getDomainType(), operations);
            }

//            final String namedQueryName = queryMethod.getNamedQueryName();
//            if (namedQueries.hasQuery(namedQueryName)) {
//                final String namedQuery = namedQueries.getQuery(namedQueryName);
//                return new ConditionBasedMapRQuery(queryMethod, operations);
//            } else if (queryMethod.hasAnnotatedQuery()) {
//                return new ConditionBasedMapRQuery(queryMethod, operations);
//            } else {
//                return new ConditionBasedMapRQuery(queryMethod, operations);
//            }
        }

    }


}
