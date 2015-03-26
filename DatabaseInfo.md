# Edit #
  * No longer need to install the driver, add JAR's or add sqlauth.dll to Native Library.
  * Only need to install the database and enable TCP/IP

# Install Database #
  1. Download and install: [Sql Server](http://www.microsoft.com/express/Database/)
  1. Start cmd prompt.
  1. Change Directory to the installCardGame.sql file
  1. run following command
  1. osql -E -S localhost -i installCardGame.sql (localhost\SQLEXPRESS??)
  1. [Enable TCP/IP](http://msdn.microsoft.com/en-us/library/bb909712%28v=vs.90%29.aspx) for the Database




**How to Install Driver**
  * Download and run [JDBC Driver](http://msdn.microsoft.com/en-us/data/aa937724.aspx)
  * Extract to C:\Program Files\Microsoft SQL Server JDBC Driver 3.0

**Add JAR's**
  * Right click JRE System Library -> Build Path -> Configure Build Path...
  * Add External JARs...
  * Browse to and add sqljdbc4.jar

**Add sqlauth.dll to Native Library**
  * Right click project -> Properties
  * Java Build Path -> Libraries
  * Expand JRE System Library
  * Click Native Library location -> Edit...
  * Navigate to and add sqlauth.dll
    * For 32 bit of Java: C:/Program Files/Microsoft SQL Server JDBC Driver 3.0/sqljdbc\_3.0/enu/auth/x86/
    * For 64 bit of Java: C:/Program Files (x86)/Microsoft SQL Server JDBC   Driver 3.0/sqljdbc\_3.0/enu/auth/x64/