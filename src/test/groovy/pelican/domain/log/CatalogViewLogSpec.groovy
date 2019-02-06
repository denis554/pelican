package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Account
import io.pelican.Layout
import io.pelican.Catalog
import io.pelican.log.CatalogViewLog

import pelican.common.DomainMockHelper

class CatalogViewLogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain CatalogViewLog
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def catalog = DomainMockHelper.getMockCatalog(layout)
		catalog.save(flush:true)
		
		def catalogViewLog = DomainMockHelper.getMockCatalogViewLog(account, catalog)
		catalogViewLog.save(flush:true)
		
	    expect:	    
		Account.count() == 1
	    Layout.count() == 1
	    Catalog.count() == 1
	    CatalogViewLog.count() == 1
	}
	

}