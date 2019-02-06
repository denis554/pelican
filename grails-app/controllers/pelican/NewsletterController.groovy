package pelican

import static org.springframework.http.HttpStatus.OK
import grails.plugin.springsecurity.annotation.Secured

import io.pelican.Account


class NewsletterController {

    static allowedMethods = [signup: ["GET", "POST"], opt_opt: "POST"]
    
    String csvMimeType
    String encoding


    @Secured(['permitAll'])
    def index(){}


    @Secured(['permitAll'])
    def signup(){
    	def account = new Account()
    	account.username = params.email
    	account.email = params.email
    	account.emailOptIn = true
    	account.password = "change"

    	def existingAccount = Account.findByEmail(params.email)
    	if(existingAccount && existingAccount.emailOptIn){
    		flash.message = "You have already signed up for news and updates"
    		render(view : "signup")
    		return
    	}


    	if(existingAccount && !existingAccount.emailOptIn){
    		flash.message = "We found an account already for email entered. Please opt in for news and updates"
    		redirect(action: "found", id: existingAccount.id)
    		return
    	}

    	if(!account.save(flush:true)){
    		flash.message = "Please enter a valid email address"
    	}else{
    		flash.message = "<strong>" + account.email + "</strong> : Successfully signed up for news & updates"
    	}


    	account.errors.allErrors.each{ println it }

    	redirect(action: "index")
    }



    @Secured(['permitAll'])
    def found(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = "Unable to find account"
    		redirect(action: "signup")
    		return
    	}
		[account : account]
    }



    @Secured(['ROLE_ADMIN'])
    def opt_in(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = "Unable to find account"
    	}

    	account.emailOptIn = true
    	account.save(flush:true)

    	flash.message = "Successfully opted in account: " + account.email
		redirect(action: "index")
    }



    @Secured(['ROLE_ADMIN'])
    def admin_opt_in(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = "Unable to find account"
    	}

    	account.emailOptIn = true
    	account.save(flush:true)

    	flash.message = "Successfully opted in account: " + account.email
		redirect(controller:"account", action: "admin_edit", id: account.id)
    }



    @Secured(["permitAll", "ROLE_ADMIN"])
    def opt_out(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = "Unable to find account"
    	}

    	account.emailOptIn = false
    	account.save(flush:true)

    	flash.message = "Successfully opted out account: " + account.email

    	if(params.redirect == "true"){
    		redirect(action: "index")
    		return
    	}

		redirect(action: "list")
    }



    @Secured(['ROLE_ADMIN'])
    def admin_opt_out(Long id){
    	def account = Account.get(id)
    	if(!account){
    		flash.message = "Unable to find account"
    	}

    	account.emailOptIn = false
    	account.save(flush:true)

    	flash.message = "Successfully opted out account: " + account.email
		redirect(controller:"account", action: "admin_edit", id: account.id)
    }



    @Secured(["permitAll"])
    def confirm(){
    	def account = Account.findByEmail(params.email)
    	if(!account){
    		flash.message = "Unable to find account"
    		redirect(action: "index")
    		return
    	}

    	[account: account]
    }



    @Secured(['ROLE_ADMIN'])
    def list(){
    	def max = 10
		def offset = params?.offset ? params.offset : 0
		def sort = params?.sort ? params.sort : "id"
		def order = params?.order ? params.order : "asc"

		def accountsList = Account.findAllByEmailOptIn(true, [max: max, offset: offset, sort: sort, order: order ])

		[accountsList: accountsList, accountsTotal: Account.countByEmailOptIn(true)]
    }


    @Secured(['ROLE_ADMIN'])
    def export(){
        def accountsCsvArray = []
        def accounts = Account.findAllByEmailOptIn(true)

        accounts.each { account ->
            accountsCsvArray.add(account.email)
        }

        def filename = "account-emails.csv"
        def outs = response.outputStream
        response.status = OK.value()
        response.contentType = "${csvMimeType};charset=${encoding}";
        response.setHeader "Content-disposition", "attachment; filename=${filename}"
 
        accountsCsvArray.each { String line ->
            outs << "${line}\n"
        }
 
        outs.flush()
        outs.close()
    } 





}