package be.ordina.sest.homearchive.helper;

import java.util.Date;

import lombok.extern.log4j.Log4j;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsCriteria;

/**
 * Builds GridFS queries
 *
 *@author sest
 */
@Log4j
public class GridFsQueryBuilder {
    public Query query;

    /**
     * adds filename to the query
     *
     * @param fileName name of the file
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
     * @param id document id
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
     * @param contentType content type
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
     * adds description
     *
     * @param description document description
     * @return QueryBuilder
     */
    public GridFsQueryBuilder addDescription (final String description) {
        if (StringUtils.isNotEmpty(description)) {
            log.debug("Building query with description: " + description);
            Criteria criteria = GridFsCriteria.whereMetaData("description").is(description);
            createOrGetQuery(criteria);
        }
        return this;
    }

    public GridFsQueryBuilder addDateRange(final Date beginningDate, final Date endDate) {

        if (beginningDate != null) {
            Date startBeginningDate = DateUtils.getBeginningOfDay(beginningDate);
            if (endDate == null) {
                Date endBeginningDate = DateUtils.getEndOfDay(beginningDate);
                createOrGetQuery(Criteria.where("uploadDate").gte(startBeginningDate).lte(endBeginningDate));

            } else {
                Date endEndDate = DateUtils.getEndOfDay(endDate);
                createOrGetQuery(Criteria.where("uploadDate").gte(startBeginningDate).lte(endEndDate));
            }
        }else if(endDate!=null){
            Date endEndDate = DateUtils.getEndOfDay(endDate);
            createOrGetQuery(Criteria.where("uploadDate").lte(endEndDate));
        }
        return this;
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
     * @param criteria criteria
     */
    private void createOrGetQuery(final Criteria criteria) {
        if (query == null) {
            query = new Query(criteria);
        } else {
            query.addCriteria(criteria);
        }
    }

}
