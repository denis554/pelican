package pelican

import pelican.common.BaseController
import io.pelican.AppConstants

//import org.apache.commons.io.IOUtils
import grails.io.IOUtils

import java.io.FileOutputStream;
import java.io.FileInputStream
import grails.converters.*

import com.easypost.EasyPost
import com.easypost.model.Address
import com.easypost.model.Parcel
import com.easypost.model.Shipment
import com.easypost.exception.EasyPostException
import grails.util.Environment

import io.pelican.Country
import io.pelican.State
import io.pelican.Upload
import io.pelican.Product
import io.pelican.Page

import grails.plugin.springsecurity.annotation.Secured
import io.pelican.api.mail.EasyPostShipmentApi
import io.pelican.api.mail.ShipmentAddress

import io.pelican.Layout


@Mixin(BaseController)
class ConfigurationController {

	def emailService
	def applicationService
	def currencyService
	def commonUtilities
	
	private final String SETTINGS_FILE = "settings.properties"
	
	private final String STORE_PRIVATE_KEY = "store.key"
	
	private final String STORE_CURRENCY = "store.currency"
	private final String STORE_COUNTRY_CODE = "store.country.code"
	private final String STORE_NAME = "store.name"
	private final String STORE_ADDRESS1 = "store.address1"
	private final String STORE_ADDRESS2 = "store.address2"
	private final String STORE_CITY = "store.city"
	private final String STORE_COUNTRY = "store.country"
	private final String STORE_STATE = "store.state"
	private final String STORE_ZIP = "store.zip"
	private final String STORE_PHONE = "store.phone"
	private final String STORE_EMAIL = "store.email"
	private final String STORE_SHIPPING = "store.shipping"
	
	/**TODO:
	private final String CUSTOMS_CONTENTS_TYPE = "customs.contents.type"
	private final String CUSTOMS_CONTENTS_EXPLANATION = "customs.contents.explanation"
	private final String CUSTOMS_RESTRICTION_TYPE = "customs.restriction.type"
	private final String CUSTOMS_RESTRICTION_COMMENTS = "customs.restriction.comments"
	private final String CUSTOMS_CERTIFY = "customs.certify"
	private final String CUSTOMS_SIGNER = "customs.signer"
	private final String CUSTOMS_NON_DELIVERY_OPTION = "customs.non.delivery.option"
	private final String CUSTOMS_EEL_PFC = "customs.eel.pfc"
	**/
	
	
	private final String STORE_TAX_RATE = "store.tax.rate"
	
	private final String META_KEYWORDS = "meta.keywords"
	private final String META_DESCRIPTION = "meta.description"
	private final String GOOGLE_ANALYTICS = "google.analytics"
	private final String SOCIAL_MEDIA_ENABLED = "social.media.enabled"
	
	private final String MAIL_ADMIN_EMAIL_ADDRESS = "mail.smtp.adminEmail"
	private final String MAIL_SUPPORT_EMAIL_ADDRESS = "mail.smtp.supportEmail"
	private final String MAIL_USERNAME = "mail.smtp.username"
	private final String MAIL_PASSWORD = "mail.smtp.password"
	private final String MAIL_HOST = "mail.smtp.host"
	private final String MAIL_PORT = "mail.smtp.port"
	private final String MAIL_STARTTLS = "mail.smtp.starttls.enabled"
	private final String MAIL_AUTH = "mail.smtp.auth"
	
	private final String STRIPE_ENABLED_KEY = "stripe.enabled"
	private final String STRIPE_DEVELOPMENT_API_KEY = "stripe.development.apiKey"
	private final String STRIPE_DEVELOPMENT_PUBLISHABLE_KEY = "stripe.development.publishableKey"
	private final String STRIPE_PRODUCTION_API_KEY = "stripe.production.apiKey"
	private final String STRIPE_PRODUCTION_PUBLISHABLE_KEY = "stripe.production.publishableKey"
	
