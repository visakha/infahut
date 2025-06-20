# InfaHut - Informatica Asset Browser

InfaHut is a modular Spring Boot application designed to browse and manage Informatica IICS (Intelligent Cloud Services) and CDI (Cloud Data Integration) assets through a clean web interface with a powerful plugin system.

## Features

- **Modular Architecture**: Built using Spring Modulith for clean separation of concerns
- **Dynamic Plugin System**: Extensible plugin architecture for custom functionality
- **Modern UI**: Clean, responsive web interface built with JTE templates and Tailwind CSS
- **Informatica Integration**: Seamless integration with Informatica Cloud APIs
- **Session Management**: Automatic session handling and refresh capabilities
- **SQLite Database**: Lightweight database for storing session and metadata information

## Architecture

The application follows a modular monolith pattern with the following modules:

### Core Module (`com.infahud.infahut.core`)
- Database configuration
- Domain models (LoginCredential, LoginSession)
- Singleton pattern for session management (LoginCred)

### Plugins Module (`com.infahud.infahut.plugins`)
- Plugin system infrastructure
- Login-related plugins:
  - **LoginApiPlugin**: Handles authentication with Informatica Cloud
  - **RetrieveSessionIdPlugin**: Retrieves stored session information
  - **RefreshSessionIdPlugin**: Automatically refreshes sessions

### Web Module (`com.infahud.infahut.web`)
- REST controllers
- JTE templates
- Frontend assets

## Technology Stack

- **Backend**: Spring Boot 3.5, Spring Modulith 1.4, Spring Data JDBC
- **Frontend**: JTE Templates, Tailwind CSS, JavaScript
- **Database**: SQLite
- **Build**: Maven
- **Java**: Version 24

## Prerequisites

- JDK 24 or higher
- Maven 3.9+
- Git

## Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd infahut
```

### 2. Configure Informatica Credentials
Edit `src/main/resources/application.properties`:

```properties
informatica.username=your_username
informatica.password=your_password
informatica.login.url=https://dmp-us.informaticacloud.com/saas/public/core/v3/login
```

### 3. Build the Application
```bash
mvn clean compile
```


### 3.1. Run the point test
```bash
mvn -Dtest=com.infahud.infahut.plugins.login.LoginApiPluginTest test
mvn -Dtest=LoginApiPluginTest test

```


### 4. Run the Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## Project Structure

```
src/
├── main/
│   ├── java/com/infahud/infahut/
│   │   ├── InfaHutApplication.java          # Main application class
│   │   ├── core/                            # Core module
│   │   │   ├── config/DatabaseConfig.java  # Database configuration
│   │   │   ├── model/                       # Domain models
│   │   │   └── singleton/LoginCred.java     # Session singleton
│   │   ├── plugins/                         # Plugins module
│   │   │   ├── core/                        # Plugin infrastructure
│   │   │   └── login/                       # Login plugins
│   │   └── web/                             # Web module
│   │       ├── controller/                  # REST controllers
│   │       └── service/                     # Web services
│   ├── jte/                                 # JTE templates
│   │   └── index.jte                        # Main dashboard template
│   └── resources/
│       └── application.properties           # Configuration
└── test/                                    # Test classes
```

## Plugin Development

### Creating a New Plugin

1. **Extend AbstractPlugin**:
```java
@Component
public class MyCustomPlugin extends AbstractPlugin {
    public MyCustomPlugin() {
        super("my-custom-plugin", "1.0.0");
    }
    
    @Override
    protected void doInitialize() {
        // Initialization logic
    }
    
    @Override
    protected void doStart() {
        // Start logic
    }
    
    @Override
    protected void doStop() {
        // Stop logic
    }
    
    @Override
    protected void doDestroy() {
        // Cleanup logic
    }
}
```

2. **Register the Plugin**:
```java
@Configuration
public class MyPluginConfiguration implements CommandLineRunner {
    @Autowired
    private PluginManager pluginManager;
    
    @Autowired
    private MyCustomPlugin myPlugin;
    
