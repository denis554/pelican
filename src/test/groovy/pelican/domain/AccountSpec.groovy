package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Account

import pelican.common.DomainMockHelper

class AccountSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain Account
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)

	    expect:
	    Account.count() == 1
	}
	
}