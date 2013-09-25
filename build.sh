#!/bin/sh

export CLASSPATH=$( build-classpath httpcomponents/httpcore \
                    commons-logging \
                    wsdl4j javamail geronimo-jta jaxen servlet \
                    commons-fileupload commons-httpclient commons-cli \
                    geronimo-jms httpcomponents/httpcore-nio )
ant 
