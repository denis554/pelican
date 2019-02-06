package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Account
import io.pelican.Layout
import io.pelican.Product
import io.pelican.log.ProductViewLog

import pelican.common.DomainMockHelper

class ProductViewLogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain ProductViewLog
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def product = DomainMockHelper.getMockProduct(layout)
		product.save(flush:true)
		
		def productViewLog = DomainMockHelper.getMockProductViewLog(account, product)
		productViewLog.save(flush:true)
		
	    expect:	    
		Account.count() == 1
	    Layout.count() == 1
	    Product.count() == 1
	    ProductViewLog.count() == 1
	}
	

}