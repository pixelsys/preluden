package ox.softeng.oboservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.biojava3.ontology.Ontology;
import org.biojava3.ontology.Term;
import org.biojava3.ontology.Triple;
import org.biojava3.ontology.utils.Annotation;

public class OntologyHandler {

	//private Ontology ontology;
	public HashMap<String,LocalTerm> db;
	
	
	public OntologyHandler(){
	}
	
	public OntologyHandler(HashMap<String,LocalTerm> db){
		this.db = db;
	}
	
	public OntologyHandler(Ontology ontology){
		//this.ontology = ontology;
		db = buildInMemoryDB(ontology);
	}
	
	
	public static HashMap<String,LocalTerm> buildInMemoryDB(Ontology ontology){
		
		HashMap<String,LocalTerm> db = new HashMap<String,LocalTerm>();
	
		if(ontology == null)
			return db;

		//get all ontology triples like: HP:0000498(Subject) is_a HP:0100533(Object)
		Object[] triples =  ontology.getTriples(null, null, null).toArray();

		//for each triple, build its memory tree
		for (Object triple : triples) {
			Triple trip = (Triple) triple;							  	
			
			if( trip!=null && trip.getPredicate().getName().equals("is_a")){	
				
				//get subject and object from this triple 
				Term subject =  trip.getSubject();
				Term object  =  trip.getObject(); 		
				
				LocalTerm subjectLocalTerm = null;
				LocalTerm objectLocalTerm  = null;
							
				//check if subject is in Memory database
				if(!db.containsKey(subject.getName())){
					subjectLocalTerm = buildTerm(subject);
					db.put(subject.getName(),buildTerm(subject));
				}else{
					subjectLocalTerm = db.get(subject.getName());
				}
				
				//check if object is in Memory database
				if(!db.containsKey(object.getName())){
					objectLocalTerm = buildTerm(object);
					db.put(object.getName(),buildTerm(object));
				}else{
					objectLocalTerm = db.get(object.getName());
				}
				
				//add object as broader element into the subject term
				subjectLocalTerm.broaderTerms.add(objectLocalTerm);
				db.put(subject.getName(),subjectLocalTerm);

				//add subject as narrower element into the object term
				objectLocalTerm.narrowTerms.add(subjectLocalTerm);
				db.put(object.getName(),objectLocalTerm);
			}
		}
		return db;
	}
	
	public ArrayList<LocalTerm> getBroaderTerms(String termId){
				
		ArrayList<LocalTerm> broaderTerms = new ArrayList<LocalTerm>();
		if(db.containsKey(termId)){
			broaderTerms = db.get(termId).broaderTerms;
		}
		return broaderTerms;
		
//		//get all ontology triples like: HP:0000498(Subject) is_a HP:0100533(Object)
//		Object[] triples =  ontology.getTriples(null, null, null).toArray();
//
//		//for each triple, try to find the one that matches the termId parameter
//		for(int i = 0; i < triples.length; i++)
//		{
//			Triple trip = (Triple) triples[i];							  	
//			
//			if( trip!=null && trip.getPredicate().getName().equals("is_a")){	
//				
//				//get subject and object from this triple 
//				Term subject =  trip.getSubject();
//				Term object  =  trip.getObject(); 				
//				
//				//if subject matches with the termId provided in the parameter
//				//then add its object to the result		
//				if(subject.getName().equals(termId)){
// 					LocalTerm term = buildTerm(object);
// 					broaderTerms.add(term);
//				}
//			}
//		}		
//		return broaderTerms;
	}
		
	
	public ArrayList<LocalTerm> getNarrowTerms(String termId){
		
		ArrayList<LocalTerm> narrowTerms = new ArrayList<LocalTerm>();
		if(db.containsKey(termId)){
			narrowTerms = db.get(termId).narrowTerms;
		}
		return narrowTerms;
//		
//		//get all ontology triples like: HP:0000498(Subject) is_a HP:0100533(Object)
//		Object[] triples =  ontology.getTriples(null, null, null).toArray();
//
//		//for each triple, try to find the one that matches the termId parameter
//		for(int i = 0; i < triples.length; i++)
//		{
//			Triple trip = (Triple) triples[i];							  	
//			
//			if( trip!=null && trip.getPredicate().getName().equals("is_a")){	
//				
//				//get subject and object from this triple 
//				Term subject =  trip.getSubject();
//				Term object  =  trip.getObject(); 				
//				
//				//if object matches with the termId provided in the parameter
//				//then add its subject to the result		
//				if(object.getName().equals(termId)){
// 					LocalTerm term = buildTerm(subject);
//					narrowTerms.add(term);
//				}
//			}
//		}		
//		return narrowTerms;
	}

