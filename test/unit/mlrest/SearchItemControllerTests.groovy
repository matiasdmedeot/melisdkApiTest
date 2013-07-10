package mlrest



import org.junit.*
import grails.test.mixin.*

@TestFor(SearchItemController)
@Mock(SearchItem)
class SearchItemControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/searchItem/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.searchItemInstanceList.size() == 0
        assert model.searchItemInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.searchItemInstance != null
    }

    void testSave() {
        controller.save()

        assert model.searchItemInstance != null
        assert view == '/searchItem/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/searchItem/show/1'
        assert controller.flash.message != null
        assert SearchItem.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/searchItem/list'

        populateValidParams(params)
        def searchItem = new SearchItem(params)

        assert searchItem.save() != null

        params.id = searchItem.id

        def model = controller.show()

        assert model.searchItemInstance == searchItem
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/searchItem/list'

        populateValidParams(params)
        def searchItem = new SearchItem(params)

        assert searchItem.save() != null

        params.id = searchItem.id

        def model = controller.edit()

        assert model.searchItemInstance == searchItem
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/searchItem/list'

        response.reset()

        populateValidParams(params)
        def searchItem = new SearchItem(params)

        assert searchItem.save() != null

        // test invalid parameters in update
        params.id = searchItem.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/searchItem/edit"
        assert model.searchItemInstance != null

        searchItem.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/searchItem/show/$searchItem.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        searchItem.clearErrors()

        populateValidParams(params)
        params.id = searchItem.id
        params.version = -1
        controller.update()

        assert view == "/searchItem/edit"
        assert model.searchItemInstance != null
        assert model.searchItemInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/searchItem/list'

        response.reset()

        populateValidParams(params)
        def searchItem = new SearchItem(params)

        assert searchItem.save() != null
        assert SearchItem.count() == 1

        params.id = searchItem.id

        controller.delete()

        assert SearchItem.count() == 0
        assert SearchItem.get(searchItem.id) == null
        assert response.redirectedUrl == '/searchItem/list'
    }
}
