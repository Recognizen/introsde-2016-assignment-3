package introsde.assignment.soap.client;
 

import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import introsde.assignment.soap.model.Measure;
import introsde.assignment.soap.model.MeasureDefinition;
import introsde.assignment.soap.model.Person;
import introsde.assignment.soap.ws.People;
 
public class PeopleClient{
	public static void main(String[] args) throws Exception {
		URL url = new URL("http://localhost:6902/ws/people?wsdl");
        //1st argument service URI, refer to wsdl document above
		//2nd argument is service name, refer to wsdl document above
        QName qname = new QName("http://ws.soap.assignment.introsde/", "PeopleService");
        Service service = Service.create(url, qname);
        
        //Instancing
        People people = service.getPort(People.class);
        /*
        //Task 1 
        List<Person> pList = people.readPersonList();
        System.out.println("Result ==> "+pList);
        System.out.println("First Person in the list ==> "+pList.get(0).getFirstname());
        */
        //Task 2
        Person p = people.readPerson(3);
        System.out.println("Result ==> "+p.getFirstname());
        
        //Task 3
        p.setFirstname("Chucky");
        people.updatePerson(p);
        
        //Task 2
        Person newP = people.readPerson(3);
        System.out.println("Result ==> "+newP.getFirstname());
        
        //Task 4
        newP.setFirstname("Jim");
        Person jim = people.createPerson(newP);
        
        //Task 5
        people.deletePerson(jim.getPersonId());
        
        //Task 6
       // System.out.println(people.readPersonHistory(3,"weight").get(0).getMeasureValue());
        
        //Task 7
        for(String s : people.readMeasureTypes()){
        	System.out.println(s);
        }
        //Task 8
        System.out.println("1 "+ people.readPersonMeasure(3, "weight", 1));
        System.out.println("2 "+ people.readPersonMeasure(3, "height", 1));
        
        MeasureDefinition mDef = new MeasureDefinition();
        mDef.setTid(3);
        mDef.setMeasureType("steps");
        
        Measure m = new Measure();
        m.setMeasureDefinition(mDef);
        m.setMeasureValue("20");
        //Task 9
     //   people.savePersonMeasure(3, m);
        
        m.setMeasureValue("999");
        m.setMid(786);
        //Task 10
        people.updatePersonMeasure(3, m);
        
    }
}