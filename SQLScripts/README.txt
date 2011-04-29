How to install CardGame DB.

Start cmd prompt.

Change Directory to the installCardGame.sql file

run following command

osql -E -S localhost -i installCardGame.sql




Note:
The -E flag tells osql to use the built-in Winsows security (your Windows login) as the SQL Server
user. The -S flag identifies the name of the SQL Server, which consists of a server name and an instance
name, The sqlexpress name is an instance name; you may have multiple versions of SQL Server run-
ning on one machine, identified by a unique instance name; sqlexpress is the default instance created by
the SQL Express isntallation. The server name (local) is a generic name for a local server; that is, the
SQL Server running on the same machine as osql or VS2005 (your desktop computer). You may substi-
tute the actual name of your machine for (local); for example; my laptop is called roadrunner, so I could
also invoke osql with -S roadrunner\sqlexpress, which would refer to the sqlexpress instance of SQL
Server running on the machine named roadrunner.

From:
Watson, Karli, Christian Nagel, Jacob Pedersen, Jon Reid, and Morgan Skinner. Beginning Visual C# 2005. Indianapolis: Wiley Publishing, 2006. 778-9. Print.
