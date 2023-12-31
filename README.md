### 1. How many total lines of code written?
  2921 lines of code
### 2. What are the features  (including required assignments features) implemented and functional in your project?
  - User account, profile, apply to and bookmark jobs
  - Autocomplete feature
  - Trending keyword searches
  - Analytics: top searches bar graph
  - Geospatial Near me search
  - OpenAI recommendation based on past applied jobs
  - Webscrapping jobs using selenium (Stored in MySQL)
  - Exports data as JSON to Neo4j
    
### 3. What are the Assignments features that are NOT implemented?
  - We implemented a knowledge graph, but there is currently no search implementation
### 4. What are the Assignments features that are attempted but NOT functional?
  - None that we are aware of
---
## Getting Started:
### Installation:
#### You will need:
- Java
- Node.js
- MySQL
- Neo4j

### Setting up ENV variable:
- Create a .env file in both /frontend and /backend
```
/backend.env
WEBDRIVER_CHROME_DRIVER="path to driver"
DB_USERNAME="MySQL username, usually root by default"
DB_PASSWORD="MySQL password"
OPENAI_TOKEN="Openai_token"
```

```
/frontend.env
REACT_APP_NEO4JPASS="Neo4j password"
REACT_APP_GEOAPI="make an account at https://www.geonames.org/export/web-services.html and use the username here"
```
### For Selenium to work:
- Download Chromedriver (ver 119) and save the chromedriver.exe filepath.
- Ensure your Chrome browser (separate from the Chromedriver) is updated to at least v119 as well.
- If you haven't already, create a .env file in the /backend dir.
- add variable WEBDRIVER_CHROME_DRIVER="insert file path".

# Set-up
- Check SqlDump for table creation queries
- 
### Springboot Java Backend:
- Run the Springboot backend in commandline in the /Backend dir using the command:
```
mvn spring-boot:run
```
- Run the React.JS frontend in commandline in a separate terminal in the /Frontend dir using the command:
```
# If running for the first time
npm i
```
```
npm start
```
