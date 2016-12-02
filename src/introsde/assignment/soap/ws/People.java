package introsde.assignment.soap.ws;


import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import introsde.assignment.soap.model.Person;
import introsde.assignment.soap.model.HealthMeasureHistory;
import introsde.assignment.soap.model.Measure;

@WebService
@SOAPBinding(style = Style.DOCUMENT, use=Use.LITERAL) //optional
public interface People {
	 
	//Task 1
    @WebMethod(operationName="readPersonList")
    @WebResult(name="people") 
    public List<Person> readPersonList();
	
    //Task 2
    @WebMethod(operationName="readPerson")
    @WebResult(name="person") 
    public Person readPerson(@WebParam(name="personId") long id); 
    
    //Task 3
    @WebMethod(operationName="updatePerson")
    @WebResult(name="person") 
    public Person updatePerson(@WebParam(name="person") Person p);

    
    //Tast 4
    @WebMethod(operationName="createPerson")
    @WebResult(name="person") 
    public Person createPerson(@WebParam(name="person") Person p);

    //Task 5
    @WebMethod(operationName="deletePerson")
    @WebResult(name="personId") 
    public long deletePerson(@WebParam(name="personId") long id);
    
    //Task 6
    @WebMethod(operationName="readPersonHistory")
    @WebResult(name="healthHistory") 
    public List<HealthMeasureHistory> readPersonHistory(@WebParam(name="personId") long id, @WebParam(name="measureType") String measureType);

    //Task 7
    @WebMethod(operationName="readMeasureTypes")
    @WebResult(name="measureTypes") 
    public List<String> readMeasureTypes();

    
    //Task 8
    @WebMethod(operationName="readPersonMeasure")
    @WebResult(name="measure") 
    public HealthMeasureHistory readPersonMeasure(@WebParam(name="personId") long id, @WebParam(name="measureType") String measureType, @WebParam(name="mid") long mid );

    //Task 9
    @WebMethod(operationName="savePersonMeasure")
    @WebResult(name="measure") 
    public Person savePersonMeasure(@WebParam(name="personId") long id, @WebParam(name="measure") Measure m);

    //Task 10.1 Update current Measure
    @WebMethod(operationName="updatePersonMeasure")
    @WebResult(name="measure") 
    public Measure updatePersonMeasure(@WebParam(name="personId") long id, @WebParam(name="measure") Measure m);

    //Task 10.2 Update old Measure in history
    @WebMethod(operationName="updatePersonHistoryMeasure")
    @WebResult(name="measure") 
    public HealthMeasureHistory updatePersonHistoryMeasure(@WebParam(name="personId") long id, @WebParam(name="measure") HealthMeasureHistory m);

}
