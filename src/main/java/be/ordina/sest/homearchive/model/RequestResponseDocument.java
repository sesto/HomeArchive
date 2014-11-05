package be.ordina.sest.homearchive.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 *
 * Document entity
 *
 */
@Data
@Document(indexName = "mongoindex", type = "requestresponseentity")
public class RequestResponseDocument implements Serializable {

    private static final long serialVersionUID = -5211227215172538582L;

    @Id
    private String _id;

    private String filename;

    private String contentType;

    private Metadata metadata;

    private Date startDate;

    private Date endDate;

    private Date uploadDate;

    /**
     *
     * null-safe getter
     *
     * @return {@link Metadata}
     */
    public Metadata getMetadata() {
        if (this.metadata == null) {
            this.metadata = new Metadata();
        }
        return metadata;
    }

}
