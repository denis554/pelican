package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Account
import io.pelican.Catalog
import io.pelican.Layout
import io.pelican.Product
import io.pelican.ShoppingCart
import io.pelican.ShoppingCartItem

import pelican.common.DomainMockHelper

class ShoppingCartSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain ShoppingCart
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def catalog = DomainMockHelper.getMockCatalog(layout)
		catalog.save(flush:true)
	    
		def product = DomainMockHelper.getMockProduct(catalog, layout)
		product.save(flush:true)
		
		def shoppingCart = DomainMockHelper.getMockShoppingCart(account)
		shoppingCart.save(flush:true)
		
	    expect:
	    Account.count() == 1
	    Layout.count() == 1
	    Catalog.count() == 1
		Product.count() == 1
	    ShoppingCart.count() == 1
	}
	

}