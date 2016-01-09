package ox.softeng.oboservice.GSonExclusion;

import ox.softeng.oboservice.OntologyHandler;
import ox.softeng.oboservice.OntologyHandler.LocalTerm;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class ExclBroaderTerms implements ExclusionStrategy {
    public boolean shouldSkipClass(Class<?> arg0) {
        return false;
    }
    public boolean shouldSkipField(FieldAttributes f) {
        return (f.getDeclaringClass() == LocalTerm.class && f.getName().equals("broaderTerms"));
    }
}