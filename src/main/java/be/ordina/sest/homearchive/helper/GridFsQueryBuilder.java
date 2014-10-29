package be.ordina.sest.homearchive.helper;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
        if (StringUtils.isNotEmpty(fileName)) {
            Criteria criteria = GridFsCriteria.whereFilename().regex(fileName);
            createOrGetQuery(criteria);
        }
        return this;
    }

    /**
     *
     * adds _id to search
     *
     * @param id
     * @return QueryBuilder
     */
    public GridFsQueryBuilder addId(final String id) {
        if (StringUtils.isNotEmpty(id)) {
            Criteria criteria = GridFsCriteria.where("_id").is(id);
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
        if (StringUtils.isNotEmpty(contentType)) {
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
            Criteria criteria = GridFsCriteria.whereMetaData("tags").in(tags);
            createOrGetQuery(criteria);
        }
        return this;
    }

    public GridFsQueryBuilder addDateRange(final Date beginningDate, final Date endDate) {

        if (beginningDate != null) {
            Date startBeginningDate = getBeginningOfDay(beginningDate);
            if (endDate == null) {
                Date endBeginningDate = getEndOfDay(beginningDate);
                createOrGetQuery(Criteria.where("uploadDate").gte(startBeginningDate).lte(endBeginningDate));

            } else {
                Date endEndDate = getEndOfDay(endDate);
                createOrGetQuery(Criteria.where("uploadDate").gte(startBeginningDate).lte(endEndDate));
            }
        }else if(endDate!=null){
            Date endEndDate = getEndOfDay(endDate);
            createOrGetQuery(Criteria.where("uploadDate").lte(endEndDate));
        }
        return this;
    }

    private Date getBeginningOfDay(final Date beginningDate) {
        return new DateTime(beginningDate).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0)
            .withMillisOfSecond(0).toDate();
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
