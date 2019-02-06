package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Account
import io.pelican.Catalog
import io.pelican.Layout
import io.pelican.Product
import io.pelican.ProductOption
import io.pelican.ShoppingCart
import io.pelican.ShoppingCartItem
import io.pelican.ShoppingCartItemOption
import io.pelican.Variant

import pelican.common.DomainMockHelper

class ShoppingCartItemOptionSpec extends Specification implements DataTest {
	
	void setupSpec(){
		mockDomain Account
        mockDomain ShoppingCartItemOption
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
		def shoppingCartItem = DomainMockHelper.getMockShoppingCartItem(product, shoppingCart)
		shoppingCartItem.save(flush:true)
		
		shoppingCart.addToShoppingCartItems(shoppingCartItem)
		shoppingCart.save(flush:true)
		
		def productOption = DomainMockHelper.getMockProductOption(product)
		productOption.save(flush:true)
		
		def variant = DomainMockHelper.getMockVariant(productOption)
		variant.save(flush:true)
		
		def shoppingCartItemOption = DomainMockHelper.getMockShoppingCartItemOption(variant, shoppingCartItem)
		shoppingCartItemOption.save(flush:true)
		
	    expect:
	    Account.count() == 1
	    Layout.count() == 1
	    Catalog.count() == 1
	    Product.count() == 1
	    ShoppingCart.count() == 1
	    ShoppingCartItem.count() == 1
		ProductOption.count() == 1
		Variant.count() == 1
		ShoppingCartItemOption.count() == 1
	}
	

}