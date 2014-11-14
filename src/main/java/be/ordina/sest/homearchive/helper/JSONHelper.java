package be.ordina.sest.homearchive.helper;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;

import lombok.extern.log4j.Log4j;

import org.elasticsearch.common.xcontent.XContentBuilder;

@Log4j
public class JSONHelper {

    public static String convertString(final String source) {
        return source.replaceAll("\"", "\\\\\"");
    }

    public static XContentBuilder getMapping() throws IOException {
        return jsonBuilder()
                .startObject()
                    .startObject("requestresponseentity")
                        .startObject("properties")
                            .startObject("filename")
                                .field("type", "string")
                                .field("analyzer", "homearchive_ngram_analyzer")
                            .endObject()
                            .startObject("metadata")
                                .startObject("properties")
                                    .startObject("description")
                                        .field("type", "string")
                                        .field("analyzer", "homearchive_ngram_analyzer")
                                    .endObject()
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject();
    }

    public static XContentBuilder getSettings() throws IOException {

        return jsonBuilder()
                .startObject()
                    .startObject("settings")
                        .startObject("analysis")
                            .startObject("analyzer")
                                .startObject("homearchive_ngram_analyzer")
                                    .array("filter", "lowercase", "homearchive_ngram_filter")
                                    .field("tokenizer", "whitespace")
                                .endObject()
                            .endObject()
                            .startObject("filter")
                                .startObject("homearchive_ngram_filter")
                                    .field("type", "edgeNGram")
                                    .field("min_gram", "1")
                                    .field("max_gram", "10")
                                    .array("token_chars", "letter", "digit", "whitespace", "punctuation")
                                .endObject()
                            .endObject()
                        .endObject()
                    .endObject()
                .endObject();
    }

    public static XContentBuilder getRiverSettings() throws IOException {
        return jsonBuilder()
                .startObject()
                    .field("type", "mongodb")
                    .field("mongodb")
                        .startObject()
                            .field("db", "homearchive_elastic")
                            .field("collection", "fs.files")
                        .endObject()
                    .field("index")
                        .startObject()
                            .field("name", "mongoindex")
                            .field("type", "requestresponseentity")
                        .endObject()
                .endObject();
    }
}
