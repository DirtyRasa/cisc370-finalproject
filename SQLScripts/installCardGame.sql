SET NOCOUNT ON
GO
USE master
GO
if exists (select * from sysdatabases where name='CardGame')
		drop database CardGame
go
DECLARE @device_directory NVARCHAR(520)
SELECT @device_directory = SUBSTRING(filename, 1, CHARINDEX(N'master.mdf', LOWER(filename)) - 1)
FROM master.dbo.sysaltfiles WHERE dbid = 1 AND fileid = 1

EXECUTE (N'CREATE DATABASE CardGame
  ON PRIMARY (NAME = N''CardGame'', FILENAME = N''' + @device_directory + N'CardGame.mdf'')
  LOG ON (NAME = N''CardGame_log'',  FILENAME = N''' + @device_directory + N'CardGame.ldf'')')
go
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [CardGame].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [CardGame] SET ANSI_NULL_DEFAULT OFF
GO
ALTER DATABASE [CardGame] SET ANSI_NULLS OFF
GO
ALTER DATABASE [CardGame] SET ANSI_PADDING OFF
GO
ALTER DATABASE [CardGame] SET ANSI_WARNINGS OFF
GO
ALTER DATABASE [CardGame] SET ARITHABORT OFF
GO
ALTER DATABASE [CardGame] SET AUTO_CLOSE OFF
GO
ALTER DATABASE [CardGame] SET AUTO_CREATE_STATISTICS ON
GO
ALTER DATABASE [CardGame] SET AUTO_SHRINK OFF
GO
ALTER DATABASE [CardGame] SET AUTO_UPDATE_STATISTICS ON
GO
ALTER DATABASE [CardGame] SET CURSOR_CLOSE_ON_COMMIT OFF
GO
ALTER DATABASE [CardGame] SET CURSOR_DEFAULT  GLOBAL
GO
ALTER DATABASE [CardGame] SET CONCAT_NULL_YIELDS_NULL OFF
GO
ALTER DATABASE [CardGame] SET NUMERIC_ROUNDABORT OFF
GO
ALTER DATABASE [CardGame] SET QUOTED_IDENTIFIER OFF
GO
ALTER DATABASE [CardGame] SET RECURSIVE_TRIGGERS OFF
GO
ALTER DATABASE [CardGame] SET  DISABLE_BROKER
GO
ALTER DATABASE [CardGame] SET AUTO_UPDATE_STATISTICS_ASYNC OFF
GO
ALTER DATABASE [CardGame] SET DATE_CORRELATION_OPTIMIZATION OFF
GO
ALTER DATABASE [CardGame] SET TRUSTWORTHY OFF
GO
ALTER DATABASE [CardGame] SET ALLOW_SNAPSHOT_ISOLATION OFF
GO
ALTER DATABASE [CardGame] SET PARAMETERIZATION SIMPLE
GO
ALTER DATABASE [CardGame] SET READ_COMMITTED_SNAPSHOT OFF
GO
ALTER DATABASE [CardGame] SET HONOR_BROKER_PRIORITY OFF
GO
ALTER DATABASE [CardGame] SET  READ_WRITE
GO
ALTER DATABASE [CardGame] SET RECOVERY SIMPLE
GO
ALTER DATABASE [CardGame] SET  MULTI_USER
GO
ALTER DATABASE [CardGame] SET PAGE_VERIFY CHECKSUM
GO
ALTER DATABASE [CardGame] SET DB_CHAINING OFF
GO
USE [CardGame]
GO
/****** Object:  ForeignKey [FK_Stats_Users]    Script Date: 04/28/2011 21:53:01 ******/
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Stats_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Stats]'))
ALTER TABLE [dbo].[Stats] DROP CONSTRAINT [FK_Stats_Users]
GO
/****** Object:  ForeignKey [FK_Money_Users]    Script Date: 04/28/2011 21:53:01 ******/
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Money_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Money]'))
ALTER TABLE [dbo].[Money] DROP CONSTRAINT [FK_Money_Users]
GO
/****** Object:  Table [dbo].[Money]    Script Date: 04/28/2011 21:53:01 ******/
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Money_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Money]'))
ALTER TABLE [dbo].[Money] DROP CONSTRAINT [FK_Money_Users]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Money]') AND type in (N'U'))
DROP TABLE [dbo].[Money]
GO
/****** Object:  Table [dbo].[Stats]    Script Date: 04/28/2011 21:53:01 ******/
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Stats_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Stats]'))
ALTER TABLE [dbo].[Stats] DROP CONSTRAINT [FK_Stats_Users]
GO
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Stats]') AND type in (N'U'))
DROP TABLE [dbo].[Stats]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 04/28/2011 21:53:01 ******/
IF  EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))
DROP TABLE [dbo].[Users]
GO
USE [master]
GO
/****** Object:  Login [cisc370]    Script Date: 04/28/2011 21:53:01 ******/
IF  EXISTS (SELECT * FROM sys.server_principals WHERE name = N'cisc370')
DROP LOGIN [cisc370]
GO
/****** Object:  Login [cisc370]    Script Date: 04/28/2011 21:53:01 ******/
IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = N'cisc370')
CREATE LOGIN [cisc370] WITH PASSWORD=N'finalproject', DEFAULT_DATABASE=[CardGame], DEFAULT_LANGUAGE=[us_english], CHECK_EXPIRATION=OFF, CHECK_POLICY=off
GO

