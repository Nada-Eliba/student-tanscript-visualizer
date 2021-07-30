package org.nadaeliba.studentTranscript.handlers;

import com.arangodb.ArangoCursor;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.model.AqlQueryOptions;
import com.arangodb.util.MapBuilder;

public class ArangoDBHandler {
    private ArangoDB arangoDB;
    private static ArangoDatabase arangoDatabase;
    private static final String databaseName = "student_transcript";

    public ArangoDBHandler(){
        this.arangoDB = new ArangoDB
                .Builder()
                .host("localhost", 8529)
                .user("root")
                .password("root")
                .build();

        this.arangoDatabase = arangoDB.db(databaseName);
    }

    public ArangoCursor<Object> executeLinkDataQuery(String query, MapBuilder paramMapBuilder) {
        while(true) {
            try {
                return arangoDatabase.query(
                        query,
                        paramMapBuilder.get(),
                        new AqlQueryOptions(),
                        Object.class
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
