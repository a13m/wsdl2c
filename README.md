wsdl2c
======

A stripped down axis2 source bundle suitable for running WSDL2CThis is an amalgam of code from several java projects which are required to 
use WSDL2C from the Apache Axis 2 project.  The goal is to have code which
can be built from source without the chain of dependencies needed to build
each of these projects in their entirety.

The code in this project coms from the following sources:

* http://svn.apache.org/repos/asf/axis/axis2/java/core/tags/v1.4.1
* http://svn.apache.org/repos/asf/webservices/commons/tags/axiom/1.2.12 
* http://svn.apache.org/repos/asf/webservices/commons/tags/neethi/neethi-3.0.1
* http://svn.apache.org/repos/asf/webservices/commons/tags/XmlSchema/1.4.2
* http://svn.apache.org/repos/asf/webservices/woden/tags/1.0M9
* https://svn.java.net/svn/jsr311~svn/tags/jsr311-api-1.1.1
* svn://svn.annogen.codehaus.org/annogen/scm [1]

At this point, none of the sources have been modified.  Note that the jsr311 
code (i.e., the files under javax/ws/rs) is provided under the CDDL, while
all other code uses the Apache Software License, version 2.0.

Notes:
[1] I could not access this link, and pulled the code out of a jpackage RPM,
but theoretically the code still lives in this repo.
