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
package org.jabylon.rest.ui.tools;


import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyLikeThisQuery;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.common.util.URI;
import org.jabylon.index.properties.QueryService;
import org.jabylon.index.properties.SearchResult;
import org.jabylon.properties.Project;
import org.jabylon.properties.ProjectLocale;
import org.jabylon.properties.PropertiesFactory;
import org.jabylon.properties.Property;
import org.jabylon.properties.PropertyFileDescriptor;
import org.jabylon.rest.ui.Activator;
import org.jabylon.rest.ui.model.PropertyPair;
import org.jabylon.rest.ui.wicket.pages.ResourcePage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Johannes Utzig (jutzig.dev@googlemail.com)
 */
public class SimilarStringsToolPanel
    extends GenericPanel<PropertyPair>
{

    private static final long serialVersionUID = 1L;
    private static Logger logger = LoggerFactory.getLogger(SimilarStringsToolPanel.class);
    @Inject
    private QueryService queryService;

    /**
     * copies similar strings to translation area
     */
    private static final String JS = 
    "$(\"#similarity-table i.icon-share\").click(function () { " +
    	"var translation = $(this).prev(\"span\");" +
    	"var widget = $(\"#translation\");" +
    	"if(widget.attr(\"readonly\")!=='readonly') {" +
    	"widget.val(translation.text());" +
    	"markDirty()};" +
    "});";
    
    public SimilarStringsToolPanel(String id, IModel<PropertyPair> model)
    {
        super(id, model);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
    	super.renderHead(response);
    	response.render(OnDomReadyHeaderItem.forScript(JS));
    }

    @Override
    protected void onBeforeRender()
    {
        List<Similarity> result = doSearch(getModel());
        ListView<Similarity> list = new ListView<Similarity>("children", result)
        {

            private static final long serialVersionUID = 1L;


            @Override
            protected void populateItem(ListItem<Similarity> item)
            {
                Similarity similarity = item.getModelObject();
                Label kind = new Label("kind", "");
                kind.setVisible(similarity.isSameProject());
                item.add(kind);
                item.add(new Label("template", similarity.getOriginal()));
                item.add(new Label("translation", similarity.getTranslation()));
                item.add(new AttributeAppender("title", similarity.getFullPath()));
                PageParameters params = new PageParameters();

                URI uri = URI.createURI(similarity.getUri());
                for (int i = 0; i < uri.segmentCount(); i++)
                {
                    params.set(i, uri.segment(i));
                }
                params.add("key", similarity.getKey());
                item.add(new BookmarkablePageLink<Void>("link", ResourcePage.class, params));
                WebMarkupContainer progress = new WebMarkupContainer("similarity");
                item.add(progress);
                progress.add(new AttributeModifier("style", "width: " + similarity.getSimilarity() + "%"));

            }
        };
        addOrReplace(list);
        super.onBeforeRender();
    }


    protected List<Similarity> doSearch(IModel<PropertyPair> model)
    {
        long time = System.currentTimeMillis();
        PropertyPair pair = model.getObject();
        if (pair == null || pair.getOriginal() == null)
            return Collections.emptyList();

        Project project = getProject(pair);
        FuzzyLikeThisQuery query = new FuzzyLikeThisQuery(10, new StandardAnalyzer(Version.LUCENE_35));
        query.addTerms(pair.getOriginal(), QueryService.FIELD_VALUE, 0.6f, 3);
        //make sure we only look for templates, not translations
        query.addTerms(QueryService.MASTER, QueryService.FIELD_LOCALE, 0.99f, 3);
        SearchResult result = queryService.search(query, 10);

        if (result == null)
            return Collections.emptyList();
        Set<Similarity> resultList = new TreeSet<SimilarStringsToolPanel.Similarity>();
        TopDocs topDocs = result.getTopDocs();
        ScoreDoc[] doc = topDocs.scoreDocs;
        int hitNumber = 0;
        for (ScoreDoc scoreDoc : doc)
        {
            if (scoreDoc.score < 0.20)
                continue;

            try
            {
                Document document = result.getSearcher().doc(scoreDoc.doc);
                Similarity similarity = createSimilarity(document, pair.getLanguage(), (int)(scoreDoc.score * 100),hitNumber++,project);
                if (similarity == null)
                    continue;
                resultList.add(similarity);
            }
            catch (CorruptIndexException e)
            {
                logger.error("Failed to find similar strings", e);
            }
            catch (IOException e)
            {
                logger.error("Failed to find similar strings", e);
            }

        }
        try
        {
            result.getSearcher().close();
            logger.debug("Computing Similarities took {} ms",System.currentTimeMillis()-time);
        }
        catch (IOException e)
        {
            logger.error("Failed to close searcher", e);
        }
        return new ArrayList<SimilarStringsToolPanel.Similarity>(resultList);

    }


    private Project getProject(PropertyPair pair)
    {
        try
        {
            CDOObject object = Activator.getDefault().getRepositoryLookup().resolve(pair.getDescriptorID());
            if (object instanceof PropertyFileDescriptor)
            {
                PropertyFileDescriptor descriptor = (PropertyFileDescriptor)object;
                return descriptor.getProjectLocale().getParent().getParent();
            }
        }
        catch (Exception e)
        {
            logger.error("Failed to lookup project for "+pair);
        }
        return null;
    }


    private Similarity createSimilarity(Document masterDoc, Locale language, int score, int hitNumber, Project originalProject)
        throws CorruptIndexException, IOException
    {
		if("true".equals(masterDoc.get(QueryService.FIELD_TMX)))
		{
			return createTMXSimilarity(masterDoc,language,score,hitNumber);
		}
		PropertyFileDescriptor descriptor = queryService.getDescriptor(masterDoc);
		if (descriptor == null)
			return null;
		String key = masterDoc.get(QueryService.FIELD_KEY);
		BooleanQuery query = new BooleanQuery();
		if(!ProjectLocale.TEMPLATE_LOCALE.equals(language)) // for the template. this field is not set
			query.add(new TermQuery(new Term(QueryService.FIELD_TEMPLATE_LOCATION, descriptor.getLocation().toString())), Occur.MUST);
		query.add(new TermQuery(new Term(QueryService.FIELD_LOCALE, language==null ? "" : language.toString())), Occur.MUST);
		query.add(new TermQuery(new Term(QueryService.FIELD_KEY, key)), Occur.MUST);

		SearchResult searchResult = queryService.search(query, 1);

		TopDocs topDocs = searchResult.getTopDocs();
		if (topDocs.totalHits == 0)
			return null;
		Document translationDoc = searchResult.getSearcher().doc(searchResult.getTopDocs().scoreDocs[0].doc);
		Property property = PropertiesFactory.eINSTANCE.createProperty();
		property.setKey(key);
		property.setValue(translationDoc.get(QueryService.FIELD_VALUE));
		property.setComment(translationDoc.get(QueryService.FIELD_COMMENT));
		PropertyFileDescriptor slave = queryService.getDescriptor(translationDoc);
		if (slave == null)
		{
			return null;
		}
		
		PropertyPair pair = getModelObject();
		// that would mean we found the current property, which is (of course) similar :-)
		if (pair.getKey().equals(key) && slave.cdoID().equals(pair.getDescriptorID()))
			return null;
		URI originalProjectPath = originalProject.fullPath();
		String resultPath = masterDoc.get(QueryService.FIELD_FULL_PATH);
		boolean isSameProject = resultPath!=null && resultPath.startsWith(originalProjectPath.path() + "/");
		Similarity similarity = new Similarity(masterDoc.get(QueryService.FIELD_VALUE), translationDoc.get(QueryService.FIELD_VALUE), score,
				masterDoc.get(QueryService.FIELD_FULL_PATH), slave.toURI().toString(), key, hitNumber, isSameProject);
		return similarity;

    }


	private Similarity createTMXSimilarity(Document masterDoc, Locale language, int score, int hitNumber) throws IOException {
		if(!language.toString().equals(masterDoc.get(QueryService.FIELD_TMX_LOCALE)))
			return null;
		
		String key = masterDoc.get(QueryService.FIELD_KEY);
		Similarity similarity = new Similarity(masterDoc.get(QueryService.FIELD_VALUE), masterDoc.get(QueryService.FIELD_TMX_VALUE), score,
				masterDoc.get(QueryService.FIELD_FULL_PATH), masterDoc.get(QueryService.FIELD_TEMPLATE_LOCATION), key, hitNumber, false);
		return similarity;
	}


	public static class Similarity
        implements Serializable, Comparable<Similarity>
    {

        private static final long serialVersionUID = 1L;
        private String original;
        private String translation;
        private int similarity;
        private String fullPath;
        private String uri;
        private String key;
        /** the order number is to make sure compareTo never returns 0 */
        private int orderNumber;
		private boolean sameProject;


        public Similarity(String original,
                          String translation,
                          int similartiy,
                          String fullPath,
                          String uri,
                          String key,
                          int orderNumber, boolean sameProject)
        {
            super();
            this.original = original;
            this.translation = translation;
            this.similarity = similartiy;
            this.fullPath = fullPath;
            this.uri = uri;
            this.key = key;
            this.orderNumber = orderNumber;
            this.sameProject = sameProject;
        }


        public String getKey()
        {
            return key;
        }


        public String getOriginal()
        {
            return original;
        }


        public String getTranslation()
        {
            return translation;
        }


        public int getSimilarity()
        {
            return similarity;
        }


        public String getFullPath()
        {
            return fullPath;
        }


        public String getUri()
        {
            return uri;
        }

        public boolean isSameProject()
        {
			return sameProject;
		}

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((fullPath == null) ? 0 : fullPath.hashCode());
            result = prime * result + ((uri == null) ? 0 : uri.hashCode());
            result = prime * result + ((original == null) ? 0 : original.hashCode());
            result = prime * result + similarity;
            result = prime * result + ((translation == null) ? 0 : translation.hashCode());
            return result;
        }


        @Override
        public boolean equals(Object obj)
        {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Similarity other = (Similarity)obj;
            if (fullPath == null)
            {
                if (other.fullPath != null)
                    return false;
            }
            else if (!fullPath.equals(other.fullPath))
                return false;
            if (uri == null)
            {
                if (other.uri != null)
                    return false;
            }
            else if (!uri.equals(other.uri))
                return false;
            if (original == null)
            {
                if (other.original != null)
                    return false;
            }
            else if (!original.equals(other.original))
                return false;
            if (similarity != other.similarity)
                return false;
            if (translation == null)
            {
                if (other.translation != null)
                    return false;
            }
            else if (!translation.equals(other.translation))
                return false;
            return true;
        }


        @Override
        public int compareTo(Similarity o)
        {
           int result = o.getSimilarity() - getSimilarity();
           if(result==0)
           {
               if(equals(o))
                   return 0;
               return orderNumber-o.orderNumber;
           }
           return result;
        }

    }
}
