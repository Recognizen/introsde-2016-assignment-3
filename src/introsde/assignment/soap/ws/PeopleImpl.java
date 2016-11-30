package introsde.assignment.soap.ws;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import introsde.assignment.soap.model.HealthMeasureHistory;
import introsde.assignment.soap.model.Measure;
import introsde.assignment.soap.model.Person;
import introsde.assignment.soap.model.MeasureDefinition;
import introsde.assignment.soap.model.MeasureTypes;

//Service Implementation

@WebService(endpointInterface = "introsde.assignment.soap.ws.People",
	serviceName="PeopleService")
public class PeopleImpl implements People {	
	
	//Task 1
	@Override
	public List<Person> readPersonList() {
		return Person.getAll();
	}

	//Task 2
	@Override
	public Person readPerson(long id) {
		System.out.println("---> Reading Person by id = "+id);
		Person p = Person.getPersonById(id);
		if (p!=null) {
			System.out.println("---> Found Person by id = "+id+" => "+p.getFirstname());
		} else {
			System.out.println("---> Didn't find any Person with  id = "+id);
		}
		return p;
	}
	
	//Task 3
	@Override
	public Person updatePerson(Person p) {
		
		//Get the existing person from the database
		Person existing = Person.getPersonById(p.getPersonId());
		//Update the existing person with the new information only if specified
		if(existing != null){
			if(p.getFirstname() != null)
				existing.setFirstname(p.getFirstname());
			if(p.getLastname() != null)
				existing.setLastname(p.getLastname());
			if(p.getBirthdate() != null)
				existing.setBirthdate(p.getBirthdate());
			if(p.getEmail() != null)
				existing.setEmail(p.getEmail());
			if(p.getUsername() != null)
				existing.setUsername(p.getUsername());
		
			//Measures will not be touched
			Person.updatePerson(existing);
		}
		return existing;
	}

	//Task 4
	@Override
	public Person createPerson(Person p) {
		//reset id to avoid conflicts
		p.setPersonId(0);
		
		List<Measure> profile = p.getCurrentHealth();
		//List<HealthMeasureHistory> history = p.getHealthHistory();
		
		if(profile != null && !profile.isEmpty()){
			for(Measure m : profile)
			{
				//reset measure id to avoid conflicts
				m.setMid(0);
				//set the personId in the measure table
				m.setPerson(p);
				//If no date is specified, assign NOW as date
				if(m.getDateRegistered() == null)
					m.setDateRegistered(new Date());
				
				//find a pre-defined MeasureDefinition type
				MeasureDefinition mDef = MeasureDefinition.getByName(m.getMeasureDefinition().getMeasureType());
				
				//if the measure does not have a correct type defined then remove it
				if(mDef != null){
					m.setMeasureDefinition(mDef);
				}else{
					//this will stop the request from being processed and the person is not saved in the database
					profile.remove(m);
				}
			}
		}		
		
		Person.savePerson(p);
		return p;
	}

	//Task 5
	@Override
	public long deletePerson(long id) {
		Person p = Person.getPersonById(id);
		if (p!=null) {
			Person.removePerson(p);
			return 0;
		} else {
			return -1;
		}
	}
	//Tasl 6
	@Override
	public List<HealthMeasureHistory> readPersonHistory(long id, String measureType) {
			Person p = Person.getPersonById(id);
			List<HealthMeasureHistory> healthHistory = new ArrayList<>();
			for(HealthMeasureHistory m : p.getHealthHistory()){
				if(m.getMeasureDefinition().getMeasureType().equals(measureType)){
					healthHistory.add(m);
				}
			}
			return healthHistory;
	}
	
	//Task 7
    public List<String> readMeasureTypes(){
		
		MeasureTypes types = new MeasureTypes();
		
		return types.getMeasureType();
    }
	
