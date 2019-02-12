// Place your Spring DSL code here
import io.pelican.exception.BaseException
import io.pelican.handlers.PelicanAuthenticationSuccessHandler
import io.pelican.common.CommonUtilities

beans = {
	commonUtilities(CommonUtilities)
	exceptionHandler(io.pelican.exception.BaseException) {
	    exceptionMappings = ['java.lang.Exception': '/error']
	}
	authenticationSuccessHandler(PelicanAuthenticationSuccessHandler) {
		//https://groggyman.com/2015/04/05/custom-authentication-success-handler-with-grails-and-spring-security/
        requestCache = ref('requestCache')
        redirectStrategy = ref('redirectStrategy')
	}
}
