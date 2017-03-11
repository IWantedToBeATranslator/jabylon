/**
 * (C) Copyright 2013 Jabylon (http://www.jabylon.org) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
/**
 *
 */
package org.jabylon.rest.ui.wicket.panels;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.search.ScoreDoc;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.eclipse.emf.common.util.URI;
import org.jabylon.cdo.server.ServerConstants;
import org.jabylon.index.properties.QueryService;
import org.jabylon.index.properties.SearchResult;
import org.jabylon.properties.ProjectLocale;
import org.jabylon.properties.ProjectVersion;
import org.jabylon.properties.PropertiesPackage;
import org.jabylon.rest.ui.security.CDOAuthenticatedSession;
import org.jabylon.rest.ui.util.WicketUtil;
import org.jabylon.rest.ui.wicket.pages.ResourcePage;
import org.jabylon.security.CommonPermissions;
import org.jabylon.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Johannes Utzig (jutzig.dev@googlemail.com)
 *
 */
public class SearchResultPanel<T> extends GenericPanel<T> {

    private static final long serialVersionUID = 1L;

    @Inject
    private QueryService queryService;

    private static final Logger logger = LoggerFactory.getLogger(SearchResultPanel.class);

    public SearchResultPanel(String id, final SearchResult result, PageParameters parameters) {
        super(id);

        List<ScoreDoc> list = Arrays.asList(result.getTopDocs().scoreDocs);
    	
        ListView<ScoreDoc> repeater = new ListView<ScoreDoc>("results", list) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(ListItem<ScoreDoc> item) {
                ScoreDoc doc = item.getModelObject();
                Document document;
                try {

                    document = result.getSearcher().doc(doc.doc);
                    item.add(new Label("value", document.get(QueryService.FIELD_VALUE)));
                    String projectLabel = "{0} ({1})";
                    String projectName = document.get(QueryService.FIELD_PROJECT);
                	Session session = getSession();
                    User user = null;
                	if (session instanceof CDOAuthenticatedSession) {
            			CDOAuthenticatedSession authSession = (CDOAuthenticatedSession) session;
            			user = authSession.getUser();
                	}
                    if(user==null || !CommonPermissions.hasPermission(user,CommonPermissions.constructPermissionName(PropertiesPackage.Literals.PROJECT.getName(), projectName, CommonPermissions.ACTION_VIEW)))
                    	item.setVisible(false);
                    String projectVersion = document.get(QueryService.FIELD_VERSION);
                    String projectLocale = document.get(QueryService.FIELD_LOCALE);
                    String descriptorURI = document.get(QueryService.FIELD_URI);
                    PageParameters params = new PageParameters();
                    int startParam = 0;
                    params.set(startParam++, ServerConstants.WORKSPACE_RESOURCE);
                    params.set(startParam++, projectName);
                    params.set(startParam++, projectVersion);
                    params.set(startParam++, projectLocale);
                    URI uri = URI.createURI(descriptorURI);

                    for (int i = startParam; i < uri.segmentCount()+startParam; i++) {
                        params.set(i, uri.segment(i-startParam));
                    }
                    BookmarkablePageLink<Void> link = new BookmarkablePageLink<Void>("link", ResourcePage.class, params);
                    link.add(new Label("title", uri.lastSegment()));
                    item.add(link);
                    projectLabel = MessageFormat.format(projectLabel, projectName, projectVersion);

                    BookmarkablePageLink<ResourcePage<ProjectVersion>> projectVersionLink = new BookmarkablePageLink<ResourcePage<ProjectVersion>>("project-link", ResourcePage.class, createPageParams(ServerConstants.WORKSPACE_RESOURCE,projectName,projectVersion));
                    projectVersionLink.add(new Label("project", projectLabel));
                    item.add(projectVersionLink);

                    String key = document.get(QueryService.FIELD_KEY);
                    BookmarkablePageLink<ResourcePage<ProjectVersion>> projectVersionKeyLink = new BookmarkablePageLink<ResourcePage<ProjectVersion>>("key-link", ResourcePage.class, params.set("key", key));
                    projectVersionKeyLink.add(new Label("key", key));
                    item.add(projectVersionKeyLink);

                    item.add(new Label("comment", document.get(QueryService.FIELD_COMMENT)));

                    item.add(new Label("mastervalue", document.get(QueryService.FIELD_MASTER_VALUE)));
                    item.add(new Label("mastercomment", document.get(QueryService.FIELD_MASTER_COMMENT)));

                    Locale locale = WicketUtil.getLocaleFromString(projectLocale);
                    String localeLabel = locale.getDisplayName(WicketUtil.getUserLocale());
                    item.add(new Label("language", localeLabel));
                    if(locale==null || locale.equals(ProjectLocale.TEMPLATE_LOCALE)) {
                        WebMarkupContainer markupContainer = new WebMarkupContainer("flag-icon");
                        item.add(markupContainer);
                        markupContainer.add(new AttributeModifier("class", "icon-book"));
                    }
                    else {
                    	item.add(new Image("flag-icon", WicketUtil.getIconForLocale(locale)));                    	
                    }
                } catch (CorruptIndexException e) {
                    error(e.getMessage());
                    logger.error("Search failed", e);
                } catch (IOException e) {
                    error(e.getMessage());
                    logger.error("Search failed", e);
                }

            }

        };
        add(repeater);
    }
    
    public QueryService getQueryService() {
        return queryService;
    }

    protected PageParameters createPageParams(String... segments)
    {
        PageParameters params = new PageParameters();
        for (int i = 0; i < segments.length; i++) {
            params.set(i, segments[i]);
        }
        return params;
    }

}
