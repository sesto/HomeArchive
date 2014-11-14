package be.ordina.sest.homearchive.service;

import be.ordina.sest.homearchive.dao.ElasticsearchDao;
import be.ordina.sest.homearchive.model.RequestResponseDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class SearchServiceElasticImplTest {

    @Mock
    private ElasticsearchDao elasticsearchDao;

    @InjectMocks
    private SearchServiceElasticImpl searchServiceElastic = new SearchServiceElasticImpl();

    @Test
    public void testFindDocuments() throws Exception {
        RequestResponseDocument requestResponseDocument = new RequestResponseDocument();
        searchServiceElastic.findDocuments(requestResponseDocument);
        verify(elasticsearchDao, times(1)).findDocuments(any(SearchQuery.class));
    }
}