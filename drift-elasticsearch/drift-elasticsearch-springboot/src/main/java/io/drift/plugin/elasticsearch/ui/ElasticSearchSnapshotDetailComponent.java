package io.drift.plugin.elasticsearch.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.drift.elasticsearch.ElasticSearchSettings;
import io.drift.elasticsearch.ElasticSearchSnapshot;
import org.apache.wicket.markup.html.panel.Panel;

import static io.drift.ui.infra.WicketUtil.label;
import static io.drift.ui.infra.WicketUtil.listView;

public class ElasticSearchSnapshotDetailComponent extends Panel {

    public ElasticSearchSnapshotDetailComponent(String id, ElasticSearchSnapshot elasticSearchSnapshot, ElasticSearchSettings elasticSearchSettings) {
        super(id);
        add(listView("indices", () -> elasticSearchSettings.getIndices(), indexItem -> {
            String indexName = indexItem.getModelObject();
            indexItem.add(label("indexName", indexName));
            indexItem.add(listView("hits", () -> elasticSearchSnapshot.getIndexSnapshots().get(indexName).getHits(), hitItem -> {
                hitItem.add(label("content", pretty(hitItem.getModelObject().getContent())));
            }));
        }));
    }

    private String pretty(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Object json = mapper.readValue(jsonString, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
