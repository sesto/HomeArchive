package be.ordina.sest.homearchive.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class RequestResponseDocument implements Serializable{

    private static final long serialVersionUID = -5211227215172538582L;
    private String id;
    private String filename;
    private String documentType;
    private Metadata metadata = new Metadata();
    private Date startDate;
    private Date endDate;
    private Date docDate;

}
