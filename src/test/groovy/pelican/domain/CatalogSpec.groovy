package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Layout
import io.pelican.Catalog

import pelican.common.DomainMockHelper

class CatalogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain Layout
        mockDomain Catalog
	}

	void "test basic persistence mocking"() {
	    setup:
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def catalog = DomainMockHelper.getMockCatalog(layout)
		catalog.save(flush:true)
		
	    expect:
	    Layout.count() == 1
	    Catalog.count() == 1
	}
	

}