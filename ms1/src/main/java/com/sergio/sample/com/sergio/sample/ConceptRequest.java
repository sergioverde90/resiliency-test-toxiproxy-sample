package com.sergio.sample.com.sergio.sample;

public class ConceptRequest {

    private String concept;

    private ConceptRequest() {}

    public ConceptRequest(String concept) {
        this.concept = concept;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }
}

