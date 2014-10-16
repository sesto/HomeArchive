package be.ordina.sest.homearchive.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class RequestDocument implements Serializable{

    private static final long serialVersionUID = -5211227215172538582L;
    private String id;
    private String fileName;
    private String documentType;
    private List<String> tags;
    private Date startDate;
    private Date endDate;
    private Date docDate;

    /**
     *
     * adds tags to the list
     *
     * @param tag
     */
    public void addTag(final String tag) {
        if (tags == null) {
            tags = new ArrayList<>();
        }
        tags.add(tag);
    }
}
