package studio.deervision.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.transaction.Transactional;
import java.util.List;

@Service
public class QueryExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryExecutorService.class);

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public QueryExecutorService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Transactional
    public String executeSelect(String sqlQuery) {
        LOGGER.info("Execute select query: " + sqlQuery);

        StringBuilder queryResult;
        EntityManager em = entityManagerFactory.createEntityManager();
        em.joinTransaction();
        @SuppressWarnings("unchecked") List<Object> results = em.createNativeQuery(sqlQuery).getResultList();
        queryResult = new StringBuilder("Total results: " + results.size() + "\n\n");
        for (Object resultLine : results) {
            if (resultLine.getClass().isArray()) {
                for (Object lineElement : (Object[])resultLine) {
                    if (lineElement != null) {
                        queryResult.append(lineElement).append(", ");
                    } else {
                        queryResult.append("[empty]").append(", ");
                    }
                }
            } else {
                queryResult.append(resultLine);
            }
            queryResult.append("\n\n");
        }
        return queryResult.toString();
    }

    @Transactional
    public int executeUpdate(String sqlQuery) {
        LOGGER.info("Execute update query: " + sqlQuery);

        EntityManager em = entityManagerFactory.createEntityManager();
        em.joinTransaction();
        return em.createNativeQuery(sqlQuery).executeUpdate();
    }

}
