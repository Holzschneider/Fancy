# Fancy
Fairly atypical nontrivial collections library

A collection of custom Collection implementations that have been missing in java.util.Collections.


Release
-------

The base release 1.0.x corresponds to the unmodified collections implementation, as they've been extracted from legacy projects.


Releases are deployed automatically to the deploy branch of this github repostory. 
To add a dependency on *Fancy* using maven, modify your *repositories* section to include the git based repository.

	<repositories>
	 ...
	  <repository>
	    <id>Fancy-Repository</id>
	    <name>Fancy's Git-based repo</name>
	    <url>https://raw.githubusercontent.com/Holzschneider/Fancy/deploy/</url>
	  </repository>
	...
	</repositories>
	
and modify your *dependencies* section to include the *Fancy* dependency
 
	  <dependencies>
	  ...
	  	<dependency>
	  		<groupId>de.dualuse</groupId>
	  		<artifactId>Fancy</artifactId>
	  		<version>LATEST</version>
	  	</dependency>
	  ...
	  </dependencies>


To add the repository and the dependency using gradle refer to this

	repositories {
	    maven {
	        url "https://raw.githubusercontent.com/Holzschneider/Fancy/deploy/"
	    }
	}

and this

	dependencies {
	  compile 'de.dualuse:Fancy:1.0.+'
	}
