# VSON: Json Parser

Vson is Wrapper on Google's Gson Parser

It has: 

+ Gson object with pre-defined settings

+ Strict Json parsing: normally Gson parses just set not-existant field to null without error, but strict parsing prevents such behaviour by falling into parsing exception when field is absent or empty
 
+ Annotations:
 
     + @Optional - marks field as not nessesary required (There won't be error if field is absent) 
     + @EmptyValueAllowed - allows field to be empty:
   
            For java.lang.String: 0-char string
            For collections (java.util.Collection): collection without elements
            For arrays: array without elements
            For rest objects: null

+ Parser class that provides Gson via static method
 
## Is it stable?
 
 Not yet, but I use it in several projects.   
 Many classes still undocumented yet.
 Project currently in alpha stage. 
 
## BugTracker and HowTo contact developers

 BugTracker is located here: http://track.virtadev.net/issues/VSON
 
 List of developers (with contacts and timezone) can be found at pom.xml
 
 
## Release history
 + Version 0.3.3 (Released: 18/07/14)
 
 Moving to BinTray. Added maven-release-plugin into pom.xml
 Code remains unchanged (same as 0.3.2).   
 
 + Version 0.3.2 (Released: 17/01/14)
 
 First public version. Originally posted at Maven Central. 