    @Override
    public void run(String... args) {
        pluginManager.registerPlugin(myPlugin);
    }
}
```

3. **Configure Plugin Loading**:
Add to `application.properties`:
```properties
plugins.child=plugin-RetrieveSessionId,plugin-RefreshSessionId,my-custom-plugin
```

### Plugin Lifecycle

Plugins follow a defined lifecycle:
1. **UNLOADED** → **INITIALIZED** (via `initialize()`)
2. **INITIALIZED** → **STARTED** (via `start()`)
3. **STARTED** → **STOPPED** (via `stop()`)
4. **STOPPED** → **UNLOADED** (via `destroy()`)

## API Endpoints

### Authentication
- `POST /api/login` - Perform login to Informatica Cloud
- `POST /api/refresh` - Refresh current session
- `GET /api/status` - Get current login and plugin status

### Management
- `GET /actuator/health` - Application health check
- `GET /actuator/modulith` - Module structure information

## Database Schema

The application creates the following SQLite tables:

### login_sessions
```sql
CREATE TABLE login_sessions (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    seq INTEGER,
    session_id TEXT,
    base_api_url TEXT,
    response TEXT,
    timestamp TEXT
);
```

## Configuration Properties

| Property | Default | Description |
|----------|---------|-------------|
| `server.port` | 8080 | Application port |
| `informatica.login.url` | Informatica login endpoint | Login API URL |
| `informatica.username` | - | Informatica username |
| `informatica.password` | - | Informatica password |
| `plugins.root` | plugin-LoginAPI | Root plugins to load |
| `plugins.child` | plugin-RetrieveSessionId,plugin-RefreshSessionId | Child plugins to load |

## Development

### Running Tests
```bash
mvn test
```

### Generating Documentation
The application automatically generates module documentation:
```bash
mvn test -Dtest=ModularityTests
```

### Development Mode
For development, set:
```properties
gg.jte.development-mode=true
logging.level.com.infahud=DEBUG
```

## Deployment

### Building for Production
```bash
mvn clean package -Pproduction
```

### Running the JAR
```bash
java -jar target/infahut-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### Common Issues

1. **SQLite Database Lock**
   - Ensure only one instance is running
   - Delete `infahut.db` and restart

2. **Plugin Loading Errors**
   - Check plugin dependencies in `application.properties`
   - Verify plugin classes are properly annotated with `@Component`

3. **Login Failures**
   - Verify Informatica credentials
   - Check network connectivity
   - Review application logs

### Logging

Enable debug logging for troubleshooting:
```properties
logging.level.com.infahud=DEBUG
logging.level.org.springframework.modulith=DEBUG
```

### spotless and git hook for pre-commit
```textContent
    npm install husky --save-dev && npx husky install
    npx husky install
    npx husky add .husky/pre-commit "mvn spotless:apply"

    
    ---
    Husky is now properly integrated: the pre-commit hook is set up to run mvn spotless:apply before each commit, using the correct Husky shell script format.
    You can now commit with confidence that your Java code will be auto-formatted on every commit. Let me know if you want to test this or need further automation!
    todo: see if husky can be removed and git commit hook can be jvm driven
    ---
```

### remove sensitive info in the pre-commit git 
```textContent
    If Husky is not being invoked on git push, it usually means the Git hooks are not installed or the pre-push hook is missing. Here’s how to fix it:

    Make sure Husky is installed:
    Run npm install husky --save-dev

    Enable Git hooks (if not already):
    Run npx husky install

    Add the install command to your package.json scripts (so hooks are set up after every install):

    Add a pre-push hook:
    Run npx husky add .husky/pre-push "npm test"
    (Replace "npm test" with your desired command.)

    Commit the .husky folder and hooks to your repo.

    Would you like me to check your workspace and set this up for you automatically?
    >> yes

    Husky is now set up correctly with a pre-push hook in your project. On your next git push, you should see "pre-push hook running" in the terminal, confirming the hook is invoked.

    You can now replace the echo command in pre-push with any checks or scripts you want to run before pushing. Let me know if you want to customize the hook further!
    test: git push origin test-01:test-01


```

#Postgress DB prep

## DB Create

```sql
CREATE DATABASE huddb
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;


CREATE SCHEMA hut
    AUTHORIZATION postgres;


```
SET PATH=C:\Users\vamsi\PostgreSQL\17\bin;%PATH%


img ![docs\imgs\postgress-db-schema-ss.jpg]

## Contributing

1. Fork the repository
2. Create a feature branch
3. Follow the plugin development guidelines
4. Write tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For issues and questions:
- Check the troubleshooting section
- Review the