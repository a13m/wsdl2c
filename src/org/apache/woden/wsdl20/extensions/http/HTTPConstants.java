package org.apache.woden.wsdl20.extensions.http;

import java.net.URI;

import javax.xml.namespace.QName;

public class HTTPConstants {

    // Extension namespace
    public static final String NS_STRING_HTTP = "http://www.w3.org/ns/wsdl/http";
    public static final URI NS_URI_HTTP = URI.create(NS_STRING_HTTP);
    
    // Extension namespace prefix
    public static final String PFX_WHTTP = "whttp";

    // Extension element names.
    public static final String ELEM_HEADER = "header";
    
    // Extension attribute names
    public static final String ATTR_AUTHENTICATION_REALM = "authenticationRealm";
    public static final String ATTR_AUTHENTICATION_SCHEME = "authenticationScheme";
    public static final String ATTR_CODE = "code";
    public static final String ATTR_CONTENT_ENCODING = "contentEncoding";
    public static final String ATTR_CONTENT_ENCODING_DEFAULT = "contentEncodingDefault";
    public static final String ATTR_COOKIES = "cookies";
    public static final String ATTR_FAULT_SERIALIZATION = "faultSerialization";
    public static final String ATTR_IGNORE_UNCITED = "ignoreUncited";
    public static final String ATTR_INPUT_SERIALIZATION = "inputSerialization";
    public static final String ATTR_LOCATION = "location";
    public static final String ATTR_METHOD = "method";
    public static final String ATTR_METHOD_DEFAULT = "methodDefault";
    public static final String ATTR_OUTPUT_SERIALIZATION = "outputSerialization";
    public static final String ATTR_QUERY_PARAMETER_SEPARATOR = "queryParameterSeparator";
    public static final String ATTR_QUERY_PARAMETER_SEPARATOR_DEFAULT = "queryParameterSeparatorDefault";
    
    // Extension element QNames
    public static final QName Q_ELEM_HTTP_HEADER = new QName(NS_STRING_HTTP, ELEM_HEADER, PFX_WHTTP);
   
    // Extension attribute QNames
    public static final QName Q_ATTR_AUTHENTICATION_REALM = new QName(NS_STRING_HTTP, ATTR_AUTHENTICATION_REALM, PFX_WHTTP);
    public static final QName Q_ATTR_AUTHENTICATION_SCHEME = new QName(NS_STRING_HTTP, ATTR_AUTHENTICATION_SCHEME, PFX_WHTTP);
    public static final QName Q_ATTR_CODE = new QName(NS_STRING_HTTP, ATTR_CODE, PFX_WHTTP);
    public static final QName Q_ATTR_CONTENT_ENCODING = new QName(NS_STRING_HTTP, ATTR_CONTENT_ENCODING, PFX_WHTTP);
    public static final QName Q_ATTR_CONTENT_ENCODING_DEFAULT = new QName(NS_STRING_HTTP, ATTR_CONTENT_ENCODING_DEFAULT, PFX_WHTTP);
    public static final QName Q_ATTR_COOKIES = new QName(NS_STRING_HTTP, ATTR_COOKIES, PFX_WHTTP);
    public static final QName Q_ATTR_FAULT_SERIALIZATION = new QName(NS_STRING_HTTP, ATTR_FAULT_SERIALIZATION, PFX_WHTTP);
    public static final QName Q_ATTR_IGNORE_UNCITED = new QName(NS_STRING_HTTP, ATTR_IGNORE_UNCITED, PFX_WHTTP);
    public static final QName Q_ATTR_INPUT_SERIALIZATION = new QName(NS_STRING_HTTP, ATTR_INPUT_SERIALIZATION, PFX_WHTTP);
    public static final QName Q_ATTR_LOCATION = new QName(NS_STRING_HTTP, ATTR_LOCATION, PFX_WHTTP);
    public static final QName Q_ATTR_METHOD = new QName(NS_STRING_HTTP, ATTR_METHOD, PFX_WHTTP);
    public static final QName Q_ATTR_METHOD_DEFAULT = new QName(NS_STRING_HTTP, ATTR_METHOD_DEFAULT, PFX_WHTTP);
    public static final QName Q_ATTR_OUTPUT_SERIALIZATION = new QName(NS_STRING_HTTP, ATTR_OUTPUT_SERIALIZATION, PFX_WHTTP);
    public static final QName Q_ATTR_QUERY_PARAMETER_SEPARATOR = new QName(NS_STRING_HTTP, ATTR_QUERY_PARAMETER_SEPARATOR, PFX_WHTTP);
    public static final QName Q_ATTR_QUERY_PARAMETER_SEPARATOR_DEFAULT = new QName(NS_STRING_HTTP, ATTR_QUERY_PARAMETER_SEPARATOR_DEFAULT, PFX_WHTTP);
    
    // Extension property names
    public static final String PROP_HTTP_AUTHENTICATION_REALM = "http authentication realm";
    public static final String PROP_HTTP_AUTHENTICATION_SCHEME = "http authentication scheme";
    public static final String PROP_HTTP_CONTENT_ENCODING = "http content encoding";
    public static final String PROP_HTTP_CONTENT_ENCODING_DEFAULT = "http content encoding default";
    public static final String PROP_HTTP_COOKIES = "http cookies";
    public static final String PROP_HTTP_ERROR_STATUS_CODE = "http error status code";
    public static final String PROP_HTTP_FAULT_SERIALIZATION = "http fault serialization";
    public static final String PROP_HTTP_HEADERS = "http headers";
    public static final String PROP_HTTP_INPUT_SERIALIZATION = "http input serialization";
    public static final String PROP_HTTP_LOCATION = "http location";
    public static final String PROP_HTTP_LOCATION_IGNORE_UNCITED = "http location ignore uncited";
    public static final String PROP_HTTP_METHOD = "http method";
    public static final String PROP_HTTP_METHOD_DEFAULT = "http method default";
    public static final String PROP_HTTP_OUTPUT_SERIALIZATION = "http output serialization";
    public static final String PROP_HTTP_QUERY_PARAMETER_SEPARATOR = "http query parameter separator";
    public static final String PROP_HTTP_QUERY_PARAMETER_SEPARATOR_DEFAULT = "http query parameter separator default";
    
    //{http method} constants
    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";
    
    //{input/output serialization} constants
    public static final String SERIAL_APP_URLENCODED = "application/x-www-form-urlencoded";
    public static final String SERIAL_APP_XML = "application/xml";
    public static final String QUERY_SEP_AMPERSAND = "&";

}
