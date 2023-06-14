# Zoo-Animal-Management system

Here is my Zoo-Animal-Management app for the 2023 Twoday java internship.
And here is the link to the **postman** workspace where you can find and try/test all the valid requests for this application:

https://www.postman.com/bold-astronaut-704144/workspace/twoday-internship-task

**Main technologies used where:** _Spring boot, Docker, PostgreSQL_

## **Endpoints:**
### Add Enclosures
Endpoint: POST /zoo/enclosure/addEnclosures

Adds a list of enclosures to a zoo.

### Remove Enclosure by ID
Endpoint: DELETE /zoo/enclosure/removeEnclosureById/{enclosureId}

Removes an enclosure from a zoo based on its ID.

### Get All Enclosures by Zoo Name
Endpoint: GET /zoo/enclosure/getAllByZooName/{zooName}

Retrieves a list of enclosures associated with a specific zoo.

### Create a New Zoo
Endpoint: POST /zoo

Creates a new zoo. 
You can also optionally add enclosures and animals to the zoo in this one request.

### Add Animals to Zoo
Endpoint: POST /zoo/animal/addAnimals

Adds a list of animals to a zoo.

### Remove Animal by ID
Endpoint: PUT /zoo/animal/removeById/{id}

Removes a specified number of animals from a zoo based on their ID and the desired amount.

### Get All Animals by Zoo Name
Endpoint: GET /zoo/animal/getAllByZooName/{zooName}

Retrieves a list of animals associated with a specific zoo based on its name.
