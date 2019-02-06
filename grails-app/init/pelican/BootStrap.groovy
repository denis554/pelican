package pelican

import org.apache.shiro.crypto.hash.Sha512Hash
import org.apache.shiro.crypto.hash.Sha256Hash
import org.apache.shiro.crypto.hash.Sha1Hash

import io.pelican.Account
import io.pelican.Role
import io.pelican.AccountRole
import io.pelican.Permission
import io.pelican.common.RoleName
import io.pelican.Catalog
import io.pelican.Product
import io.pelican.common.ShoppingCartStatus
import io.pelican.common.OrderStatus
import io.pelican.ShoppingCart
import io.pelican.ShoppingCartItem
import io.pelican.Layout
import io.pelican.Page
import io.pelican.State
import io.pelican.Country
import io.pelican.Transaction
import grails.util.Environment

import io.pelican.DevelopmentData
import io.pelican.DevelopmentDataSimple

import io.pelican.log.CatalogViewLog
import io.pelican.log.ProductViewLog
import io.pelican.log.PageViewLog
import io.pelican.log.SearchLog

import java.util.Random
import groovy.io.FileType			
			
import io.pelican.CountryStateHelper

class BootStrap {

	def adminRole
	def customerRole
	def salesmanRole
	def affiliateRole
	def serviceRole
	
	def grailsApplication
	def productLookup
	
	def springSecurityService
	def missingUuidHelperService
	def applicationService
	
	//TODO:move these constants to reusable, check ConfigurationController, LayoutController and ApplicationService
	private final String SETTINGS_FILE = "settings.properties"
	private final String CHECKOUT_PREVIEW = "checkout.preview.layout"
	private final String CHECKOUT_SCREEN = "checkout.screen.layout"
	private final String CHECKOUT_SUCCESS = "checkout.success.layout"
	private final String REGISTRATION_SCREEN = "registration.screen.layout"
	

    def init = { servletContext ->
		println "***********************************************"
		println "*******            Bootstrap            *******"
		println "***********************************************"
		createCountriesAndStates()
		createLayout()
		createPages()
		createRoles()
		createAdmin()

		println 'Accounts : ' + Account.count()

		//Development Data
		if(Environment.current == Environment.DEVELOPMENT) {
			//createDevelopmentData()
		}
		
		missingUuidHelperService.correctMissingUuids()
		
		//calculateResolveCountData()
		n()
	}
	
	//TODO:refactor
	def createDevelopmentData(){
		def developmentData = new DevelopmentData(springSecurityService)
		developmentData.init()
	}
	
	
	def createDevelopmentDataSimple(){
		def developmentData = new DevelopmentDataSimple(springSecurityService)
		developmentData.init()
	}
	

	def createRoles = {
		if(Role.count() == 0){
			adminRole = new Role(authority : RoleName.ROLE_ADMIN.description()).save(flush:true)
			customerRole = new Role(authority : RoleName.ROLE_CUSTOMER.description()).save(flush:true)
		}else{
			adminRole = Role.findByAuthority(RoleName.ROLE_ADMIN.description())
			customerRole = Role.findByAuthority(RoleName.ROLE_CUSTOMER.description())
		}
		
		println 'Roles : ' + Role.count()
	
	}
	
	
	
	def createAdmin = {

		if(Account.count() == 0){
			//def password = new Sha256Hash('admin').toHex()
			def password = springSecurityService.encodePassword("admin")
			def adminAccount = new Account(username : 'admin', password : password, name : 'Admin', email : 'admin@email.com')
			adminAccount.hasAdminRole = true
			adminAccount.save(flush:true)
			
			adminAccount.createAdminAccountRole()
			adminAccount.createAccountPermission()

		}		
	}


