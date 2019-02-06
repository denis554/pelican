package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Account
import io.pelican.Catalog
import io.pelican.Country
import io.pelican.Layout
import io.pelican.Product
import io.pelican.ShoppingCart
import io.pelican.ShoppingCartItem
import io.pelican.State
import io.pelican.Transaction

import pelican.common.DomainMockHelper

class TransactionSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain Transaction
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def country = DomainMockHelper.getMockCountry()
		country.save(flush:true)
		
		def state = DomainMockHelper.getMockState(country)
		state.save(flush:true)
		
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def catalog = DomainMockHelper.getMockCatalog(layout)
		catalog.save(flush:true)
	    
		def product = DomainMockHelper.getMockProduct(catalog, layout)
		product.save(flush:true)
		
		def shoppingCart = DomainMockHelper.getMockShoppingCart(account)
		shoppingCart.save(flush:true)
		
		def transaction = DomainMockHelper.getMockTransaction(account, shoppingCart, country, state)
		transaction.save(flush:true)
		
	    expect:
	    Account.count() == 1
	    Layout.count() == 1
	    Country.count() == 1
	    State.count() == 1
	    Catalog.count() == 1
		Product.count() == 1
	    ShoppingCart.count() == 1
	    Transaction.count() == 1
	}
	

}