package pelican

import grails.plugin.springsecurity.annotation.Secured
import io.pelican.Account

class StoreController {

 	@Secured(['permitAll'])
    def index() {
    	def accounts = Account.list()
    	[accounts : accounts]
    }

}