	//Task 8
	@Override
	public HealthMeasureHistory readPersonMeasure(long id, String measureType, long mid) {
		
		HealthMeasureHistory measure = null;
		
		Person p = Person.getPersonById(id);
		
		//filter measures to find the correct one
		for(HealthMeasureHistory m : p.getHealthHistory()){
			if(m.getMeasureDefinition().getMeasureType().equals(measureType)){
				//if type matches, find the one having id equals to given mid
				if(m.getMid() == mid){
					measure = m;
				}
			}
		}

		return measure;		
	}
	
	//Task 9
	public Person savePersonMeasure(long id, Measure measure){
	
		//Making sure the type defined is allowed
		String measureType = measure.getMeasureDefinition().getMeasureType();
		measure.setMeasureDefinition(MeasureDefinition.getByName(measureType));
		
		//type check if(good type) do else error response
		if(measure.getMeasureDefinition() != null){
			
			Measure existing = null;
			
			Person p = Person.getPersonById(id);
			//fill in missing information
			measure.setPerson(p);
			if(measure.getDateRegistered() == null)
				measure.setDateRegistered(new Date());
			
			//find the measure in the current list
			List<Measure> oldMeasures = p.getCurrentHealth();
			for(int i = 0; i < oldMeasures.size(); i++){
				Measure tmp = oldMeasures.get(i);
				if(tmp.getMeasureDefinition().getMeasureType().equals(measureType)){
					existing = tmp;
					
					HealthMeasureHistory newEntry = new HealthMeasureHistory();
					newEntry.setPerson(p);
					newEntry.setMeasureValue(existing.getMeasureValue());
					newEntry.setDateRegistered(existing.getDateRegistered());
					newEntry.setMeasureDefinition(existing.getMeasureDefinition());
					
					HealthMeasureHistory.saveHealthMeasureHistory(newEntry);
					Measure.removeMeasure(existing);
					
					oldMeasures.remove(existing);
					oldMeasures.add(measure);
				}
			}
			
			if(existing == null)
			{
				oldMeasures.add(measure);
			}
			
			Measure.saveMeasure(measure);
			
			return Person.updatePerson(p);
		}
		
		return null;
	}
	
	//Task 10.1 Current Measure
	public Measure updatePersonMeasure(long id, Measure measure){
		
		Person p = Person.getPersonById(id);
		
	    Measure existing = null;
	    //find the measure of the person having given id (check the type constraint)
	    for(Measure m : p.getCurrentHealth()){
	    	if(m.getMid() == measure.getMid() 
	    			&& m.getMeasureDefinition().getMeasureType()
	    			.equals(measure.getMeasureDefinition().getMeasureType())){
	    		
	    		//the measure will keep the old MeasureDefinition
	    		existing = m;
	    	}
	    }
	    
	    if(existing != null ){
		    //update only given fields
		    if(measure.getMeasureValue() != null)
		    	existing.setMeasureValue(measure.getMeasureValue());
		    if(measure.getDateRegistered() != null)
		    	existing.setDateRegistered(measure.getDateRegistered());
		    
		    return Measure.updateMeasure(existing);
	    }
	    
	    return null;
	}
	
	//Task 10.2 History Measure
	public HealthMeasureHistory updatePersonMeasure(long id, HealthMeasureHistory measure){
		
		Person p = Person.getPersonById(id);
		
	    HealthMeasureHistory existing = null;
	    //find the measure of the person having given id (check the type constraint)
	    for(HealthMeasureHistory m : p.getHealthHistory()){
	    	if(m.getMid() == measure.getMid() 
	    			&& m.getMeasureDefinition().getMeasureType()
	    			.equals(measure.getMeasureDefinition().getMeasureType())){
	    		
	    		//the measure will keep the old MeasureDefinition
	    		existing = m;
	    	}
	    }
	    
	    if(existing != null ){
		    //update only given fields
		    if(measure.getMeasureValue() != null)
		    	existing.setMeasureValue(measure.getMeasureValue());
		    if(measure.getDateRegistered() != null)
		    	existing.setDateRegistered(measure.getDateRegistered());
		    
		    return HealthMeasureHistory.updateHealthMeasureHistory(existing);
	    }
	    
	    return null;
	}
	
}