	private static LocalTerm buildTerm(Term term){
		String defStr = null;
		String commentStr = null;
		Annotation annotation = term.getAnnotation();
		if(annotation != null){
			Map map = annotation.asMap();
			Object def = map.get("def");
			Object comment =  map.get("comment");
			if(def != null){
				defStr = (String) def;
				defStr = defStr.replaceAll("\\[.*\\]","");
				defStr = defStr.replaceAll("\"","").trim();
			}
			if(comment != null){
				commentStr = (String) comment;
				commentStr = commentStr.replaceAll("\\[.*\\]","");
				commentStr = commentStr.replaceAll("\"","");
			}	
		}
		OntologyHandler handler = new OntologyHandler();
 		return handler.new LocalTerm(term.getName(),term.getDescription(),defStr,commentStr);
	}
	
//	private LocalTerm buildTerm(String termId){
//		Set<Term> termSet = ontology.getTerms();
//		Iterator<Term> termIterator = termSet.iterator();		
//		while (termIterator.hasNext()){
//			Term term = (Term) termIterator.next();
//			if(term.getName() != null && termId.equals(term.getName())){
//				return buildTerm(term);
//			}
//		}
//		return null;
//	}

	
	public LocalTerm getNarrowTermsTree(String termId){
		
		return db.get(termId);
		
//		LocalTerm localTerm = buildTerm(termId);
//		Object[] triples =  ontology.getTriples(null, null, null).toArray();
//		findNarrowTermsTree(triples, localTerm);		
//		return localTerm;
	}
	
	public ArrayList<LocalTerm> getNarrowTermsTreeAsFlatList(String termId){		
//		LocalTerm localTerm = buildTerm(termId);		
//		Object[] triples =  ontology.getTriples(null, null, null).toArray();
//		findNarrowTermsTree(triples, localTerm);
		
		LocalTerm localTerm = db.get(termId);
				
		ArrayList<LocalTerm> flatList = new ArrayList<LocalTerm>();		
		if(localTerm != null){
			makeFlatList(localTerm,flatList,0);
		}
		
		return flatList;
	}
	
	
	//recursively finds the narrow terms for a term
//	private void findNarrowTermsTree(Object[] triples,LocalTerm localTerm) {		
//		for(int i = 0; i < triples.length; i++)
//		{
//			Triple trip = (Triple) triples[i];		
//			if( trip!=null && trip.getPredicate().getName().equals("is_a")){
//				
//				if(trip.getObject().getName().equals(localTerm.id)){
//					LocalTerm narrowTerm = buildTerm(trip.getSubject());					
//					localTerm.narrowTerms.add(narrowTerm);				
//					findNarrowTermsTree(triples,narrowTerm);
//				}
//			}
//		}
//	}
	
	private void makeFlatList(LocalTerm term,ArrayList<LocalTerm> flatList, int identLevel){
		
		String space="";
//		for(int i = 0; i < identLevel*2 ; i++){
//			space = space+ "&nbsp;";
//		}
		flatList.add(new LocalTerm(term.id,space + term.name,term.def,term.comment,identLevel));
				
		identLevel++;		
		for(int i = 0; i < term.narrowTerms.size(); i++){
			makeFlatList(term.narrowTerms.get(i),flatList, identLevel);
		}
	}
	
	
//this class is used to retrieve the narrower terms recursively
public class LocalTerm {
		public String id;
		public String name;	
		public String def;
		public String comment;
		
		public int depthLevel;
		
		public ArrayList<LocalTerm> narrowTerms;
		public ArrayList<LocalTerm> broaderTerms;

		
		
		
		public LocalTerm(String id,String name,String def,String comment,int depthLevel){
			this.id = id;
			this.name = name;
			this.def = def;
			this.comment = comment;
			this.depthLevel = depthLevel;
			
			narrowTerms = new ArrayList<LocalTerm>();
			broaderTerms = new ArrayList<LocalTerm>();
		}
		
		
		public LocalTerm(String id,String name,String def,String comment){
			this.id = id;
			this.name = name;
			this.def = def;
			this.comment = comment;
			
			narrowTerms = new ArrayList<LocalTerm>();
			broaderTerms = new ArrayList<LocalTerm>();
		}
		
		public String toString() { 
		    return id + " " + name;
		    }
	}	 
}


 
 
