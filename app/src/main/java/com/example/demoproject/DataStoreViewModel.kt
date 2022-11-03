import androidx.lifecycle.ViewModel
import com.example.demoproject.DatastoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton


class DataStoreViewModel @Inject constructor(
     var datastoreRepository : DatastoreRepo
) {

    fun storeAccessToken(value:String) = runBlocking {
        datastoreRepository.putString("accesstoken",value)
    }
    fun storeUserid(value:String) = runBlocking {
        datastoreRepository.putString("uid",value)
    }
    fun getUserAccessToken():String = runBlocking {
        datastoreRepository.getString("accesstoken")!!
    }
    fun getUserid():String = runBlocking {
        datastoreRepository.getString("uid")!!
    }
    fun clearPreferences(key:String) = runBlocking {
        datastoreRepository.clearPReferences(key)
    }
    fun storeUserclient(value:String) = runBlocking {
        datastoreRepository.putString("client",value)
    }
    fun getUserclient():String = runBlocking {
        datastoreRepository.getString("client")!!
    }
}