	private final String BRAINTREE_ENABLED = "braintree.enabled"
	private final String BRAINTREE_MERCHANT_ID = "braintree.merchantId"
	private final String BRAINTREE_PUBLIC_KEY = "braintree.publicKey"
	private final String BRAINTREE_PRIVATE_KEY = "braintree.privateKey"
	
	private final String EASYPOST_ENABLED = "easypost.enabled"
	private final String EASYPOST_TEST_API_KEY = "easypost.test.apiKey"
	private final String EASYPOST_LIVE_API_KEY = "easypost.live.apiKey"
	
	private final String EASYPOST_ADDRESS_EXCEPTION_STRING = "An error occured. Response code: 400 Response body"
	
	def links() {
		authenticatedAdmin{ adminAccount ->
		}
	}
	
 	@Secured(['ROLE_ADMIN'])
    def index() {
		authenticatedAdmin{ adminAccount ->
		}
	}

	

 	@Secured(['ROLE_ADMIN'])
	def settings(){
		authenticatedAdmin{ adminAccount ->
			
			Properties prop = new Properties();
			try{
			
				File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
				FileInputStream inputStream = new FileInputStream(propertiesFile)
				prop.load(inputStream);
				
				def settings = [:]
				settings["storeName"] = prop.getProperty(STORE_NAME);
				settings["storePhone"] = prop.getProperty(STORE_PHONE);
				settings["storeEmail"] = prop.getProperty(STORE_EMAIL);
				settings["keywords"] = prop.getProperty(META_KEYWORDS);
				settings["description"] = prop.getProperty(META_DESCRIPTION);
				settings["taxRate"] = prop.getProperty(STORE_TAX_RATE);
				settings["googleAnalytics"] = prop.getProperty(GOOGLE_ANALYTICS);

				String socialMediaEnabled = prop.getProperty(SOCIAL_MEDIA_ENABLED);
				if(socialMediaEnabled == "true")settings["socialMediaEnabled"] = "checked"
				
				[ settings : settings ]
				
			} catch (IOException e){
			    log.debug"Exception occured while reading properties file :"+e
			} //TODO:add excpetion handler for IOException
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def save_settings(){

		authenticatedAdmin{ adminAccount ->
			
			String storeName = params.storeName
			String storePhone = params.storePhone
			String storeEmail = params.storeEmail
			String keywords = params.keywords
			String description = params.description
			String taxRate = params.taxRate
			String googleAnalytics = params.googleAnalytics
			String socialMediaEnabled = params.socialMediaEnabled
			
			
			if(socialMediaEnabled == "on")socialMediaEnabled = true
			if(!socialMediaEnabled)socialMediaEnabled = false

			Properties prop = new Properties();
		
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			prop.load(inputStream);
			
			try{
			    
				prop.setProperty(STORE_NAME, storeName);
				prop.setProperty(STORE_PHONE, storePhone);
				prop.setProperty(STORE_EMAIL, storeEmail);
				prop.setProperty(META_KEYWORDS, keywords);
				prop.setProperty(META_DESCRIPTION, description);
				prop.setProperty(STORE_TAX_RATE, taxRate);
				prop.setProperty(GOOGLE_ANALYTICS, googleAnalytics);
				prop.setProperty(SOCIAL_MEDIA_ENABLED, socialMediaEnabled);
				
				def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
				absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
				def filePath = absolutePath + SETTINGS_FILE
				
			    prop.store(new FileOutputStream(filePath), null);
				applicationService.setProperties()
				
				flash.message = "Successfully saved store settings"
				redirect(action : 'settings')
				
			} catch (IOException e){
			    log.debug"exception occured while saving properties file :"+e
				flash.message = "Something went wrong... "
				redirect(action : 'settings')
				return
			}
		}
	}
	
	





 	@Secured(['ROLE_ADMIN'])
	def email_settings(){
		authenticatedAdmin{ adminAccount ->
			
			Properties prop = new Properties();
			try{
			
				File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
				FileInputStream inputStream = new FileInputStream(propertiesFile)
				
				prop.load(inputStream);
				
				def email_settings = [:]
				email_settings["adminEmail"] = prop.getProperty(MAIL_ADMIN_EMAIL_ADDRESS)
				email_settings["supportEmail"] = prop.getProperty(MAIL_SUPPORT_EMAIL_ADDRESS)
				email_settings["username"] = prop.getProperty(MAIL_USERNAME)
				email_settings["password"] = prop.getProperty(MAIL_PASSWORD)
				email_settings["host"] = prop.getProperty(MAIL_HOST)
				email_settings["port"] = prop.getProperty(MAIL_PORT)
				
				def startTls = prop.getProperty(MAIL_STARTTLS)
				def auth = prop.getProperty(MAIL_AUTH)
				
				if(startTls == "true")email_settings["startTls"] = prop.getProperty(MAIL_STARTTLS)
				if(auth == "true")email_settings["auth"] = prop.getProperty(MAIL_AUTH)
				
				if(email_settings["startTls"])email_settings["startTls"] = "checked"
				if(email_settings["auth"])email_settings["auth"] = "checked"
				
				[ email_settings : email_settings ]
				
			} catch (IOException e){
			    log.debug"Exception occured while reading properties file :"+e
			}
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def save_email_settings(){

		authenticatedAdmin{ adminAccount ->
		
			def homepage = Page.findByTitle("Home")
			homepage.content = params.homepage
			homepage.save(flush:true)

			String adminEmail = params.adminEmail
			String supportEmail = params.supportEmail
			String username = params.username
			String password = params.password
			String host = params.host
			String port = params.port
			String startTls = params.startTls
			String auth = params.auth
			
			if(startTls == "on")startTls = true
			if(auth == "on")auth = true
			
			if(!startTls)startTls = false
			if(!auth)auth = false
			
			
			Properties prop = new Properties();
		
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			
			prop.load(inputStream);
			
			try{
				
				prop.setProperty(MAIL_ADMIN_EMAIL_ADDRESS, adminEmail)
				prop.setProperty(MAIL_SUPPORT_EMAIL_ADDRESS, supportEmail)
				prop.setProperty(MAIL_USERNAME, username);
				prop.setProperty(MAIL_PASSWORD, password);
				prop.setProperty(MAIL_HOST, host);
				prop.setProperty(MAIL_PORT, port);
				prop.setProperty(MAIL_STARTTLS, startTls)
				prop.setProperty(MAIL_AUTH, auth)
				
				def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
				absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
				def filePath = absolutePath + SETTINGS_FILE
				
			    prop.store(new FileOutputStream(filePath), null);
				
				applicationService.setProperties()
				
				flash.message = "Successfully saved email settings"
				redirect(action : 'email_settings')
				
			} catch (IOException e){
			    log.debug"exception occured while saving properties file :"+e
				flash.message = "Something went wrong... "
				redirect(action : 'email_settings')
				return
			}
			
			
		}
	}
	
	
	
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def payment_settings(){
		authenticatedAdmin { adminAccount ->
			Properties prop = new Properties();
			try{
		
				File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
				FileInputStream inputStream = new FileInputStream(propertiesFile)
				prop.load(inputStream);
				
				def payment_settings = [:]
				
				payment_settings["storeCurrency"] = prop.getProperty(STORE_CURRENCY);
				payment_settings["storeCountryCode"] = prop.getProperty(STORE_COUNTRY_CODE);
				payment_settings["stripeDevApiKey"] = prop.getProperty(STRIPE_DEVELOPMENT_API_KEY)
				payment_settings["stripeDevPublishableKey"] = prop.getProperty(STRIPE_DEVELOPMENT_PUBLISHABLE_KEY)
				payment_settings["stripeProdApiKey"] = prop.getProperty(STRIPE_PRODUCTION_API_KEY)
				payment_settings["stripeProdPublishableKey"] = prop.getProperty(STRIPE_PRODUCTION_PUBLISHABLE_KEY)
				
				
				payment_settings["braintreeMerchantId"] = prop.getProperty(BRAINTREE_MERCHANT_ID)
				payment_settings["braintreePublicKey"] = prop.getProperty(BRAINTREE_PUBLIC_KEY)
				payment_settings["braintreePrivateKey"] = prop.getProperty(BRAINTREE_PRIVATE_KEY)
				
				
				[ payment_settings : payment_settings ]
				
			} catch (IOException e){
			    log.debug"Exception occured while reading properties file :" + e
				flash.message = "Something went wrong... "  + e
			}
		}
	}
	
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def save_payment_settings(){

		authenticatedAdmin{ adminAccount ->
		
			//String enabled = params.enabled
			println params.storeCurrency
			String storeCurrency = params.storeCurrency
			String storeCountryCode = params.storeCountryCode
			String stripeDevApiKey = params.stripeDevApiKey
			String stripeDevPublishableKey = params.stripeDevPublishableKey
			String stripeProdApiKey = params.stripeProdApiKey
			String stripeProdPublishableKey = params.stripeProdPublishableKey
			
			String braintreeMerchantId = params.braintreeMerchantId
			String braintreePublicKey = params.braintreePublicKey
			String braintreePrivateKey = params.braintreePrivateKey
			
			
			//if(enabled == "on")enabled = true
			//if(!enabled)enabled = false
			
			
			Properties prop = new Properties();
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			
			prop.load(inputStream);
			
			try{
				
				//prop.setProperty(STRIPE_ENABLED_KEY, enabled);
				prop.setProperty(STORE_CURRENCY, storeCurrency);
				prop.setProperty(STORE_COUNTRY_CODE, storeCountryCode);
				prop.setProperty(STRIPE_DEVELOPMENT_API_KEY, stripeDevApiKey);
				prop.setProperty(STRIPE_DEVELOPMENT_PUBLISHABLE_KEY, stripeDevPublishableKey);
				prop.setProperty(STRIPE_PRODUCTION_API_KEY, stripeProdApiKey);
				prop.setProperty(STRIPE_PRODUCTION_PUBLISHABLE_KEY, stripeProdPublishableKey);

				prop.setProperty(BRAINTREE_MERCHANT_ID, braintreeMerchantId);
				prop.setProperty(BRAINTREE_PUBLIC_KEY, braintreePublicKey);
				prop.setProperty(BRAINTREE_PRIVATE_KEY, braintreePrivateKey);
				
				
				def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
				absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
				def filePath = absolutePath + SETTINGS_FILE
				
			    prop.store(new FileOutputStream(filePath), null);

				applicationService.setProperties()
				
				flash.message = "Successfully saved Payment settings"
				redirect(action : 'payment_settings')
				
			} catch (IOException e){
			    log.debug"exception occured while saving properties file :"+e
				flash.message = "Something went wrong... "
				redirect(action : 'payment_settings')
				return
			}
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def select_gateway(){
		Properties prop = new Properties();
		try{
		
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			prop.load(inputStream);
			
			def gateway_settings = [:]
			gateway_settings["braintreeEnabled"] = prop.getProperty(BRAINTREE_ENABLED);
			
			[ gateway_settings : gateway_settings ]
			
		} catch (IOException e){
		    log.debug"Exception occured while reading properties file :" + e
			flash.message = "Something went wrong... "  + e
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def update_gateway(){
		
		String braintreeEnabled = params.enabled
		
		//if(enabled == "on")enabled = true
		//if(!enabled)enabled = false
		
		
		Properties prop = new Properties();
		File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
		FileInputStream inputStream = new FileInputStream(propertiesFile)
		
		prop.load(inputStream);
		
		try{
			
			prop.setProperty(BRAINTREE_ENABLED, braintreeEnabled);
			
			def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
			absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
			def filePath = absolutePath + SETTINGS_FILE
			
		    prop.store(new FileOutputStream(filePath), null);

			applicationService.setProperties()
			
			flash.message = "Successfully set Payment Gateway"
			redirect(action : 'select_gateway')
			
		} catch (IOException e){
		    log.debug"exception occured while saving properties file :"+e
			flash.message = "Something went wrong... "
			redirect(action : 'select_gateway')
			return
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def shipping_settings(){
		authenticatedAdmin{ adminAccount ->
			Properties prop = new Properties();
			try{
		
				File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
				FileInputStream inputStream = new FileInputStream(propertiesFile)
				prop.load(inputStream);
				
				def shipping_settings = [:]
				
				shipping_settings["address1"] = prop.getProperty(STORE_ADDRESS1);
				shipping_settings["address2"] = prop.getProperty(STORE_ADDRESS2);
				shipping_settings["city"] = prop.getProperty(STORE_CITY);
				shipping_settings["country"] = prop.getProperty(STORE_COUNTRY);
				shipping_settings["state"] = prop.getProperty(STORE_STATE);
				shipping_settings["zip"] = prop.getProperty(STORE_ZIP);
				shipping_settings["storePhone"] = prop.getProperty(STORE_PHONE);
				shipping_settings["shipping"] = prop.getProperty(STORE_SHIPPING);
				
				String easypostEnabled = prop.getProperty(EASYPOST_ENABLED);
				if(easypostEnabled == "true")shipping_settings["easypostEnabled"] = "checked"
				
				shipping_settings["testApiKey"] = prop.getProperty(EASYPOST_TEST_API_KEY);
				shipping_settings["liveApiKey"] = prop.getProperty(EASYPOST_LIVE_API_KEY);
				
				[ shipping_settings : shipping_settings, countries: Country.list() ]
				
			} catch (IOException e){
			    log.debug"Exception occured while reading properties file :"+e
			}
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def save_shipping_settings(){
		
		authenticatedAdmin{ adminAccount ->
			
			String easypostEnabled = params.easypostEnabled		
			String testApiKey = params.testApiKey
			String liveApiKey = params.liveApiKey

			String shipping = params.shipping
			
			String address1 = params.address1
			String address2 = params.address2
			String city = params.city
			String countryId = params.country
			String stateId = params.state
			String zip = params.zip
			String phone = params.storePhone
			
			/** TODO: ?
			String customsContentType = params.customsContentType
			String customsContentsExplanation = params.customsContentsExplanation
			String customsRestrictionType = params.customsRestrictionType
			String customsRestrictionComments = params.customsRestrictionComments
			String customsCertify = params.customsCertify
			String customsSigner = params.customsSigner
			String customsNonDeliveryOption = params.customsNonDeliveryOption
			String customsEelPfc = params.customsEelPfc
			**/
			
			
			if(easypostEnabled == "on")easypostEnabled = true
			
			if(!easypostEnabled)easypostEnabled = false
			
			if(!countryId || !countryId.isInteger()){
				flash.error = "Something went wrong, please try again..."
				redirect(action : 'shipping_settings')
				return
			}
			
			def country = Country.get(countryId)
			if(!country){
				flash.error = "Something went wrong"
				redirect(action : 'shipping_settings')
				return
			}
			
			def state
			
			if(stateId && stateId.isInteger()){
				state = State.get(stateId)
				if(!state){
					flash.error = "Something went wrong with the state"
					redirect(action : 'shipping_settings')
					return
				}
			}
			def shipmentApi
			def message = ""
			if(easypostEnabled == "true"){
				if(testApiKey == ""){
					flash.error = "EasyPost Test API Key cannot be blank"
					redirect(action : 'shipping_settings')
					return
				}
				if(liveApiKey == ""){
					flash.error = "EasyPost Live API Key cannot be blank"
					redirect(action : 'shipping_settings')
					return
				}

				shipmentApi = new EasyPostShipmentApi(applicationService, currencyService)
			}
			
			
			Properties prop = new Properties();
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			
			prop.load(inputStream);
			
			try{
				
				prop.setProperty(EASYPOST_ENABLED, easypostEnabled);
				prop.setProperty(EASYPOST_TEST_API_KEY, testApiKey);
				prop.setProperty(EASYPOST_LIVE_API_KEY, liveApiKey);
				
				prop.setProperty(STORE_SHIPPING, shipping);
				
				prop.setProperty(STORE_ADDRESS1, address1);
				prop.setProperty(STORE_ADDRESS2, address2);
				prop.setProperty(STORE_CITY, city);
				prop.setProperty(STORE_COUNTRY, countryId);
				if(state)prop.setProperty(STORE_STATE, stateId);
				prop.setProperty(STORE_ZIP, zip);
				prop.setProperty(STORE_PHONE, phone);
				
				
				/**
				
				contents_type = The type of item you are sending. You pass one of the following: 'merchandise', 'returned_goods', 'documents', 'gift', 'sample', 'other'.
				contents_explanation = If you specify ‘other’ in the ‘contents_type’ attribute, you must supply a brief description in this attribute.
				restriction_type = Describes if your shipment requires any special treatment / quarantine when entering the country. You pass one of the following: 'none', 'other', 'quarantine', 'sanitary_phytosanitary_inspection'.
				restriction_comments = If you specify ‘other’ in the “restriction_type attribute”, you must supply a brief description of what is required.
				customs_certify = This is a boolean value (true, false) that takes the place of the signature on the physical customs form. This is how you indicate that the information you have provided is accurate.
				customs_signer = This is the name of the person who is certifying that the information provided on the customs form is accurate. Use a name of the person in your organization who is responsible for this.
				non_delivery_option = In case the shipment cannot be delivered, this option tells the carrier what you want to happen to the package. You can pass either: 'abandon', 'return'. The value defaults to 'return'. If you pass ‘abandon’, you will not receive the package back if it cannot be delivered.
				eel_pfc = When shipping outside the US, you need to provide either an Exemption and Exclusion Legend (EEL) code or a Proof of Filing Citation (PFC). Which you need is based on the value of the goods being shipped.
				
				
				prop.setProperty(CUSTOMS_CONTENTS_TYPE, customsContentType)
				prop.setProperty(CUSTOMS_CONTENTS_EXPLANATION, customsContentsExplanation)
				prop.setProperty(CUSTOMS_RESTRICTION_TYPE, customsRestrictionType)
				prop.setProperty(CUSTOMS_RESTRICTION_COMMENTS, customsRestrictionComments)
				prop.setProperty(CUSTOMS_CERTIFY, customsCertify)
				prop.setProperty(CUSTOMS_SIGNER, customsSigner)
				prop.setProperty(CUSTOMS_NON_DELIVERY_OPTION, customsNonDeliveryOption)
				prop.setProperty(CUSTOMS_EEL_PFC, customsEelPfc)
				
				**/
				
				
				def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
				absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
				def filePath = absolutePath + SETTINGS_FILE
				
			    prop.store(new FileOutputStream(filePath), null);
    			applicationService.setProperties()
				
				if(shipmentApi){
			
					try{
					
						def address = new Address()
				    	address.company = applicationService.getStoreName()
						address.street1 = address1
						address.street2 = address2
						address.city = city
						address.country = country.name
						if(state)address.state = state.name
						address.zip = zip
						/**TODO:add**/
						if(phone)address.phone = phone
			    	
						/**TODO: need to romanize?**/
						/**address = new Address()
						address.street1 = "पीएस बिजनेस सेंटर ड्राइव"
						address.city = "नई दिल्ली"
						address.country = "India"
						address.zip = "110012"
						**/
					
					
						if(!shipmentApi.validAddress(address)){
		   					flash.error = "Address cannot be verified. Please update your address with valid information..."
							redirect(action : 'shipping_settings')
		   					return
						}
			    	
				    	message = "Successfully validated address "
					
					}catch(Exception e){
						flash.message = e.printStackTrace()
						redirect(action : 'shipping_settings')
						return
					}
				}
				
				
				if(message){
					message = message + "and saved shipping settings."
				}else{
					message = "Successfully saved shipping settings."
				}
				
				
				flash.message = message
				redirect(action : 'shipping_settings')
				
			} catch (IOException e){
			    log.debug"exception occured while saving properties file :" + e
				flash.error = "Something went wrong... "
				redirect(action : 'shipping_settings')
				return
			}
		}
	}
	
	
	def saveApiKeys(developmentApiKey, liveApiKey){
		Properties prop = new Properties();
		File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
		FileInputStream inputStream = new FileInputStream(propertiesFile)
		
		prop.load(inputStream);
		prop.setProperty(EASYPOST_TEST_API_KEY, testApiKey);
		prop.setProperty(EASYPOST_LIVE_API_KEY, liveApiKey);
		def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
		absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
		def filePath = absolutePath + SETTINGS_FILE
		
	    prop.store(new FileOutputStream(filePath), null);
		applicationService.setProperties()
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def edit_homepage(){
		authenticatedAdmin{ adminAccount ->
			def homepage = Page.findByTitle("Home")
			[homepage: homepage]
		}
	}
	
 	@Secured(['ROLE_ADMIN'])
	def save_homepage(){
		authenticatedAdmin{ adminAccount ->
			def homepage = Page.findByTitle("Home")
			homepage.content = params.homepage
			if(homepage.save(flush:true)){
				flash.message = "Successfully saved home page"
			}else{
				flash.message = "Something went wrong while trying to save Home Page. Please try again"
			}
			render(view:'edit_homepage', model:[ homepage: homepage ])
		}
	}
	
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def remove_upload(Long id){
		authenticatedAdmin{ adminAccount ->
			def upload = Upload.get(id)
			if(upload){
				upload.delete(flush:true)
				flash.message = "Successfully removed upload"
				redirect(action:'uploads')
			}else{
				flash.message = "Upload not found... "
				redirect(action:'uploads')
			}
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def upload(){
		authenticatedAdmin{ adminAccount ->
    		
			def file = request.getFile('file')
			if(file){
				def fullFileName = file.getOriginalFilename()
				
				String[] nameSplit = fullFileName.toString().split("\\.")
				def fileName = nameSplit[0]
				def extension = nameSplit[1]
			
				fileName = fileName.replaceAll("[^\\w\\s]","")
				fileName = fileName.replaceAll(" ", "_")
				
				def newFileName = "${fileName}.${extension}"
				
				def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('uploads')
				absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
				def filePath = "${absolutePath}${newFileName}"
				
				InputStream is = file.getInputStream()
				OutputStream os = new FileOutputStream(filePath)
			
				try {
				    IOUtils.copy(is, os);
				} finally {
				    IOUtils.closeQuietly(os);
				    IOUtils.closeQuietly(is);
				}
				
				def upload = new Upload()
				upload.url = "uploads/${fileName}.${extension}"
				
				upload.save(flush:true)
				
				flash.message = "Successfully uploaded file"
				redirect(action:'uploads')
			}else{
				flash.message = "Please specify a file to upload"
				redirect(action:'uploads')
			}
		}
	}
	
	
	
 	@Secured(['ROLE_ADMIN'])
	def uploads(Integer max){
		authenticatedAdmin{ adminAccount ->
			params.max = Math.min(max ?: 10, 100)
			[uploadInstanceList : Upload.list(params), uploadInstanceTotal: Upload.count()]
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def import_products_view(){
		authenticatedAdmin{ adminAccount ->
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def import_products(){
		authenticatedAdmin{ adminAccount ->
			def file = request.getFile('file')
			def is = file.getInputStream()
			
			BufferedReader br = null;
			StringBuilder sb = new StringBuilder();
        	
 	   		def count = 0
			def skipped = 0
			def errored = 0
			String line;
			
			def defaultLayout = Layout.findByDefaultLayout(true)
			def importUuid = commonUtilities.generateRandomString(7)

			try {
	    	
				br = new BufferedReader(new InputStreamReader(is));
				while ((line = br.readLine()) != null) {
					def fields = line.split(",", -1);
					
					try{
					
						def name = fields[0]
						def quantity = Integer.parseInt(fields[1])
						def price = new BigDecimal(fields[2])
						def salesPrice = new BigDecimal(fields[3])
						def weight = new BigDecimal(fields[4])
						def description = fields[5]
        	
						def existingProduct = Product.findByName(name)
						
						if(!existingProduct){
							
							def product = new Product()
							product.name = name
							product.quantity = quantity
							product.price = price
							
							if(salesPrice != 0){
								product.salesPrice = salesPrice
							}

							product.weight = weight
							product.description = description
							product.layout = defaultLayout
							product.importUuid = importUuid
							product.save(flush:true)

							println product.errors

							count++							
							
						}else{
							skipped++
						}
						
					}catch(Exception ae){
						println ae
						errored++
					}
				}
				
			} catch (IOException e) {
				flash.error = "Something went wrong while trying to import.  Please confirm correct formatting"
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			
			if(count == 0 && skipped > 0){
				flash.message = "Products already exist"
			}
 	   		if(count > 0){
				flash.message = "Successfully imported <strong>${count}</strong> products"
			}
			if(errored > 0){
				flash.error = "Errored on <strong>${errored}</strong> products.  Please review file and results to resolve"
			}
	    	
			
			render(view: 'import_products_view')
		}
	}
	
	
 	@Secured(['ROLE_ADMIN'])
	def manage_key(){
		Properties prop = new Properties();
		try{
	
			File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
			FileInputStream inputStream = new FileInputStream(propertiesFile)
			prop.load(inputStream);
			
			def privateKey = prop.getProperty(STORE_PRIVATE_KEY);
			
			def url = request.getRequestURL()
			def split = url.toString().split("/${applicationService.getContextName()}/")
			def httpSection = split[0]
			def accountsUrl = "${httpSection}/${applicationService.getContextName()}/data/accounts?k=${privateKey}"
			
			
			[ privateKey : privateKey, accountsUrl: accountsUrl ]
			
		}catch(Exception e){
			e.printStackTrace()
		}
	}
 	
	
	@Secured(['ROLE_ADMIN'])
	def generate_key(){

		def key = generator( (('a'..'z')+('A'..'Z')+('0'..'9')).join(), 9)
		
		Properties prop = new Properties();
		File propertiesFile = grailsApplication.mainContext.getResource("settings/${SETTINGS_FILE}").file
		FileInputStream inputStream = new FileInputStream(propertiesFile)
		
		prop.load(inputStream);
		prop.setProperty(STORE_PRIVATE_KEY, key);
		def absolutePath = grailsApplication.mainContext.servletContext.getRealPath('settings')
		absolutePath = absolutePath.endsWith("/") ? absolutePath : absolutePath + "/"
		def filePath = absolutePath + SETTINGS_FILE
		
	    prop.store(new FileOutputStream(filePath), null);
		applicationService.setProperties()
		
		flash.message = "Successfully generated private access key. You can now use this to access data"
		redirect(action: "manage_key")
	}
	
	
	def generator = { String alphabet, int n ->
	  new Random().with {
	    (1..n).collect { alphabet[ nextInt( alphabet.length() ) ] }.join()
	  }
	}
}
