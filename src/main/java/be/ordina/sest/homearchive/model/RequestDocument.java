package be.ordina.sest.homearchive.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class RequestDocument {
    private String fileName;
    private String documentType;
    private List<String> tags;
    private Date startDate;
    private Date endDate;

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