use [master]
GO
GRANT ADMINISTER BULK OPERATIONS TO [cisc370]
GO
use [master]
GO
GRANT ALTER ANY CONNECTION TO [cisc370]
GO
use [master]
GO
GRANT ALTER ANY CREDENTIAL TO [cisc370]
GO
use [master]
GO
GRANT ALTER ANY DATABASE TO [cisc370]
GO
use [master]
GO
GRANT ALTER ANY ENDPOINT TO [cisc370]
GO
use [master]
GO
GRANT ALTER ANY EVENT NOTIFICATION TO [cisc370]
GO
use [master]
GO
GRANT ALTER ANY LINKED SERVER TO [cisc370]
GO
use [master]
GO
GRANT ALTER ANY LOGIN TO [cisc370]
GO
use [master]
GO
GRANT ALTER ANY SERVER AUDIT TO [cisc370]
GO
use [master]
GO
GRANT ALTER RESOURCES TO [cisc370]
GO
use [master]
GO
GRANT ALTER SERVER STATE TO [cisc370]
GO
use [master]
GO
GRANT ALTER SETTINGS TO [cisc370]
GO
use [master]
GO
GRANT ALTER TRACE TO [cisc370]
GO
use [master]
GO
GRANT AUTHENTICATE SERVER TO [cisc370]
GO
use [master]
GO
GRANT CONNECT SQL TO [cisc370]
GO
use [master]
GO
GRANT CONTROL SERVER TO [cisc370]
GO
use [master]
GO
GRANT CREATE ANY DATABASE TO [cisc370]
GO
use [master]
GO
GRANT CREATE DDL EVENT NOTIFICATION TO [cisc370]
GO
use [master]
GO
GRANT CREATE ENDPOINT TO [cisc370]
GO
use [master]
GO
GRANT CREATE TRACE EVENT NOTIFICATION TO [cisc370]
GO
use [master]
GO
GRANT EXTERNAL ACCESS ASSEMBLY TO [cisc370]
GO
use [master]
GO
GRANT SHUTDOWN TO [cisc370]
GO
use [master]
GO
GRANT UNSAFE ASSEMBLY TO [cisc370]
GO
use [master]
GO
GRANT VIEW ANY DATABASE TO [cisc370]
GO
use [master]
GO
GRANT VIEW ANY DEFINITION TO [cisc370]
GO
use [master]
GO
GRANT VIEW SERVER STATE TO [cisc370]
GO
USE [master]
GO
EXEC xp_instance_regwrite N'HKEY_LOCAL_MACHINE', N'Software\Microsoft\MSSQLServer\MSSQLServer', N'LoginMode', REG_DWORD, 2
GO

USE [CardGame]
GO
/****** Object:  Table [dbo].[Users]    Script Date: 04/28/2011 21:53:01 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Users]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Users](
	[Name] [varchar](50) NOT NULL,
	[Password] [varchar](50) NOT NULL,
	[eMail] [varchar](50) NOT NULL,
 CONSTRAINT [PK_Users] PRIMARY KEY CLUSTERED 
(
	[Name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Stats]    Script Date: 04/28/2011 21:53:01 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Stats]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Stats](
	[UserName] [varchar](50) NOT NULL,
	[Wins] [int] NOT NULL,
	[Losses] [int] NOT NULL,
	[Pushes] [int] NOT NULL,
	[Total] [int] NOT NULL,
 CONSTRAINT [PK_Stats] PRIMARY KEY CLUSTERED 
(
	[UserName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Money]    Script Date: 04/28/2011 21:53:01 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[Money]') AND type in (N'U'))
BEGIN
CREATE TABLE [dbo].[Money](
	[UserName] [varchar](50) NOT NULL,
	[Money] [decimal](18, 2) NOT NULL,
 CONSTRAINT [PK_Money] PRIMARY KEY CLUSTERED 
(
	[UserName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_PADDING OFF
GO
/****** Object:  ForeignKey [FK_Stats_Users]    Script Date: 04/28/2011 21:53:01 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Stats_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Stats]'))
ALTER TABLE [dbo].[Stats]  WITH CHECK ADD  CONSTRAINT [FK_Stats_Users] FOREIGN KEY([UserName])
REFERENCES [dbo].[Users] ([Name])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Stats_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Stats]'))
ALTER TABLE [dbo].[Stats] CHECK CONSTRAINT [FK_Stats_Users]
GO
/****** Object:  ForeignKey [FK_Money_Users]    Script Date: 04/28/2011 21:53:01 ******/
IF NOT EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Money_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Money]'))
ALTER TABLE [dbo].[Money]  WITH CHECK ADD  CONSTRAINT [FK_Money_Users] FOREIGN KEY([UserName])
REFERENCES [dbo].[Users] ([Name])
GO
IF  EXISTS (SELECT * FROM sys.foreign_keys WHERE object_id = OBJECT_ID(N'[dbo].[FK_Money_Users]') AND parent_object_id = OBJECT_ID(N'[dbo].[Money]'))
ALTER TABLE [dbo].[Money] CHECK CONSTRAINT [FK_Money_Users]
GO
