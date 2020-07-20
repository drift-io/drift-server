package com.github.driftserver.ui.app.component.stacktrace;

import com.github.driftserver.core.infra.logging.ProblemDescription;
import com.github.driftserver.ui.infra.ListSelector;
import com.github.driftserver.ui.infra.WicketUtil;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LambdaModel;
import org.danekja.java.util.function.serializable.SerializableSupplier;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class ProblemDescriptionListComponent extends Panel {

    private ListSelector exceptionSelector = new ListSelector(null);
    private ListSelector problemSelector = new ListSelector(exceptionSelector);

    private WebMarkupContainer overview, problemDetail, exceptionDetail;
    private IModel<List<ProblemDescription>> problemDescriptions;

    public ProblemDescriptionListComponent(String id, SerializableSupplier<List<ProblemDescription>> lambda) {
        super(id);
        problemDescriptions = LambdaModel.of(lambda);
        setOutputMarkupId(true);
        add(overview = new WebMarkupContainer("overview"));
        overview.add(WicketUtil.listView("problems", problemDescriptions, (item) -> {

            ProblemDescription problemDescription = item.getModelObject();
            item.add(new Label("description", getFormatted(problemDescription)));

            Link select = WicketUtil.ajaxLink("select", (target) -> {
                problemSelector.select(0);
                target.add(ProblemDescriptionListComponent.this);
            });

            item.add(select);

        }));
        add(problemDetail = new WebMarkupContainer("problemDetail"));
        add(exceptionDetail = new WebMarkupContainer("exceptionDetail"));

    }

    private String getFormatted(ProblemDescription problemDescription) {
        return String.format(
                "%s while %s for %s ",
                problemDescription.getProblem(),
                problemDescription.getAction(),
                problemDescription.getLocation()
        );
    }

    protected void onConfigure() {
        super.onConfigure();
        overview.setVisible(!problemSelector.isSelected());
        problemDetail.setVisible(problemSelector.isSelected() && !exceptionSelector.isSelected());
        exceptionDetail.setVisible(problemSelector.isSelected() && exceptionSelector.isSelected());

        if (problemDetail.isVisible()) {
            replace(problemDetail = new WebMarkupContainer("problemDetail"));
            ProblemDescription problemDescription = problemDescriptions.getObject().get(problemSelector.getSelection());

            problemDetail.add(WicketUtil.ajaxLink("unselect", (target) -> {
                problemSelector.emptySelection();
                target.add(ProblemDescriptionListComponent.this);
            }));

            if (problemDescription.isDriftException()) {
                ExternalLink externalLink = WicketUtil.externalLink("documentationLink", "https://drift-io.gitbook.io/drift-server/errorcode/" + problemDescription.getDriftException().getCode());
                problemDetail.add(externalLink);
                externalLink.add(WicketUtil.label("errorCode", problemDescription.getDriftException().getCode()));
            } else {
                ExternalLink externalLink = WicketUtil.externalLink("documentationLink", "");
                externalLink.setVisible(false);
                problemDetail.add(externalLink);
            }

            problemDetail.add(WicketUtil.label("description", getFormatted(problemDescription)));

            problemDetail.add(WicketUtil.listView("exceptions", problemDescription.getMessages(), (item) -> {
                item.add(WicketUtil.label("message", item.getModelObject()));
                item.add(WicketUtil.ajaxLink("selectException", (target) -> {
                    exceptionSelector.select(0);
                    target.add(ProblemDescriptionListComponent.this);
                }));

            }));
        }

        if (exceptionDetail.isVisible()) {
            replace(exceptionDetail = new WebMarkupContainer("exceptionDetail"));

            exceptionDetail.add(WicketUtil.ajaxLink("unselectException", (target) -> {
                exceptionSelector.emptySelection();
                target.add(ProblemDescriptionListComponent.this);
            }));

            ProblemDescription problemDescription = problemDescriptions.getObject().get(problemSelector.getSelection());
            exceptionDetail.add(WicketUtil.label("exception", stackTraceToString(problemDescription.getException())));

        }

    }

    private String stackTraceToString(Exception e) {
        try (
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
        ) {
            e.printStackTrace(pw);
            return sw.toString();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}