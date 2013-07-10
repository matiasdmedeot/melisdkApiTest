package mlrest

import org.springframework.dao.DataIntegrityViolationException

import com.mercadolibre.sdk.Meli;

class SearchItemController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
		
        params.max = Math.min(max ?: 10, 100)
        [searchItemInstanceList: SearchItem.list(params), searchItemInstanceTotal: SearchItem.count()]
    }

    def create() {
        [searchItemInstance: new SearchItem(params)]
    }

    def save() {
        def searchItemInstance = new SearchItem(params)
        if (!searchItemInstance.save(flush: true)) {
            render(view: "create", model: [searchItemInstance: searchItemInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), searchItemInstance.id])
        redirect(action: "show", id: searchItemInstance.id)
    }

    def show(Long id) {
		
		Meli m = new Meli(1523503382449349,"GwGazuvAjH4VbJlpcw9UZFW6TczYCv5a")
		def url = m.getAuthUrl("http://localhost:8080/mlrest/searchItem/list")
		m.authorize("TG-51dc7b06e4b0a50787467db7", "http://localhost:8080/mlrest/searchItem/list")
		def token = m.getAccessToken();
	
		
		
		
        def searchItemInstance = SearchItem.get(id)
        if (!searchItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "list")
            return
        }

        [searchItemInstance: searchItemInstance]
    }

    def edit(Long id) {
        def searchItemInstance = SearchItem.get(id)
        if (!searchItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "list")
            return
        }

        [searchItemInstance: searchItemInstance]
    }

    def update(Long id, Long version) {
        def searchItemInstance = SearchItem.get(id)
        if (!searchItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (searchItemInstance.version > version) {
                searchItemInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'searchItem.label', default: 'SearchItem')] as Object[],
                          "Another user has updated this SearchItem while you were editing")
                render(view: "edit", model: [searchItemInstance: searchItemInstance])
                return
            }
        }

        searchItemInstance.properties = params

        if (!searchItemInstance.save(flush: true)) {
            render(view: "edit", model: [searchItemInstance: searchItemInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), searchItemInstance.id])
        redirect(action: "show", id: searchItemInstance.id)
    }

    def delete(Long id) {
        def searchItemInstance = SearchItem.get(id)
        if (!searchItemInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "list")
            return
        }

        try {
            searchItemInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'searchItem.label', default: 'SearchItem'), id])
            redirect(action: "show", id: id)
        }
    }
}
