package be.ordina.sest.homearchive.helper;

import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

public class ElasticQueryBuilder {

    private BoolQueryBuilder queryBuilder;

    public ElasticQueryBuilder addFileName(final String fileName) {
        if (StringUtils.isNotEmpty(fileName)) {
            getOrCreateQueryBuilder().must(new TermQueryBuilder("filename", fileName));

        }
        return this;
    }

    public ElasticQueryBuilder addDescription(final String description) {
        if (StringUtils.isNotEmpty(description)) {
            getOrCreateQueryBuilder().must(new TermQueryBuilder("metadata.description", description));

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
