# introsde-2016-assignment-3-server

	Bogdan Liviu Alexandru

------------WORKED ALONE----------------

#Description

Repository contains four packages:
* introsde.assignment.soap.model - annotated classes used to model the database structure
* introsde.assignment.soap.dao - used to connect the model to the database
* introsde.assignment.soap.ws - contains the People SOAP interface as well as the methods implementation PeopleImpl
* introsde.assignment.soap.endpoint - contains the PeoplePublisher which creates a standalone server that exposes the people webservice

##The Model
The model includes a class for storing a Person. 
The person then stores two lists, List<Measure> currentHealth which stores the most recent (unique by type) measures and List<HealthMeasureHistory> which contains all the other measures of that person.

Measure and HealthMeasureHistory classes are very similar and contain all information relevant to a measure, including the measureType which is references a MeasureDefinition class.

##The Service
The service functionality is defined using the document style. The method definitions are saved in the People interface.
The PeopleImpl contains the implementation of the methods whose code is executed when a client calls the specified operation name.

Given the distinction between Measure and HealthMeasureHistory measures, a second method for updating is provided:
* Measure updatePersonMeasure(@WebParam(name="personId") long id, @WebParam(name="measure") Measure m)
* HealthMeasureHistory updatePersonHistoryMeasure(@WebParam(name="personId") long id, @WebParam(name="measureHistory") HealthMeasureHistory m)

The two methods essentially do the same thing, update any information for a specified existing measure.

#ServerURL

http://safe-everglades-33716.herokuapp.com/ws/people?wsdl

#Client

Client code contained in the following repository: https://github.com/Recognizen/introsde-2016-assignment-3-client

Execute Client: ant execute.client