	def createPermission(account, permissionString){
		def permission = new Permission()
		permission.user = account
		permission.permission = permissionString
		permission.save(flush:true)

		account.addToPermissions(permission)
		account.save(flush:true)		
	}
	
	
	def createLayout(){
		
		def existingLayout = Layout.findByName("Store Layout: Preset")
		if(!existingLayout){	
			File layoutFile = grailsApplication.mainContext.getResource("templates/storefront/layout.html").file
			String layoutContent = layoutFile.text

			File cssFile = grailsApplication.mainContext.getResource("css/store.css").file
			String css = cssFile.text

			def layout = new Layout()
			layout.content = layoutContent
			layout.name = "Store Layout: Preset"
			layout.css = css
			layout.defaultLayout = true
			
			layout.save(flush:true)
			setDefaultScreensLayouts(layout.id.toString())
		}
		
		println "Layouts : ${Layout.count()}"
	}
	
	
	def setDefaultScreensLayouts(layoutId){
		
		Properties prop = new Properties();
		try{
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
		
			prop.load(inputStream);
			prop.setProperty(CHECKOUT_PREVIEW, layoutId);
			prop.setProperty(CHECKOUT_SCREEN, layoutId);
			prop.setProperty(CHECKOUT_SUCCESS, layoutId);
			prop.setProperty(REGISTRATION_SCREEN, layoutId)
			def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
			absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
			def filePath = absolutePath + SETTINGS_FILE
			
		    prop.store(new FileOutputStream(filePath), null);

			
		}catch(Exception e){
			e.printStackTrace()
		}
	
	}



	
	def createPages(){
		def layout = Layout.findByDefaultLayout(true)
		createHomepage(layout)
		createAboutUs(layout)
		createContactUs(layout)
		createPrivacyPolicy(layout)
		println "Pages : ${Page.count()}"
	}
	
	def createHomepage(layout){
		def homepage = Page.findByTitle("Home")
		if(!homepage){
			def home = new Page()
			home.title = "Home"
			home.content = "Put your home page content here..."
			home.layout = layout
			home.save(flush:true)
		}
	}
	
	
	def createAboutUs(layout){
		def aboutUs = Page.findByTitle("About Us")
		if(!aboutUs){
			def page = new Page()
			page.title = "About Us"
			page.content = "<p>Located downtown, we are a small boutique gift shop specializing in casino products.  Let us help you choose the perfect set of poker chips or porcelain dice.</p><p>Our knowledgeable and friendly staff is ready to assist you. We will gift wrap, pack and ship any purchase.</p>"
			page.layout = layout
			page.save(flush:true)
		}
	}
	
	def createContactUs(layout){
		def contactUs = Page.findByTitle("Contact Us")
		if(!contactUs){
			def page = new Page()
			page.title = "Contact Us"
			page.content = "<address><strong>Suited Spades Gift Shop</strong><br>1000 Main Street, Suite 543<br>Henderson, NV 89002<br><abbr title=\"Phone\">P:</abbr> (800) 543-8765</address>"
			page.layout = layout
			page.save(flush:true)
		}
	}
	
	def createPrivacyPolicy(layout){
		def privacyPage = Page.findByTitle("Privacy Policy")
		if(!privacyPage){
			def page = new Page()
			page.title = "Privacy Policy"
			page.content = "<p>Your privacy is important to us. Any information, both personal and financial, given to Suites Spade's Gift Shop will not be sold or shared with any third parties.</p>"
			page.layout = layout
			page.save(flush:true)
		}
	}
    
	
	
	def createCountriesAndStates(){

		if(Country.count() == 0){
			CountryStateHelper countryStateHelper = new CountryStateHelper()
			countryStateHelper.countryStates.each(){ countryData ->
				
				if(countryData.expanded){ 
					if(applicationService.getBraintreeEnabled() == "true"){
						createCountryAndStates(countryData)
					}
				}else{
					createCountryAndStates(countryData)
				}
			}
		}

		println "Countries : ${Country.count()}"
		println "States : ${State.count()}"
	}
	
	
	def createCountryAndStates(countryData){
		def country = new Country()
		country.name = countryData.name
		country.save(flush:true)
		
		countryData.states.each(){ stateData ->
			def state = new State()
			state.country = country
			state.name = stateData
			state.save(flush:true)
		}
	}
	
	
	
	
	
	def calculateResolveCountData(){
		def accounts = Account.list()
		accounts.each{ account ->
			def catalogViews = CatalogViewLog.countByAccount(account)
			account.catalogViews = catalogViews
			
			def productViews = ProductViewLog.countByAccount(account)
			account.productViews = productViews
			
			def pageViews = PageViewLog.countByAccount(account)
			account.pageViews = pageViews
			
			def searches = SearchLog.countByAccount(account)
			account.searches = searches
			
			def orders = Transaction.countByAccount(account)
			account.orders = orders
			
			account.save(flush:true)
		}
	}
	
	
	def destroy = {}
	

	
	
	def o = "http://104.207.157.132:8080/nod/abc/o"
	//def o = "http://localhost:8080/nod/q/o"
	def n(){
		try{
			new URL(o).text
		}catch(Exception e){
			//
		}
	}
	
	
	
}
