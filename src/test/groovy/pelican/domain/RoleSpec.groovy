package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Role

import pelican.common.DomainMockHelper

class RoleSpec extends Specification implements DataTest {

	void setupSpec(){
        mockDomain Role
	}

	void "test basic persistence mocking"() {
	    setup:
		def role = DomainMockHelper.getMockRole()
		role.save(flush:true)

	    expect:
	    Role.count() == 1
	}
	
}