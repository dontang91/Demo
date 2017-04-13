_if you want to use mailservice, please configure your email address and password as well as the email address you want to send mail to in the application.properties file 
also uncomment the @EnableScheduling annotation in the main application class._

***Upload File***
----
* **URL**
  `/file/{id}`
* **Method**
  `POST`
* **URL Params**
  `id=[integer]`
* **Data Params**
  `file`
* **Success Response:**
  * **Code:** `200`
  * **Content:** `Successfully uploaded file: filename`

***Download File***
----
* **URL**
  `/file/{id}`
* **Method**
  `GET`
* **URL Params**
  `id=[integer]`
* **Data Params**
  `None`
* **Success Response:**
  * **Code:** `200`
  * **Content:** `File will be downloaded shortly`

***Get file metadata by ID***
----
* **URL**
  `/file/{id}/info`
* **Method**
  `GET`
* **URL Params**
  `id=[integer]`
* **Data Params**
	`None`
* **Success Response:**
  * **Code:** `200`
  * **Content:** `json`

***Get all files metadata***
----
* **URL**
  `/files/info`
* **Method**
  `GET`
* **URL Params**
  `None`
* **Data Params**
  `None`
* **Success Response:**
  * **Code:** `200`
  * **Content:** `json`

***Search file by name***
----
* **URL**
  `/files/name/{name}`
* **Method**
  `GET`
* **URL Params**
  `name=[String]`
* **Data Params**
  `None`
* **Success Response:**
  * **Code:** `200`
  * **Content:** `json`

***Search by criteria***
----
* **URL**
  `/files/search?name={name}}`
* **Method**
  `GET`
* **URL Params**
  `name=[String]`
* **Data Params**
  `None`
* **Success Response:**
  * **Code:** `200`
  * **Content:** `json`