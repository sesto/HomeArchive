package be.ordina.sest.homearchive.rs;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import be.ordina.sest.homearchive.service.DeleteService;

@RestController
public class DeleteRsController {

    @Autowired
    private DeleteService service;

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, value = "/findFiles/{id}")
    public void deleteDocument(@PathVariable("id") final String id) throws IOException {
        service.deleteDocument(id);

    }
}
