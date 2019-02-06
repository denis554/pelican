package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Catalog
import io.pelican.Layout
import io.pelican.Product
import io.pelican.Specification
import io.pelican.SpecificationOption

import pelican.common.DomainMockHelper

class SpecificationOptionSpec extends spock.lang.Specification implements DataTest {

	void setupSpec(){
        mockDomain SpecificationOption
	}

	void "test basic persistence mocking"() {
	    setup:
		def specification = DomainMockHelper.getMockSpecification()
		specification.save(flush:true)
		
		def specificationOption = DomainMockHelper.getMockSpecificationOption(specification)
		specificationOption.save(flush:true)

	    expect:
	    Specification.count() == 1	
	    SpecificationOption.count() == 1	
	}
	
}