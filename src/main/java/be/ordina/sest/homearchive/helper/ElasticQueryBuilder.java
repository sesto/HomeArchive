package be.ordina.sest.homearchive.helper;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyLikeThisFieldQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

public class ElasticQueryBuilder {

    private BoolQueryBuilder queryBuilder;

    public ElasticQueryBuilder addFileName(final String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            getOrCreateQueryBuilder().must(new FuzzyLikeThisFieldQueryBuilder("filename")
            .fuzziness(Fuzziness.fromEdits(2))
            .maxQueryTerms(100)
            .likeText(fileName));
        }
        return this;
    }

    public ElasticQueryBuilder addDescription(final String description) {
        if (StringUtils.isNotEmpty(description)) {
            getOrCreateQueryBuilder().must(
                new FuzzyLikeThisFieldQueryBuilder("metadata.description")
                .fuzziness(Fuzziness.fromEdits(2))
                .maxQueryTerms(100)
                .likeText(description));
        }
        return this;
    }

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

    public SearchQuery buildQuery() {
        return new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
    }

    private BoolQueryBuilder getOrCreateQueryBuilder() {
        if (this.queryBuilder == null) {
            this.queryBuilder = new BoolQueryBuilder();
        }
        return this.queryBuilder;
    }
}
