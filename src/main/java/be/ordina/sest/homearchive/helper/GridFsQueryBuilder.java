package be.ordina.sest.homearchive.helper;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Builds queries
 *
 *
 */
public class GridFsQueryBuilder {
    public Query query;

    /**
     * adds filename to the query TODO
     *
     * @param fileName
     * @return QueryBuilder
     */
    public GridFsQueryBuilder addFileName(final String fileName) {
        if (fileName != null) {
            Criteria criteria = GridFsCriteria.whereFilename().is(fileName);
            createOrGetQuery(criteria);
        }
        return this;
    }

    /**
     *
     * adds content type to the query
     *
     * @param contentType
     * @return QueryBuilder
     */
    public GridFsQueryBuilder addContentType(final String contentType) {
        if (contentType != null) {
            Criteria criteria = GridFsCriteria.whereContentType().is(contentType);
            createOrGetQuery(criteria);
        }
        return this;
    }

    /**
     *
     * adds list of tags
     *
     * @param tags
     * @return QueryBuilder
     */
    public GridFsQueryBuilder addTags(final List<String> tags) {
        if (CollectionUtils.isNotEmpty(tags)) {
            DBObject metaData = new BasicDBObject("tags", tags);
            Criteria criteria = GridFsCriteria.whereMetaData().is(metaData);
            createOrGetQuery(criteria);
        }
        return this;
    }

    public GridFsQueryBuilder addDateRange(final Date beginningDate, final Date endDate) {

        if (beginningDate != null) {
            DateTime startBeginningDate = getBeginningOfDay(beginningDate);
            if (endDate == null) {
                Date endBeginningDate = getEndOfDay(beginningDate);
                createOrGetQuery(Criteria.where("uploadDate").gte(startBeginningDate).lte(endBeginningDate));

            } else {
                Date endEndDate = getEndOfDay(endDate);
                createOrGetQuery(Criteria.where("uploadDate").gte(startBeginningDate).lte(endEndDate));
            }
        }
        return this;
    }

    private DateTime getBeginningOfDay(final Date beginningDate) {
        return new DateTime(beginningDate).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
            .withMillisOfSecond(0);
    }

    private Date getEndOfDay(final Date date) {
        DateTime res =
            new DateTime(date).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).withMillisOfSecond(999);
        return res.toDate();
    }

    /**
     *
     * gets query
     *
     * @return query
     */
    public Query getQuery() {
        return query;
    }

    /**
     *
     * Gets or creates query
     *
     * @param criteria
     */
    private void createOrGetQuery(final Criteria criteria) {
        if (query == null) {
            query = new Query(criteria);
        } else {
            query.addCriteria(criteria);
        }
    }

}
