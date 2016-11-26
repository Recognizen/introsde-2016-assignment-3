package introsde.assignment.soap.ws;


import java.util.List;

import javax.jws.WebService;

import introsde.assignment.soap.model.Measure;
import introsde.assignment.soap.model.Person;

//Service Implementation

@WebService(endpointInterface = "introsde.assignment.soap.ws.People",
	serviceName="PeopleService")
public class PeopleImpl implements People {

	@Override
	public Person readPerson(int id) {
		System.out.println("---> Reading Person by id = "+id);
		Person p = Person.getPersonById(id);
		if (p!=null) {
			System.out.println("---> Found Person by id = "+id+" => "+p.getFirstname());
		} else {
			System.out.println("---> Didn't find any Person with  id = "+id);
		}
		return p;
	}

	@Override
	public List<Person> getPeople() {
		return Person.getAll();
	}

	@Override
	public long addPerson(Person person) {
		Person.savePerson(person);
		return person.getPersonId();
	}

	@Override
	public long updatePerson(Person person) {
		Person.updatePerson(person);
		return person.getPersonId();
	}

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

	@Override
	public long updatePersonHP(long id, Measure hp) {
	/*	Measure ls = Measure.getMeasureById(hp.getMid());
		if (ls.getPerson().getId() == id) {
			Measure.updateMeasure(hp);
			return hp.getMid();
		} else {
			return -1;
		}*/
		return -1;
	}
}
