package com.treemarks.app.triplestore;

import com.treemarks.app.domain.Category;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.vocabulary.DCTERMS;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by jeroen@kransen.nl on 03-12-16.
 */
@Component
public class TripleStore {

    private static final String IRI_BASE = "http://kransen.nl/jeroen/ontologies/treemarks/";

    private Repository repo1;
    private ValueFactory vf;
    private IRI categoryTypeIri;

    @PostConstruct
    public void init() throws Exception {
        String serverURL = "http://192.168.1.41:7200";
        RepositoryManager repositoryManager = new RemoteRepositoryManager(serverURL);
        repositoryManager.initialize();
        String identity = "repo1";
        this.repo1 = repositoryManager.getRepository(identity);
        if (this.repo1 == null) {
            throw new ExceptionInInitializerError("Could not open remote repository: " + serverURL + "/" + identity);
        }
        vf = this.repo1.getValueFactory();
        categoryTypeIri = vf.createIRI(IRI_BASE + "Category");
    }

    @PreDestroy
    public void cleanUp() throws Exception {
    }

    public void saveCategory(Category category) {
        RepositoryConnection conn = repo1.getConnection();
        IRI categoryIri = vf.createIRI(category.getIri());
        conn.add(categoryIri, RDF.TYPE, categoryTypeIri, categoryIri);
        IRI ownerIRI = vf.createIRI(IRI_BASE + "users/" + category.getOwner().getLogin());
        conn.add(ownerIRI, RDF.TYPE, FOAF.PERSON); // global context to avoid repeating statement
        conn.add(categoryIri, DCTERMS.CREATOR, ownerIRI, categoryIri);
        conn.close();
    }
}
