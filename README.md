# oppna-program-secure-http

The projects features a way to redirect the user, coming from a journal system, to another web-based healthcare system by automatically submitting a form to the target system with a signed ticket. Since the url to this application is securely protected (in current setup client certificate from a smart card is required) a user is securely authenticated also in the target system by means of a signed ticket which can only be produced by this application.

## Portlet module
The portlet is deployed in Liferay Portal. It is configured with a friendly URL {protocol}://{domain}:{port}/{pathToPage}/-/sesam/{requestType} where {requestType} is either "order" or "view".

## Web module
The web module is a Spring MVC web application. After deployment to a servlet container it is targeted at {protocol}://{domain}:{port}/secure-http-web/. A reverse proxy may be setup to map a similar url originally used to target the portlet module to instead target the web module. That's useful if one wants to avoid changing configuration of the journal system.

The background to the creation of the web module was that the journal system uses an internal Internet Explorer which is sensitive to Javascript errors. The problems are resolved by using a web module which doesn't need a portal page surrounding the portlet. Instead a minimal web page is used.

## Incoming request
Only post requests are allowed. The url must end with "order" or "view". Depending on which of the two different target system URL:s are targeted by the automatic form submit. The request body should contain parameter key-values. These are the parameters:

* pid (patient id)
* usr (the user id)
* logicaladdress (system specific)
* contactid (system specific)
* forskrivningsid (system specific)

A header with the user id must also be included. If the header value doesn't match the "usr" parameter an exception is thrown (the application must only accept connections from the authentication proxy - otherwise it would be easy to add the header without proper authentication). 

## Outgoing request
If the incoming request validates a form is populated in the html view and automatically submitted to the target system. The incoming parameters and an additional timestamp parameter are included in a signed XML document (enveloped signature). The signed xml is transformed to base64 and included in the form as a parameter "data".


## Sequence diagram of the flow from the journal system to the target system

![Sequence diagram](https://raw.githubusercontent.com/wiki/Vastra-Gotalandsregionen/oppna-program-secure-http/Melior%20uthopp%20till%20Sesam%20via%20Regionportalen%20-%20XML%20POST.png)
# oppna-program-secure-http

The projects features a way to redirect the user, coming from a journal system, to another web-based healthcare system by automatically submitting a form to the target system with a signed ticket. Since the url to this application is securely protected (in current setup client certificate from a smart card is required) a user is securely authenticated also in the target system by means of a signed ticket which can only be produced by this application.

## Portlet module
The portlet is deployed in Liferay Portal. It is configured with a friendly URL {protocol}://{domain}:{port}/{pathToPage}/-/sesam/{requestType} where {requestType} is either "order" or "view".

## Web module
The web module is a Spring MVC web application. After deployment to a servlet container it is targeted at {protocol}://{domain}:{port}/secure-http-web/. A reverse proxy may be setup to map a similar url originally used to target the portlet module to instead target the web module. That's useful if one wants to avoid changing configuration of the journal system.

The background to the creation of the web module was that the journal system uses an internal Internet Explorer which is sensitive to Javascript errors. The problems are resolved by using a web module which doesn't need a portal page surrounding the portlet. Instead a minimal web page is used.

## Incoming request
Only post requests are allowed. The url must end with "order" or "view". Depending on which of the two different target system URL:s are targeted by the automatic form submit. The request body should contain parameter key-values. These are the parameters:

* pid (patient id)
* usr (the user id)
* logicaladdress (system specific)
* contactid (system specific)
* forskrivningsid (system specific)

A header with the user id must also be included. If the header value doesn't match the "usr" parameter an exception is thrown (the application must only accept connections from the authentication proxy - otherwise it would be easy to add the header without proper authentication). 

## Outgoing request
If the incoming request validates a form is populated in the html view and automatically submitted to the target system. The incoming parameters and an additional timestamp parameter are included in a signed XML document (enveloped signature). The signed xml is transformed to base64 and included in the form as a parameter "data".


## Sequence diagram of the flow from the journal system to the target system

![Sequence diagram](https://raw.githubusercontent.com/wiki/Vastra-Gotalandsregionen/oppna-program-secure-http/Melior%20uthopp%20till%20Sesam%20via%20Regionportalen%20-%20XML%20POST.png)

  <p>
    <tt>
      oppna-program-secure-http
    </tt>
     ?r en del i V?stra G?talandsregionens satsning p? ?ppen k?llkod inom ramen f?r 
    <a href="https://github.com/Vastra-Gotalandsregionen//oppna-program">
      ?ppna Program
    </a>
    . 
  </p>