package be.ordina.sest.homearchive.helper;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import java.util.Date;

/**
 * Builds elasticsearch query
 * @author sest
 */
public class ElasticQueryBuilder {

    private BoolQueryBuilder queryBuilder;

    /**
     *
     * adds filename to the query
     *
     * @param fileName String
     * @return {@link ElasticQueryBuilder}
     */
    public ElasticQueryBuilder addFileName(final String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            getOrCreateQueryBuilder().must(
                QueryBuilders.matchQuery("filename", fileName).analyzer("homearchive_ngram_analyzer"));
        }
        return this;
    }

    /**
     *
     * adds description to the query
     *
     * @param description String
     * @return {@link ElasticQueryBuilder}
     */

    public ElasticQueryBuilder addDescription(final String description) {
        if (StringUtils.isNotEmpty(description)) {
            getOrCreateQueryBuilder().must(
                QueryBuilders.matchQuery("metadata.description", description));

        }
        return this;
    }

    /**
     * adds date range to the query
     *
     *
     * @param beginningDate Date
     * @param endDate Date
     * @return {@link ElasticQueryBuilder}
     */
    public ElasticQueryBuilder addDateRange(final Date beginningDate, final Date endDate) {
        if (beginningDate != null) {
            Date startBeginningDate = DateUtils.getBeginningOfDay(beginningDate);
            if (endDate == null) {
                Date endBeginningDate = DateUtils.getEndOfDay(beginningDate);
                getOrCreateQueryBuilder().must(
                    new RangeQueryBuilder("uploadDate").gte(startBeginningDate).lte(endBeginningDate));
            } else {
                Date endEndDate = DateUtils.getEndOfDay(endDate);
                getOrCreateQueryBuilder().must(
                    new RangeQueryBuilder("uploadDate").gte(startBeginningDate).lte(endEndDate));
            }
        } else if (endDate != null) {
            Date endEndDate = DateUtils.getEndOfDay(endDate);
            getOrCreateQueryBuilder().must(new RangeQueryBuilder("uploadDate").lte(endEndDate));

        }

        return this;
    }

    /**
     *
     * builds query
     *
     * @return {@link SearchQuery}
     */
    public SearchQuery buildQuery() {
        return new NativeSearchQueryBuilder().withQuery(queryBuilder).withPageable(new PageRequest(0, 100)).build();
    }

    /**
     *
     * gets {@link BoolQueryBuilder} or initializes, if is not initialized yet
     *
     * @return queryBuilder
     */
    private BoolQueryBuilder getOrCreateQueryBuilder() {
        if (this.queryBuilder == null) {
            this.queryBuilder = new BoolQueryBuilder();
        }
        return this.queryBuilder;
    }
}
