package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Account
import io.pelican.Layout
import io.pelican.log.SearchLog

import pelican.common.DomainMockHelper

class SearchLogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain SearchLog
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def searchlog = DomainMockHelper.getMockSearchLog(account)
		searchlog.save(flush:true)
		
	    expect:	    
		Account.count() == 1
	    SearchLog.count() == 1
	}

}