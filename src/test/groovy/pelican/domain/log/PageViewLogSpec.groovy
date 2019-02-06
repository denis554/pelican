package pelican.domain

import spock.lang.Specification
import grails.testing.gorm.DomainUnitTest
import grails.testing.gorm.DataTest

import io.pelican.Account
import io.pelican.Layout
import io.pelican.Page
import io.pelican.log.PageViewLog

import pelican.common.DomainMockHelper

class PageViewLogSpec extends Specification implements DataTest {
	
	void setupSpec(){
        mockDomain PageViewLog
	}

	void "test basic persistence mocking"() {
	    setup:
	    def account = DomainMockHelper.getMockAccount()
		account.save(flush:true)
		
		def layout = DomainMockHelper.getMockLayout()
		layout.save(flush:true)
		
		def page = DomainMockHelper.getMockPage(layout)
		page.save(flush:true)
		
		def pageViewLog = DomainMockHelper.getMockPageViewLog(account, page)
		pageViewLog.save(flush:true)
		
	    expect:	    
		Account.count() == 1
	    Layout.count() == 1
	    Page.count() == 1
	    PageViewLog.count() == 1
	}
	

}