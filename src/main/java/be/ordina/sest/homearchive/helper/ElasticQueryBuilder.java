package be.ordina.sest.homearchive.helper;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyLikeThisFieldQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

public class ElasticQueryBuilder {

    private BoolQueryBuilder queryBuilder;

    /**
     *
     * adds filename to the query
     *
     * @param fileName
     * @return {@link ElasticQueryBuilder}
     */
    public ElasticQueryBuilder addFileName(final String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            getOrCreateQueryBuilder().must(
                QueryBuilders.fuzzyLikeThisQuery("filename").fuzziness(Fuzziness.fromEdits(2)).maxQueryTerms(100)
                .likeText(fileName));
        }
        return this;
    }

    /**
     *
     * adds description to the query
     *
     * @param description
     * @return {@link ElasticQueryBuilder}
     */

    public ElasticQueryBuilder addDescription(final String description) {
        if (StringUtils.isNotEmpty(description)) {
            getOrCreateQueryBuilder().must(QueryBuilders.fuzzyLikeThisQuery("metadata.description").fuzziness(Fuzziness.fromEdits(2))
                .maxQueryTerms(100).likeText(description));

        }
        return this;
    }

    /**
     * adds date range to the query
     *
     *
     * @param beginningDate
     * @param endDate
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
        return new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
    }

    /**
     *
     * gets {@link BoolQueryBuilder} or initializes, if is not initialized yet
     *
     * @return
     */
    private BoolQueryBuilder getOrCreateQueryBuilder() {
        if (this.queryBuilder == null) {
            this.queryBuilder = new BoolQueryBuilder();
        }
        return this.queryBuilder;
    }
}
