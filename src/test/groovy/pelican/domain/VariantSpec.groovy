package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Layout
import io.pelican.Catalog
import io.pelican.Product
import io.pelican.ProductOption
import io.pelican.Variant

import pelican.common.DomainMockHelper

class VariantSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain Variant
	}

	void "test basic persistence mocking"() {
	    setup:
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def catalog = DomainMockHelper.getMockCatalog(layout)
		catalog.save(flush:true)
	    
		def product = DomainMockHelper.getMockProduct(catalog, layout)
		product.save(flush:true)
	    
		def productOption = DomainMockHelper.getMockProductOption(product)
		productOption.save(flush:true)
		
	    def variant = DomainMockHelper.getMockVariant(productOption)
		variant.save(flush:true)

	    expect:
		Layout.count() == 1
	    Catalog.count() == 1
	    Product.count() == 1
	    ProductOption.count() == 1
	    Variant.count() == 1
	}
	
}